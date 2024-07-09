package kz.projects.telemedicine.repositories;


import kz.projects.telemedicine.model.Appointment;
import kz.projects.telemedicine.model.Patient;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {

  @NonNull
  Optional<Appointment> findById(@NonNull Long id);

  void deleteById(@NonNull Long id);

  List<Appointment> findAllByPatient(Patient patient);
}
