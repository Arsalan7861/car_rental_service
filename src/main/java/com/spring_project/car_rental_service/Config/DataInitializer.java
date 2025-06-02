package com.spring_project.car_rental_service.Config;

import com.spring_project.car_rental_service.Entity.User;
import com.spring_project.car_rental_service.Enums.UserRole;
import com.spring_project.car_rental_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@mail.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setFirst_name("Admin");
            admin.setLast_name("Manager");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin user created ✅");
        } else {
            System.out.println("Admin user already exists ⚠️");
        }
    }
}
