package com.wms.controller;

import com.wms.dto.common.ApiResponse;
import com.wms.dto.request.StockMovementRequest;
import com.wms.dto.response.MovementSummaryResponse;
import com.wms.dto.response.StockMovementResponse;
import com.wms.enums.MovementType;
import com.wms.service.StockMovementService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    /**
     * Record inbound stock movement (stock receipt)
     * POST /api/stock-movements/inbound
     */
    @PostMapping("/inbound")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordInbound(
            @Valid @RequestBody StockMovementRequest request) {

        StockMovementResponse response = stockMovementService.recordInbound(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Inbound stock movement recorded successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Record outbound stock movement (stock dispatch)
     * POST /api/stock-movements/outbound
     */
    @PostMapping("/outbound")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordOutbound(
            @Valid @RequestBody StockMovementRequest request) {

        StockMovementResponse response = stockMovementService.recordOutbound(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Outbound stock movement recorded successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Record stock adjustment
     * POST /api/stock-movements/adjustment?positive=true
     */
    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordAdjustment(
            @Valid @RequestBody StockMovementRequest request,
            @RequestParam(defaultValue = "true") boolean positive) {

        StockMovementResponse response = stockMovementService.recordAdjustment(request, positive);
        String message = positive ? "Positive adjustment recorded successfully" :
                "Negative adjustment recorded successfully";
        return new ResponseEntity<>(
                ApiResponse.created(response, message),
                HttpStatus.CREATED
        );
    }

    /**
     * Get all stock movements with optional filters
     * GET /api/stock-movements?inventoryId=1&type=INBOUND&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovements(
            @RequestParam(required = false) Long inventoryId,
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<StockMovementResponse> movements;

        if (inventoryId != null || type != null || startDate != null || endDate != null) {
            movements = stockMovementService.getMovementsByFilters(inventoryId, type, startDate, endDate);
        } else {
            movements = stockMovementService.getAllMovements();
        }

        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements retrieved successfully"));
    }

    /**
     * Get stock movement by ID
     * GET /api/stock-movements/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockMovementResponse>> getMovementById(@PathVariable Long id) {
        StockMovementResponse response = stockMovementService.getMovementById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock movement retrieved successfully"));
    }

    /**
     * Get movements by reference number
     * GET /api/stock-movements/reference/{referenceNumber}
     */
    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByReference(
            @PathVariable String referenceNumber) {

        List<StockMovementResponse> movements = stockMovementService.getMovementsByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements retrieved successfully"));
    }

    /**
     * Get movements by warehouse
     * GET /api/stock-movements/warehouse/{warehouseId}
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByWarehouse(
            @PathVariable Long warehouseId) {

        List<StockMovementResponse> movements = stockMovementService.getMovementsByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements retrieved successfully"));
    }

    /**
     * Get movements by product
     * GET /api/stock-movements/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByProduct(
            @PathVariable Long productId) {

        List<StockMovementResponse> movements = stockMovementService.getMovementsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements retrieved successfully"));
    }

    /**
     * Get movement summary for an inventory item
     * GET /api/stock-movements/summary/{inventoryId}
     */
    @GetMapping("/summary/{inventoryId}")
    public ResponseEntity<ApiResponse<MovementSummaryResponse>> getMovementSummary(
            @PathVariable Long inventoryId) {

        MovementSummaryResponse summary = stockMovementService.getMovementSummary(inventoryId);
        return ResponseEntity.ok(ApiResponse.success(summary, "Movement summary retrieved successfully"));
    }
}
