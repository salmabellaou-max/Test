package com.mywelly.service;

import com.mywelly.model.Doctor;
import com.mywelly.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public List<Doctor> searchDoctors(String name, String specialty, String location,
                                     Double minPrice, Double maxPrice) {
        return doctorRepository.searchDoctors(name, specialty, location, minPrice, maxPrice);
    }

    public List<String> getAllSpecialties() {
        return doctorRepository.findAllSpecialties();
    }

    public List<String> getAllLocations() {
        return doctorRepository.findAllLocations();
    }

    public void updateLocation(Long doctorId, String newLocation) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setLocation(newLocation);
        doctorRepository.save(doctor);
    }

    public void updateRating(Long doctorId, Double avgRating, Integer totalReviews) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setAverageRating(avgRating);
        doctor.setTotalReviews(totalReviews);
        doctorRepository.save(doctor);
    }
}
