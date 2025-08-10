package com.rasel.security_demo.Controller;

import com.rasel.security_demo.ResourceNotFoundException.ResourceNotFoundException;
import com.rasel.security_demo.Service.FileStorageService;
import com.rasel.security_demo.Service.UserService;
import com.rasel.security_demo.dto.UserProfileDTO;
import com.rasel.security_demo.model.User;
import com.rasel.security_demo.model.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;
    private final FileStorageService storageService;

    public ProfileController(UserService userService, FileStorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }
 
    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(mapToProfileDto(user));
    }

    @PostMapping("/photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestParam("file") MultipartFile file, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try{
            String filename = storageService.store(file, user.getId());
            user.setProfileImagePath(filename);
            userService.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile photo uploaded successfully");
            response.put("imageUrl", user.getProfileImageUrl());

            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/editProfile/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDTO userProfileDTO, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findById(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setPhoneNumber(userProfileDTO.getPhoneNumber());
        user.setBirthDate(LocalDate.parse(userProfileDTO.getBirthDate()));
        user.setGender(userProfileDTO.getGender());

        //Address update
        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

//    @GetMapping("/getAll/profileUpdate")
//    public ResponseEntity<UserProfileDTO> getAll(@RequestBody User user)

    private UserProfileDTO mapToProfileDto(User user){
        return new UserProfileDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                user.getBirthDate().toString(),
                user.getGender()
        );
    }
}
