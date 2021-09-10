package com.safetynet.safetynetalerts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import org.json.JSONObject;

import java.time.LocalDate;

public class Person {

    private String    firstName;
    private String    lastName;
    private LocalDate birthDate;
    private String    address;
    private String    city;
    private Integer   zip;
    private String    phone;
    private String    email;

    @JsonIgnore
    private MedicalRecord medicalRecord;

    public Person() {

    }

    public Person(String firstName, String lastName, String address, Integer zip, String city, String phone, String email) {

        this.firstName = firstName;
        this.lastName  = lastName;
        this.address   = address;
        this.city      = city;
        this.zip       = zip;
        this.phone     = phone;
        this.email     = email;
    }

    public Person(Person person) {

        this.firstName     = person.getFirstName();
        this.lastName      = person.getLastName();
        this.birthDate     = person.getBirthDate();
        this.address       = person.getAddress();
        this.city          = person.getCity();
        this.zip           = person.getZip();
        this.phone         = person.getPhone();
        this.email         = person.getEmail();
        this.medicalRecord = person.getMedicalRecord();
    }

    public Person(MedicalRecordDTO medicalRecordDTO) {

        this.setFirstName(medicalRecordDTO.getFirstName());
        this.setLastName(medicalRecordDTO.getLastName());
        this.setBirthDate(medicalRecordDTO.getBirthdate());
        this.setMedicalRecord(new MedicalRecord(medicalRecordDTO.getMedications(), medicalRecordDTO.getAllergies()));
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {

        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {

        this.birthDate = birthDate;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public Integer getZip() {

        return zip;
    }

    public void setZip(Integer zip) {

        this.zip = zip;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public MedicalRecord getMedicalRecord() {

        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {

        this.medicalRecord = medicalRecord;
    }

    @Override
    public int hashCode() {

        return this.firstName.hashCode() * this.getLastName().hashCode() * this.email.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (o == null) {

            return false;
        }

        if (this.getClass() != o.getClass()) {

            return false;
        }

        Person person = (Person) o;

        return this.firstName.equalsIgnoreCase(person.firstName)
               && this.lastName.equalsIgnoreCase(person.lastName);
    }

    @Override
    public String toString() {

        return getLastName() + " " + getFirstName();
    }

    public JSONObject toJson() {

        return new JSONObject() {{
            this.put("firstName", Person.this.firstName);
            this.put("lastName", Person.this.lastName);
            this.put("birthDate", Person.this.birthDate);
            this.put("address", Person.this.address);
            this.put("city", Person.this.city);
            this.put("zip", Person.this.zip);
            this.put("phone", Person.this.phone);
            this.put("email", Person.this.email);
        }};
    }
}
