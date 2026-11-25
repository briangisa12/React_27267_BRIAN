package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping({"/api/appointment","/api/appointments"})
public class AppointmentController {

    private final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping({"", "/save"})
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        if (appointmentService.isDoctorAlreadyBooked(appointment)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor is already booked on that date");
        }
        Appointment savedAppointment = appointmentService.createAppointment(appointment);
        if (savedAppointment == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
    @GetMapping("/by-status")
    public List<Appointment> getAppointmentsByStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(@RequestParam String start, @RequestParam String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            return ResponseEntity.ok(appointmentService.getAppointmentsBetween(startDate, endDate));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/stats/by-status")
    public List<Object[]> getAppointmentStatsByStatus() {
        return appointmentService.getAppointmentStatsByStatus();
    }
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDoctorDay(@RequestParam UUID doctorId, @RequestParam String date) {
        LocalDate day;
        try {
            day = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date must be in the format yyyy-MM-dd");
        }

        int cancelled = appointmentService.cancelDoctorDay(doctorId, day);
        return ResponseEntity.ok(cancelled + " appointments cancelled");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getAppointmentsPage(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "appointmentDate,asc") String sort) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        if (page < 0 || size < 1) {
        return ResponseEntity.badRequest().body("page mustbe 0 or more and size must be 1 ormore");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        try {
            Page<Appointment> appointments = appointmentService.getAppointmentsPage(pageable);
            return ResponseEntity.ok(appointments);
        } catch (PropertyReferenceException e) {
            return ResponseEntity.badRequest().body("Cannot sort by " + sortField);
        }
    }
    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> deleteCancelledBefore(@RequestParam String date) {
        LocalDate day;
        try {
        day = LocalDate.parse(date);
    } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date must be in the format yyyy-MM-dd");
        }
        int deleted = appointmentService.deleteCancelledBefore(day);
        return ResponseEntity.ok(deleted + " appointments deleted");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        return appointmentService.getAppointmentById(id)
 .map(ResponseEntity::ok)
    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
    return appointmentService.updateAppointment(id, appointment)
    .map(ResponseEntity::ok)
     .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
    if (!appointmentService.deleteAppointment(id)) {
      return ResponseEntity.notFound().build();
    }        return ResponseEntity.noContent().build();
    }
}
