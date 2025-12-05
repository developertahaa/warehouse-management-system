package com.wms.repository;

import com.wms.enums.OrderStatus;
import com.wms.enums.OrderType;
import com.wms.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByOrderType(OrderType orderType);

    List<Order> findByStatusAndOrderType(OrderStatus status, OrderType orderType);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByOrderType(OrderType orderType, Pageable pageable);

    Page<Order> findByStatusAndOrderType(OrderStatus status, OrderType orderType, Pageable pageable);

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:orderType IS NULL OR o.orderType = :orderType) AND " +
            "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR o.createdAt <= :endDate) " +
            "ORDER BY o.createdAt DESC")
    List<Order> findByFilters(
            @Param("status") OrderStatus status,
            @Param("orderType") OrderType orderType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED' AND o.orderType = :orderType")
    BigDecimal getTotalAmountByOrderType(@Param("orderType") OrderType orderType);

    boolean existsByOrderNumber(String orderNumber);
}