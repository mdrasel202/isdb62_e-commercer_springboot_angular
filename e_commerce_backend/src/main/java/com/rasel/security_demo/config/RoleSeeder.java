//package com.rasel.security_demo.config;
//
//import com.rasel.security_demo.Repository.RoleRepository;
//import com.rasel.security_demo.model.Role;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class RoleSeeder implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if(roleRepository.count() == 0) {
//            roleRepository.save(new Role(null, Role.ERole.ROLE_USER));
//            roleRepository.save(new Role(null, Role.ERole.ROLE_ADMIN));
//            System.out.println("Default roles inserted.");
//        }
//    }
//}