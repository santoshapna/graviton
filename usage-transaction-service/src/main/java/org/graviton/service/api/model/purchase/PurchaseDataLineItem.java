package org.graviton.service.api.model.purchase;

import org.graviton.service.api.model.CreditPackage;

public class PurchaseDataLineItem {
    private long timeStamp;
    private String userId;
    private CreditPackage creditPackage;
    private int quantity;

    public PurchaseDataLineItem(long timeStamp, String userId, CreditPackage creditPackage, int quantity) {
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.creditPackage = creditPackage;
        this.quantity = quantity;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CreditPackage getCreditPackage() {
        return creditPackage;
    }

    public void setCreditPackage(CreditPackage creditPackage) {
        this.creditPackage = creditPackage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
