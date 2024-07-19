package kz.projects.telemedicine.dto;


public record PrescriptionDTO(Long id,PatientDTO patient,DoctorDTO doctor,
                              String medication,String dosage,String duration) { }
