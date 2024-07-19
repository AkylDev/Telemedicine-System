package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.requests.ChangeRecordRequest;
import kz.projects.telemedicine.dto.PrescriptionDTO;
import kz.projects.telemedicine.exceptions.PatientNotFoundException;
import kz.projects.telemedicine.mapper.PrescriptionMapper;
import kz.projects.telemedicine.model.Doctor;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.Prescriptions;
import kz.projects.telemedicine.model.User;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.repositories.PatientRepository;
import kz.projects.telemedicine.repositories.PrescriptionsRepository;
import kz.projects.telemedicine.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private AuthService authService;

  @Mock
  private DoctorRepository doctorRepository;

  @Mock
  private PrescriptionsRepository prescriptionsRepository;

  @Mock
  private PrescriptionMapper prescriptionMapper;

  @InjectMocks
  private DoctorServiceImpl doctorService;

  @Test
  public void testGetPatientRecordsSuccessfully() {
    Long patientId = 1L;
    String medicalHistory = "Medical history details";

    Patient patient = new Patient();
    patient.setId(patientId);
    patient.setMedicalHistory(medicalHistory);

    when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

    String result = doctorService.getPatientRecords(patientId);

    assertNotNull(result);
    assertEquals(medicalHistory, result);

    verify(patientRepository, times(1)).findById(patientId);
  }

  @Test
  public void testGetPatientRecords_PatientNotFound() {
    Long patientId = 1L;

    when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

    PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
            () -> doctorService.getPatientRecords(patientId));
    assertEquals("Patient not found", exception.getMessage());

    verify(patientRepository, times(1)).findById(patientId);
  }

  @Test
  public void testChangePatientRecordSuccessfully() {
    Long patientId = 1L;
    String newMedicalHistory = "Updated medical history";

    ChangeRecordRequest changeRecordRequest = new ChangeRecordRequest(newMedicalHistory);

    Patient patient = new Patient();
    patient.setId(patientId);
    patient.setMedicalHistory("Old medical history");

    when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
    when(patientRepository.save(any(Patient.class))).thenReturn(patient);

    String result = doctorService.changePatientRecord(patientId, changeRecordRequest);

    assertNotNull(result);
    assertEquals(newMedicalHistory, result);

    verify(patientRepository, times(1)).findById(patientId);
    verify(patientRepository, times(1)).save(patient);
  }

  @Test
  public void testChangePatientRecordWhenPatientNotFound() {
    Long patientId = 1L;
    ChangeRecordRequest changeRecordRequest = new ChangeRecordRequest("New record");

    when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

    PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
            () -> doctorService.changePatientRecord(patientId, changeRecordRequest));
    assertEquals("Patient not found", exception.getMessage());

    verify(patientRepository, times(1)).findById(patientId);
    verify(patientRepository, times(0)).save(any(Patient.class));
  }

  @Test
  public void testMakePrescriptionSuccessfully() {
    Long patientId = 1L;
    String doctorEmail = "doctor@example.com";

    User currentUser = new User();
    currentUser.setEmail(doctorEmail);
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    Doctor doctor = new Doctor();
    doctor.setEmail(doctorEmail);
    when(doctorRepository.findByEmail(doctorEmail)).thenReturn(Optional.of(doctor));

    Patient patient = new Patient();
    patient.setId(patientId);
    when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

    PrescriptionDTO prescriptionDTO = new PrescriptionDTO(
            null, // id will be set by the repository
            null, // patient will be set by the repository
            null, // doctor will be set by the repository
            "Medication",
            "Dosage",
            "Duration"
    );

    Prescriptions prescription = new Prescriptions();
    prescription.setDoctor(doctor);
    prescription.setPatient(patient);
    prescription.setMedication(prescriptionDTO.medication());
    prescription.setDosage(prescriptionDTO.dosage());
    prescription.setDuration(prescriptionDTO.duration());

    when(prescriptionsRepository.save(any(Prescriptions.class))).thenReturn(prescription);
    when(prescriptionMapper.toDto(any(Prescriptions.class))).thenReturn(prescriptionDTO);

    PrescriptionDTO result = doctorService.makePrescription(patientId, prescriptionDTO);

    assertNotNull(result);
    assertEquals(prescriptionDTO.medication(), result.medication());
    assertEquals(prescriptionDTO.dosage(), result.dosage());
    assertEquals(prescriptionDTO.duration(), result.duration());

    verify(doctorRepository, times(1)).findByEmail(doctorEmail);
    verify(patientRepository, times(1)).findById(patientId);
    verify(prescriptionsRepository, times(1)).save(any(Prescriptions.class));
    verify(prescriptionMapper, times(1)).toDto(any(Prescriptions.class));
  }

  @Test
  public void testMakePrescriptionWhenPatientNotFound() {
    Long patientId = 1L;
    String doctorEmail = "doctor@example.com";

    User currentUser = new User();
    currentUser.setEmail(doctorEmail);
    when(authService.getCurrentSessionUser()).thenReturn(currentUser);

    when(doctorRepository.findByEmail(doctorEmail)).thenReturn(Optional.of(new Doctor()));
    when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

    PrescriptionDTO prescriptionDTO = new PrescriptionDTO(
            null, // id will be set by the repository
            null, // patient will be set by the repository
            null, // doctor will be set by the repository
            "Medication",
            "Dosage",
            "Duration"
    );

    PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
            () -> doctorService.makePrescription(patientId, prescriptionDTO));
    assertEquals("Patient not found", exception.getMessage());

    verify(doctorRepository, times(1)).findByEmail(doctorEmail);
    verify(patientRepository, times(1)).findById(patientId);
    verify(prescriptionsRepository, never()).save(any(Prescriptions.class));
    verify(prescriptionMapper, never()).toDto(any(Prescriptions.class));
  }

  @Test
  public void testGetPatientPrescriptions_Success() {
    Long patientId = 1L;

    Patient patient = new Patient();
    patient.setId(patientId);
    when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

    List<Prescriptions> prescriptions = new ArrayList<>();
    Prescriptions prescription1 = new Prescriptions();
    prescription1.setId(1L);
    Prescriptions prescription2 = new Prescriptions();
    prescription2.setId(2L);
    prescriptions.add(prescription1);
    prescriptions.add(prescription2);

    when(prescriptionsRepository.findAllByPatient(patient)).thenReturn(Optional.of(prescriptions));

    List<PrescriptionDTO> prescriptionDTOs = new ArrayList<>();
    PrescriptionDTO prescriptionDTO1 = new PrescriptionDTO(
            1L, // id
            null, // patient
            null, // doctor
            null, // medication
            null, // dosage
            null  // duration
    );
    PrescriptionDTO prescriptionDTO2 = new PrescriptionDTO(
            2L, // id
            null, // patient
            null, // doctor
            null, // medication
            null, // dosage
            null  // duration
    );
    prescriptionDTOs.add(prescriptionDTO1);
    prescriptionDTOs.add(prescriptionDTO2);

    when(prescriptionMapper.toDtoList(prescriptions)).thenReturn(prescriptionDTOs);

    List<PrescriptionDTO> result = doctorService.getPatientPrescriptions(patientId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(prescriptionDTO1.id(), result.get(0).id());
    assertEquals(prescriptionDTO2.id(), result.get(1).id());

    verify(patientRepository, times(1)).findById(patientId);
    verify(prescriptionsRepository, times(1)).findAllByPatient(patient);
    verify(prescriptionMapper, times(1)).toDtoList(prescriptions);
  }

  @Test
  public void testGetPatientPrescriptions_NoPrescriptionsFound() {
    Long patientId = 1L;

    Patient patient = new Patient();
    patient.setId(patientId);
    when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

    when(prescriptionsRepository.findAllByPatient(patient)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class,
            () -> doctorService.getPatientPrescriptions(patientId));

    verify(patientRepository, times(1)).findById(patientId);
    verify(prescriptionsRepository, times(1)).findAllByPatient(patient);
    verify(prescriptionMapper, never()).toDtoList(anyList());
  }
}

