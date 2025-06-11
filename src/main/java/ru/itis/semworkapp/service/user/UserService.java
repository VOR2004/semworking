package ru.itis.semworkapp.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.forms.RegistrationForm;

import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    Optional<UserEntity> getUserByEmail(String email);
    UserEntity requireUserByEmail(String email);
    void registerUser(RegistrationForm form);
    UserEntity requireUserById(UUID id);
}
