package kz.projects.telemedicine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO {
  private Long id;
  private PatientDTO patient;
  private DoctorDTO doctor;
  private String medication;
  private String dosage;
  private String duration;
}
