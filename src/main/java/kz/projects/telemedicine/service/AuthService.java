package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
  PatientDTO register(PatientDTO request);
  UserDetails login(LoginRequest request);
  DoctorDTO addDoctor(DoctorDTO request);
  User getCurrentSessionUser();
}
