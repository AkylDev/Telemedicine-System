package kz.projects.telemedicine.service.impl;

import kz.projects.telemedicine.model.User;
import kz.projects.telemedicine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link UserDetailsService} для загрузки пользовательских данных по имени пользователя (email).
 * Используется Spring Security для аутентификации пользователей.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  /**
   * Загружает данные пользователя по имени пользователя (email).
   * Проверяет наличие пользователя в репозитории и возвращает его данные.
   *
   * @param username email пользователя
   * @return {@link UserDetails} объект, представляющий найденного пользователя
   * @throws UsernameNotFoundException если пользователь с указанным email не найден
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user != null) {
      return user;
    } else {
      throw new UsernameNotFoundException(username);
    }
  }
}
