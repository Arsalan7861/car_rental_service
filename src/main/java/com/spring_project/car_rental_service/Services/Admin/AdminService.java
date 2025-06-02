package com.spring_project.car_rental_service.Services.Admin;

import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Dto.CarDtoListDto;
import com.spring_project.car_rental_service.Dto.SearchCarDto;

import java.util.List;

public interface AdminService {
    boolean create_a_car(CarDto carDto);
    List<CarDto> getAll_cars();
    void delete_a_car(long carId);
    boolean update_a_car(long carId, CarDto carDto);
    CarDtoListDto searchCar(SearchCarDto searchCarDto);
}
