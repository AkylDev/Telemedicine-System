package kz.projects.telemedicine.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.UserDTO;
import kz.projects.telemedicine.dto.requests.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication related operations")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Register a new patient")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Patient registered successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request")
  })
  @PostMapping("/register")
  public ResponseEntity<PatientDTO> register(@RequestBody PatientDTO request) {
    PatientDTO patient = authService.register(request);
    return new ResponseEntity<>(patient, HttpStatus.CREATED);
  }

  @Operation(summary = "Login a user")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User logged in successfully"),
          @ApiResponse(responseCode = "401", description = "Invalid credentials")
  })
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      UserDTO user = authService.login(request);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  @Operation(summary = "Add a new doctor")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Doctor added successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request")
  })
  @PostMapping("/add-doctor")
  public ResponseEntity<DoctorDTO> addDoctor(@RequestBody DoctorDTO request) {
    DoctorDTO doctor = authService.addDoctor(request);
    return new ResponseEntity<>(doctor, HttpStatus.CREATED);
  }

}
