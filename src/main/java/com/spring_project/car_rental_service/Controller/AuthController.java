package com.spring_project.car_rental_service.Controller;

import com.spring_project.car_rental_service.Dto.UserDto;
import com.spring_project.car_rental_service.Services.Auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthController {

    AuthService authService;

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "TopicListing/signup";
    }

   @PostMapping("/register")
   public String registerUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "TopicListing/signup";
        }
       authService.createUser(userDto);
       return "TopicListing/login";
   }


    @GetMapping("/loginpage")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "registered", required = false) String registered,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please login.");
        }
        return "TopicListing/login";
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                return "redirect:/customer/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/";
    }

}
