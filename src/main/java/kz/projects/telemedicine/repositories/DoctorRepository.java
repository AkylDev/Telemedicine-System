package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  Optional<Doctor> findByEmail(String email);
}
