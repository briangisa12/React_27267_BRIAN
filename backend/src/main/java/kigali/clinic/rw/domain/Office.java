package kigali.clinic.rw.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity 
@Table (name="office")
public class Office {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    @Column (name="name")
    private String name;
    @Column (name="office_number", unique = true)
    private Integer officeNumber;
    @OneToOne (mappedBy = "office")
    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties({"office", "appointments", "specializations"})
    private Doctor doctor;
    public String getName() {
        return name;}

    public void setName(String name) {
        this.name = name;
    }
    public Integer getOfficeNumber() {
   return officeNumber;
    }
    public void setOfficeNumber(Integer officeNumber) {
        this.officeNumber = officeNumber;
    }
    public UUID getId() {
   return id;
    }
    public Doctor getDoctor() {
        return doctor;
    }
}
