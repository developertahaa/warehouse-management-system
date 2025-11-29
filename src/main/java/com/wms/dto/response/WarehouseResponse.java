package com.wms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wms.model.Warehouse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseResponse {

    private Long id;
    private String name;
    private String code;
    private String address;
    private Integer capacity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private List<LocationResponse> locations;

    // Constructors
    public WarehouseResponse() {}

    public WarehouseResponse(Long id, String name, String code, String address, Integer capacity,
                             LocalDateTime createdAt, List<LocationResponse> locations) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.locations = locations;
    }

    // Static factory method - with locations
    public static WarehouseResponse fromEntity(Warehouse warehouse) {
        List<LocationResponse> locationResponses = warehouse.getLocations().stream()
                .map(LocationResponse::fromEntity)
                .collect(Collectors.toList());

        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getCode(),
                warehouse.getAddress(),
                warehouse.getCapacity(),
                warehouse.getCreatedAt(),
                locationResponses
        );
    }

    // Static factory method - without locations (for list views)
    public static WarehouseResponse fromEntityWithoutLocations(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getCode(),
                warehouse.getAddress(),
                warehouse.getCapacity(),
                warehouse.getCreatedAt(),
                null
        );
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<LocationResponse> getLocations() { return locations; }
    public void setLocations(List<LocationResponse> locations) { this.locations = locations; }
}