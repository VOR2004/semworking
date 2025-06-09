package ru.itis.semworkapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
    Optional<ChatEntity> findBySellerAndBuyer(UserEntity seller, UserEntity buyer);
    List<ChatEntity> findAllBySellerOrBuyer(UserEntity seller, UserEntity buyer);
}