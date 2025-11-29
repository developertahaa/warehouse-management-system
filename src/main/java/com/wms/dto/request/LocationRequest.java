package com.wms.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class LocationRequest {

    @NotBlank(message = "Zone is required")
    @Size(max = 10, message = "Zone must not exceed 10 characters")
    private String zone;

    @NotBlank(message = "Aisle is required")
    @Size(max = 10, message = "Aisle must not exceed 10 characters")
    private String aisle;

    @NotBlank(message = "Rack is required")
    @Size(max = 10, message = "Rack must not exceed 10 characters")
    private String rack;

    @NotBlank(message = "Shelf is required")
    @Size(max = 10, message = "Shelf must not exceed 10 characters")
    private String shelf;

    @Positive(message = "Capacity must be positive")
    private Integer capacity = 100;

    // Constructors
    public LocationRequest() {}

    public LocationRequest(String zone, String aisle, String rack, String shelf, Integer capacity) {
        this.zone = zone;
        this.aisle = aisle;
        this.rack = rack;
        this.shelf = shelf;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }

    public String getRack() { return rack; }
    public void setRack(String rack) { this.rack = rack; }

    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}