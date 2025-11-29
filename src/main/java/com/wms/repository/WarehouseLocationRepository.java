package com.wms.repository;

import com.wms.model.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {

    List<WarehouseLocation> findByWarehouseId(Long warehouseId);

    List<WarehouseLocation> findByWarehouseIdAndZone(Long warehouseId, String zone);

    Optional<WarehouseLocation> findByLocationCode(String locationCode);

    boolean existsByWarehouseIdAndZoneAndAisleAndRackAndShelf(
            Long warehouseId, String zone, String aisle, String rack, String shelf);
}