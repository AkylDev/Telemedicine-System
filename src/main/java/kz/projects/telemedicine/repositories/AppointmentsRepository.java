package kz.projects.telemedicine.repositories;


import kz.projects.telemedicine.model.Appointment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {

  Optional<Appointment> findById(Long id);

  void deleteById(Long id);
}
