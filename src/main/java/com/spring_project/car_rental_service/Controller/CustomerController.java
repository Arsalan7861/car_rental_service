package com.spring_project.car_rental_service.Controller;

import com.spring_project.car_rental_service.CarDetails.CarDetails;
import com.spring_project.car_rental_service.Dto.BookACarDto;
import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Dto.CarDtoListDto;
import com.spring_project.car_rental_service.Dto.SearchCarDto;
import com.spring_project.car_rental_service.Entity.BookACar;
import com.spring_project.car_rental_service.Entity.User;
import com.spring_project.car_rental_service.Enums.BookCarStatus;
import com.spring_project.car_rental_service.Mapping.Mapper;
import com.spring_project.car_rental_service.Repository.BookACarRepository;
import com.spring_project.car_rental_service.Repository.CarRepository;
import com.spring_project.car_rental_service.Repository.UserRepository;
import com.spring_project.car_rental_service.Services.Admin.AdminService;
import com.spring_project.car_rental_service.Services.Customer.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/customer")
@Controller
public class CustomerController {

    private final AdminService adminService;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookingServiceImpl bookingService;
    private final BookACarRepository bookACarRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Authentication auth, Model model) {
        String username = auth.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        model.addAttribute("user", user);
        List<CarDto> cars = adminService.getAll_cars();
        model.addAttribute("cars", cars);
        return "TopicListing/customer/dashboard";
    }

    @GetMapping("/car/{id}")
    public String showBookAcar(Authentication auth, Model model, @PathVariable("id") long id) {
        CarDto bookingcar = carRepository.findCarDtoById(id);
        Optional<User> userOptional = userRepository.findByEmail(auth.getName());

        if (userOptional.isEmpty()) {
            return "redirect:/login?error=userNotFound";
        }

        model.addAttribute("user", userOptional.get());
        model.addAttribute("bookingcar", bookingcar);
        return "TopicListing/customer/bookacar";
    }

    @PostMapping("/bookacar/{carId}")
    public String bookCar(
            @PathVariable long carId,
            @ModelAttribute BookACarDto bookACarDto,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        Optional<User> user = userRepository.findByEmail(auth.getName());
        bookACarDto.setUserId(user.map(User::getId).get());

        List<BookACar> existingBookings = bookACarRepository.findByCarIdAndBookCarStatus(carId, BookCarStatus.APPROVED);
        for (BookACar booking : existingBookings) {
            boolean isOverlapping = !(bookACarDto.getToDate().isBefore(booking.getFromDate()) ||
                    bookACarDto.getFromDate().isAfter(booking.getToDate()));
            if (isOverlapping) {
                redirectAttributes.addFlashAttribute("error", "This car is already booked during the selected dates.");
                return "redirect:/customer/car/" + carId;
            }
        }
        long days = ChronoUnit.DAYS.between(
                bookACarDto.getFromDate(),
                bookACarDto.getToDate()
        );

        CarDto car = carRepository.findCarDtoById(carId);
        bookACarDto.setCarId(car.getId());
        bookACarDto.setDays(days);
        bookACarDto.setPrice(days * car.getPrice());
        bookACarDto.setBookCarStatus(BookCarStatus.PENDING);

        bookingService.createBook(bookACarDto);
        redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
        return "redirect:/customer/dashboard";
    }


    @GetMapping("/bookings")
    public String showBookings(Authentication auth, Model model) {
        Optional<User> userOptional = userRepository.findByEmail(auth.getName());
        long userId = userOptional.map(User::getId).get();
        List<BookACar> bookings = bookACarRepository.findAllByUser_Id(userId);
        model.addAttribute("bookings", bookings);
        return "TopicListing/customer/bookings";
    }

    @GetMapping("/searchpage")
    public String showSearch(Model model) {
        model.addAttribute("searchCarDto", new SearchCarDto());
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);
        model.addAttribute("carDtoList", new CarDtoListDto(Collections.emptyList()));
        return "TopicListing/customer/searchpage";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("searchCarDto") SearchCarDto searchCarDto, Model model) {
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);

        CarDtoListDto carDtoList = adminService.searchCar(searchCarDto);
        model.addAttribute("carDtoList", carDtoList);
        return "TopicListing/customer/searchpage";
    }

}
