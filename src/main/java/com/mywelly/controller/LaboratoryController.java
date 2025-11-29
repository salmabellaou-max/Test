package com.mywelly.controller;

import com.mywelly.model.*;
import com.mywelly.service.*;
import com.mywelly.repository.LaboratoryRepository;
import com.mywelly.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/laboratory")
public class LaboratoryController {

    private final UserService userService;
    private final LaboratoryRepository laboratoryRepository;
    private final ReviewRepository reviewRepository;

    public LaboratoryController(UserService userService,
                               LaboratoryRepository laboratoryRepository,
                               ReviewRepository reviewRepository) {
        this.userService = userService;
        this.laboratoryRepository = laboratoryRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Laboratory laboratory = laboratoryRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Laboratory not found"));

        List<Review> recentReviews = reviewRepository.findByLaboratoryOrderByCreatedAtDesc(laboratory);

        model.addAttribute("laboratory", laboratory);
        model.addAttribute("recentReviews", recentReviews);

        return "laboratory/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Laboratory laboratory = laboratoryRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Laboratory not found"));

        model.addAttribute("user", user);
        model.addAttribute("laboratory", laboratory);

        return "laboratory/profile";
    }
}
