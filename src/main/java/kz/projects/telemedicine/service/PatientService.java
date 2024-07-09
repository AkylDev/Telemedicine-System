package kz.projects.telemedicine.service;


import kz.projects.telemedicine.dto.AppointmentDTO;
import kz.projects.telemedicine.dto.DoctorDTO;

import java.util.List;

public interface PatientService {
  List<DoctorDTO> getDoctors();
  AppointmentDTO makeAppointment(AppointmentDTO appointment);
  AppointmentDTO changeAppointment(Long id);
  void cancelAppointment(Long id);
  List<AppointmentDTO> getAppointments();
}
