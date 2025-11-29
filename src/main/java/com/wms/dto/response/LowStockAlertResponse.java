package com.wms.dto.response;

import com.wms.model.Inventory;

public class LowStockAlertResponse {

    private Long inventoryId;
    private String productSku;
    private String productName;
    private String productCategory;
    private Integer currentQuantity;
    private Integer minQuantity;
    private Integer deficit;
    private String locationCode;
    private String warehouseName;
    private String warehouseCode;

    // Constructors
    public LowStockAlertResponse() {}

    // Static factory method
    public static LowStockAlertResponse fromEntity(Inventory inventory) {
        LowStockAlertResponse response = new LowStockAlertResponse();
        response.setInventoryId(inventory.getId());
        response.setProductSku(inventory.getProduct().getSku());
        response.setProductName(inventory.getProduct().getName());
        response.setProductCategory(inventory.getProduct().getCategory());
        response.setCurrentQuantity(inventory.getQuantity());
        response.setMinQuantity(inventory.getMinQuantity());
        response.setDeficit(inventory.getMinQuantity() - inventory.getQuantity());
        response.setLocationCode(inventory.getLocation().getLocationCode());
        response.setWarehouseName(inventory.getLocation().getWarehouse().getName());
        response.setWarehouseCode(inventory.getLocation().getWarehouse().getCode());
        return response;
    }

    // Getters and Setters
    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }

    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }

    public Integer getDeficit() { return deficit; }
    public void setDeficit(Integer deficit) { this.deficit = deficit; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
}