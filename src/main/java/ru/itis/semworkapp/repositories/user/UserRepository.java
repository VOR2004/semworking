package ru.itis.semworkapp.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semworkapp.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}
