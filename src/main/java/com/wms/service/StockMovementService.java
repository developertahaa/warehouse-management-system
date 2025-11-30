package com.wms.service;

import com.wms.dto.request.StockMovementRequest;
import com.wms.dto.response.MovementSummaryResponse;
import com.wms.dto.response.StockMovementResponse;
import com.wms.enums.MovementType;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementService {

    StockMovementResponse recordInbound(StockMovementRequest request);

    StockMovementResponse recordOutbound(StockMovementRequest request);

    StockMovementResponse recordAdjustment(StockMovementRequest request, boolean isPositive);

    StockMovementResponse getMovementById(Long id);

    List<StockMovementResponse> getAllMovements();

    List<StockMovementResponse> getMovementsByInventory(Long inventoryId);

    List<StockMovementResponse> getMovementsByType(MovementType type);

    List<StockMovementResponse> getMovementsByReferenceNumber(String referenceNumber);

    List<StockMovementResponse> getMovementsByFilters(Long inventoryId, MovementType type,
                                                       LocalDateTime startDate, LocalDateTime endDate);

    List<StockMovementResponse> getMovementsByWarehouse(Long warehouseId);

    List<StockMovementResponse> getMovementsByProduct(Long productId);

    MovementSummaryResponse getMovementSummary(Long inventoryId);
}