package com.mywelly.config;

import com.mywelly.model.*;
import com.mywelly.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserService userService,
                                   DoctorService doctorService,
                                   PatientService patientService) {
        return args -> {
            // Only initialize if database is empty
            if (doctorService.getAllDoctors().isEmpty()) {
                initializeSampleData(userService);
            }
        };
    }

    private void initializeSampleData(UserService userService) {
        // Create sample patient
        User patientUser = new User();
        patientUser.setEmail("patient@test.com");
        patientUser.setPassword("password123");

        Patient patient = new Patient();
        patient.setFullName("Ahmed Benali");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patient.setGender("Male");
        patient.setIdNumber("AB123456");
        patient.setPhoneNumber("+212612345678");
        patient.setUsername("ahmed_benali");

        userService.registerPatient(patientUser, patient);

        // Create sample doctors
        String[][] doctors = {
            {"dr.hassan@mywelly.ma", "Dr. Hassan Alami", "Cardiology", "Casablanca",
             "+212612111111", "9:00-17:00", "MD Cardiology, University of Rabat", "500"},
            {"dr.fatima@mywelly.ma", "Dr. Fatima Zahra", "Dermatology", "Rabat",
             "+212612222222", "8:00-16:00", "MD Dermatology, University of Casablanca", "400"},
            {"dr.mohammed@mywelly.ma", "Dr. Mohammed Bennis", "Pediatrics", "Marrakech",
             "+212612333333", "10:00-18:00", "MD Pediatrics, University of Marrakech", "350"},
            {"dr.amina@mywelly.ma", "Dr. Amina Idrissi", "Orthopedics", "Tangier",
             "+212612444444", "9:00-17:00", "MD Orthopedics, University of Tangier", "600"},
            {"dr.youssef@mywelly.ma", "Dr. Youssef El Amrani", "Neurology", "Agadir",
             "+212612555555", "8:00-14:00", "MD Neurology, University of Agadir", "700"},
            {"dr.sara@mywelly.ma", "Dr. Sara Benjelloun", "General Practice", "Laayoune",
             "+212612666666", "9:00-19:00", "MD General Practice, University of Laayoune", "300"},
            {"dr.karim@mywelly.ma", "Dr. Karim Benkirane", "Cardiology", "Guelmim",
             "+212612777777", "8:30-16:30", "MD Cardiology, University of Guelmim", "450"},
            {"dr.zineb@mywelly.ma", "Dr. Zineb Chakir", "Dermatology", "Ben Guerir",
             "+212612888888", "10:00-18:00", "MD Dermatology, University of Ben Guerir", "380"},
        };

        for (String[] docData : doctors) {
            User doctorUser = new User();
            doctorUser.setEmail(docData[0]);
            doctorUser.setPassword("doctor123");

            Doctor doctor = new Doctor();
            doctor.setName(docData[1]);
            doctor.setSpecialty(docData[2]);
            doctor.setLocation(docData[3]);
            doctor.setPhoneNumber(docData[4]);
            doctor.setWorkingHours(docData[5]);
            doctor.setCertificates(docData[6]);
            doctor.setConsultationFee(Double.parseDouble(docData[7]));
            doctor.setAverageRating(4.0 + (Math.random() * 1.0));
            doctor.setTotalReviews((int)(Math.random() * 50) + 5);

            userService.registerDoctor(doctorUser, doctor);
        }

        // Create sample laboratories
        String[][] labs = {
            {"lab.biotech@mywelly.ma", "BioTech Lab Casablanca", "Casablanca",
             "+212612990001", "7:00-19:00"},
            {"lab.medanalysis@mywelly.ma", "MedAnalysis Rabat", "Rabat",
             "+212612990002", "8:00-18:00"},
            {"lab.lifelab@mywelly.ma", "LifeLab Marrakech", "Marrakech",
             "+212612990003", "7:30-17:30"},
        };

        for (String[] labData : labs) {
            User labUser = new User();
            labUser.setEmail(labData[0]);
            labUser.setPassword("lab123");

            Laboratory lab = new Laboratory();
            lab.setLabName(labData[1]);
            lab.setLocation(labData[2]);
            lab.setPhoneNumber(labData[3]);
            lab.setWorkingHours(labData[4]);
            lab.setAverageRating(4.2 + (Math.random() * 0.8));
            lab.setTotalReviews((int)(Math.random() * 30) + 10);

            userService.registerLaboratory(labUser, lab);
        }

        System.out.println("Sample data initialized successfully!");
    }
}
