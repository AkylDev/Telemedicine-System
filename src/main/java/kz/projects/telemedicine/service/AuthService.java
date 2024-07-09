package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.DoctorRequest;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.RegisterRequest;
import kz.projects.telemedicine.model.User;

public interface AuthService {
  RegisterRequest register(RegisterRequest request);
  User login(LoginRequest request);
  DoctorRequest addDoctor(DoctorRequest request);

  User getCurrentSessionUser();
}
