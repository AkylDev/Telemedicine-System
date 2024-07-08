package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.model.Doctor;
import kz.projects.telemedicine.repositories.DoctorRepository;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

  private final DoctorRepository doctorRepository;

  @Override
  public List<Doctor> getDoctors() {
    return doctorRepository.findAll();
  }
}
