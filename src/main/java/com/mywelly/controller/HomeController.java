package com.mywelly.controller;

import com.mywelly.model.*;
import com.mywelly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam("userType") String userType,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam(value = "fullName", required = false) String fullName,
                               @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
                               @RequestParam(value = "gender", required = false) String gender,
                               @RequestParam(value = "idNumber", required = false) String idNumber,
                               @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "doctorName", required = false) String doctorName,
                               @RequestParam(value = "specialty", required = false) String specialty,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "workingHours", required = false) String workingHours,
                               @RequestParam(value = "certificates", required = false) String certificates,
                               @RequestParam(value = "consultationFee", required = false) Double consultationFee,
                               @RequestParam(value = "labName", required = false) String labName,
                               RedirectAttributes redirectAttributes) {

        if (userService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/signup";
        }

        try {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            switch (userType) {
                case "PATIENT":
                    Patient patient = new Patient();
                    patient.setFullName(fullName);
                    patient.setDateOfBirth(dateOfBirth != null ? LocalDate.parse(dateOfBirth) : null);
                    patient.setGender(gender);
                    patient.setIdNumber(idNumber);
                    patient.setPhoneNumber(phoneNumber);
                    patient.setUsername(username);
                    userService.registerPatient(user, patient);
                    break;

                case "DOCTOR":
                    Doctor doctor = new Doctor();
                    doctor.setName(doctorName);
                    doctor.setSpecialty(specialty);
                    doctor.setLocation(location);
                    doctor.setPhoneNumber(phoneNumber);
                    doctor.setWorkingHours(workingHours);
                    doctor.setCertificates(certificates);
                    doctor.setConsultationFee(consultationFee);
                    userService.registerDoctor(user, doctor);
                    break;

                case "LABORATORY":
                    Laboratory laboratory = new Laboratory();
                    laboratory.setLabName(labName);
                    laboratory.setLocation(location);
                    laboratory.setPhoneNumber(phoneNumber);
                    laboratory.setWorkingHours(workingHours);
                    userService.registerLaboratory(user, laboratory);
                    break;
            }

            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating account: " + e.getMessage());
            return "redirect:/signup";
        }
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("PATIENT"))) {
            return "redirect:/patient/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("DOCTOR"))) {
            return "redirect:/doctor/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("LABORATORY"))) {
            return "redirect:/laboratory/dashboard";
        }
        return "redirect:/";
    }
}
