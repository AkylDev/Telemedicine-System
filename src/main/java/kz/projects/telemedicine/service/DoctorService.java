package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.ChangeRecordRequest;
import kz.projects.telemedicine.model.Prescriptions;

import java.util.List;

public interface DoctorService {
  String getPatientRecords(Long id);

  String changePatientRecord(Long id, ChangeRecordRequest request);

  Prescriptions makePrescription(Long id, Prescriptions prescriptions);

  List<Prescriptions> getPatientPrescriptions(Long id);
}
