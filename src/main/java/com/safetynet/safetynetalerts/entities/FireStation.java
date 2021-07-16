package com.safetynet.safetynetalerts.entities;

public class FireStation {

    private Long   station;
    private String address;

    public FireStation() {

    }

    public Long getStation() {

        return station;
    }

    public void setStation(Long station) {

        this.station = station;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;
        }

        if (obj == null) {

            return false;
        }

        if (this.getClass() != obj.getClass()) {

            return false;
        }

        FireStation fireStation = (FireStation) obj;

        return this.address.equals(fireStation.getAddress());
    }
}
