package com.wms.model;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_locations")
public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "zone", nullable = false, length = 10)
    private String zone;

    @Column(name = "aisle", nullable = false, length = 10)
    private String aisle;

    @Column(name = "rack", nullable = false, length = 10)
    private String rack;

    @Column(name = "shelf", nullable = false, length = 10)
    private String shelf;

    @Column(name = "capacity")
    private Integer capacity = 100;

    @Column(name = "location_code", length = 50)
    private String locationCode;

    @PrePersist
    protected void onCreate() {
        generateLocationCode();
    }

    @PreUpdate
    protected void onUpdate() {
        generateLocationCode();
    }

    private void generateLocationCode() {
        this.locationCode = String.format("%s-%s-%s-%s", zone, aisle, rack, shelf);
    }

    // Constructors
    public WarehouseLocation() {}

    public WarehouseLocation(String zone, String aisle, String rack, String shelf, Integer capacity) {
        this.zone = zone;
        this.aisle = aisle;
        this.rack = rack;
        this.shelf = shelf;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

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