package com.safetynet.safetynetalerts.DTO;

import org.json.JSONObject;

public class PersonDTO {

    private String  firstName;
    private String  lastName;
    private String  address;
    private String  city;
    private Integer zip;
    private String  phone;
    private String  email;

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

    public JSONObject toJson() {

        return new JSONObject() {{
            this.put("firstName", PersonDTO.this.firstName);
            this.put("lastName", PersonDTO.this.lastName);
            this.put("address", PersonDTO.this.address);
            this.put("city", PersonDTO.this.city);
            this.put("zip", PersonDTO.this.zip);
            this.put("phone", PersonDTO.this.phone);
            this.put("email", PersonDTO.this.email);
        }};
    }
}
