package org.graviton.service.api;

import org.graviton.service.api.model.purchase.PurchaseData;

public interface PurchaseDataService {
    void populatePurchaseDataFromLogs(String filePath);
    PurchaseData getPurchaseData();
}
