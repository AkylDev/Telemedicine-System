package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionsRepository extends JpaRepository<Prescriptions, Long> {
}
