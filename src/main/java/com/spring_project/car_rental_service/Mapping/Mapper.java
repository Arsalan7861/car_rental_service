package com.spring_project.car_rental_service.Mapping;

import com.spring_project.car_rental_service.Dto.BookACarDto;
import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Dto.UserDto;
import com.spring_project.car_rental_service.Entity.BookACar;
import com.spring_project.car_rental_service.Entity.Car;
import com.spring_project.car_rental_service.Entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Mapper {

    public static UserDto entityToDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getFirst_name(),
                user.getLast_name(), user.getPassword(), user.getRole());
    }

    public static User dtoToEntity(UserDto dto) {
        return new User(dto.getId(), dto.getEmail(), dto.getFirst_name(),
                dto.getLast_name(), dto.getPassword(), dto.getRole());
    }

    public static CarDto entityToDto(Car car) {
        return new CarDto(car.getId(), car.getBrand(), car.getColor(),
                car.getName(), car.getType(), car.getTransmission(),
                car.getDescription(), car.getPrice(), car.getYear(), car.getImagePath());
    }


    public static Car dtoToEntity(CarDto dto) {
        return new Car(dto.getId(), dto.getBrand(), dto.getColor(),
                dto.getName(), dto.getType(), dto.getTransmission(),
                dto.getDescription(), dto.getPrice(), dto.getYear(), dto.getImagePath());
    }

    public static BookACarDto entityToDto(BookACar bookACar) {
        return new BookACarDto(bookACar.getId(), bookACar.getFromDate(),
                bookACar.getToDate(),bookACar.getDays(), bookACar.getPrice(), bookACar.getBookCarStatus(),
                bookACar.getUser().getId(),bookACar.getCar().getId());
    }



    public static List<BookACarDto> toDtoList(List<BookACar> bookings) {
        return bookings.stream().map(Mapper::entityToDto).toList();
    }

}
