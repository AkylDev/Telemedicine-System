package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.model.Doctor;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PatientController {

  private final PatientService patientService;

  @GetMapping(value = "/doctors")
  public ResponseEntity<List<Doctor>> getDoctors(){
    return new ResponseEntity<>(patientService.getDoctors(), HttpStatus.OK);
  }
}
