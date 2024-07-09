package kz.projects.telemedicine.repositories;


import kz.projects.telemedicine.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {
}
