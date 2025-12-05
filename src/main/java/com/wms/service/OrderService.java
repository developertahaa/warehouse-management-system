package com.wms.service;

import com.wms.dto.request.OrderRequest;
import com.wms.dto.request.OrderStatusUpdateRequest;
import com.wms.dto.response.OrderResponse;
import com.wms.enums.OrderStatus;
import com.wms.enums.OrderType;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByStatus(OrderStatus status);

    List<OrderResponse> getOrdersByType(OrderType orderType);

    List<OrderResponse> getOrdersByFilters(OrderStatus status, OrderType orderType,
                                            LocalDateTime startDate, LocalDateTime endDate);

    OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request);

    void deleteOrder(Long id);

    Long countOrdersByStatus(OrderStatus status);
}