package com.wms.dto.response;

public class MovementSummaryResponse {

    private Long inventoryId;
    private String productSku;
    private String productName;
    private String locationCode;
    private Integer currentQuantity;
    private Long totalInbound;
    private Long totalOutbound;
    private Long netMovement;

    // Constructors
    public MovementSummaryResponse() {}

    public MovementSummaryResponse(Long inventoryId, String productSku, String productName,
                                    String locationCode, Integer currentQuantity,
                                    Long totalInbound, Long totalOutbound) {
        this.inventoryId = inventoryId;
        this.productSku = productSku;
        this.productName = productName;
        this.locationCode = locationCode;
        this.currentQuantity = currentQuantity;
        this.totalInbound = totalInbound != null ? totalInbound : 0L;
        this.totalOutbound = totalOutbound != null ? totalOutbound : 0L;
        this.netMovement = this.totalInbound - this.totalOutbound;
    }

    // Getters and Setters
    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }

    public Long getTotalInbound() { return totalInbound; }
    public void setTotalInbound(Long totalInbound) { this.totalInbound = totalInbound; }

    public Long getTotalOutbound() { return totalOutbound; }
    public void setTotalOutbound(Long totalOutbound) { this.totalOutbound = totalOutbound; }

    public Long getNetMovement() { return netMovement; }
    public void setNetMovement(Long netMovement) { this.netMovement = netMovement; }
}