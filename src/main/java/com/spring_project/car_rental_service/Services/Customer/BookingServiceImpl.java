package com.spring_project.car_rental_service.Services.Customer;

import com.spring_project.car_rental_service.Dto.BookACarDto;
import com.spring_project.car_rental_service.Entity.BookACar;
import com.spring_project.car_rental_service.Entity.Car;
import com.spring_project.car_rental_service.Entity.User;
import com.spring_project.car_rental_service.Enums.BookCarStatus;
import com.spring_project.car_rental_service.Repository.BookACarRepository;
import com.spring_project.car_rental_service.Repository.CarRepository;
import com.spring_project.car_rental_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookACarRepository bookACarRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    @Override
    public BookACar createBook(BookACarDto bookACarDto) {
        User user = userRepository.findById(bookACarDto.getUserId()).get();
        Car car = carRepository.findById(bookACarDto.getCarId()).get();
        BookACar bookACar = new BookACar(null,bookACarDto.getFromDate(), bookACarDto.getToDate(),
                bookACarDto.getDays(), bookACarDto.getPrice(), bookACarDto.getBookCarStatus(),
                user, car);
        bookACarRepository.save(bookACar);
        return bookACar;
    }

    @Override
    public void approveBookStatus(long id) {
        BookACar bookACar = bookACarRepository.findById(id);
        bookACar.setBookCarStatus(BookCarStatus.APPROVED);
        bookACarRepository.save(bookACar);
    }

    @Override
    public void rejectBookStatus(long id) {
        BookACar bookACar = bookACarRepository.findById(id);
        bookACar.setBookCarStatus(BookCarStatus.REJECTED);
        bookACarRepository.save(bookACar);
    }


}
