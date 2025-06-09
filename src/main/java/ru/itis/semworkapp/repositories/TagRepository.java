package ru.itis.semworkapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semworkapp.entities.TagEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {
    Optional<TagEntity> findByNameIgnoreCase(String name);
    List<TagEntity> findByNameContainingIgnoreCase(String part);
}