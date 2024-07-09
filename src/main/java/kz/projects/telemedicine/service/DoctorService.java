package kz.projects.telemedicine.service;

import kz.projects.telemedicine.dto.ChangeRecordRequest;
import kz.projects.telemedicine.dto.PrescriptionDTO;

import java.util.List;

public interface DoctorService {
  String getPatientRecords(Long id);

  String changePatientRecord(Long id, ChangeRecordRequest request);

  PrescriptionDTO makePrescription(Long id, PrescriptionDTO prescriptions);

  List<PrescriptionDTO> getPatientPrescriptions(Long id);
}
