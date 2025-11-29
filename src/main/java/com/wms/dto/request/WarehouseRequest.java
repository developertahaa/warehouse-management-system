package com.wms.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class WarehouseRequest {

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @Valid
    private List<LocationRequest> locations = new ArrayList<>();

    // Constructors
    public WarehouseRequest() {}

    public WarehouseRequest(String name, String code, String address, Integer capacity, List<LocationRequest> locations) {
        this.name = name;
        this.code = code;
        this.address = address;
        this.capacity = capacity;
        this.locations = locations;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public List<LocationRequest> getLocations() { return locations; }
    public void setLocations(List<LocationRequest> locations) { this.locations = locations; }
}