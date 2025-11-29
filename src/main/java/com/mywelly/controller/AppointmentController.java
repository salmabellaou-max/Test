package com.mywelly.controller;

import com.mywelly.model.*;
import com.mywelly.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public AppointmentController(UserService userService,
                                PatientService patientService,
                                DoctorService doctorService,
                                AppointmentService appointmentService) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/book/{doctorId}")
    public String showBookingForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        model.addAttribute("doctor", doctor);
        return "appointment/book";
    }

    @PostMapping("/book/{doctorId}")
    public String bookAppointment(@PathVariable Long doctorId,
                                  @RequestParam("date") String date,
                                  @RequestParam("time") String time,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Patient patient = patientService.getPatientByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            Doctor doctor = doctorService.getDoctorById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            appointmentService.createAppointment(patient, doctor, LocalDate.parse(date), time);

            redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
            return "redirect:/patient/dashboard";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/appointment/book/" + doctorId;
        }
    }

    @PostMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId,
                                   @RequestParam(value = "reason", required = false) String reason,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserRole() == User.UserRole.PATIENT) {
            appointmentService.cancelAppointmentByPatient(appointmentId);
            redirectAttributes.addFlashAttribute("success", "Appointment cancelled successfully");
            return "redirect:/patient/dashboard";
        } else if (user.getUserRole() == User.UserRole.DOCTOR) {
            appointmentService.cancelAppointmentByDoctor(appointmentId, reason);
            redirectAttributes.addFlashAttribute("success", "Appointment cancelled successfully");
            return "redirect:/doctor/dashboard";
        }

        return "redirect:/dashboard";
    }
}
