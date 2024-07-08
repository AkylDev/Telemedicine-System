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
  public String getDoctors() {
    List<Doctor> allDoctors = doctorRepository.findAll();
    StringBuilder list = new StringBuilder();

    for (Doctor doc : allDoctors){
      list.append(doc.getName()).append(" is available at ").append(doc.getSchedule()).append("\n");
    }

    return String.valueOf(list);
  }
}
