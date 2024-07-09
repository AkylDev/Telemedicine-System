package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Patient;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  Optional<Patient> findByEmail(String email);

  @NonNull
  Optional<Patient> findById(@NonNull Long id);
}
