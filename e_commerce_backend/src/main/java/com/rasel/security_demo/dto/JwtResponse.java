package com.rasel.security_demo.dto;

import lombok.AllArgsConstructor;

import java.util.Collection;

public class JwtResponse {
        private String token;
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private Collection<String> roles;


        public JwtResponse(String token, Long id, String username,
                           String firstName, String lastName, Collection<String> roles) {
            this.token = token;
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.roles = roles;
        }

        // Getters and Setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Collection<String> getRoles() {
            return roles;
        }

        public void setRoles(Collection<String> roles) {
            this.roles = roles;
        }
    }

