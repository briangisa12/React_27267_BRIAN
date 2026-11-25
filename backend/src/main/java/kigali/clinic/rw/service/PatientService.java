package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    public PatientService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
    }
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }
    public List<Patient> getAllPatients() {
   return patientRepository.findAll();
    }
    public List<Patient> getPatientsByLastName(String lastName) {
   return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }
    public List<Patient> getPatientsOfDoctor(UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {            return null;
        }
        return patientRepository.findPatientsOfDoctor(doctorId);
    }
    public List<Patient> getFrequentPatients(long min) {
   return patientRepository.findFrequentPatients(min);
    }
    public Optional<Patient> getPatientById(UUID id) {
   return patientRepository.findById(id);
    }
    public Optional<Patient> updatePatient(UUID id, Patient patientDetails) {
   Optional<Patient> foundPatient = patientRepository.findById(id);
        if (foundPatient.isEmpty()) {
            return Optional.empty();
        }
        Patient patient = foundPatient.get();
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        return Optional.of(patientRepository.save(patient));
    }
    public boolean deletePatient(UUID id) {
   if (!patientRepository.existsById(id)) {            return false;
        }

        patientRepository.deleteById(id);
        return true;
    }}