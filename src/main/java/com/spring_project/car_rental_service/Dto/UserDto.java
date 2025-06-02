package com.spring_project.car_rental_service.Dto;

import com.spring_project.car_rental_service.Enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Invalid email format!")
    private String email;
    @NotEmpty(message = "First Name cannot be empty!")
    private String first_name;
    @NotEmpty(message = "Last Name cannot be empty!")
    private String last_name;
    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 5, message = "Password must be at least 5 characters long!")
    private String password;
    private UserRole role;
}
