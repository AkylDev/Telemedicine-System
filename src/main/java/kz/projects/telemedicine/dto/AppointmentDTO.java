package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.AppointmentStatus;


import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDTO(
        Long id,
        PatientDTO patient,
        DoctorDTO doctor,
        LocalDate date,
        LocalTime time,
        AppointmentStatus status,
        String info
) { }

