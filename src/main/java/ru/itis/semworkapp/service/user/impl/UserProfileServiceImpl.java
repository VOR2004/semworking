package ru.itis.semworkapp.service.user.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semworkapp.entities.UserProfile;
import ru.itis.semworkapp.repositories.user.UserProfileRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl {
    private final UserProfileRepository userProfileRepository;

    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));
    }
    @Transactional
    public void updateAvatarUrl(UUID id, String avatarUrl) {
        UserProfile profile = userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));
        profile.setAvatarUrl(avatarUrl);
        userProfileRepository.save(profile);
    }

    @Transactional
    public void updatePhoneNumber(UUID id, String phoneNumber) {
        UserProfile profile = userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));
        profile.setPhoneNumber(phoneNumber);
        userProfileRepository.save(profile);
    }

    @Transactional
    public void updateBio(UUID id, String bio) {
        UserProfile profile = userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));
        profile.setBio(bio);
        userProfileRepository.save(profile);
    }
}

