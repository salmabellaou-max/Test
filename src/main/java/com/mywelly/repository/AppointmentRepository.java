package com.mywelly.repository;

import com.mywelly.model.Appointment;
import com.mywelly.model.Appointment.AppointmentStatus;
import com.mywelly.model.Doctor;
import com.mywelly.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientAndStatusOrderByAppointmentDateAscAppointmentTimeAsc(
        Patient patient, AppointmentStatus status);

    List<Appointment> findByPatientAndStatusInOrderByAppointmentDateDescAppointmentTimeDesc(
        Patient patient, List<AppointmentStatus> statuses);

    List<Appointment> findByDoctorAndStatusOrderByAppointmentDateAscAppointmentTimeAsc(
        Doctor doctor, AppointmentStatus status);

    List<Appointment> findByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
        Doctor doctor, LocalDate date, String time, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.status = 'SCHEDULED' AND " +
           "CONCAT(a.appointmentDate, ' ', a.appointmentTime) < :threshold")
    List<Appointment> findCompletedAppointments(@Param("threshold") String threshold);

    Long countByPatientAndStatus(Patient patient, AppointmentStatus status);
}
