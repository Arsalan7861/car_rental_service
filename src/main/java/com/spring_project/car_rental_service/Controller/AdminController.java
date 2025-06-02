package com.spring_project.car_rental_service.Controller;

import com.spring_project.car_rental_service.CarDetails.CarDetails;
import com.spring_project.car_rental_service.Dto.BookACarDto;
import com.spring_project.car_rental_service.Dto.CarDto;
import com.spring_project.car_rental_service.Dto.CarDtoListDto;
import com.spring_project.car_rental_service.Dto.SearchCarDto;
import com.spring_project.car_rental_service.Entity.BookACar;
import com.spring_project.car_rental_service.Entity.User;
import com.spring_project.car_rental_service.Mapping.Mapper;
import com.spring_project.car_rental_service.Repository.BookACarRepository;
import com.spring_project.car_rental_service.Repository.CarRepository;
import com.spring_project.car_rental_service.Repository.UserRepository;
import com.spring_project.car_rental_service.Services.Admin.AdminService;
import com.spring_project.car_rental_service.Services.Customer.BookingService;
import com.spring_project.car_rental_service.Services.Customer.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final AdminService adminService;
    private final CarRepository carRepository;
    private final BookACarRepository bookACarRepository;
    private final UserRepository userRepository;
    private final BookingServiceImpl bookingService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Authentication auth, Model model) {
        model.addAttribute("user", auth.getName());
        List<CarDto> cars = adminService.getAll_cars();
        model.addAttribute("cars", cars);
        return "TopicListing/admin/dashboard";
    }

    @GetMapping("/postcarpage")
    public String showPostCarPage(Model model) {
        model.addAttribute("car", new CarDto());
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);
        return "TopicListing/admin/postcarpage";
    }

    @PostMapping("/postcar")
    public String postCar(@ModelAttribute("car") CarDto carDto, @RequestParam("image") MultipartFile image,
                          RedirectAttributes redirectAttributes) {
        if (!image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                carDto.setImagePath(imagePath);
                System.out.println("Image saved at: " + imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Image upload failed.");
            }
        }

        boolean posted = adminService.create_a_car(carDto);
        if (posted) {
            redirectAttributes.addFlashAttribute("successMessage", "Car created successfully");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong");
        }
        return "redirect:/admin/dashboard";
    }


    @PostMapping("/deletecar/{id}")
    public String deleteCar(@PathVariable("id") long id) {
        adminService.delete_a_car(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/car/{id}")
    public String showUpdateCarPage(Model model, @PathVariable("id") long id) {
        CarDto carDto = carRepository.findCarDtoById(id);
        if (carDto == null) {
            System.out.println("Car not found");
        }else {
            System.out.println("Car found and here is a car: " + carDto);
        }
        model.addAttribute("car", carDto);
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);
        model.addAttribute("isUpdate", true);

        return "TopicListing/admin/updatecarpage";
    }

    @PostMapping("/updatecar/{id}")
    public String updateCar(@ModelAttribute("car") CarDto carDto,
                            @PathVariable("id") long id,
                            RedirectAttributes redirectAttributes,@RequestParam("image") MultipartFile image) {
        CarDto existingCar = carRepository.findCarDtoById(id);

        if (!image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                carDto.setImagePath(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            carDto.setImagePath(existingCar.getImagePath());
        }

        boolean updated = adminService.update_a_car(id, carDto);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Car updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Update failed. Car not found.");
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/bookings")
    public String showBookings(Model model) {
        List<BookACar> bookings = bookACarRepository.findAll();
        List<BookACarDto> bookingDtos = Mapper.toDtoList(bookings);

        List<Map<String, Object>> enrichedBookings = new ArrayList<>();

        for (BookACarDto dto : bookingDtos) {
            Optional<User> userOpt = userRepository.findById(dto.getUserId());

            Map<String, Object> map = new HashMap<>();
            map.put("id", dto.getId());
            map.put("fromDate", dto.getFromDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
            map.put("toDate", dto.getToDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
            map.put("days", dto.getDays());
            map.put("price", dto.getPrice());
            map.put("bookCarStatus", dto.getBookCarStatus());
            map.put("userId", dto.getUserId());
            map.put("carId", dto.getCarId());
            map.put("email", userOpt.map(User::getUsername).orElse("Unknown"));

            enrichedBookings.add(map);
        }

        model.addAttribute("bookings", enrichedBookings);
        return "TopicListing/admin/bookings";
    }

    @PostMapping("/bookings/approve/{id}")
    public String approveBooking(@PathVariable("id") long id) {
        bookingService.approveBookStatus(id);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/bookings/reject/{id}")
    public String rejectBooking(@PathVariable("id") long id) {
        bookingService.rejectBookStatus(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/searchpage")
    public String showSearch(Model model) {
        model.addAttribute("searchCarDto", new SearchCarDto());
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);
        model.addAttribute("carDtoList", new CarDtoListDto(Collections.emptyList()));
        return "TopicListing/admin/searchpage";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("searchCarDto") SearchCarDto searchCarDto, Model model) {
        model.addAttribute("listOfBrands", CarDetails.BRANDS);
        model.addAttribute("listOfType", CarDetails.TYPES);
        model.addAttribute("listOfTransmission", CarDetails.TRANSMISSIONS);
        model.addAttribute("listOfColor", CarDetails.COLORS);

        CarDtoListDto carDtoList = adminService.searchCar(searchCarDto);
        model.addAttribute("carDtoList", carDtoList);
        return "TopicListing/admin/searchpage";
    }


    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "car_rental_uploads/";

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }
}
