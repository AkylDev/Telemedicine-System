package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.UserDTO;
import kz.projects.telemedicine.dto.requests.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.mapper.DoctorMapper;
import kz.projects.telemedicine.mapper.PatientMapper;
import kz.projects.telemedicine.model.*;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.repositories.PermissionsRepository;
import kz.projects.telemedicine.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private PermissionsRepository permissionRepository;

  @Mock
  private PatientMapper patientMapper;

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private MyUserDetailsService userDetailsService;

  @Mock
  private DoctorRepository doctorRepository;

  @Mock
  private DoctorMapper doctorMapper;

  @InjectMocks
  private AuthServiceImpl authService;

  @Test
  public void testRegisterPatientSuccessfully() {
    PatientDTO newPatient = new PatientDTO(null, "testName", "testEmail@example.com", "testPhone", "testPassword");

    when(userRepository.findByEmail(newPatient.email())).thenReturn(null);

    Permissions defaultPermission = new Permissions();
    defaultPermission.setId(1L);
    defaultPermission.setRole("ROLE_PATIENT");
    when(permissionRepository.findByRole("ROLE_PATIENT")).thenReturn(defaultPermission);

    Patient patient = new Patient();
    patient.setName(newPatient.name());
    patient.setEmail(newPatient.email());
    patient.setPhone(newPatient.phone());

    when(patientRepository.save(patient)).thenReturn(patient);
    when(patientMapper.toModel(newPatient)).thenReturn(patient);
    when(patientMapper.toDto(patient)).thenReturn(newPatient);
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

    User newUser = new User();
    newUser.setEmail(newPatient.email());
    newUser.setPassword("encodedPassword");
    newUser.setPermissionList(Collections.singletonList(defaultPermission));
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    PatientDTO savedPatientDTO = authService.register(newPatient);

    assertThat(savedPatientDTO).isNotNull();
    assertEquals(newPatient.email(), savedPatientDTO.email());
    assertEquals(newUser.getPassword(), "encodedPassword");
    assertEquals(1, newUser.getPermissionList().size());
    verify(userRepository, times(1)).findByEmail(newPatient.email());
    verify(userRepository, times(1)).save(any(User.class));
    verify(permissionRepository, times(1)).findByRole("ROLE_PATIENT");
    verify(permissionRepository, times(0)).save(any(Permissions.class)); // Assuming defaultPermission exists
    verify(patientRepository, times(1)).save(patient);
    verify(patientMapper, times(1)).toModel(newPatient);
    verify(patientMapper, times(1)).toDto(patient);
  }


  @Test
  public void testRegisterPatientWhenUserExists() {
    PatientDTO registerRequest = new PatientDTO(null, null, "test@example.com", null, "testPassword");

    User existingUser = new User();
    existingUser.setEmail(registerRequest.email());
    when(userRepository.findByEmail(registerRequest.email())).thenReturn(existingUser);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.register(registerRequest));
    assertEquals("User with this email already exists.", exception.getMessage());

    verify(userRepository, times(1)).findByEmail(registerRequest.email());
    verify(userRepository, times(0)).save(any(User.class));
    verify(permissionRepository, times(0)).findByRole(anyString());
    verify(patientRepository, times(0)).save(any(Patient.class));
    verify(patientMapper, times(0)).toModel(any(PatientDTO.class));
    verify(patientMapper, times(0)).toDto(any(Patient.class));
  }


  @Test
  public void testLoginSuccessfully() {
    String email = "test@example.com";
    String password = "testPassword";
    LoginRequest request = new LoginRequest(email, password);

    // Create a mock User instance
    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password)); // Encoding the password for matching
    user.setPermissionList(Collections.singletonList(new Permissions(1L, "ROLE_PATIENT")));

    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

    UserDTO result = authService.login(request);

    assertEquals(email, result.email());
    verify(userDetailsService, times(1)).loadUserByUsername(email);
    verify(passwordEncoder, times(1)).matches(password, user.getPassword());
  }

  @Test
  public void testLoginWithInvalidCredentials() {
    String email = "test@example.com";
    String password = "testPassword";
    LoginRequest request = new LoginRequest(email, password);

    UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email)
            .password("differentPassword")
            .roles("PATIENT")
            .build();
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

    when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(false);

    BadCredentialsException exception = assertThrows(BadCredentialsException.class,
            () -> authService.login(request));
    assertEquals("Invalid credentials", exception.getMessage());

    verify(userDetailsService, times(1)).loadUserByUsername(email);
    verify(passwordEncoder, times(1)).matches(password, userDetails.getPassword());
  }

  @Test
  public void testAddDoctorSuccessfully() {
    DoctorDTO doctorRequest = new DoctorDTO(
            null, // id will be null for requests
            "Dr. John Doe",
            "john.doe@example.com",
            "password123",
            Specialization.DENTIST,
            "Monday to Friday, 9 AM - 5 PM"
    );

    when(userRepository.findByEmail(doctorRequest.email())).thenReturn(null);

    Permissions defaultPermission = new Permissions();
    defaultPermission.setId(1L);
    defaultPermission.setRole("ROLE_DOCTOR");
    when(permissionRepository.findByRole("ROLE_DOCTOR")).thenReturn(defaultPermission);

    Doctor doctor = new Doctor();
    doctor.setEmail(doctorRequest.email());
    doctor.setName(doctorRequest.name());
    doctor.setSpecialization(doctorRequest.specialization());
    doctor.setSchedule(doctorRequest.schedule());

    when(doctorRepository.save(doctor)).thenReturn(doctor);
    when(doctorMapper.toModel(doctorRequest)).thenReturn(doctor);
    when(doctorMapper.toDto(doctor)).thenReturn(doctorRequest);
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

    User newUser = new User();
    newUser.setEmail(doctorRequest.email());
    newUser.setPassword(doctorRequest.password());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    DoctorDTO result = authService.addDoctor(doctorRequest);

    assertThat(result).isNotNull();
    assertEquals(doctorRequest.email(), result.email());
    assertEquals(doctorRequest.name(), result.name());
    assertEquals(doctorRequest.specialization(), result.specialization());
    assertEquals(doctorRequest.schedule(), result.schedule());

    verify(userRepository, times(1)).findByEmail(doctorRequest.email());
    verify(permissionRepository, times(1)).findByRole("ROLE_DOCTOR");
    verify(userRepository, times(1)).save(any(User.class));
    verify(doctorRepository, times(1)).save(any(Doctor.class));
  }
}
