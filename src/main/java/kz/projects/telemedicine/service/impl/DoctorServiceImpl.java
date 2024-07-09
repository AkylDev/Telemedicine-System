package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.exceptions.PatientNotFoundException;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

  private final PatientRepository patientRepository;

  @Override
  public String getPatientRecords(Long id) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isPresent()){
      Patient patient = patientOptional.get();
      return patient.getName() + "\n" + patient.getMedicalHistory();
    }else {
      throw new PatientNotFoundException("Patient not found");
    }
  }
}
