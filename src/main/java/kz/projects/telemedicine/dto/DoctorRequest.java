package kz.projects.telemedicine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRequest {
  private String name;
  private String email;
  private String password;
  private String specialization;
  private String schedule;
}
