package com.spring_project.car_rental_service.Repository;

import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT new com.spring_project.car_rental_service.Dto.CarDto(" +
            "c.id, c.brand, c.color, c.name, c.type, c.transmission, c.description, c.price, c.year, c.imagePath) " +
            "FROM Car c WHERE c.id = :id")
    CarDto findCarDtoById(@Param("id") long id);

    @Query("SELECT c FROM Car c WHERE " +
            "(:brand IS NULL OR LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:type IS NULL OR LOWER(c.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
            "(:transmission IS NULL OR LOWER(c.transmission) LIKE LOWER(CONCAT('%', :transmission, '%'))) AND " +
            "(:color IS NULL OR LOWER(c.color) LIKE LOWER(CONCAT('%', :color, '%')))")
    List<Car> searchCars(@Param("brand") String brand, @Param("type") String type,
                         @Param("transmission") String transmission, @Param("color") String color);

}
