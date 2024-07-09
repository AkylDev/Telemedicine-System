package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.model.Appointment;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

  @PutMapping("/{id}")
  private ResponseEntity<Appointment> changeAppointment(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(patientService.changeAppointment(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Appointment>> getAppointments(){
    return new ResponseEntity<>(patientService.getAppointments(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelAppointment(@PathVariable(name = "id") Long id) {
    patientService.cancelAppointment(id);
    return ResponseEntity.noContent().build();
  }
}
