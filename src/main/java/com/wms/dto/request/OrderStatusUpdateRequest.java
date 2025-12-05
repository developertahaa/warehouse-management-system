package com.wms.dto.request;

import com.wms.enums.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull(message = "New status is required")
    private OrderStatus newStatus;

    private String notes;

    // Constructors
    public OrderStatusUpdateRequest() {}

    public OrderStatusUpdateRequest(OrderStatus newStatus, String notes) {
        this.newStatus = newStatus;
        this.notes = notes;
    }

    // Getters and Setters
    public OrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(OrderStatus newStatus) { this.newStatus = newStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}