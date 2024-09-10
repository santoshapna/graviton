package org.graviton.service.impl;

import org.graviton.service.api.PurchaseDataService;
import org.graviton.service.api.UsageDataService;
import org.graviton.service.api.model.CreditPackage;
import org.graviton.service.api.model.Service;
import org.graviton.service.api.model.output.Status;
import org.graviton.service.api.model.output.TransactionLine;
import org.graviton.service.api.model.output.TransactionSummary;
import org.graviton.service.api.model.purchase.PurchaseData;
import org.graviton.service.api.model.purchase.PurchaseDataLineItem;
import org.graviton.service.api.model.usage.UsageData;
import org.graviton.service.api.model.usage.UsageDataLineItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {
    @Mock
    OutputToJSONFileService outPutJsonServiceMock;
    @Mock
    PurchaseDataService purchaseDataServiceMock;
    @Mock
    UsageDataService usageDataServiceMock;

    @InjectMocks
    TransactionServiceImpl transactionService;

    String outputPath = "path";
    String user1 = "user1";

    private Map<String, CreditPackage> packageDetails = new HashMap<>();
    private Map<String, Service> serviceDetails = new HashMap<>();
//    PackageAndServiceInfoLoaderImpl packageAndServiceDataLoader;

    @Before
    public void setup() {
        populateCreditPackageAndServiceData();
        PurchaseData purchaseData = preparePurchaseData();
        UsageData usageData = prepareUsageData();
        Mockito.when(purchaseDataServiceMock.getPurchaseData()).thenReturn(purchaseData);
        Mockito.when(usageDataServiceMock.getUsageData()).thenReturn(usageData);
        doNothing().when(outPutJsonServiceMock).writeOutputToFile(anyList(), anyString());
    }

    private UsageData prepareUsageData() {
        UsageData usageData = new UsageData();
        List<UsageDataLineItem> lineItems = new ArrayList<>();
        usageData.getUsageDataForUser().put(user1, lineItems);

        lineItems.add(new UsageDataLineItem(1, user1, serviceDetails.get("S1")));
        lineItems.add(new UsageDataLineItem(2, user1, serviceDetails.get("S1")));
        return usageData;
    }

    private void populateCreditPackageAndServiceData() {
        /*
        * {
              "servicePricing": [
                {"name":  "S1", "charge":  1},
                {"name":  "S2", "charge":  2},
                {"name":  "S3", "charge":  3}
              ],

              "creditPackage" : [
                {"name" :  "Basic", "cost": 100, "currency" :  "$", "credit" :  100},
                {"name" :  "Standard", "cost": 225, "currency" :  "$", "credit" :  250},
                {"name" :  "Premium", "cost": 450, "currency" :  "$", "credit" :  500}
              ]
            }
        * */
        packageDetails.put("Basic", new CreditPackage("Basic", 100, "$", 100));
        packageDetails.put("Standard", new CreditPackage("Standard", 225, "$", 250));
        packageDetails.put("Premium", new CreditPackage("Premium", 450, "$", 500));

        serviceDetails.put("S1", new Service("S1", 1));
        serviceDetails.put("S2", new Service("S2", 2));
        serviceDetails.put("S3", new Service("S3", 3));

    }

    private PurchaseData preparePurchaseData() {
        PurchaseData purchaseData = new PurchaseData();
        List<PurchaseDataLineItem> lineItems = new ArrayList<>();
        purchaseData.getPurchaseDataForUser().put(user1, lineItems);
        lineItems.add(new PurchaseDataLineItem(2, user1, packageDetails.get("Basic"),1 ));
        lineItems.add(new PurchaseDataLineItem(4, user1, packageDetails.get("Basic"),1 ));
        return purchaseData;
    }

    @Test
    public void testGenerateTransactionSummary() {
        transactionService.generateTransactionSummary(outputPath);
        List<TransactionSummary> txnSummaryForAllUsers = transactionService.getTxnSummaryForAllUsers();
        assertNotNull(txnSummaryForAllUsers);
        assertTrue("Only one user will be there in the list", txnSummaryForAllUsers.size() == 1);
        TransactionSummary txnSummary = txnSummaryForAllUsers.get(0);
        assertEquals("User did not match for the transaction summary", user1, txnSummary.getUser());
        List<TransactionLine> transactions = txnSummary.getTransactions();
        assertFalse("Transaction summary should have line items", transactions.isEmpty());
        //first usage is before any credit so it will be denied
        assertEquals("Service usage should not be allowed without enough credit", Status.DENIED, transactions.get(0).getStatus());
        //second row should have a credit of 1 Basic at time 2
        assertEquals("", packageDetails.get("Basic").getCredit(), transactions.get(1).getBalance());
    }

}
