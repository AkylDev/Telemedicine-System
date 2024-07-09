package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.dto.ChangeRecordRequest;
import kz.projects.telemedicine.exceptions.PatientNotFoundException;
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
    patient.setMedicalHistory(request.getRecord());
    patientRepository.save(patient);

    return patient.getMedicalHistory();

  }

  @Override
  public Prescriptions makePrescription(Long id, Prescriptions prescriptions) {
    Optional<Doctor> doctorOptional = doctorRepository.findByEmail(getCurrentUser().getEmail());
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isPresent() && doctorOptional.isPresent()){
      Doctor doctor = doctorOptional.get();
      prescriptions.setDoctor(doctor);

      Patient patient = patientOptional.get();
      prescriptions.setPatient(patient);

      return prescriptionsRepository.save(prescriptions);
    } else {
      throw new PatientNotFoundException("Patient not found");
    }
  }

  @Override
  public List<Prescriptions> getPatientPrescriptions(Long id) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
    if (patientOptional.isPresent()){
      Patient patient = patientOptional.get();
      return prescriptionsRepository.findAllByPatient(patient).orElseThrow();
    } else {
      throw new PatientNotFoundException("Patient not found");
    }
  }
}
