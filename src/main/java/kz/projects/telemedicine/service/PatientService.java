package kz.projects.telemedicine.service;


import kz.projects.telemedicine.model.Appointment;

import java.util.List;

public interface PatientService {
  String getDoctors();

  Appointment makeAppointment(Appointment appointment);
  Appointment changeAppointment(Long id);

  void cancelAppointment(Long id);

  List<Appointment> getAppointments();
}
