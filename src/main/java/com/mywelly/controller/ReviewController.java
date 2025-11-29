package com.mywelly.controller;

import com.mywelly.model.*;
import com.mywelly.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ReviewService reviewService;

    public ReviewController(UserService userService,
                           PatientService patientService,
                           DoctorService doctorService,
                           ReviewService reviewService) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.reviewService = reviewService;
    }

    @GetMapping("/doctor/{doctorId}")
    public String showReviewForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        model.addAttribute("doctor", doctor);
        return "review/create";
    }

    @PostMapping("/doctor/{doctorId}")
    public String submitReview(@PathVariable Long doctorId,
                              @RequestParam("rating") Integer rating,
                              @RequestParam("comment") String comment,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Patient patient = patientService.getPatientByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            Doctor doctor = doctorService.getDoctorById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            reviewService.createReviewForDoctor(patient, doctor, rating, comment);

            redirectAttributes.addFlashAttribute("success", "Review submitted successfully!");
            return "redirect:/patient/doctor/" + doctorId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting review: " + e.getMessage());
            return "redirect:/review/doctor/" + doctorId;
        }
    }
}
