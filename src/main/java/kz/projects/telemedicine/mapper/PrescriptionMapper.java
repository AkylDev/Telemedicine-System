package kz.projects.telemedicine.mapper;

import kz.projects.telemedicine.dto.PrescriptionDTO;
import kz.projects.telemedicine.model.Prescriptions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, DoctorMapper.class})
public interface PrescriptionMapper {
  PrescriptionDTO toDto(Prescriptions prescriptions);

  List<PrescriptionDTO> toDtoList(List<Prescriptions> prescriptionsList);
}
