package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    public List<Doctor> getAllDoctors() {
    return doctorRepository.findAll();
    }
    public List<Doctor> getDoctorsBySpecialization(String name) {
 return doctorRepository.findBySpecializationName(name);
    }
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();}
    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepository.findById(id);
    }
    public Optional<Doctor> updateDoctor(UUID id, Doctor doctorDetails) {
        Optional<Doctor> foundDoctor = doctorRepository.findById(id);
        if (foundDoctor.isEmpty()) {
        return Optional.empty();
        }
        Doctor doctor = foundDoctor.get();
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setDateOfBirth(doctorDetails.getDateOfBirth());
        doctor.setOffice(doctorDetails.getOffice());
        return Optional.of(doctorRepository.save(doctor));
    }
    public boolean deleteDoctor(UUID id) {
        if (!doctorRepository.existsById(id)) {
     return false;
    }
        doctorRepository.deleteById(id);
        return true;
}}