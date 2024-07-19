package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.Permissions;

import java.util.List;


public record UserDTO(Long id, String email, List<Permissions> permissionList) {}
