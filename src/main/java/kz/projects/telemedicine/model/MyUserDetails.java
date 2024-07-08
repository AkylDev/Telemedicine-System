package kz.projects.telemedicine.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {

  final Patient patient;

  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Permissions> permissionList;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.permissionList;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return patient.getEmail();
  }
}
