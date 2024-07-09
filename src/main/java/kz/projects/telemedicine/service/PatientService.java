package kz.projects.telemedicine.service;


import kz.projects.telemedicine.dto.AppointmentDTO;

import java.util.List;

public interface PatientService {
  String getDoctors();

  AppointmentDTO makeAppointment(AppointmentDTO appointment);
  AppointmentDTO changeAppointment(Long id);

  void cancelAppointment(Long id);

  List<AppointmentDTO> getAppointments();
}
