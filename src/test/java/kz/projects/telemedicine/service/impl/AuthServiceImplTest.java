package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.LoginRequest;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    PatientDTO newPatient = new PatientDTO();
    newPatient.setName("testName");
    newPatient.setEmail("testEmail@example.com");
    newPatient.setPassword("testPassword");
    newPatient.setPhone("testPhone");

    when(userRepository.findByEmail(newPatient.getEmail())).thenReturn(null);

    Permissions defaultPermission = new Permissions();
    defaultPermission.setId(1L);
    defaultPermission.setRole("ROLE_PATIENT");
    when(permissionRepository.findByRole("ROLE_PATIENT")).thenReturn(defaultPermission);

    Patient patient = new Patient();
    patient.setName("testName");
    patient.setEmail("testEmail@example.com");
    patient.setPhone("testPhone");

    when(patientRepository.save(patient)).thenReturn(patient);
    when(patientMapper.toModel(newPatient)).thenReturn(patient);
    when(patientMapper.toDto(patient)).thenReturn(newPatient);
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

    User newUser = new User();
    newUser.setEmail(newPatient.getEmail());
    newUser.setPassword(newPatient.getPassword());
    newUser.setPermissionList(Collections.singletonList(defaultPermission));
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    PatientDTO savedPatientDTO = authService.register(newPatient);

    assertThat(savedPatientDTO).isNotNull();
    assertEquals(newPatient.getEmail(), savedPatientDTO.getEmail());
    assertEquals(newUser.getPassword(), newPatient.getPassword());
    assertEquals(1, newUser.getPermissionList().size());
    verify(userRepository, times(1)).findByEmail(newPatient.getEmail());
    verify(userRepository, times(1)).save(any(User.class));
    verify(permissionRepository, times(1)).findByRole("ROLE_PATIENT");
    verify(permissionRepository, times(0)).save(any(Permissions.class)); // Assuming defaultPermission exists
    verify(patientRepository, times(1)).save(patient);
    verify(patientMapper, times(1)).toModel(newPatient);
    verify(patientMapper, times(1)).toDto(patient);
  }

  @Test
  public void testRegisterPatient_UserExists() {
    PatientDTO registerRequest = new PatientDTO();
    registerRequest.setEmail("test@example.com");
    registerRequest.setPassword("testPassword");

    User existingUser = new User();
    existingUser.setEmail(registerRequest.getEmail());
    when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(existingUser);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.register(registerRequest));
    assertEquals("User with this email already exists.", exception.getMessage());

    verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
    verify(userRepository, times(0)).save(any(User.class));
    verify(permissionRepository, times(0)).findByRole(anyString());
    verify(patientRepository, times(0)).save(any(Patient.class));
    verify(patientMapper, times(0)).toModel(any(PatientDTO.class));
    verify(patientMapper, times(0)).toDto(any(Patient.class));
  }

  @Test
  public void testLogin_Success() {
    LoginRequest request = new LoginRequest();
    String email = "test@example.com";
    String password = "testPassword";
    request.setEmail(email);
    request.setPassword(password);


    UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email)
            .password(password)
            .roles("PATIENT")
            .build();
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

    when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);

    UserDetails result = authService.login(request);

    assertEquals(email, result.getUsername());
    verify(userDetailsService, times(1)).loadUserByUsername(email);
    verify(passwordEncoder, times(1)).matches(password, userDetails.getPassword());
  }

  @Test
  public void testLogin_InvalidCredentials() {
    LoginRequest request = new LoginRequest();
    String email = "test@example.com";
    String password = "testPassword";
    request.setEmail(email);
    request.setPassword(password);

    UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email)
            .password("differentPassword")
            .roles("PATIENT")
            .build();
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

    when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(false);

    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> authService.login(request));
    assertEquals("Invalid credentials", exception.getMessage());

    verify(userDetailsService, times(1)).loadUserByUsername(email);
    verify(passwordEncoder, times(1)).matches(password, userDetails.getPassword());
  }

  @Test
  public void testAddDoctor_Successfully() {
    DoctorDTO doctorRequest = new DoctorDTO();
    doctorRequest.setName("Dr. John Doe");
    doctorRequest.setEmail("john.doe@example.com");
    doctorRequest.setPassword("password123");
    doctorRequest.setSpecialization(Specialization.DENTIST);
    doctorRequest.setSchedule("Monday to Friday, 9 AM - 5 PM");

    when(userRepository.findByEmail(doctorRequest.getEmail())).thenReturn(null);

    Permissions defaultPermission = new Permissions();
    defaultPermission.setId(1L);
    defaultPermission.setRole("ROLE_DOCTOR");
    when(permissionRepository.findByRole("ROLE_DOCTOR")).thenReturn(defaultPermission);

    Doctor doctor = new Doctor();
    doctor.setEmail(doctorRequest.getEmail());
    doctor.setName(doctorRequest.getName());
    doctor.setSpecialization(doctorRequest.getSpecialization());
    doctor.setSchedule(doctorRequest.getSchedule());

    when(doctorRepository.save(doctor)).thenReturn(doctor);
    when(doctorMapper.toModel(doctorRequest)).thenReturn(doctor);
    when(doctorMapper.toDto(doctor)).thenReturn(doctorRequest);
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

    User newUser = new User();
    newUser.setEmail(doctorRequest.getEmail());
    newUser.setPassword(doctorRequest.getPassword());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    DoctorDTO result = authService.addDoctor(doctorRequest);

    assertThat(result).isNotNull();
    assertEquals(doctorRequest.getEmail(), result.getEmail());
    assertEquals(doctorRequest.getName(), result.getName());
    assertEquals(doctorRequest.getSpecialization(), result.getSpecialization());
    assertEquals(doctorRequest.getSchedule(), result.getSchedule());

    verify(userRepository, times(1)).findByEmail(doctorRequest.getEmail());
    verify(permissionRepository, times(1)).findByRole("ROLE_DOCTOR");
    verify(userRepository, times(1)).save(any(User.class));
    verify(doctorRepository, times(1)).save(any(Doctor.class));
  }

}
