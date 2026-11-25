package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping({"/api/doctor", "/api/doctors"})
public class DoctorController {
    private final DoctorService doctorService;
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;}
    @PostMapping({"", "/save"})
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
    Doctor savedDoctor = doctorService.createDoctor(doctor);
    return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }
    @GetMapping
    public List<Doctor> getAllDoctors() {
   return doctorService.getAllDoctors();
    }
    @GetMapping("/by-specialization")
    public List<Doctor> getDoctorsBySpecialization(@RequestParam String name) {
    return doctorService.getDoctorsBySpecialization(name);
    }
    @GetMapping("/without-office")
    public List<Doctor> getDoctorsWithoutOffice() {
    return doctorService.getDoctorsWithoutOffice();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable UUID id) {
 return doctorService.getDoctorById(id)
.map(ResponseEntity::ok)
.orElseGet(() -> ResponseEntity.notFound().build());
  }

 @PutMapping("/{id}")
 public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
 return doctorService.updateDoctor(id, doctor)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
    if (!doctorService.deleteDoctor(id)) {
    return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}