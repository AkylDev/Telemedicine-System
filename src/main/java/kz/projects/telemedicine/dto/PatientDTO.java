package kz.projects.telemedicine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {
  private String name;
  private String email;
  private String phone;
  private String password;
}
