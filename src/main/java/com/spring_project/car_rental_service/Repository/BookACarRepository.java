package com.spring_project.car_rental_service.Repository;

import com.spring_project.car_rental_service.Entity.BookACar;
import com.spring_project.car_rental_service.Enums.BookCarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookACarRepository extends JpaRepository<BookACar, Long> {
    List<BookACar> findAllByUser_Id(Long userId);

    List<BookACar> findAll();

    BookACar findById(long id);

    List<BookACar> findByCarIdAndBookCarStatus(long id, BookCarStatus status);

}
