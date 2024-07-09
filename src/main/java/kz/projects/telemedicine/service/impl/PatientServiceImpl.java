package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.AppointmentRequest;
import kz.projects.telemedicine.model.Appointment;
import kz.projects.telemedicine.model.Doctor;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.User;
import kz.projects.telemedicine.repositories.AppointmentsRepository;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.service.AuthService;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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

    for (Doctor doc : allDoctors){
      list.append(doc.getName()).append(" is available at ").append(doc.getSchedule()).append("\n");
    }

    return String.valueOf(list);
  }

  @Override
  public Appointment makeAppointment(AppointmentRequest appointmentRequest) {
    Appointment appointment = new Appointment();
    appointment.setDoctor(appointmentRequest.getDoctor());
    appointment.setDate(appointmentRequest.getDate());
    appointment.setTime(appointmentRequest.getTime());
    appointment.setStatus(appointmentRequest.getStatus());

    User currentUser = authService.getCurrentSessionUser();
    Patient patient = patientRepository.findByEmail(currentUser.getEmail());
    appointment.setPatient(patient);

    return appointmentsRepository.save(appointment);
  }
}
