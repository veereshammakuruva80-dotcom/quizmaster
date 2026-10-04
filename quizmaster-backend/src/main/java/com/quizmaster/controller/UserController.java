package com.quizmaster.controller;

import com.quizmaster.payload.request.UpdateProfileRequest;
import com.quizmaster.payload.response.MessageResponse;
import com.quizmaster.payload.response.UserProfileDto;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.getId(), request));
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody Map<String, String> body) {
        String avatarUrl = body.get("avatarUrl");
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Avatar URL cannot be empty", false));
        }
        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setAvatarUrl(avatarUrl);
        return ResponseEntity.ok(userService.updateProfile(userDetails.getId(), req));
    }
}
