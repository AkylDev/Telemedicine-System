package kz.projects.telemedicine.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.projects.telemedicine.dto.AppointmentDTO;
import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.dto.requests.RescheduleRequest;
import kz.projects.telemedicine.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Patient appointment related operations")
public class PatientController {

  private final PatientService patientService;

  @Operation(summary = "Get list of all doctors")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "List of doctors retrieved successfully")
  })
  @GetMapping(value = "/doctors")
  public ResponseEntity<List<DoctorDTO>> getDoctors(){
    return new ResponseEntity<>(patientService.getDoctors(), HttpStatus.OK);
  }

  @Operation(summary = "Make a new appointment")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid appointment data")
  })
  @PostMapping
  public ResponseEntity<AppointmentDTO> makeAppointment(@RequestBody AppointmentDTO appointment){
    AppointmentDTO createdAppointment = patientService.makeAppointment(appointment);
    return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
  }

  @Operation(summary = "Change an existing appointment")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Appointment changed successfully"),
          @ApiResponse(responseCode = "404", description = "Appointment not found"),
          @ApiResponse(responseCode = "400", description = "Invalid appointment data")
  })
  @PutMapping("/{id}")
  private ResponseEntity<AppointmentDTO> changeAppointment(@PathVariable(name = "id") Long id,
                                                           @RequestBody RescheduleRequest request){
    return new ResponseEntity<>(patientService.changeAppointment(id, request), HttpStatus.OK);
  }

  @Operation(summary = "Get list of all appointments for the current patient")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
          @ApiResponse(responseCode = "404", description = "Patient not found")
  })
  @GetMapping
  public ResponseEntity<List<AppointmentDTO>> getAppointments(){
    return new ResponseEntity<>(patientService.getAppointments(), HttpStatus.OK);
  }

  @Operation(summary = "Cancel an existing appointment")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Appointment cancelled successfully"),
          @ApiResponse(responseCode = "404", description = "Appointment not found"),
          @ApiResponse(responseCode = "403", description = "Unauthorized to cancel the appointment")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelAppointment(@PathVariable(name = "id") Long id) {
    patientService.cancelAppointment(id);
    return ResponseEntity.noContent().build();
  }
}
