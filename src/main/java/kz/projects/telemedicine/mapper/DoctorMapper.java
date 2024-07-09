package kz.projects.telemedicine.mapper;

import kz.projects.telemedicine.dto.DoctorRequest;
import kz.projects.telemedicine.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  @Mapping(target = "password", ignore = true)
  DoctorRequest toDto(Doctor doctor);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  Doctor toModel(DoctorRequest request);
}
