package com.rasel.security_demo.Service;

import com.rasel.security_demo.Repository.UserRepository;
import com.rasel.security_demo.dto.ChangePasswordDTO;
import com.rasel.security_demo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public String changePasswords(Long userId, ChangePasswordDTO changePasswordDTO){
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            return "User not found";
        }

        User user = optionalUser.get();

        //old password
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())){
            return "Old password wrong";
        }

        //old and new password
        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
            return "new and old password wrong";
        }

        //new password set
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return "Password changed successful";
    }
}
