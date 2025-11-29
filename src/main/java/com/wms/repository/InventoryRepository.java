package com.wms.repository;

import com.wms.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByProductId(Long productId);

    List<Inventory> findByLocationId(Long locationId);

    @Query("SELECT i FROM Inventory i WHERE i.location.warehouse.id = :warehouseId")
    List<Inventory> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    Optional<Inventory> findByProductIdAndLocationId(Long productId, Long locationId);

    boolean existsByProductIdAndLocationId(Long productId, Long locationId);

    @Query("SELECT i FROM Inventory i WHERE i.quantity < i.minQuantity")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.location.warehouse.id = :warehouseId AND i.quantity < i.minQuantity")
    List<Inventory> findLowStockItemsByWarehouse(@Param("warehouseId") Long warehouseId);

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.product.id = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.location.warehouse.id = :warehouseId AND i.product.id = :productId")
    List<Inventory> findByWarehouseIdAndProductId(@Param("warehouseId") Long warehouseId,
                                                   @Param("productId") Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.product.category = :category")
    List<Inventory> findByProductCategory(@Param("category") String category);
}