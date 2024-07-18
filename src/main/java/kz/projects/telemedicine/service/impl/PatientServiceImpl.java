package kz.projects.telemedicine.service.impl;

import jakarta.transaction.Transactional;
import kz.projects.telemedicine.dto.AppointmentDTO;
import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.PrescriptionDTO;
import kz.projects.telemedicine.dto.requests.RescheduleRequest;
import kz.projects.telemedicine.exceptions.AppointmentNotFoundException;
import kz.projects.telemedicine.exceptions.DoctorNotFoundException;
import kz.projects.telemedicine.exceptions.PatientNotFoundException;
import kz.projects.telemedicine.exceptions.UnauthorizedException;
import kz.projects.telemedicine.mapper.AppointmentMapper;
import kz.projects.telemedicine.mapper.DoctorMapper;
import kz.projects.telemedicine.mapper.PrescriptionMapper;
import kz.projects.telemedicine.model.*;
import kz.projects.telemedicine.repositories.AppointmentsRepository;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.repositories.PrescriptionsRepository;
import kz.projects.telemedicine.service.AuthService;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

  private final DoctorRepository doctorRepository;

  private final AppointmentsRepository appointmentsRepository;

  private final PatientRepository patientRepository;

  private final PrescriptionsRepository prescriptionsRepository;

  private final AuthService authService;

  private final AppointmentMapper appointmentMapper;

  private final PrescriptionMapper prescriptionMapper;

  private final DoctorMapper doctorMapper;

  public final User getCurrentUser(){
    return authService.getCurrentSessionUser();
  }


  @Override
  public List<DoctorDTO> getDoctors() {
    return doctorMapper.toDtoList(doctorRepository.findAll());
  }

  @Override
  @Transactional
  public AppointmentDTO makeAppointment(AppointmentDTO appointmentRequest) {
    String currentUserEmail = getCurrentUser().getEmail();

    Optional<Patient> patientOptional = patientRepository.findByEmail(currentUserEmail);
    if (patientOptional.isEmpty()) {
      throw new PatientNotFoundException("Patient not found");
    }
    Patient patient = patientOptional.get();

    Appointment appointment = appointmentMapper.toModel(appointmentRequest);
    appointment.setPatient(patient);
    appointment.setStatus(AppointmentStatus.SCHEDULED);

    Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentRequest.getDoctor().getId());
    if (doctorOptional.isEmpty()) {
      throw new DoctorNotFoundException("Doctor not found");
    }

    Doctor doctor = doctorOptional.get();
    appointment.setDoctor(doctor);

    appointment.getPatient()
            .setMedicalHistory(appointment.getPatient().getMedicalHistory() + "\n" + appointment.getInfo());

    Appointment savedAppointment = appointmentsRepository.save(appointment);
    return appointmentMapper.toResponseDto(savedAppointment);
  }

  @Override
  public AppointmentDTO changeAppointment(Long id, RescheduleRequest request) {
    Optional<Appointment> appointmentOptional = appointmentsRepository.findById(id);

    if (appointmentOptional.isEmpty()) {
      throw new AppointmentNotFoundException("Appointment not found");
    }

    Appointment appointment = appointmentOptional.get();

    if (!appointment.getPatient().getUser().getEmail().equals(getCurrentUser().getEmail())){
      throw new UnauthorizedException("You are not authorized to change this appointment");
    }

    appointment.setStatus(AppointmentStatus.RESCHEDULED);
    appointment.setDate(request.getDate());
    appointment.setTime(request.getTime());
    appointment.getPatient()
            .setMedicalHistory(appointment.getPatient().getMedicalHistory() + "\n" + appointment.getInfo());
    Appointment savedAppointment = appointmentsRepository.save(appointment);

    return appointmentMapper.toResponseDto(savedAppointment);
  }

  @Override
  public void cancelAppointment(Long id) {
    Optional<Appointment> appointmentOptional = appointmentsRepository.findById(id);

    if (appointmentOptional.isEmpty()) {
      throw new AppointmentNotFoundException("Appointment not found");
    }

    Appointment appointment = appointmentOptional.get();

    if (!appointment.getPatient().getUser().getEmail().equals(getCurrentUser().getEmail())) {
      throw new UnauthorizedException("You are not authorized to cancel this appointment");
    }
    appointment.setStatus(AppointmentStatus.CANCELLED);
    appointment.getPatient()
            .setMedicalHistory(appointment.getPatient().getMedicalHistory() + "\n" + appointment.getInfo());

    appointmentsRepository.deleteById(id);
  }

  @Override
  public List<AppointmentDTO> getAppointments() {
    Optional<Patient> patientOptional = patientRepository.findByEmail(getCurrentUser().getEmail());
    if (patientOptional.isEmpty()) {
      throw new PatientNotFoundException("Patient not found");
    }
    Patient currentPatient = patientOptional.get();

    List<Appointment> appointments = appointmentsRepository.findAllByPatient(currentPatient);
    return appointmentMapper.toDtoList(appointments);
  }

  @Override
  public List<PrescriptionDTO> getPrescriptions() {
    String currentUserEmail = getCurrentUser().getEmail();

    Patient patient = patientRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

    List<Prescriptions> prescriptions = prescriptionsRepository.findAllByPatient(patient).orElseThrow();
    return prescriptionMapper.toDtoList(prescriptions);
  }


}
