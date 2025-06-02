package com.spring_project.car_rental_service.Dto;

import lombok.Data;

@Data
public class SearchCarDto {
    private String brand;
    private String type;
    private String transmission;
    private String color;
}
