package com.rasel.security_demo.Controller;

import com.rasel.security_demo.Repository.UserRepository;
import com.rasel.security_demo.Service.UserService;
import com.rasel.security_demo.dto.ChangePasswordDTO;
import com.rasel.security_demo.model.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswords(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        Long userId = userDetails.getId();
        String result = userService.changePasswords(userId, changePasswordDTO);

        if(result.equals("Password changed successful")){
            return ResponseEntity.ok(result);
        }else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
