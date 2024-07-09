package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.exceptions.AppointmentNotFoundException;
import kz.projects.telemedicine.exceptions.UnauthorizedException;
import kz.projects.telemedicine.model.*;
import kz.projects.telemedicine.repositories.AppointmentsRepository;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
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

  private final AuthService authService;

  @Override
  public String getDoctors() {
    List<Doctor> allDoctors = doctorRepository.findAll();
    StringBuilder list = new StringBuilder();

    for (Doctor doc : allDoctors) {
      list.append(doc.getName()).append(" is available at ").append(doc.getSchedule()).append("\n");
    }

    return String.valueOf(list);
  }

  @Override
  public Appointment makeAppointment(Appointment appointmentRequest) {
    User currentUser = authService.getCurrentSessionUser();
    Patient patient = patientRepository.findByEmail(currentUser.getEmail());
    appointmentRequest.setPatient(patient);
    appointmentRequest.setStatus(AppointmentStatus.SCHEDULED);

    return appointmentsRepository.save(appointmentRequest);
  }

  @Override
  public Appointment changeAppointment(Long id) {
    Optional<Appointment> appointmentOptional = appointmentsRepository.findById(id);

    if (appointmentOptional.isPresent()) {
      Appointment appointment = appointmentOptional.get();

      User currentUser = authService.getCurrentSessionUser();

      if (!appointment.getPatient().getUser().getEmail().equals(currentUser.getEmail())){
        throw new UnauthorizedException("You are not authorized to change this appointment");
      }

      appointment.setStatus(AppointmentStatus.RESCHEDULED);
      return appointmentsRepository.save(appointment);
    } else {
      throw new AppointmentNotFoundException("Appointment not found with id " + id);
    }
  }

  @Override
  public void cancelAppointment(Long id) {
    Optional<Appointment> appointmentOptional = appointmentsRepository.findById(id);

    if (appointmentOptional.isPresent()) {
      Appointment appointment = appointmentOptional.get();

      User currentUser = authService.getCurrentSessionUser();

      if (!appointment.getPatient().getUser().getEmail().equals(currentUser.getEmail())) {
        throw new UnauthorizedException("You are not authorized to cancel this appointment");
      }

      appointmentsRepository.deleteById(id);
    } else {
      throw new AppointmentNotFoundException("Appointment not found with id " + id);
    }
  }
}
