package com.quizmaster.service;

import com.quizmaster.entity.Role;
import com.quizmaster.entity.User;
import com.quizmaster.payload.request.UpdateProfileRequest;
import com.quizmaster.payload.response.UserProfileDto;
import com.quizmaster.repository.RoleRepository;
import com.quizmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToDto(user);
    }

    @Transactional
    public UserProfileDto updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEducation() != null) user.setEducation(request.getEducation());
        if (request.getSkills() != null) user.setSkills(request.getSkills());
        if (request.getInterests() != null) user.setInterests(request.getInterests());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public UserProfileDto toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setEnabled(!Boolean.TRUE.equals(user.getEnabled()));
        return mapToDto(userRepository.save(user));
    }

    @Transactional
    public UserProfileDto toggleAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        boolean hasAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
        if (hasAdmin) {
            user.getRoles().removeIf(r -> "ROLE_ADMIN".equals(r.getName()));
        } else {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            user.getRoles().add(adminRole);
        }

        return mapToDto(userRepository.save(user));
    }

    private UserProfileDto mapToDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setEducation(user.getEducation());
        dto.setSkills(user.getSkills());
        dto.setInterests(user.getInterests());
        dto.setBio(user.getBio());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return dto;
    }
}
