package kz.projects.telemedicine.dto.requests;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleRequest(
        LocalDate date,
        LocalTime time
) { }
