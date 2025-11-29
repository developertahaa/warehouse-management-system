package com.wms.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseLocation> locations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructors
    public Warehouse() {}

    public Warehouse(String name, String code, String address, Integer capacity) {
        this.name = name;
        this.code = code;
        this.address = address;
        this.capacity = capacity;
    }

    // Helper method to add location
    public void addLocation(WarehouseLocation location) {
        locations.add(location);
        location.setWarehouse(this);
    }

    // Helper method to remove location
    public void removeLocation(WarehouseLocation location) {
        locations.remove(location);
        location.setWarehouse(null);
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

    public List<WarehouseLocation> getLocations() { return locations; }
    public void setLocations(List<WarehouseLocation> locations) { this.locations = locations; }
}