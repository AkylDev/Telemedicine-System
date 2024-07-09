package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionsRepository extends JpaRepository<Prescriptions, Long> {
  Optional<List<Prescriptions>> findAllByPatient(Patient patient);
}
