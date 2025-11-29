package com.mywelly.service;

import com.mywelly.model.Laboratory;
import com.mywelly.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<Laboratory> getAllLaboratories() {
        return laboratoryRepository.findAll();
    }

    public Optional<Laboratory> getLaboratoryById(Long id) {
        return laboratoryRepository.findById(id);
    }

    public Optional<Laboratory> getLaboratoryByUserId(Long userId) {
        return laboratoryRepository.findByUserId(userId);
    }

    public List<Laboratory> searchLaboratories(String name, String location) {
        return laboratoryRepository.searchLaboratories(name, location);
    }

    public List<String> getAllLocations() {
        return laboratoryRepository.findAll().stream()
                .map(Laboratory::getLocation)
                .distinct()
                .sorted()
                .toList();
    }
}
