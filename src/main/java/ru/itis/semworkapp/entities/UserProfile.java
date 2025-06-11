package ru.itis.semworkapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private UUID id;
    
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    @Column(length = 2000)
    private String avatarUrl;
    private String phoneNumber;
    private String bio;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}