package com.wms.repository;

import com.wms.enums.MovementType;
import com.wms.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByInventoryId(Long inventoryId);

    List<StockMovement> findByMovementType(MovementType movementType);

    List<StockMovement> findByReferenceNumber(String referenceNumber);

    List<StockMovement> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.inventory.product.id = :productId")
    List<StockMovement> findByProductId(@Param("productId") Long productId);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.inventory.location.warehouse.id = :warehouseId")
    List<StockMovement> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT sm FROM StockMovement sm WHERE " +
            "(:inventoryId IS NULL OR sm.inventory.id = :inventoryId) AND " +
            "(:movementType IS NULL OR sm.movementType = :movementType) AND " +
            "(:startDate IS NULL OR sm.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR sm.createdAt <= :endDate) " +
            "ORDER BY sm.createdAt DESC")
    List<StockMovement> findByFilters(
            @Param("inventoryId") Long inventoryId,
            @Param("movementType") MovementType movementType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.inventory.location.warehouse.id = :warehouseId " +
            "AND sm.createdAt >= :startDate AND sm.createdAt <= :endDate ORDER BY sm.createdAt DESC")
    List<StockMovement> findByWarehouseIdAndDateRange(
            @Param("warehouseId") Long warehouseId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(CASE WHEN sm.movementType = 'INBOUND' THEN sm.quantity ELSE 0 END) as totalInbound, " +
            "SUM(CASE WHEN sm.movementType = 'OUTBOUND' THEN sm.quantity ELSE 0 END) as totalOutbound " +
            "FROM StockMovement sm WHERE sm.inventory.id = :inventoryId")
    Object[] getMovementSummaryByInventory(@Param("inventoryId") Long inventoryId);
}