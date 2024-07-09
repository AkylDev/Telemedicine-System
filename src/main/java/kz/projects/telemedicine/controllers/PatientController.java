package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.model.Appointment;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class PatientController {

  private final PatientService patientService;

  @GetMapping(value = "/doctors")
  public ResponseEntity<String> getDoctors(){
    return new ResponseEntity<>(patientService.getDoctors(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Appointment> makeAppointment(@RequestBody Appointment appointment){
    return new ResponseEntity<>(patientService.makeAppointment(appointment), HttpStatus.CREATED);
  }
}
