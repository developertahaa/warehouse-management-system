package com.wms.dto.request;

import com.wms.enums.OrderType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequest> items = new ArrayList<>();

    // Constructors
    public OrderRequest() {}

    public OrderRequest(OrderType orderType, String notes, List<OrderItemRequest> items) {
        this.orderType = orderType;
        this.notes = notes;
        this.items = items;
    }

    // Getters and Setters
    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}