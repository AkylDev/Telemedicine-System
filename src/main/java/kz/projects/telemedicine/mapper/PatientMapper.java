package kz.projects.telemedicine.mapper;

import kz.projects.telemedicine.dto.PatientDTO;
import kz.projects.telemedicine.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

  @Mapping(target = "password", ignore = true)
  PatientDTO toDto(Patient patient);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "medicalHistory", ignore = true)
  @Mapping(target = "user", ignore = true)
  Patient toModel(PatientDTO request);
}
