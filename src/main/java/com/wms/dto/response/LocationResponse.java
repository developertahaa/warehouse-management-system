package com.wms.dto.response;

import com.wms.model.WarehouseLocation;

public class LocationResponse {

    private Long id;
    private String zone;
    private String aisle;
    private String rack;
    private String shelf;
    private Integer capacity;
    private String locationCode;

    // Constructors
    public LocationResponse() {}

    public LocationResponse(Long id, String zone, String aisle, String rack, String shelf,
                            Integer capacity, String locationCode) {
        this.id = id;
        this.zone = zone;
        this.aisle = aisle;
        this.rack = rack;
        this.shelf = shelf;
        this.capacity = capacity;
        this.locationCode = locationCode;
    }

    // Static factory method
    public static LocationResponse fromEntity(WarehouseLocation location) {
        return new LocationResponse(
                location.getId(),
                location.getZone(),
                location.getAisle(),
                location.getRack(),
                location.getShelf(),
                location.getCapacity(),
                location.getLocationCode()
        );
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
}