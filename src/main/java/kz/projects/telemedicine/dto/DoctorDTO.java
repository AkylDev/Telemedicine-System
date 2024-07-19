package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.Specialization;
public record DoctorDTO(Long id, String name, String email, String password, Specialization specialization, String schedule) { }

