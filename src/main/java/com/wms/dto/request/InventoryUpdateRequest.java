package com.wms.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class InventoryUpdateRequest {

    @Min(value = 0, message = "Minimum quantity cannot be negative")
    private Integer minQuantity;

    @Positive(message = "Maximum quantity must be positive")
    private Integer maxQuantity;

    // Constructors
    public InventoryUpdateRequest() {}

    public InventoryUpdateRequest(Integer minQuantity, Integer maxQuantity) {
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    // Getters and Setters
    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }

    public Integer getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(Integer maxQuantity) { this.maxQuantity = maxQuantity; }
}