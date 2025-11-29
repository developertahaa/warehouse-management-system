package com.wms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wms.model.Inventory;

import java.time.LocalDateTime;

public class InventoryResponse {

    private Long id;
    private Integer quantity;
    private Integer minQuantity;
    private Integer maxQuantity;
    private boolean lowStock;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Nested product info
    private ProductInfo product;

    // Nested location info
    private LocationInfo location;

    // Constructors
    public InventoryResponse() {}

    // Static factory method
    public static InventoryResponse fromEntity(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setQuantity(inventory.getQuantity());
        response.setMinQuantity(inventory.getMinQuantity());
        response.setMaxQuantity(inventory.getMaxQuantity());
        response.setLowStock(inventory.isLowStock());
        response.setUpdatedAt(inventory.getUpdatedAt());

        // Set product info
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(inventory.getProduct().getId());
        productInfo.setSku(inventory.getProduct().getSku());
        productInfo.setName(inventory.getProduct().getName());
        productInfo.setCategory(inventory.getProduct().getCategory());
        response.setProduct(productInfo);

        // Set location info
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setId(inventory.getLocation().getId());
        locationInfo.setLocationCode(inventory.getLocation().getLocationCode());
        locationInfo.setZone(inventory.getLocation().getZone());
        locationInfo.setWarehouseId(inventory.getLocation().getWarehouse().getId());
        locationInfo.setWarehouseName(inventory.getLocation().getWarehouse().getName());
        locationInfo.setWarehouseCode(inventory.getLocation().getWarehouse().getCode());
        response.setLocation(locationInfo);

        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }

    public Integer getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(Integer maxQuantity) { this.maxQuantity = maxQuantity; }

    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public ProductInfo getProduct() { return product; }
    public void setProduct(ProductInfo product) { this.product = product; }

    public LocationInfo getLocation() { return location; }
    public void setLocation(LocationInfo location) { this.location = location; }

    // Inner class for Product info
    public static class ProductInfo {
        private Long id;
        private String sku;
        private String name;
        private String category;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    // Inner class for Location info
    public static class LocationInfo {
        private Long id;
        private String locationCode;
        private String zone;
        private Long warehouseId;
        private String warehouseName;
        private String warehouseCode;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getLocationCode() { return locationCode; }
        public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public Long getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
        public String getWarehouseName() { return warehouseName; }
        public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    }
}