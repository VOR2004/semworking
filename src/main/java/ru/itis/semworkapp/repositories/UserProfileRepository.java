package ru.itis.semworkapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semworkapp.entities.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserId(UUID id);
}
