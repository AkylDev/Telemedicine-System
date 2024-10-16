package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.UserDTO;
import kz.projects.telemedicine.dto.requests.LoginRequest;
import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.mapper.DoctorMapper;
import kz.projects.telemedicine.mapper.PatientMapper;
import kz.projects.telemedicine.mapper.UserMapper;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Реализация сервиса для аутентификации и регистрации пользователей.
 * Обрабатывает регистрацию пациентов и докторов, а также аутентификацию пользователей.
 */
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

  /**
   * Регистрирует нового пациента.
   * Проверяет, существует ли уже пользователь с таким email,
   * создаёт нового пользователя и пациента, устанавливает необходимые разрешения.
   *
   * @param registerRequest данные для регистрации пациента
   * @return зарегистрированный пациент в виде {@link PatientDTO}
   * @throws IllegalArgumentException если пользователь с таким email уже существует
   */
  @Override
  public PatientDTO register(PatientDTO registerRequest) {
    User checkUser = userRepository.findByEmail(registerRequest.email());
    if (checkUser != null) {
      throw new IllegalArgumentException("User with this email already exists.");
    }

    User user = new User();
    user.setEmail(registerRequest.email());
    user.setPassword(passwordEncoder.encode(registerRequest.password()));

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

  /**
   * Выполняет аутентификацию пользователя.
   * Проверяет корректность введённых данных и устанавливает аутентификацию в контексте безопасности.
   *
   * @param request запрос на вход в систему с email и паролем
   * @return данные пользователя в виде {@link UserDTO}
   * @throws UsernameNotFoundException если учётные данные неверны
   */
  @Override
  public UserDTO login(LoginRequest request) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
    if (passwordEncoder.matches(request.password(), userDetails.getPassword())) {
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                      userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      return UserMapper.toDto((User) userDetails);
    } else {
      throw new BadCredentialsException("Invalid credentials");
    }
  }

  /**
   * Добавляет нового доктора.
   * Проверяет, существует ли уже пользователь с таким email,
   * создаёт нового пользователя и доктора, устанавливает необходимые разрешения.
   *
   * @param doctorRequest данные для добавления доктора
   * @return добавленный доктор в виде {@link DoctorDTO}
   * @throws IllegalArgumentException если пользователь с таким email уже существует
   */
  @Override
  public DoctorDTO addDoctor(DoctorDTO doctorRequest) {
    User checkUser = userRepository.findByEmail(doctorRequest.email());
    if (checkUser != null) {
      throw new IllegalArgumentException("User with this email already exists.");
    }

    User user = new User();
    user.setEmail(doctorRequest.email());
    user.setPassword(passwordEncoder.encode(doctorRequest.password()));

    Permissions defaultPermission = permissionRepository.findByRole("ROLE_DOCTOR");
    if (defaultPermission == null) {
      defaultPermission = new Permissions();
      defaultPermission.setRole("ROLE_DOCTOR");
      defaultPermission = permissionRepository.save(defaultPermission);
    }
    user.setPermissionList(Collections.singletonList(defaultPermission));

    User savedUser = userRepository.save(user);

    Doctor doctor = doctorMapper.toModel(doctorRequest);
    doctor.setUser(savedUser);

    Doctor savedDoctor = doctorRepository.save(doctor);

    return doctorMapper.toDto(savedDoctor);
  }

  /**
   * Возвращает текущего аутентифицированного пользователя из Spring Security контекста.
   *
   * @return текущий пользователь в виде {@link User} или {@code null}, если пользователь не аутентифицирован
   */
  @Override
  public User getCurrentSessionUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

}
