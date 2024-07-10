package kz.projects.telemedicine.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RescheduleRequest {
  private LocalDate date;
  private LocalTime time;
}
