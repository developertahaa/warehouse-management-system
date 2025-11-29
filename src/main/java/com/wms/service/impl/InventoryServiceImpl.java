package com.wms.service.impl;

import com.wms.dto.request.InventoryRequest;
import com.wms.dto.request.InventoryUpdateRequest;
import com.wms.dto.response.InventoryResponse;
import com.wms.dto.response.LowStockAlertResponse;
import com.wms.exception.BadRequestException;
import com.wms.exception.DuplicateResourceException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.model.Inventory;
import com.wms.model.Product;
import com.wms.model.WarehouseLocation;
import com.wms.repository.InventoryRepository;
import com.wms.repository.ProductRepository;
import com.wms.repository.WarehouseLocationRepository;
import com.wms.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository locationRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                 ProductRepository productRepository,
                                 WarehouseLocationRepository locationRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public InventoryResponse addInventory(InventoryRequest request) {
        // Check for duplicate
        if (inventoryRepository.existsByProductIdAndLocationId(request.getProductId(), request.getLocationId())) {
            throw new DuplicateResourceException("Inventory",
                    "product-location combination",
                    request.getProductId() + "-" + request.getLocationId());
        }

        // Get product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Get location
        WarehouseLocation location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", request.getLocationId()));

        // Validate thresholds
        if (request.getMinQuantity() != null && request.getMaxQuantity() != null &&
                request.getMinQuantity() > request.getMaxQuantity()) {
            throw new BadRequestException("Minimum quantity cannot be greater than maximum quantity");
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setLocation(location);
        inventory.setQuantity(request.getQuantity());
        inventory.setMinQuantity(request.getMinQuantity() != null ? request.getMinQuantity() : 10);
        inventory.setMaxQuantity(request.getMaxQuantity() != null ? request.getMaxQuantity() : 1000);

        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryResponse.fromEntity(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        return InventoryResponse.fromEntity(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryByWarehouse(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId).stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId).stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryByWarehouseAndProduct(Long warehouseId, Long productId) {
        return inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId).stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockAlertResponse> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(LowStockAlertResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockAlertResponse> getLowStockItemsByWarehouse(Long warehouseId) {
        return inventoryRepository.findLowStockItemsByWarehouse(warehouseId).stream()
                .map(LowStockAlertResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponse updateInventoryThresholds(Long id, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));

        Integer newMin = request.getMinQuantity() != null ? request.getMinQuantity() : inventory.getMinQuantity();
        Integer newMax = request.getMaxQuantity() != null ? request.getMaxQuantity() : inventory.getMaxQuantity();

        // Validate thresholds
        if (newMin > newMax) {
            throw new BadRequestException("Minimum quantity cannot be greater than maximum quantity");
        }

        inventory.setMinQuantity(newMin);
        inventory.setMaxQuantity(newMax);

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryResponse.fromEntity(updatedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalStockByProduct(Long productId) {
        Integer total = inventoryRepository.getTotalQuantityByProductId(productId);
        return total != null ? total : 0;
    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory", "id", id);
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    public void updateQuantity(Long inventoryId, int quantityChange) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", inventoryId));

        int newQuantity = inventory.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new BadRequestException("Insufficient stock. Available: " + inventory.getQuantity() +
                    ", Requested: " + Math.abs(quantityChange));
        }

        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
    }
}