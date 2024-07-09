package kz.projects.telemedicine.service;


import kz.projects.telemedicine.dto.AppointmentRequest;
import kz.projects.telemedicine.model.Appointment;

public interface PatientService {
  String getDoctors();

  Appointment makeAppointment(AppointmentRequest appointment);
}
