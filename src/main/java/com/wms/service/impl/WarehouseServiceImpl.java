package com.wms.service.impl;

import com.wms.dto.request.LocationRequest;
import com.wms.dto.request.WarehouseRequest;
import com.wms.dto.response.LocationResponse;
import com.wms.dto.response.WarehouseResponse;
import com.wms.exception.DuplicateResourceException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.model.Warehouse;
import com.wms.model.WarehouseLocation;
import com.wms.repository.WarehouseLocationRepository;
import com.wms.repository.WarehouseRepository;
import com.wms.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationRepository locationRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                 WarehouseLocationRepository locationRepository) {
        this.warehouseRepository = warehouseRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        // Check if code already exists
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Warehouse", "code", request.getCode());
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setAddress(request.getAddress());
        warehouse.setCapacity(request.getCapacity());

        // Add locations if provided
        if (request.getLocations() != null && !request.getLocations().isEmpty()) {
            for (LocationRequest locRequest : request.getLocations()) {
                WarehouseLocation location = new WarehouseLocation();
                location.setZone(locRequest.getZone());
                location.setAisle(locRequest.getAisle());
                location.setRack(locRequest.getRack());
                location.setShelf(locRequest.getShelf());
                location.setCapacity(locRequest.getCapacity());
                warehouse.addLocation(location);
            }
        }

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return WarehouseResponse.fromEntity(savedWarehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id));
        return WarehouseResponse.fromEntity(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseByCode(String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "code", code));
        return WarehouseResponse.fromEntity(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(WarehouseResponse::fromEntityWithoutLocations)
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id));

        // Check if new code conflicts with another warehouse
        if (!warehouse.getCode().equals(request.getCode()) &&
                warehouseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Warehouse", "code", request.getCode());
        }

        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setAddress(request.getAddress());
        warehouse.setCapacity(request.getCapacity());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return WarehouseResponse.fromEntity(updatedWarehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse", "id", id);
        }
        warehouseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getWarehouseLocations(Long warehouseId) {
        // Verify warehouse exists
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse", "id", warehouseId);
        }

        return locationRepository.findByWarehouseId(warehouseId).stream()
                .map(LocationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getWarehouseLocationsByZone(Long warehouseId, String zone) {
        // Verify warehouse exists
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse", "id", warehouseId);
        }

        return locationRepository.findByWarehouseIdAndZone(warehouseId, zone).stream()
                .map(LocationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LocationResponse addLocationToWarehouse(Long warehouseId, LocationRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));

        // Check for duplicate location
        if (locationRepository.existsByWarehouseIdAndZoneAndAisleAndRackAndShelf(
                warehouseId, request.getZone(), request.getAisle(), request.getRack(), request.getShelf())) {
            throw new DuplicateResourceException("Location",
                    "coordinates",
                    request.getZone() + "-" + request.getAisle() + "-" + request.getRack() + "-" + request.getShelf());
        }

        WarehouseLocation location = new WarehouseLocation();
        location.setZone(request.getZone());
        location.setAisle(request.getAisle());
        location.setRack(request.getRack());
        location.setShelf(request.getShelf());
        location.setCapacity(request.getCapacity());
        location.setWarehouse(warehouse);

        WarehouseLocation savedLocation = locationRepository.save(location);
        return LocationResponse.fromEntity(savedLocation);
    }

    @Override
    public void deleteLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location", "id", locationId);
        }
        locationRepository.deleteById(locationId);
    }
}