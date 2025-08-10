package com.rasel.security_demo.Service;

import com.rasel.security_demo.Repository.RoleRepository;
import com.rasel.security_demo.Repository.UserRepository;
import com.rasel.security_demo.dto.RegistrationDTO;
import com.rasel.security_demo.model.Role;
import com.rasel.security_demo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public void registerUser(RegistrationDTO registrationDTO){
        User user = new User();
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
//        user.setPassword(registrationDTO.getPassword());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setBirthDate(registrationDTO.getBirthDate());
        user.setGender(registrationDTO.getGender());
        user.setDoorNo(registrationDTO.getDoorNo());
        user.setStreet(registrationDTO.getStreet());
        user.setCity(registrationDTO.getCity());
        user.setDistrict(registrationDTO.getDistrict());
        user.setState(registrationDTO.getState());

        Collection<Role> roles = new HashSet<>();
//        Set<Role> roles = new HashSet<>();
//       try{
//           Role.ERole roleEnum = Role.ERole.valueOf(registrationDTO.getRole());
//           Role userRole = roleRepository.findByName(roleEnum)
//                   .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//           roles.add(userRole);
//       } catch (IllegalArgumentException e){
//           throw new RuntimeException("Error, Invalid role provided. Must be role");
//       }

        user.setRoles(null); // or leave unset
        User savedUser = userRepository.save(user);

//        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Role userRole = roleRepository.findByName(Role.ERole.valueOf(registrationDTO.getRole()))
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        roles.add(userRole);
        user.setRoles((Set<Role>) roles);

        userRepository.save(user);
    }
}
