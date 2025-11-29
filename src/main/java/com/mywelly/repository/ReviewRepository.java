package com.mywelly.repository;

import com.mywelly.model.Review;
import com.mywelly.model.Doctor;
import com.mywelly.model.Laboratory;
import com.mywelly.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDoctorOrderByCreatedAtDesc(Doctor doctor);
    List<Review> findByLaboratoryOrderByCreatedAtDesc(Laboratory laboratory);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId")
    Double calculateAverageRatingForDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.laboratory.id = :labId")
    Double calculateAverageRatingForLab(@Param("labId") Long labId);

    Long countByDoctor(Doctor doctor);
    Long countByLaboratory(Laboratory laboratory);
}
