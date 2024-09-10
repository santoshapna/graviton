package org.graviton.service.impl;

import org.graviton.data.access.CsvReader;
import org.graviton.service.api.PackageAndServiceInfoLoader;
import org.graviton.service.api.PurchaseDataService;
import org.graviton.service.api.model.CreditPackage;
import org.graviton.service.api.model.purchase.PurchaseData;
import org.graviton.service.api.model.purchase.PurchaseDataLineItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PurchaseDataServiceImpl implements PurchaseDataService {
    public static final String CSV_DELIMITER = ",";
    public static final int PURCHASE_LOG_COLUMNS = 4;
    private static final int TIME_INDEX = 0;
    private static final int USERID_INDEX = 1;
    private static final int PACKAGE_INDEX = 2;
    private static final int PACKAGE_QUANTITY_INDEX = 3;

    private PackageAndServiceInfoLoader packageService;
    private PurchaseData purchaseData;
    private CsvReader csvReader;

    public PurchaseDataServiceImpl(PackageAndServiceInfoLoader packageService, CsvReader csvReader) {
        this.packageService = packageService;
        this.csvReader = csvReader;
        purchaseData = new PurchaseData();
    }

    @Override
    public void populatePurchaseDataFromLogs(String filePath) {
        try(Stream<String> linesWithoutComment = csvReader.readCSV(filePath);) {
            linesWithoutComment.forEach(this::lineToData);
        } catch (IOException e) {
            //log
            throw new RuntimeException(e);
        }

    }

    @Override
    public PurchaseData getPurchaseData() {
        return purchaseData;
    }

    private void lineToData(String line) {
        String[] values = line.split(CSV_DELIMITER);
        if(values.length == PURCHASE_LOG_COLUMNS) {
            long time = Long.parseLong(values[TIME_INDEX]);
            CreditPackage creditPackage = packageService.getPackageByName(values[PACKAGE_INDEX]);
            int quantity = Integer.parseInt(values[PACKAGE_QUANTITY_INDEX]);
            if(creditPackage != null) {
                PurchaseDataLineItem dataLineItem = new PurchaseDataLineItem(time, values[USERID_INDEX],
                                                                              creditPackage, quantity);
                List<PurchaseDataLineItem> purchaseDataLineItems = purchaseData.getPurchaseDataForUser().
                        computeIfAbsent(dataLineItem.getUserId(), k -> new ArrayList<>());
                purchaseDataLineItems.add(dataLineItem);
            } else {
                // log invalid service
            }
        }
    }
}
