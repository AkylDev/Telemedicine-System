package kz.projects.telemedicine.dto.requests;


public record LoginRequest(
        String email,
        String password
) { }

