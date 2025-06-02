package com.spring_project.car_rental_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarDtoListDto {
    private List<CarDto> carDtos;
}
