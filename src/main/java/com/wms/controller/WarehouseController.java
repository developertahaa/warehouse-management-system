package com.wms.controller;

import com.wms.dto.common.ApiResponse;
import com.wms.dto.request.LocationRequest;
import com.wms.dto.request.WarehouseRequest;
import com.wms.dto.response.LocationResponse;
import com.wms.dto.response.WarehouseResponse;
import com.wms.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Create a new warehouse with optional locations
     * POST /api/warehouses
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(
            @Valid @RequestBody WarehouseRequest request) {

        WarehouseResponse response = warehouseService.createWarehouse(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Warehouse created successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Get all warehouses
     * GET /api/warehouses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAllWarehouses() {
        List<WarehouseResponse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses retrieved successfully"));
    }

    /**
     * Get warehouse by ID (includes locations)
     * GET /api/warehouses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse retrieved successfully"));
    }

    /**
     * Get warehouse by code
     * GET /api/warehouses/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseByCode(@PathVariable String code) {
        WarehouseResponse response = warehouseService.getWarehouseByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse retrieved successfully"));
    }

    /**
     * Update warehouse
     * PUT /api/warehouses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {

        WarehouseResponse response = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse updated successfully"));
    }

    /**
     * Delete warehouse
     * DELETE /api/warehouses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.deleted("Warehouse deleted successfully"));
    }

    /**
     * Get all locations for a warehouse
     * GET /api/warehouses/{id}/locations
     */
    @GetMapping("/{id}/locations")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getWarehouseLocations(
            @PathVariable Long id,
            @RequestParam(required = false) String zone) {

        List<LocationResponse> locations;
        if (zone != null && !zone.trim().isEmpty()) {
            locations = warehouseService.getWarehouseLocationsByZone(id, zone);
        } else {
            locations = warehouseService.getWarehouseLocations(id);
        }

        return ResponseEntity.ok(ApiResponse.success(locations, "Locations retrieved successfully"));
    }

    /**
     * Add a location to warehouse
     * POST /api/warehouses/{id}/locations
     */
    @PostMapping("/{id}/locations")
    public ResponseEntity<ApiResponse<LocationResponse>> addLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request) {

        LocationResponse response = warehouseService.addLocationToWarehouse(id, request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Location added successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Delete a location
     * DELETE /api/warehouses/locations/{locationId}
     */
    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable Long locationId) {
        warehouseService.deleteLocation(locationId);
        return ResponseEntity.ok(ApiResponse.deleted("Location deleted successfully"));
    }
}
