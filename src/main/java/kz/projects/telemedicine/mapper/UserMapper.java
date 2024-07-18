package kz.projects.telemedicine.mapper;

import kz.projects.telemedicine.dto.UserDTO;
import kz.projects.telemedicine.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public static UserDTO toDto(User user){
    if (user == null){
      return null;
    }

    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setPermissionList(user.getPermissionList());

    return dto;
  }

}
