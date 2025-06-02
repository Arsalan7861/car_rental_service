package com.spring_project.car_rental_service.Dto;

import com.spring_project.car_rental_service.Enums.BookCarStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookACarDto {

    private Long id;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Long days;

    private Long price;

    private BookCarStatus bookCarStatus;

    private Long userId;

    private Long carId;


}
