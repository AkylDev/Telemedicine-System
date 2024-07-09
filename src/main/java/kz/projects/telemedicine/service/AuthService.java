package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.DoctorRequest;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.model.User;

public interface AuthService {
  PatientDTO register(PatientDTO request);
  User login(LoginRequest request);
  DoctorRequest addDoctor(DoctorRequest request);

  User getCurrentSessionUser();
}
