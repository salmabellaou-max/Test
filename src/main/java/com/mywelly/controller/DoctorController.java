package com.mywelly.controller;

import com.mywelly.model.*;
import com.mywelly.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ReviewService reviewService;

    public DoctorController(UserService userService,
                           DoctorService doctorService,
                           AppointmentService appointmentService,
                           ReviewService reviewService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.reviewService = reviewService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorService.getDoctorByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> upcomingAppointments =
                appointmentService.getUpcomingAppointmentsForDoctor(doctor);

        List<Review> recentReviews = reviewService.getReviewsForDoctor(doctor);

        model.addAttribute("doctor", doctor);
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("recentReviews", recentReviews);

        return "doctor/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorService.getDoctorByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);

        return "doctor/profile";
    }

    @PostMapping("/profile/update-location")
    public String updateLocation(Authentication authentication,
                                @RequestParam("location") String newLocation,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorService.getDoctorByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctorService.updateLocation(doctor.getId(), newLocation);
        redirectAttributes.addFlashAttribute("success", "Location updated successfully");

        return "redirect:/doctor/profile";
    }
}
