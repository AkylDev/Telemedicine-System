package kz.projects.telemedicine.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.projects.telemedicine.dto.requests.ChangeRecordRequest;
import kz.projects.telemedicine.dto.PrescriptionDTO;
import kz.projects.telemedicine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Operations related to patient records and prescriptions")
public class DoctorController {
  private final DoctorService doctorService;

  @Operation(summary = "Get a patient's medical record")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Patient record retrieved successfully"),
          @ApiResponse(responseCode = "404", description = "Patient not found")
  })
  @GetMapping("/{id}/record")
  public ResponseEntity<String> getPatientRecord(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(doctorService.getPatientRecords(id), HttpStatus.OK);
  }

  @Operation(summary = "Change a patient's medical record")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Patient record changed successfully"),
          @ApiResponse(responseCode = "404", description = "Patient not found"),
          @ApiResponse(responseCode = "400", description = "Invalid record data")
  })
  @PutMapping("/{id}/record")
  public ResponseEntity<String> getPatientRecord(@PathVariable(name = "id") Long id,
                                                     @RequestBody ChangeRecordRequest request){
    return new ResponseEntity<>(doctorService.changePatientRecord(id, request), HttpStatus.OK);
  }

  @Operation(summary = "Create a prescription for a patient")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Prescription created successfully"),
          @ApiResponse(responseCode = "404", description = "Patient not found"),
          @ApiResponse(responseCode = "400", description = "Invalid prescription data")
  })
  @PostMapping("/{id}/prescriptions")
  public ResponseEntity<PrescriptionDTO> makePrescription(@PathVariable(name = "id") Long id,
                                                          @RequestBody PrescriptionDTO prescriptions){
    return new ResponseEntity<>(doctorService.makePrescription(id, prescriptions), HttpStatus.CREATED);
  }

  @Operation(summary = "Get a patient's prescriptions")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Patient prescriptions retrieved successfully"),
          @ApiResponse(responseCode = "404", description = "Patient not found")
  })
  @GetMapping("/{id}/prescriptions")
  public ResponseEntity<List<PrescriptionDTO>> getPrescription(@PathVariable(name = "id") Long id){
    return new ResponseEntity<>(doctorService.getPatientPrescriptions(id), HttpStatus.OK);
  }

}
