package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
}
