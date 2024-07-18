package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.UserDTO;
import kz.projects.telemedicine.dto.requests.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.model.User;

public interface AuthService {
  PatientDTO register(PatientDTO request);
  UserDTO login(LoginRequest request);
  DoctorDTO addDoctor(DoctorDTO request);
  User getCurrentSessionUser();
}
