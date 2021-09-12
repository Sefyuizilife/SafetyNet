package com.safetynet.safetynetalerts.DTO;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONObject toJson() {

        return new JSONObject() {{
            this.put("station", new JSONArray(FireStationDTO.this.station));
            this.put("address", new JSONArray(FireStationDTO.this.address));
        }};
    }
}
