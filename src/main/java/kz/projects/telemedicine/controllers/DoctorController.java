package kz.projects.telemedicine.controllers;

import kz.projects.telemedicine.dto.ChangeRecordRequest;
import kz.projects.telemedicine.model.Patient;
import kz.projects.telemedicine.model.Prescriptions;
import kz.projects.telemedicine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class DoctorController {
  private final DoctorService doctorService;

  @GetMapping("/{id}/record")
  public ResponseEntity<String> getPatientRecord(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(doctorService.getPatientRecords(id), HttpStatus.OK);
  }

  @PutMapping("/{id}/record")
  public ResponseEntity<Patient> getPatientRecord(@PathVariable(name = "id") Long id,
                                                  @RequestBody ChangeRecordRequest request){
    return new ResponseEntity<>(doctorService.changePatientRecord(id, request), HttpStatus.OK);
  }

  @PostMapping("/{id}/prescriptions")
  public ResponseEntity<Prescriptions> makePrescription(@PathVariable(name = "id") Long id,
                                                        @RequestBody Prescriptions prescriptions){
    return new ResponseEntity<>(doctorService.makePrescription(id, prescriptions), HttpStatus.CREATED);
  }

  @GetMapping("/{id}/prescriptions")
  public ResponseEntity<List<Prescriptions>> getPrescription(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(doctorService.getPatientPrescriptions(id), HttpStatus.CREATED);
  }

}
