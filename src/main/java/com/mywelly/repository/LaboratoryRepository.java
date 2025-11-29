package com.mywelly.repository;

import com.mywelly.model.Laboratory;
import com.mywelly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
    Optional<Laboratory> findByUser(User user);
    Optional<Laboratory> findByUserId(Long userId);
    List<Laboratory> findByLocation(String location);
    List<Laboratory> findByLabNameContainingIgnoreCase(String name);

    @Query("SELECT l FROM Laboratory l WHERE " +
           "(:name IS NULL OR LOWER(l.labName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:location IS NULL OR l.location = :location) " +
           "ORDER BY l.averageRating DESC")
    List<Laboratory> searchLaboratories(@Param("name") String name,
                                        @Param("location") String location);
}
