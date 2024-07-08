package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.DoctorRequest;
import kz.projects.telemedicine.dto.LoginRequest;
import kz.projects.telemedicine.dto.RegisterRequest;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

  @Override
  public Patient register(RegisterRequest registerRequest) {
    User checkUser = userRepository.findByEmail(registerRequest.getEmail());

    if(checkUser==null){
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

      Patient patient = new Patient();
      patient.setName(registerRequest.getName());
      patient.setEmail(registerRequest.getEmail());
      patient.setPhone(registerRequest.getPhone());
      patient.setMedicalHistory(registerRequest.getMedicalHistory());

      patient.setUser(user);
      userRepository.save(user);

      return patientRepository.save(patient);
    }
    else {
      return null;
    }
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
  public Doctor addDoctor(DoctorRequest doctorRequest) {
    User checkUser = userRepository.findByEmail(doctorRequest.getEmail());

    if(checkUser==null){
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

      Doctor doctor = new Doctor();
      doctor.setName(doctorRequest.getName());
      doctor.setEmail(doctorRequest.getEmail());
      doctor.setSpecialization(doctorRequest.getSpecialization());
      doctor.setSchedule(doctorRequest.getSchedule());
      doctor.setUser(user);

      userRepository.save(user);

      return doctorRepository.save(doctor);
    }
    else {
      return null;
    }
  }

}
