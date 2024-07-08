package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.RegisterRequest;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.User;

public interface AuthService {
  Patient register(RegisterRequest request);
  User login(LoginRequest request);
}
