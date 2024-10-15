package com.example.newfinal;

public class Purchase {
    private String purchaseId;
    private String userId;
    private String productName;
    private int productPrice;
    private long purchaseDate;

    public Purchase() {
        // Default constructor required for calls to DataSnapshot.getValue(Purchase.class)
    }

    public Purchase(String purchaseId, String userId, String productName, int productPrice, long purchaseDate) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.purchaseDate = purchaseDate;
    }

    // Getters and setters
    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
