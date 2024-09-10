package org.graviton;

import org.graviton.data.access.CsvReader;
import org.graviton.data.access.JsonReader;
import org.graviton.service.api.PackageAndServiceInfoLoader;
import org.graviton.service.api.PurchaseDataService;
import org.graviton.service.api.TransactionService;
import org.graviton.service.api.UsageDataService;
import org.graviton.service.impl.*;

import java.security.InvalidParameterException;

public class Main {
    private final PackageAndServiceInfoLoader packageAndServiceInfoService;
    private final PurchaseDataService purchaseDataService;
    private final UsageDataService usageDataService;
    private final TransactionService transactionService;


    public Main() {
        /* We can use spring to manage these bean lifecycle*/
        JsonReader jsonReader = new JsonReader();
        CsvReader csvReader = new CsvReader();
        this.packageAndServiceInfoService = new PackageAndServiceInfoLoaderImpl(jsonReader);
        this.purchaseDataService = new PurchaseDataServiceImpl(packageAndServiceInfoService, csvReader);
        this.usageDataService = new UsageDataServiceImpl(packageAndServiceInfoService, csvReader);
        this.transactionService = new TransactionServiceImpl(purchaseDataService, usageDataService, new OutputToJSONFileService());
    }

    public static void main(String[] args) {
        validate(args);
        Main main = new Main();
        /*Assumptions:
        1. Assuming that all the files are not too big and can be loaded in to memory, else we can load and process in chunks
        2. log lines for purchase and usage are in sorted/increasing order of time, else we will have to sort it;
        see sample files in resources
        3. As its static file inputs, assuming it will be executed in single threaded environment;
        */
        String pricingPackageInfoFile = args[0];
        String purchaseInfoFile = args[1];
        String usageInfoFile = args[2];
        String outputFile = "TransactionSummary.json";
        if(args.length == 4) {
            outputFile = args[3];
        }

        main.packageAndServiceInfoService.loadPackageAndServiceInfoFromFile(pricingPackageInfoFile);
        main.purchaseDataService.populatePurchaseDataFromLogs(purchaseInfoFile);
        main.usageDataService.populateUsageDataFromLogs(usageInfoFile);
        main.transactionService.generateTransactionSummary(outputFile);
    }

    private static void validate(String[] args) {
        if(!(args.length == 3 || args.length == 4)) {
            throw new InvalidParameterException("Please provide inputs for " +
                    "pricingInfo, purchaseInfo, usageInfo file locations");
        }
    }
}