package com.quizmaster.service;

import com.quizmaster.entity.Role;
import com.quizmaster.entity.User;
import com.quizmaster.entity.UserProgress;
import com.quizmaster.payload.request.ForgotPasswordRequest;
import com.quizmaster.payload.request.LoginRequest;
import com.quizmaster.payload.request.RegisterRequest;
import com.quizmaster.payload.request.ResetPasswordRequest;
import com.quizmaster.payload.response.JwtResponse;
import com.quizmaster.payload.response.MessageResponse;
import com.quizmaster.repository.RoleRepository;
import com.quizmaster.repository.UserProgressRepository;
import com.quizmaster.repository.UserRepository;
import com.quizmaster.security.JwtUtils;
import com.quizmaster.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findById(userDetails.getId()).orElse(null);
        String avatarUrl = user != null ? user.getAvatarUrl() : null;

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFullName(),
                userDetails.getUsername(),
                roles,
                avatarUrl);
    }

    @Transactional
    public MessageResponse registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already registered!", false);
        }

        // Create new user account
        User user = new User(signUpRequest.getFullName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setEducation(signUpRequest.getEducation());
        user.setSkills(signUpRequest.getSkills());
        user.setInterests(signUpRequest.getInterests());
        user.setBio(signUpRequest.getBio());
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();

        // Ensure roles exist in DB
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        roles.add(userRole);

        if ("admin".equalsIgnoreCase(signUpRequest.getRole())) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            roles.add(adminRole);
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Initialize user progress
        UserProgress progress = new UserProgress(savedUser);
        progress.setCurrentStreak(0);
        userProgressRepository.save(progress);

        return new MessageResponse("User registered successfully!", true);
    }

    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            // Return success message to prevent user enumeration
            return new MessageResponse("If an account exists with this email, a reset token has been generated.", true);
        }

        // Generate 6-digit or UUID token
        String resetToken = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        return new MessageResponse("Password reset code generated: " + resetToken + " (Valid for 1 hour)", true);
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken()).orElse(null);
        if (user == null || user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return new MessageResponse("Invalid or expired password reset token!", false);
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new MessageResponse("Password has been reset successfully! You can now log in.", true);
    }
}
