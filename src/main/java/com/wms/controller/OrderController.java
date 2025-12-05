package com.wms.controller;

import com.wms.dto.common.ApiResponse;
import com.wms.dto.request.OrderRequest;
import com.wms.dto.request.OrderStatusUpdateRequest;
import com.wms.dto.response.OrderResponse;
import com.wms.enums.OrderStatus;
import com.wms.enums.OrderType;
import com.wms.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Order created successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Get all orders with optional filters
     * GET /api/orders?status=PENDING&type=INBOUND&startDate=...&endDate=...
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) OrderType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<OrderResponse> orders;

        if (status != null || type != null || startDate != null || endDate != null) {
            orders = orderService.getOrdersByFilters(status, type, startDate, endDate);
        } else {
            orders = orderService.getAllOrders();
        }

        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    /**
     * Get order by ID
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order retrieved successfully"));
    }

    /**
     * Get order by order number
     * GET /api/orders/number/{orderNumber}
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(
            @PathVariable String orderNumber) {

        OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(response, "Order retrieved successfully"));
    }

    /**
     * Get orders by status
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
            @PathVariable OrderStatus status) {

        List<OrderResponse> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    /**
     * Get orders by type
     * GET /api/orders/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByType(
            @PathVariable OrderType type) {

        List<OrderResponse> orders = orderService.getOrdersByType(type);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    /**
     * Update order status
     * PUT /api/orders/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {

        OrderResponse response = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Order status updated successfully"));
    }

    /**
     * Delete order (only PENDING or CANCELLED)
     * DELETE /api/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.deleted("Order deleted successfully"));
    }

    /**
     * Get order count by status
     * GET /api/orders/count/{status}
     */
    @GetMapping("/count/{status}")
    public ResponseEntity<ApiResponse<Long>> countOrdersByStatus(@PathVariable OrderStatus status) {
        Long count = orderService.countOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count, "Order count retrieved successfully"));
    }
}
