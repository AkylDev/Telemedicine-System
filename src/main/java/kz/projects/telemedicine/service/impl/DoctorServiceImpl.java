package kz.projects.telemedicine.service.impl;

import jakarta.transaction.Transactional;
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
import kz.projects.telemedicine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

  private final PatientRepository patientRepository;

  private final DoctorRepository doctorRepository;

  private final PrescriptionsRepository prescriptionsRepository;

  private final AuthService authService;

  private final PrescriptionMapper prescriptionMapper;
  public final User getCurrentUser(){
    return authService.getCurrentSessionUser();
  }

  @Override
  public String getPatientRecords(Long id) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isEmpty()){
      throw new PatientNotFoundException("Patient not found");
    }
    Patient patient = patientOptional.get();
    return patient.getMedicalHistory();
  }

  @Override
  public String changePatientRecord(Long id, ChangeRecordRequest request) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isEmpty()){
      throw new PatientNotFoundException("Patient not found");
    }
    Patient patient = patientOptional.get();
    patient.setMedicalHistory(request.record());
    patientRepository.save(patient);

    return patient.getMedicalHistory();
  }

  @Override
  @Transactional
  public PrescriptionDTO makePrescription(Long id, PrescriptionDTO prescriptionDTO) {
    Optional<Doctor> doctorOptional = doctorRepository.findByEmail(getCurrentUser().getEmail());
    Optional<Patient> patientOptional = patientRepository.findById(id);

    if (patientOptional.isPresent() && doctorOptional.isPresent()){
      Doctor doctor = doctorOptional.get();
      Patient patient = patientOptional.get();

      Prescriptions prescription = new Prescriptions();
      prescription.setDoctor(doctor);
      prescription.setPatient(patient);

      prescription.setMedication(prescriptionDTO.medication());
      prescription.setDosage(prescriptionDTO.dosage());
      prescription.setDuration(prescriptionDTO.duration());

      return prescriptionMapper.toDto(prescriptionsRepository.save(prescription));
    } else {
      throw new PatientNotFoundException("Patient not found");
    }
  }

  @Override
  public List<PrescriptionDTO> getPatientPrescriptions(Long id) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isPresent()){
      Patient patient = patientOptional.get();
      List<Prescriptions> prescriptions = prescriptionsRepository.findAllByPatient(patient).orElseThrow();
      return prescriptionMapper.toDtoList(prescriptions);
    } else {
      throw new PatientNotFoundException("Patient not found");
    }
  }
}
