package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.Specialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {
  private Long id;
  private String name;
  private String email;
  private String password;
  private Specialization specialization;
  private String schedule;
}
