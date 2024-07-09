package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.AppointmentStatus;
import kz.projects.telemedicine.model.Doctor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentRequest {
  private Doctor doctor;
  private LocalDate date;
  private LocalTime time;
  private AppointmentStatus status;
}
