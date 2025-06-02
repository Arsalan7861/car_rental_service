package com.spring_project.car_rental_service.Dto;

import lombok.*;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private long id;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private long price;
    private int year;
    private String imagePath;

}
