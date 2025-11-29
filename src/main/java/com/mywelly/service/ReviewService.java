package com.mywelly.service;

import com.mywelly.model.Review;
import com.mywelly.model.Doctor;
import com.mywelly.model.Laboratory;
import com.mywelly.model.Patient;
import com.mywelly.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DoctorService doctorService;

    public ReviewService(ReviewRepository reviewRepository, DoctorService doctorService) {
        this.reviewRepository = reviewRepository;
        this.doctorService = doctorService;
    }

    public Review createReviewForDoctor(Patient patient, Doctor doctor, Integer rating, String comment) {
        Review review = new Review();
        review.setPatient(patient);
        review.setDoctor(doctor);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);

        // Update doctor's average rating
        updateDoctorRating(doctor.getId());

        return savedReview;
    }

    public Review createReviewForLab(Patient patient, Laboratory laboratory, Integer rating, String comment) {
        Review review = new Review();
        review.setPatient(patient);
        review.setLaboratory(laboratory);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForDoctor(Doctor doctor) {
        return reviewRepository.findByDoctorOrderByCreatedAtDesc(doctor);
    }

    public List<Review> getReviewsForLab(Laboratory laboratory) {
        return reviewRepository.findByLaboratoryOrderByCreatedAtDesc(laboratory);
    }

    private void updateDoctorRating(Long doctorId) {
        Double avgRating = reviewRepository.calculateAverageRatingForDoctor(doctorId);
        Long totalReviews = reviewRepository.countByDoctor(
                doctorService.getDoctorById(doctorId).orElse(null));

        if (avgRating != null) {
            doctorService.updateRating(doctorId,
                    Math.round(avgRating * 10.0) / 10.0,
                    totalReviews.intValue());
        }
    }
}
