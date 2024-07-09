package kz.projects.telemedicine.mapper;


import kz.projects.telemedicine.dto.AppointmentDTO;
import kz.projects.telemedicine.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, DoctorMapper.class})
public interface AppointmentMapper {

  @Mapping(source = "patient", target = "patient")
  @Mapping(source = "doctor", target = "doctor")
  AppointmentDTO toResponseDto(Appointment appointment);

  @Mapping(source = "patient", target = "patient")
  @Mapping(source = "doctor", target = "doctor")
  Appointment toModel(AppointmentDTO appointmentDTO);

  @Mapping(source = "patient", target = "patient")
  @Mapping(source = "doctor", target = "doctor")
  List<AppointmentDTO> toDtoList(List<Appointment> appointments);
}
