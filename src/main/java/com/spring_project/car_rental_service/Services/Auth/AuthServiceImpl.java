package com.spring_project.car_rental_service.Services.Auth;

import com.spring_project.car_rental_service.Dto.UserDto;
import com.spring_project.car_rental_service.Entity.User;
import com.spring_project.car_rental_service.Enums.UserRole;
import com.spring_project.car_rental_service.Mapping.Mapper;
import com.spring_project.car_rental_service.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void createUser(UserDto userDto) {
        User newUser = Mapper.dtoToEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(UserRole.CUSTOMER);
        newUser = userRepository.save(newUser);
        Mapper.entityToDto(newUser);
    }

}
