package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;
@Service 
public class OfficeService {
    private final OfficeRepository offRepo;
    public OfficeService(OfficeRepository offRepo) {
        this.offRepo = offRepo;
    }
    public Office saveOffice(Office office) {
   return offRepo.save(office);
    }
    public List<Office> getAllOffices() {
   return offRepo.findAll();
    }
    public Object[] getBusiestOffice() {
        List<Object[]> rows = offRepo.findBusiestOffice(PageRequest.of(0, 1));        if (rows.isEmpty()) {
       return null;
        }
        return rows.get(0);
    }
    public Optional<Office> getOfficeById(UUID id) {
   return offRepo.findById(id);
    }
    public Optional<Office> updateOffice(UUID id, Office officeDetails) {
        Optional<Office> foundOffice = offRepo.findById(id);
        if (foundOffice.isEmpty()) {
    return Optional.empty();
    }
        Office office = foundOffice.get();
        office.setName(officeDetails.getName());
        office.setOfficeNumber(officeDetails.getOfficeNumber());
        return Optional.of(offRepo.save(office));
    }
    @Transactional
    public boolean deleteOffice(UUID id) {
        Optional<Office> found = offRepo.findById(id);
        if (found.isEmpty()) {
       return false;
        }
        Office office = found.get();
        // a doctor still points to this office (foreign key), so unlink it first or the delete fails
        if (office.getDoctor() != null) {
            office.getDoctor().setOffice(null);
        }
        offRepo.delete(office);
        return true;
    }

    public String deleteofficeByOfficeNumber(int officeNumber) {
        Optional<Office> getOffice = offRepo.findByOfficeNumber(officeNumber);
        if (getOffice.isPresent()) {
            offRepo.delete(getOffice.get());
            return "delete";
        }else {
            return "not deleted";
        }
    }
}
