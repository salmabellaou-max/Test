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
@RequestMapping("/patient")
public class PatientController {

    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ReviewService reviewService;

    public PatientController(UserService userService,
                            PatientService patientService,
                            DoctorService doctorService,
                            AppointmentService appointmentService,
                            ReviewService reviewService) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.reviewService = reviewService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientService.getPatientByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> upcomingAppointments =
                appointmentService.getUpcomingAppointmentsForPatient(patient);
        List<Appointment> pastAppointments =
                appointmentService.getPastAppointmentsForPatient(patient);

        model.addAttribute("patient", patient);
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);

        return "patient/dashboard";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String query,
                        @RequestParam(value = "specialty", required = false) String specialty,
                        @RequestParam(value = "location", required = false) String location,
                        @RequestParam(value = "minPrice", required = false) Double minPrice,
                        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                        Model model) {

        List<Doctor> doctors = doctorService.searchDoctors(query, specialty, location, minPrice, maxPrice);
        List<String> specialties = doctorService.getAllSpecialties();
        List<String> locations = doctorService.getAllLocations();

        model.addAttribute("doctors", doctors);
        model.addAttribute("specialties", specialties);
        model.addAttribute("locations", locations);
        model.addAttribute("query", query);
        model.addAttribute("selectedSpecialty", specialty);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "patient/search";
    }

    @GetMapping("/doctor/{id}")
    public String viewDoctorProfile(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Review> reviews = reviewService.getReviewsForDoctor(doctor);

        model.addAttribute("doctor", doctor);
        model.addAttribute("reviews", reviews);
        return "patient/doctor-profile";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientService.getPatientByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        model.addAttribute("user", user);
        model.addAttribute("patient", patient);

        return "patient/profile";
    }

    @PostMapping("/profile/update-email")
    public String updateEmail(Authentication authentication,
                             @RequestParam("email") String newEmail,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateEmail(user.getId(), newEmail);
        redirectAttributes.addFlashAttribute("success", "Email updated successfully");

        return "redirect:/patient/profile";
    }

    @PostMapping("/profile/update-phone")
    public String updatePhoneNumber(Authentication authentication,
                                   @RequestParam("phoneNumber") String newPhoneNumber,
                                   RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientService.getPatientByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patientService.updatePhoneNumber(patient.getId(), newPhoneNumber);
        redirectAttributes.addFlashAttribute("success", "Phone number updated successfully");

        return "redirect:/patient/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(Authentication authentication,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/patient/profile";
        }

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updatePassword(user.getId(), newPassword);
        redirectAttributes.addFlashAttribute("success", "Password changed successfully");

        return "redirect:/patient/profile";
    }

    @PostMapping("/profile/delete-account")
    public String deleteAccount(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.deleteUser(user.getId());

        return "redirect:/logout";
    }
}
