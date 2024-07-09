package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.dto.DoctorRequest;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<PatientDTO> register(@RequestBody PatientDTO request) {
    PatientDTO patient = authService.register(request);
    return new ResponseEntity<>(patient, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      UserDetails userDetails = authService.login(request);
      return ResponseEntity.ok(userDetails);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  @PostMapping("/add-doctor")
  public ResponseEntity<DoctorRequest> addDoctor(@RequestBody DoctorRequest request) {
    DoctorRequest doctor = authService.addDoctor(request);
    return new ResponseEntity<>(doctor, HttpStatus.CREATED);
  }

}
