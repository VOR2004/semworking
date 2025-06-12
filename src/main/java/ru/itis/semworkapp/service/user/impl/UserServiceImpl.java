package ru.itis.semworkapp.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.semworkapp.entities.UserProfile;
import ru.itis.semworkapp.exceptions.EmailAlreadyExistsException;
import ru.itis.semworkapp.exceptions.PasswordMismatchException;
import ru.itis.semworkapp.forms.RegistrationForm;
import ru.itis.semworkapp.repositories.user.UserProfileRepository;
import ru.itis.semworkapp.repositories.user.UserRepository;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.service.user.UserService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                Collections.singleton(userEntity::getRole)
        );
    }
    @Override
    public UserEntity requireUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity requireUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void registerUser(RegistrationForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity user = UserEntity.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .role("ROLE_USER")
                .build();

        UserProfile profile = UserProfile.builder()
                .user(user)
                .build();

        userRepository.save(user);
        userProfileRepository.save(profile);
    }

}


