package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.dto.AppointmentDTO;
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
  public ResponseEntity<AppointmentDTO> makeAppointment(@RequestBody AppointmentDTO appointment){
    AppointmentDTO createdAppointment = patientService.makeAppointment(appointment);
    return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  private ResponseEntity<AppointmentDTO> changeAppointment(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(patientService.changeAppointment(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<AppointmentDTO>> getAppointments(){
    return new ResponseEntity<>(patientService.getAppointments(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelAppointment(@PathVariable(name = "id") Long id) {
    patientService.cancelAppointment(id);
    return ResponseEntity.noContent().build();
  }
}
