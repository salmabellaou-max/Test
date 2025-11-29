package com.mywelly.service;

import com.mywelly.model.Patient;
import com.mywelly.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    public void incrementNoShowCount(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setNoShowCount(patient.getNoShowCount() + 1);
        if (patient.getNoShowCount() >= 3) {
            patient.setIsBlocked(true);
        }
        patientRepository.save(patient);
    }

    public boolean isPatientBlocked(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return patient.getIsBlocked();
    }

    public void updatePhoneNumber(Long patientId, String newPhoneNumber) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setPhoneNumber(newPhoneNumber);
        patientRepository.save(patient);
    }
}
