package kz.projects.telemedicine.repositories;

import kz.projects.telemedicine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}
