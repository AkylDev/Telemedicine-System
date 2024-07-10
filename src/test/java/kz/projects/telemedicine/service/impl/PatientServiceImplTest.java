package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.AppointmentDTO;
import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.RescheduleRequest;
import kz.projects.telemedicine.exceptions.AppointmentNotFoundException;
import kz.projects.telemedicine.exceptions.PatientNotFoundException;
import kz.projects.telemedicine.exceptions.UnauthorizedException;
import kz.projects.telemedicine.mapper.AppointmentMapper;
import kz.projects.telemedicine.model.*;
import kz.projects.telemedicine.repositories.AppointmentsRepository;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private DoctorRepository doctorRepository;

  @Mock
  private AppointmentsRepository appointmentsRepository;

  @Mock
  private AuthService authService;

  @Mock
  private AppointmentMapper appointmentMapper;

  @InjectMocks
  private PatientServiceImpl patientService;

  @Test
  public void testMakeAppointmentSuccessfully() {
    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Patient patient = new Patient();
    patient.setEmail(currentUser.getEmail());
    when(patientRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(patient));

    Doctor doctor = new Doctor();
    doctor.setId(1L);
    when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

    AppointmentDTO appointmentRequest = new AppointmentDTO();
    appointmentRequest.setDoctor(new DoctorDTO());
    appointmentRequest.getDoctor().setId(1L);
    appointmentRequest.setDate(LocalDate.now());
    appointmentRequest.setTime(LocalTime.MAX);

    Appointment appointment = new Appointment();
    appointment.setDoctor(doctor);
    appointment.setDate(LocalDate.now());
    appointment.setTime(LocalTime.MAX);

    when(appointmentsRepository.save(any(Appointment.class))).thenReturn(appointment);
    when(appointmentMapper.toModel(appointmentRequest)).thenReturn(appointment);
    when(appointmentMapper.toResponseDto(appointment)).thenReturn(appointmentRequest);

    AppointmentDTO result = patientService.makeAppointment(appointmentRequest);

    assertNotNull(result);
    assertEquals(appointmentRequest.getDoctor().getId(), result.getDoctor().getId());
    assertEquals(appointmentRequest.getDate(), result.getDate());
    assertEquals(appointmentRequest.getTime(), result.getTime());

    verify(patientRepository, times(1)).findByEmail("test@example.com");
    verify(doctorRepository, times(1)).findById(1L);
    verify(appointmentsRepository, times(1)).save(appointment);
  }

  @Test
  public void testMakeAppointment_PatientNotFound() {
    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    when(patientRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.empty());

    AppointmentDTO appointmentRequest = new AppointmentDTO();
    appointmentRequest.setDoctor(new DoctorDTO());
    appointmentRequest.getDoctor().setId(1L);
    appointmentRequest.setDate(LocalDate.now());
    appointmentRequest.setTime(LocalTime.MAX);

    PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
            () -> patientService.makeAppointment(appointmentRequest));
    assertEquals("Patient not found", exception.getMessage());

    verify(patientRepository, times(1)).findByEmail("test@example.com");
    verify(doctorRepository, never()).findById(anyLong());
    verify(appointmentsRepository, never()).save(any(Appointment.class));
  }

  @Test
  public void testChangeAppointment_Success() {
    Long appointmentId = 1L;
    RescheduleRequest request = new RescheduleRequest();
    request.setDate(LocalDate.now().plusDays(1));
    request.setTime(LocalTime.NOON);

    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Patient patient = new Patient();
    patient.setUser(currentUser);
    patient.setMedicalHistory("Previous history");

    Doctor doctor = new Doctor();
    doctor.setUser(new User());
    doctor.setEmail("doctorEmail");

    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);
    when(appointmentsRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    Appointment savedAppointment = new Appointment();
    savedAppointment.setId(appointmentId);
    savedAppointment.setPatient(patient);
    savedAppointment.setStatus(AppointmentStatus.RESCHEDULED);
    savedAppointment.setDate(LocalDate.now());
    savedAppointment.setTime(LocalTime.MAX);
    when(appointmentsRepository.save(appointment)).thenReturn(savedAppointment);

    AppointmentDTO expectedResponse = new AppointmentDTO();
    when(appointmentMapper.toResponseDto(savedAppointment)).thenReturn(expectedResponse);

    AppointmentDTO result = patientService.changeAppointment(appointmentId, request);

    assertNotNull(result);
    assertEquals(expectedResponse, result);

    verify(appointmentsRepository, times(1)).findById(appointmentId);
    verify(appointmentsRepository, times(1)).save(appointment);
    verify(appointmentMapper, times(1)).toResponseDto(savedAppointment);
  }

  @Test
  public void testChangeAppointment_Unauthorized() {
    Long appointmentId = 1L;
    RescheduleRequest request = new RescheduleRequest();
    request.setDate(LocalDate.now().plusDays(1));
    request.setTime(LocalTime.NOON);

    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Patient patient = new Patient();
    User differentUser = new User();
    differentUser.setEmail("different@example.com");
    patient.setUser(differentUser);

    Doctor doctor = new Doctor();
    doctor.setUser(new User());
    doctor.setEmail("doctorEmail");

    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setDoctor(doctor);
    appointment.setPatient(patient);
    when(appointmentsRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    UnauthorizedException exception = assertThrows(UnauthorizedException.class,
            () -> patientService.changeAppointment(appointmentId, request));
    assertEquals("You are not authorized to change this appointment", exception.getMessage());

    verify(appointmentsRepository, times(1)).findById(appointmentId);
    verify(appointmentsRepository, times(0)).save(any(Appointment.class));
    verify(appointmentMapper, times(0)).toResponseDto(any(Appointment.class));
  }

  @Test
  public void testCancelAppointment_Success() {
    Long appointmentId = 1L;
    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Patient patient = new Patient();
    patient.setUser(currentUser);
    patient.setMedicalHistory("Previous history");

    Doctor doctor = new Doctor();
    doctor.setUser(new User());
    doctor.setEmail("doctorEmail");

    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setDoctor(doctor);
    appointment.setPatient(patient);
    when(appointmentsRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    patientService.cancelAppointment(appointmentId);

    verify(appointmentsRepository, times(1)).findById(appointmentId);
    verify(appointmentsRepository, times(1)).deleteById(appointmentId);
  }

  @Test
  public void testCancelAppointment_AppointmentNotFound() {
    Long appointmentId = 1L;
    when(appointmentsRepository.findById(appointmentId)).thenReturn(Optional.empty());

    AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class,
            () -> patientService.cancelAppointment(appointmentId));
    assertEquals("Appointment not found", exception.getMessage());

    verify(appointmentsRepository, times(1)).findById(appointmentId);
    verify(appointmentsRepository, times(0)).deleteById(appointmentId);
  }

  @Test
  public void testGetAppointments_Success() {
    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Patient patient = new Patient();
    patient.setEmail(currentUser.getEmail());
    when(patientRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(patient));

    List<Appointment> appointments = new ArrayList<>();
    Appointment appointment1 = new Appointment();
    Appointment appointment2 = new Appointment();
    appointments.add(appointment1);
    appointments.add(appointment2);
    when(appointmentsRepository.findAllByPatient(patient)).thenReturn(appointments);

    List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
    AppointmentDTO appointmentDTO1 = new AppointmentDTO();
    AppointmentDTO appointmentDTO2 = new AppointmentDTO();
    appointmentDTOs.add(appointmentDTO1);
    appointmentDTOs.add(appointmentDTO2);
    when(appointmentMapper.toDtoList(appointments)).thenReturn(appointmentDTOs);

    List<AppointmentDTO> result = patientService.getAppointments();

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(authService, times(1)).getCurrentSessionUser();
    verify(patientRepository, times(1)).findByEmail(currentUser.getEmail());
    verify(appointmentsRepository, times(1)).findAllByPatient(patient);
    verify(appointmentMapper, times(1)).toDtoList(appointments);
  }

  @Test
  public void testGetAppointments_PatientNotFound() {
    User currentUser = new User();
    currentUser.setEmail("test@example.com");
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    when(patientRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.empty());

    PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
            () -> patientService.getAppointments());
    assertEquals("Patient not found", exception.getMessage());

    verify(authService, times(1)).getCurrentSessionUser();
    verify(patientRepository, times(1)).findByEmail(currentUser.getEmail());
    verify(appointmentsRepository, times(0)).findAllByPatient(any(Patient.class));
    verify(appointmentMapper, times(0)).toDtoList(anyList());
  }
}
