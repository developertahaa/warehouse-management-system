package com.wms.controller;

import com.wms.dto.common.ApiResponse;
import com.wms.dto.request.InventoryRequest;
import com.wms.dto.request.InventoryUpdateRequest;
import com.wms.dto.response.InventoryResponse;
import com.wms.dto.response.LowStockAlertResponse;
import com.wms.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Add new inventory record
     * POST /api/inventory
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> addInventory(
            @Valid @RequestBody InventoryRequest request) {

        InventoryResponse response = inventoryService.addInventory(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Inventory added successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Get all inventory with optional filters
     * GET /api/inventory?warehouseId=1&productId=1
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getInventory(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long productId) {

        List<InventoryResponse> inventory;

        if (warehouseId != null && productId != null) {
            inventory = inventoryService.getInventoryByWarehouseAndProduct(warehouseId, productId);
        } else if (warehouseId != null) {
            inventory = inventoryService.getInventoryByWarehouse(warehouseId);
        } else if (productId != null) {
            inventory = inventoryService.getInventoryByProduct(productId);
        } else {
            inventory = inventoryService.getAllInventory();
        }

        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }

    /**
     * Get inventory by ID
     * GET /api/inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryById(@PathVariable Long id) {
        InventoryResponse response = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory retrieved successfully"));
    }

    /**
     * Get low stock items
     * GET /api/inventory/low-stock?warehouseId=1
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<LowStockAlertResponse>>> getLowStockItems(
            @RequestParam(required = false) Long warehouseId) {

        List<LowStockAlertResponse> lowStockItems;

        if (warehouseId != null) {
            lowStockItems = inventoryService.getLowStockItemsByWarehouse(warehouseId);
        } else {
            lowStockItems = inventoryService.getLowStockItems();
        }

        return ResponseEntity.ok(ApiResponse.success(lowStockItems, "Low stock items retrieved successfully"));
    }

    /**
     * Get total stock for a product across all locations
     * GET /api/inventory/total-stock/{productId}
     */
    @GetMapping("/total-stock/{productId}")
    public ResponseEntity<ApiResponse<Integer>> getTotalStockByProduct(@PathVariable Long productId) {
        Integer totalStock = inventoryService.getTotalStockByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(totalStock, "Total stock retrieved successfully"));
    }

    /**
     * Update inventory thresholds
     * PUT /api/inventory/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventoryThresholds(
            @PathVariable Long id,
            @Valid @RequestBody InventoryUpdateRequest request) {

        InventoryResponse response = inventoryService.updateInventoryThresholds(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory thresholds updated successfully"));
    }

    /**
     * Delete inventory record
     * DELETE /api/inventory/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(ApiResponse.deleted("Inventory deleted successfully"));
    }
}
