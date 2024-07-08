package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  Patient findByEmail(String email);
}
