package com.spring_project.car_rental_service.CarDetails;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class CarDetails {
    public static List<String> BRANDS = Arrays.asList("Toyota", "BMW", "Honda","Mercedes","Bugatti","Nissan",
            "Mazda","Volkswagen","Audi","Ford","Porsche","Kia","Lamborghini", "Lexus", "Subaru", "Chevrolet");
    public static List<String> TYPES = Arrays.asList("Petrol", "Diesel", "Electric","Hybrid");
    public static List<String> TRANSMISSIONS = Arrays.asList("Automatic", "Manual","CVT","Robot");
    public static List<String> COLORS = Arrays.asList(
            "Black",
            "White",
            "Silver",
            "Gray",
            "Blue",
            "Red",
            "Green",
            "Brown",
            "Beige",
            "Yellow",
            "Orange",
            "Gold",
            "Burgundy",
            "Purple"
    );

}
