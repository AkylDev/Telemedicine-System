package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
  Permissions findByRole(String role);
}
