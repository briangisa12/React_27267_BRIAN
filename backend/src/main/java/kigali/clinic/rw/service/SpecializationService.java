package kigali.clinic.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;
@Service
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final DoctorRepository doctorRepository;
    public SpecializationService(SpecializationRepository specializationRepository, DoctorRepository doctorRepository) {
        this.specializationRepository = specializationRepository;
        this.doctorRepository = doctorRepository;
    }
    public Specialization createSpecialization(Specialization specialization) {
   specialization.setDoctors(findDoctors(specialization.getDoctors()));        return specializationRepository.save(specialization);
    }

    public List<Specialization> getAllSpecializations() {  
    return specializationRepository.findAll();
    }
    public Optional<Specialization> getSpecializationById(UUID id) {
   return specializationRepository.findById(id);
    }
    public Optional<Specialization> updateSpecialization(UUID id, Specialization specializationDetails) {
        Optional<Specialization> foundSpecialization = specializationRepository.findById(id);
        if (foundSpecialization.isEmpty()) {
            return Optional.empty();
        }
        Specialization specialization = foundSpecialization.get();
        specialization.setName(specializationDetails.getName());
        specialization.setDoctors(findDoctors(specializationDetails.getDoctors()));
        return Optional.of(specializationRepository.save(specialization));
    }
    public List<Specialization> getUnusedSpecializations() {
   return specializationRepository.findUnusedSpecializations();
    }
    public boolean deleteSpecialization(UUID id) {
        if (!specializationRepository.existsById(id)) {
            return false;
        }
        specializationRepository.deleteById(id);
        return true;
    }
    private List<Doctor> findDoctors(List<Doctor> doctorsFromRequest) {
        List<Doctor> doctors = new ArrayList<>();
        if (doctorsFromRequest == null) {
            return doctors;
        }

        for (Doctor d : doctorsFromRequest) {
            if (d.getId() != null) {
                Optional<Doctor> found = doctorRepository.findById(d.getId());
                if (found.isPresent()) {
                    doctors.add(found.get());
                }
            }
        }

        return doctors;
    }}
