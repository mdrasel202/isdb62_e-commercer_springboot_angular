package com.rasel.security_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "d_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "Password is requird")
    @Size(max = 100, min = 8)
    private String password;

    //Address fields
    private String doorNo;
    private String street;
    private String city;
    private String district;
    private String state;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "d_user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new HashSet<>();

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Value("${app.profile-images.dir}")
    private String profileImageDir;

    @Transient
    public String getProfileImageUrl(){

        if(profileImagePath == null || profileImagePath.isEmpty()){
            return "http://localhost:8080/uploads/profile-images/default-profile.png";
//            return appBaseUrl + "/" + profileImageDir + "/default-profile.png";
        }

        return "http://localhost:8080/uploads/profile-images/" + id + "/" + profileImagePath;
//        return appBaseUrl + "/" + profileImageDir + "/" + id + "/" + profileImagePath;
    }

    // Helper method to add a role
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    // Helper method to remove a role
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public enum Gender{
        MALE, FEMALE, OTHER
    }
}
