package com.mywelly.service;

import com.mywelly.model.Appointment;
import com.mywelly.model.Appointment.AppointmentStatus;
import com.mywelly.model.Doctor;
import com.mywelly.model.Patient;
import com.mywelly.repository.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                             PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
    }

    public Appointment createAppointment(Patient patient, Doctor doctor,
                                        LocalDate date, String time) {
        // Check for conflicts
        List<Appointment> conflicts = appointmentRepository
                .findByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
                        doctor, date, time, AppointmentStatus.SCHEDULED);

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getUpcomingAppointmentsForPatient(Patient patient) {
        return appointmentRepository
                .findByPatientAndStatusOrderByAppointmentDateAscAppointmentTimeAsc(
                        patient, AppointmentStatus.SCHEDULED);
    }

    public List<Appointment> getPastAppointmentsForPatient(Patient patient) {
        return appointmentRepository
                .findByPatientAndStatusInOrderByAppointmentDateDescAppointmentTimeDesc(
                        patient, Arrays.asList(
                                AppointmentStatus.COMPLETED,
                                AppointmentStatus.CANCELLED_BY_PATIENT,
                                AppointmentStatus.CANCELLED_BY_DOCTOR,
                                AppointmentStatus.NO_SHOW
                        ));
    }

    public List<Appointment> getUpcomingAppointmentsForDoctor(Doctor doctor) {
        return appointmentRepository
                .findByDoctorAndStatusOrderByAppointmentDateAscAppointmentTimeAsc(
                        doctor, AppointmentStatus.SCHEDULED);
    }

    public void cancelAppointmentByPatient(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_PATIENT);
        appointmentRepository.save(appointment);
    }

    public void cancelAppointmentByDoctor(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_DOCTOR);
        appointment.setCancellationReason(reason);
        appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // Scheduled task to mark appointments as completed (runs every hour)
    @Scheduled(cron = "0 0 * * * *")
    public void updateCompletedAppointments() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        String threshold = twoHoursAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        List<Appointment> completedAppointments =
                appointmentRepository.findCompletedAppointments(threshold);

        for (Appointment appointment : completedAppointments) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
        }
    }
}
