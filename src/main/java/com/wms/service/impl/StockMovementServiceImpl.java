package com.wms.service.impl;

import com.wms.dto.request.StockMovementRequest;
import com.wms.dto.response.MovementSummaryResponse;
import com.wms.dto.response.StockMovementResponse;
import com.wms.enums.MovementType;
import com.wms.exception.BadRequestException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.model.Inventory;
import com.wms.model.StockMovement;
import com.wms.repository.InventoryRepository;
import com.wms.repository.StockMovementRepository;
import com.wms.service.StockMovementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final InventoryRepository inventoryRepository;

    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository,
                                     InventoryRepository inventoryRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public StockMovementResponse recordInbound(StockMovementRequest request) {
        Inventory inventory = getInventoryById(request.getInventoryId());

        int previousQuantity = inventory.getQuantity();
        int newQuantity = previousQuantity + request.getQuantity();

        // Update inventory
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);

        // Create movement record
        StockMovement movement = new StockMovement();
        movement.setInventory(inventory);
        movement.setMovementType(MovementType.INBOUND);
        movement.setQuantity(request.getQuantity());
        movement.setPreviousQuantity(previousQuantity);
        movement.setNewQuantity(newQuantity);
        movement.setReferenceNumber(request.getReferenceNumber());
        movement.setNotes(request.getNotes());
        movement.setCreatedBy("SYSTEM");

        StockMovement savedMovement = stockMovementRepository.save(movement);
        return StockMovementResponse.fromEntity(savedMovement);
    }

    @Override
    public StockMovementResponse recordOutbound(StockMovementRequest request) {
        Inventory inventory = getInventoryById(request.getInventoryId());

        int previousQuantity = inventory.getQuantity();

        // Validate sufficient stock
        if (!inventory.hasEnoughStock(request.getQuantity())) {
            throw new BadRequestException(
                    String.format("Insufficient stock. Available: %d, Requested: %d",
                            previousQuantity, request.getQuantity()));
        }

        int newQuantity = previousQuantity - request.getQuantity();

        // Update inventory
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);

        // Create movement record
        StockMovement movement = new StockMovement();
        movement.setInventory(inventory);
        movement.setMovementType(MovementType.OUTBOUND);
        movement.setQuantity(request.getQuantity());
        movement.setPreviousQuantity(previousQuantity);
        movement.setNewQuantity(newQuantity);
        movement.setReferenceNumber(request.getReferenceNumber());
        movement.setNotes(request.getNotes());
        movement.setCreatedBy("SYSTEM");

        StockMovement savedMovement = stockMovementRepository.save(movement);
        return StockMovementResponse.fromEntity(savedMovement);
    }

    @Override
    public StockMovementResponse recordAdjustment(StockMovementRequest request, boolean isPositive) {
        Inventory inventory = getInventoryById(request.getInventoryId());

        int previousQuantity = inventory.getQuantity();
        int newQuantity;

        if (isPositive) {
            newQuantity = previousQuantity + request.getQuantity();
        } else {
            if (previousQuantity < request.getQuantity()) {
                throw new BadRequestException(
                        String.format("Cannot adjust below zero. Current: %d, Adjustment: -%d",
                                previousQuantity, request.getQuantity()));
            }
            newQuantity = previousQuantity - request.getQuantity();
        }

        // Update inventory
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);

        // Create movement record
        StockMovement movement = new StockMovement();
        movement.setInventory(inventory);
        movement.setMovementType(MovementType.ADJUSTMENT);
        movement.setQuantity(isPositive ? request.getQuantity() : -request.getQuantity());
        movement.setPreviousQuantity(previousQuantity);
        movement.setNewQuantity(newQuantity);
        movement.setReferenceNumber(request.getReferenceNumber());
        movement.setNotes(request.getNotes() != null ? request.getNotes() :
                (isPositive ? "Positive adjustment" : "Negative adjustment"));
        movement.setCreatedBy("SYSTEM");

        StockMovement savedMovement = stockMovementRepository.save(movement);
        return StockMovementResponse.fromEntity(savedMovement);
    }

    @Override
    @Transactional(readOnly = true)
    public StockMovementResponse getMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement", "id", id));
        return StockMovementResponse.fromEntity(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getAllMovements() {
        return stockMovementRepository.findAll().stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByInventory(Long inventoryId) {
        return stockMovementRepository.findByInventoryId(inventoryId).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByType(MovementType type) {
        return stockMovementRepository.findByMovementType(type).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByReferenceNumber(String referenceNumber) {
        return stockMovementRepository.findByReferenceNumber(referenceNumber).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByFilters(Long inventoryId, MovementType type,
                                                              LocalDateTime startDate, LocalDateTime endDate) {
        return stockMovementRepository.findByFilters(inventoryId, type, startDate, endDate).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByWarehouse(Long warehouseId) {
        return stockMovementRepository.findByWarehouseId(warehouseId).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProductId(productId).stream()
                .map(StockMovementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MovementSummaryResponse getMovementSummary(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);

        Object[] summary = stockMovementRepository.getMovementSummaryByInventory(inventoryId);

        Long totalInbound = 0L;
        Long totalOutbound = 0L;

        if (summary != null && summary.length > 0 && summary[0] != null) {
            Object[] row = (Object[]) summary[0];
            totalInbound = row[0] != null ? ((Number) row[0]).longValue() : 0L;
            totalOutbound = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        }

        return new MovementSummaryResponse(
                inventory.getId(),
                inventory.getProduct().getSku(),
                inventory.getProduct().getName(),
                inventory.getLocation().getLocationCode(),
                inventory.getQuantity(),
                totalInbound,
                totalOutbound
        );
    }

    // Helper method
    private Inventory getInventoryById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", inventoryId));
    }
}