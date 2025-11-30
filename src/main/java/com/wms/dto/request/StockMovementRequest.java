package com.wms.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class StockMovementRequest {

    @NotNull(message = "Inventory ID is required")
    private Long inventoryId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Size(max = 50, message = "Reference number must not exceed 50 characters")
    private String referenceNumber;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;

    // Constructors
    public StockMovementRequest() {}

    public StockMovementRequest(Long inventoryId, Integer quantity, String referenceNumber, String notes) {
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}