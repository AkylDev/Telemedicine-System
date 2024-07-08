package kz.projects.telemedicine.service;

import kz.projects.telemedicine.model.Doctor;

import java.util.List;

public interface PatientService {
  List<Doctor> getDoctors();
}
