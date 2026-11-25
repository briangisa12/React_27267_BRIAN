package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    public AppointmentService(AppointmentRepository appointmentRepository,
        PatientRepository patientRepository,
          DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }
    public Appointment createAppointment(Appointment appointment) {
        Patient patient = findPatient(appointment.getPatient());
        Doctor doctor = findDoctor(appointment.getDoctor());
        if (patient == null || doctor == null) {
            return null;
        }
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }
        return appointmentRepository.save(appointment);
    }
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }
    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
       return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }
    public List<Object[]> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }
    @Transactional
    public int cancelDoctorDay(UUID doctorId, LocalDate date) {
        return appointmentRepository.cancelDoctorDay(doctorId, date,
                AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
    }
    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }
    @Transactional
    public int deleteCancelledBefore(LocalDate date) {
   return appointmentRepository.deleteByStatusBefore(AppointmentStatus.CANCELLED, date);
    }
    public boolean isDoctorAlreadyBooked(Appointment appointment) {
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null
   || appointment.getAppointmentDate() == null) {
            return false;
        }
        return appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED);
    }
    public Optional<Appointment> getAppointmentById(UUID id) {
   return appointmentRepository.findById(id);
    }
    public Optional<Appointment> updateAppointment(UUID id, Appointment appointmentDetails) {
        Optional<Appointment> foundAppointment = appointmentRepository.findById(id);
        if (foundAppointment.isEmpty()) {
            return Optional.empty();
}
        Appointment appointment = foundAppointment.get();
        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setReason(appointmentDetails.getReason());
        if (appointmentDetails.getStatus() != null) {
            appointment.setStatus(appointmentDetails.getStatus());
        }
        Patient patient = findPatient(appointmentDetails.getPatient());
        if (patient != null) {
       appointment.setPatient(patient);
        }
        Doctor doctor = findDoctor(appointmentDetails.getDoctor());
        if (doctor != null) {
            appointment.setDoctor(doctor);
        }
        return Optional.of(appointmentRepository.save(appointment));
    }
    public boolean deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            return false;
        }
        appointmentRepository.deleteById(id);
        return true;
    }
    private Patient findPatient(Patient patient) {        if (patient == null || patient.getId() == null) {
       return null;
        }
return patientRepository.findById(patient.getId()).orElse(null);    }
    private Doctor findDoctor(Doctor doctor) {
        if (doctor == null || doctor.getId() == null) {
            return null;
        }
        return doctorRepository.findById(doctor.getId()).orElse(null);
    }}
