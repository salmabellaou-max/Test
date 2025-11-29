package com.mywelly.repository;

import com.mywelly.model.Doctor;
import com.mywelly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findBySpecialty(String specialty);
    List<Doctor> findByLocation(String location);
    List<Doctor> findByNameContainingIgnoreCase(String name);

    @Query("SELECT d FROM Doctor d WHERE " +
           "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:specialty IS NULL OR d.specialty = :specialty) AND " +
           "(:location IS NULL OR d.location = :location) AND " +
           "(:minPrice IS NULL OR d.consultationFee >= :minPrice) AND " +
           "(:maxPrice IS NULL OR d.consultationFee <= :maxPrice) " +
           "ORDER BY d.averageRating DESC")
    List<Doctor> searchDoctors(@Param("name") String name,
                               @Param("specialty") String specialty,
                               @Param("location") String location,
                               @Param("minPrice") Double minPrice,
                               @Param("maxPrice") Double maxPrice);

    @Query("SELECT DISTINCT d.specialty FROM Doctor d ORDER BY d.specialty")
    List<String> findAllSpecialties();

    @Query("SELECT DISTINCT d.location FROM Doctor d ORDER BY d.location")
    List<String> findAllLocations();
}
