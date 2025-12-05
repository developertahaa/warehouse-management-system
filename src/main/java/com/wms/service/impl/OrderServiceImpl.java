package com.wms.service.impl;

import com.wms.dto.request.OrderItemRequest;
import com.wms.dto.request.OrderRequest;
import com.wms.dto.request.OrderStatusUpdateRequest;
import com.wms.dto.response.OrderResponse;
import com.wms.enums.OrderStatus;
import com.wms.enums.OrderType;
import com.wms.exception.BadRequestException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.model.Order;
import com.wms.model.OrderItem;
import com.wms.model.Product;
import com.wms.repository.OrderRepository;
import com.wms.repository.ProductRepository;
import com.wms.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // Create order
        Order order = new Order();
        order.setOrderType(request.getOrderType());
        order.setNotes(request.getNotes());
        order.setStatus(OrderStatus.PENDING);

        // Add items
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemRequest.getProductId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getUnitPrice()); // Capture current price

            order.addItem(item);
        }

        // Save order (cascades to items)
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return OrderResponse.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        return OrderResponse.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromEntityWithoutItems)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(OrderResponse::fromEntityWithoutItems)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByType(OrderType orderType) {
        return orderRepository.findByOrderType(orderType).stream()
                .map(OrderResponse::fromEntityWithoutItems)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByFilters(OrderStatus status, OrderType orderType,
                                                   LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByFilters(status, orderType, startDate, endDate).stream()
                .map(OrderResponse::fromEntityWithoutItems)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Validate status transition
        validateStatusTransition(order.getStatus(), request.getNewStatus());

        order.setStatus(request.getNewStatus());

        // Append notes if provided
        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            String existingNotes = order.getNotes() != null ? order.getNotes() + "\n" : "";
            order.setNotes(existingNotes + "[" + request.getNewStatus() + "] " + request.getNotes());
        }

        Order updatedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Only allow deletion of PENDING or CANCELLED orders
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot delete order with status: " + order.getStatus() +
                    ". Only PENDING or CANCELLED orders can be deleted.");
        }

        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    // Validate status transitions
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // COMPLETED orders cannot be changed
        if (currentStatus == OrderStatus.COMPLETED) {
            throw new BadRequestException("Cannot change status of a COMPLETED order");
        }

        // CANCELLED orders cannot be changed
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot change status of a CANCELLED order");
        }

        // Valid transitions:
        // PENDING -> PROCESSING, CANCELLED
        // PROCESSING -> COMPLETED, CANCELLED

        if (currentStatus == OrderStatus.PENDING) {
            if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                throw new BadRequestException("PENDING order can only be changed to PROCESSING or CANCELLED");
            }
        }

        if (currentStatus == OrderStatus.PROCESSING) {
            if (newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.CANCELLED) {
                throw new BadRequestException("PROCESSING order can only be changed to COMPLETED or CANCELLED");
            }
        }
    }
}