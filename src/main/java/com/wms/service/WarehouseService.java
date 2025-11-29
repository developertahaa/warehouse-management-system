package com.wms.service;

import com.wms.dto.request.LocationRequest;
import com.wms.dto.request.WarehouseRequest;
import com.wms.dto.response.LocationResponse;
import com.wms.dto.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse getWarehouseById(Long id);

    WarehouseResponse getWarehouseByCode(String code);

    List<WarehouseResponse> getAllWarehouses();

    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);

    void deleteWarehouse(Long id);

    // Location operations
    List<LocationResponse> getWarehouseLocations(Long warehouseId);

    List<LocationResponse> getWarehouseLocationsByZone(Long warehouseId, String zone);

    LocationResponse addLocationToWarehouse(Long warehouseId, LocationRequest request);

    void deleteLocation(Long locationId);
}