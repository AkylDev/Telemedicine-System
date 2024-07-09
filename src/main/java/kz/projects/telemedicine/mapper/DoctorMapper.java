package kz.projects.telemedicine.mapper;

import kz.projects.telemedicine.dto.DoctorDTO;
import kz.projects.telemedicine.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  @Mapping(target = "password", ignore = true)
  DoctorDTO toDto(Doctor doctor);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  Doctor toModel(DoctorDTO request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  List<DoctorDTO> toDtoList(List<Doctor> doctorList);
}
