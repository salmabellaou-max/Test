package com.mywelly.service;

import com.mywelly.model.*;
import com.mywelly.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                      PatientRepository patientRepository,
                      DoctorRepository doctorRepository,
                      LaboratoryRepository laboratoryRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerPatient(User user, Patient patient) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(User.UserRole.PATIENT);
        User savedUser = userRepository.save(user);

        patient.setUser(savedUser);
        patientRepository.save(patient);

        return savedUser;
    }

    public User registerDoctor(User user, Doctor doctor) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(User.UserRole.DOCTOR);
        User savedUser = userRepository.save(user);

        doctor.setUser(savedUser);
        doctorRepository.save(doctor);

        return savedUser;
    }

    public User registerLaboratory(User user, Laboratory laboratory) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(User.UserRole.LABORATORY);
        User savedUser = userRepository.save(user);

        laboratory.setUser(savedUser);
        laboratoryRepository.save(laboratory);

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void recordFailedLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);
        });
    }

    public void resetFailedLoginAttempts(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        });
    }
}
