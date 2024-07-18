package kz.projects.telemedicine.dto;

import kz.projects.telemedicine.model.Permissions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
  private Long id;

  private String email;

  private List<Permissions> permissionList;
}
