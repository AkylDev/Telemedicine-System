package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class DoctorController {
  private final DoctorService doctorService;

  @GetMapping("/{id}/record")
  public ResponseEntity<String> getPatientRecord(@PathVariable(name = "id") Long id){

    return new ResponseEntity<>(doctorService.getPatientRecords(id), HttpStatus.OK);
  }


}
