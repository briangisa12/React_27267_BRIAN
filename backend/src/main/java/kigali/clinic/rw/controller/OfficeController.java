package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;


// shortcut: allows any origin, restrict to your frontend URL before deploying
@CrossOrigin(origins = "*")
@RestController
@RequestMapping ({"/api/office", "/api/offices"})
public class OfficeController {
    private final OfficeService offServe;
    public OfficeController(OfficeService offServe) {
        this.offServe = offServe;
    }
    @PostMapping
    public ResponseEntity<Office> createOffice(@RequestBody Office office) {
     Office savedOffice = offServe.saveOffice(office);
      return new ResponseEntity<>(savedOffice, HttpStatus.CREATED);
    }

    @PostMapping(value = "/save")  
    public ResponseEntity<String> saveOffice(@RequestBody Office office) {
     offServe.saveOffice(office);
       return new ResponseEntity<>("Office is saved successfully", HttpStatus.OK);
    }
    @GetMapping
    public List<Office> getAllOffices() {
      return offServe.getAllOffices();
    }
    @GetMapping("/busiest")
    public ResponseEntity<?> getBusiestOffice() {
     Object[] busiest = offServe.getBusiestOffice();

        if (busiest == null) {
         return ResponseEntity.ok("No appointments yet");
        }
        return ResponseEntity.ok(busiest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Office> getOfficeById(@PathVariable UUID id) {
        return offServe.getOfficeById(id)
       .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Office> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
 return offServe.updateOffice(id, office)
    .map(ResponseEntity::ok)
     .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable UUID id) {
        if (!offServe.deleteOffice(id)) {
       return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-by-office-number/{officeNumber}")
    public ResponseEntity<String> deleteOfficeByNumber(@PathVariable Integer officeNumber) {
        if (offServe.deleteofficeByOfficeNumber(officeNumber).equals("delete")) {
            return new ResponseEntity<>("Office deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
    }
}
