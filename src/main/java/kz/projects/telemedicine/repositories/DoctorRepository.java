package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
