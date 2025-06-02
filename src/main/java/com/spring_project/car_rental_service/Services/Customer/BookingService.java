package com.spring_project.car_rental_service.Services.Customer;

import com.spring_project.car_rental_service.Dto.BookACarDto;
import com.spring_project.car_rental_service.Entity.BookACar;

public interface BookingService {
    BookACar createBook(BookACarDto bookACarDto);
    void approveBookStatus(long id);
    void rejectBookStatus(long id);
}
