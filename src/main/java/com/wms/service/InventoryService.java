package com.wms.service;

import com.wms.dto.request.InventoryRequest;
import com.wms.dto.request.InventoryUpdateRequest;
import com.wms.dto.response.InventoryResponse;
import com.wms.dto.response.LowStockAlertResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse addInventory(InventoryRequest request);

    InventoryResponse getInventoryById(Long id);

    List<InventoryResponse> getAllInventory();

    List<InventoryResponse> getInventoryByWarehouse(Long warehouseId);

    List<InventoryResponse> getInventoryByProduct(Long productId);

    List<InventoryResponse> getInventoryByWarehouseAndProduct(Long warehouseId, Long productId);

    List<LowStockAlertResponse> getLowStockItems();

    List<LowStockAlertResponse> getLowStockItemsByWarehouse(Long warehouseId);

    InventoryResponse updateInventoryThresholds(Long id, InventoryUpdateRequest request);

    Integer getTotalStockByProduct(Long productId);

    void deleteInventory(Long id);

    // Internal method for stock movements
    void updateQuantity(Long inventoryId, int quantityChange);
}