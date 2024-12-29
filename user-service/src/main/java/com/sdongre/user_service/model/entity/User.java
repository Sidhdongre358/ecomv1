package com.sdongre.user_service.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_username", columnNames = "userName"),
        @UniqueConstraint(name = "unique_email", columnNames = "email"),
        @UniqueConstraint(name = "unique_phone", columnNames = "phoneNumber")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Full name cannot be empty. Please enter your full name.")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
    @Column(name = "fullName")
    private String fullname;

    @NotBlank(message = "Username cannot be empty. Please enter a username.")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters.")
    @Column(name = "userName")
    private String username;

    @NaturalId
    @NotBlank(message = "Email cannot be empty. Please provide a valid email address.")
    @Size(max = 50, message = "Email must not exceed 50 characters.")
    @Email(message = "Please enter a valid email address.")
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @NotNull(message = "Password cannot be empty. Please enter a password.")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Gender is required. Please select a gender.")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Pattern(regexp = "^\\+84[0-9]{9,10}$|^0[0-9]{9,10}$", message = "Invalid phone number format. Please enter a valid phone number starting with +91 or 0 and followed by 9-10 digits.")
    @Size(min = 10, max = 11, message = "Phone number must be between 10 and 11 digits.")
    @Column(name = "phoneNumber", unique = true)
    private String phone;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL. Please enter a valid HTTP or HTTPS URL for the avatar.")
    @Lob
    @Column(name = "imageUrl")
    private String avatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
