package com.spring_project.car_rental_service.Services.Admin;

import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Dto.CarDtoListDto;
import com.spring_project.car_rental_service.Dto.SearchCarDto;
import com.spring_project.car_rental_service.Entity.Car;
import com.spring_project.car_rental_service.Mapping.Mapper;
import com.spring_project.car_rental_service.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final CarRepository carRepository;
    @Override
    public boolean create_a_car(CarDto carDto) {
        try {
            Car car = Mapper.dtoToEntity(carDto);
            carRepository.save(car);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<CarDto> getAll_cars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(Mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public void delete_a_car(long carId) {
        carRepository.deleteById(carId);
    }

    @Override
    public boolean update_a_car(long carId, CarDto carDto){
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();
            if (carDto.getImagePath() != null) {
                existingCar.setImagePath(carDto.getImagePath());
            }
            existingCar.setName(carDto.getName());
            existingCar.setBrand(carDto.getBrand());
            existingCar.setColor(carDto.getColor());
            existingCar.setPrice(carDto.getPrice());
            existingCar.setYear(carDto.getYear());
            existingCar.setType(carDto.getType());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setDescription(carDto.getDescription());
            carRepository.save(existingCar);
            return true;
        }

        return false;

    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        List<Car> cars = carRepository.searchCars(searchCarDto.getBrand(), searchCarDto.getType(),
                searchCarDto.getTransmission(), searchCarDto.getColor());
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtos(cars.stream().map(Mapper::entityToDto).collect(Collectors.toList()));
        return carDtoListDto;
    }
}
