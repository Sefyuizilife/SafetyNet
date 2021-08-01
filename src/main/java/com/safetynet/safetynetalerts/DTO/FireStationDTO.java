package com.safetynet.safetynetalerts.DTO;

public class FireStationDTO {

    private String address;
    private Long   station;

    public FireStationDTO() {

    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public Long getStation() {

        return station;
    }

    public void setStation(Long station) {

        this.station = station;
    }
}
