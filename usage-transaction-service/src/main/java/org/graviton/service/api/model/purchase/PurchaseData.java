package org.graviton.service.api.model.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseData {
    private final Map<String, List<PurchaseDataLineItem>> purchaseDataForUser;

    public PurchaseData() {
        this.purchaseDataForUser = new HashMap<>();
    }

    public Map<String, List<PurchaseDataLineItem>> getPurchaseDataForUser() {
        return purchaseDataForUser;
    }
}
