package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.mapper.DoctorMapper;
import kz.projects.telemedicine.mapper.PatientMapper;
import kz.projects.telemedicine.model.Doctor;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.Permissions;
import kz.projects.telemedicine.model.User;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.repositories.PermissionsRepository;
import kz.projects.telemedicine.repositories.UserRepository;
import kz.projects.telemedicine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PatientRepository patientRepository;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final PermissionsRepository  permissionRepository;

  private final MyUserDetailsService userDetailsService;

  private final DoctorRepository doctorRepository;

  private final DoctorMapper doctorMapper;

  private final PatientMapper patientMapper;

  @Override
  public PatientDTO register(PatientDTO registerRequest) {
    User checkUser = userRepository.findByEmail(registerRequest.getEmail());
    if (checkUser != null) {
      throw new IllegalArgumentException("User with this email already exists.");
    }

    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    Permissions defaultPermission = permissionRepository.findByRole("ROLE_PATIENT");
    if (defaultPermission == null) {
      defaultPermission = new Permissions();
      defaultPermission.setRole("ROLE_PATIENT");
      defaultPermission = permissionRepository.save(defaultPermission);
    }
    user.setPermissionList(Collections.singletonList(defaultPermission));

    Patient patient = patientMapper.toModel(registerRequest);
    patient.setMedicalHistory("");
    patient.setUser(user);

    userRepository.save(user);

    return patientMapper.toDto(patientRepository.save(patient));

  }

  @Override
  public User login(LoginRequest request) {
    User userDetails = (User) userDetailsService.loadUserByUsername(request.getEmail());
    if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                      userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      return userDetails;
    } else {
      throw new UsernameNotFoundException("Invalid credentials");
    }
  }

  @Override
  public DoctorDTO addDoctor(DoctorDTO doctorRequest) {
    User checkUser = userRepository.findByEmail(doctorRequest.getEmail());
    if (checkUser != null) {
      throw new IllegalArgumentException("User with this email already exists.");
    }

    User user = new User();
    user.setEmail(doctorRequest.getEmail());
    user.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));

    Permissions defaultPermission = permissionRepository.findByRole("ROLE_DOCTOR");
    if (defaultPermission == null) {
      defaultPermission = new Permissions();
      defaultPermission.setRole("ROLE_DOCTOR");
      defaultPermission = permissionRepository.save(defaultPermission);
    }
    user.setPermissionList(Collections.singletonList(defaultPermission));

    Doctor doctor = doctorMapper.toModel(doctorRequest);
    doctor.setUser(user);

    userRepository.save(user);

    return doctorMapper.toDto(doctorRepository.save(doctor));

  }

  @Override
  public User getCurrentSessionUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

}
