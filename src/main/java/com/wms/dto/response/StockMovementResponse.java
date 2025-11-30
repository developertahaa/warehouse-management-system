package com.wms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wms.enums.MovementType;
import com.wms.model.StockMovement;

import java.time.LocalDateTime;

public class StockMovementResponse {

    private Long id;
    private MovementType movementType;
    private Integer quantity;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String referenceNumber;
    private String notes;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Nested inventory info
    private InventoryInfo inventory;

    // Constructors
    public StockMovementResponse() {}

    // Static factory method
    public static StockMovementResponse fromEntity(StockMovement movement) {
        StockMovementResponse response = new StockMovementResponse();
        response.setId(movement.getId());
        response.setMovementType(movement.getMovementType());
        response.setQuantity(movement.getQuantity());
        response.setPreviousQuantity(movement.getPreviousQuantity());
        response.setNewQuantity(movement.getNewQuantity());
        response.setReferenceNumber(movement.getReferenceNumber());
        response.setNotes(movement.getNotes());
        response.setCreatedBy(movement.getCreatedBy());
        response.setCreatedAt(movement.getCreatedAt());

        // Set inventory info
        InventoryInfo inventoryInfo = new InventoryInfo();
        inventoryInfo.setId(movement.getInventory().getId());
        inventoryInfo.setCurrentQuantity(movement.getInventory().getQuantity());
        inventoryInfo.setProductId(movement.getInventory().getProduct().getId());
        inventoryInfo.setProductSku(movement.getInventory().getProduct().getSku());
        inventoryInfo.setProductName(movement.getInventory().getProduct().getName());
        inventoryInfo.setLocationId(movement.getInventory().getLocation().getId());
        inventoryInfo.setLocationCode(movement.getInventory().getLocation().getLocationCode());
        inventoryInfo.setWarehouseId(movement.getInventory().getLocation().getWarehouse().getId());
        inventoryInfo.setWarehouseName(movement.getInventory().getLocation().getWarehouse().getName());
        inventoryInfo.setWarehouseCode(movement.getInventory().getLocation().getWarehouse().getCode());
        response.setInventory(inventoryInfo);

        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getPreviousQuantity() { return previousQuantity; }
    public void setPreviousQuantity(Integer previousQuantity) { this.previousQuantity = previousQuantity; }

    public Integer getNewQuantity() { return newQuantity; }
    public void setNewQuantity(Integer newQuantity) { this.newQuantity = newQuantity; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public InventoryInfo getInventory() { return inventory; }
    public void setInventory(InventoryInfo inventory) { this.inventory = inventory; }

    // Inner class for Inventory info
    public static class InventoryInfo {
        private Long id;
        private Integer currentQuantity;
        private Long productId;
        private String productSku;
        private String productName;
        private Long locationId;
        private String locationCode;
        private Long warehouseId;
        private String warehouseName;
        private String warehouseCode;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Integer getCurrentQuantity() { return currentQuantity; }
        public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Long getLocationId() { return locationId; }
        public void setLocationId(Long locationId) { this.locationId = locationId; }

        public String getLocationCode() { return locationCode; }
        public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

        public Long getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

        public String getWarehouseName() { return warehouseName; }
        public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    }
}