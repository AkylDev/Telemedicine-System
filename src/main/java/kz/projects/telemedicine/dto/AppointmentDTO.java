package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
  private Long id;
  private PatientDTO patient;
  private DoctorRequest doctor;
  private LocalDate date;
  private LocalTime time;
  private AppointmentStatus status;
  private String info;
}
