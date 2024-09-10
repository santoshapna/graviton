package org.graviton.service.impl;

import org.graviton.service.api.PurchaseDataService;
import org.graviton.service.api.TransactionService;
import org.graviton.service.api.UsageDataService;
import org.graviton.service.api.model.OutputToFileService;
import org.graviton.service.api.model.output.*;
import org.graviton.service.api.model.purchase.PurchaseData;
import org.graviton.service.api.model.purchase.PurchaseDataLineItem;
import org.graviton.service.api.model.usage.UsageData;
import org.graviton.service.api.model.usage.UsageDataLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransactionServiceImpl implements TransactionService {
    private PurchaseDataService purchaseDataService;
    private UsageDataService usageDataService;
    private OutputToFileService outputToFileService;

    private List<TransactionSummary> txnSummaryForAllUsers;

    public TransactionServiceImpl(PurchaseDataService purchaseDataService, UsageDataService usageDataService,
                                  OutputToFileService outputToFileService) {
        this.purchaseDataService = purchaseDataService;
        this.usageDataService = usageDataService;
        this.outputToFileService = outputToFileService;
        txnSummaryForAllUsers = new ArrayList<>();
    }

    @Override
    public void generateTransactionSummary(String outputFilePath) {
        PurchaseData purchaseData = purchaseDataService.getPurchaseData();
        UsageData usageData = usageDataService.getUsageData();

        Set<String> users = purchaseData.getPurchaseDataForUser().keySet();
        for (String user : users) {
            List<PurchaseDataLineItem> purchaseDataForUser = purchaseData.getPurchaseDataForUser().get(user);
            List<UsageDataLineItem> usageDataForUser = usageData.getUsageDataForUser().get(user);
            txnSummaryForAllUsers.add(prepareTxnSummaryForUser(user, purchaseDataForUser, usageDataForUser));
        }
        outputToFileService.writeOutputToFile(txnSummaryForAllUsers, outputFilePath);
    }

    private TransactionSummary prepareTxnSummaryForUser(String user, List<PurchaseDataLineItem> purchaseDataForUser,
                                                        List<UsageDataLineItem> usageDataForUser) {
        /*The data is sorted based on time for purchase and the usage;
        * We will start iterating in both of the list and merge them based on the time and adjust the credit balance*/
        int balance = 0;
        int usageInd = 0;
        int purchaseInd = 0;
        List<TransactionLine> txnLines = new ArrayList<>();
        TransactionSummary txnSummary = new TransactionSummary(user, txnLines);

        while(usageInd < usageDataForUser.size() || purchaseInd < purchaseDataForUser.size()) {
            PurchaseDataLineItem purchaseDataLineItem = null;
            UsageDataLineItem usageDataLineItem = null;
            if (purchaseInd < purchaseDataForUser.size()) {
                purchaseDataLineItem = purchaseDataForUser.get(purchaseInd);
            }
            if (usageInd < usageDataForUser.size()) {
                usageDataLineItem = usageDataForUser.get(usageInd);
            }
            if(purchaseDataLineItem != null && usageDataLineItem != null) {
                if(purchaseDataLineItem.getTimeStamp() <= usageDataLineItem.getTimeStamp()) {
                    purchaseInd++;
                    balance += purchaseDataLineItem.getCreditPackage().getCredit()*purchaseDataLineItem.getQuantity();
                    txnLines.add(populateTransactionLineForPurchase(purchaseDataLineItem, balance));
                } else {
                    usageInd++;
                    int serviceCost = usageDataLineItem.getService().getCost();
                    txnLines.add(populateTransactionLineForUsage(usageDataLineItem, balance));
                    /* Update balance only if service usage is allowed */
                    balance = balance >= serviceCost ? balance - serviceCost : balance;
                }
            } else if (purchaseDataLineItem != null) {
                purchaseInd++;
                balance += purchaseDataLineItem.getCreditPackage().getCredit()*purchaseDataLineItem.getQuantity();
                txnLines.add(populateTransactionLineForPurchase(purchaseDataLineItem, balance));
            } else if (usageDataLineItem != null) {
                usageInd++;
                int serviceCost = usageDataLineItem.getService().getCost();
                txnLines.add(populateTransactionLineForUsage(usageDataLineItem, balance));
                /* Update balance only if service usage is allowed */
                balance = balance >= serviceCost ? balance - serviceCost : balance;
            }
        }
        return txnSummary;
    }

    private TransactionLine populateTransactionLineForUsage(UsageDataLineItem usageDataLineItem, int balance) {
        TransactionLine txnLine = new TransactionLine();
        txnLine.setTime(usageDataLineItem.getTimeStamp());
        txnLine.setType(Type.DEBIT);
        txnLine.setService(usageDataLineItem.getService().getName());
        if(balance >= usageDataLineItem.getService().getCost()) {
            txnLine.setBalance(balance - usageDataLineItem.getService().getCost());
            txnLine.setStatus(Status.SUCCESS);
            //We have assumed that one log line we have only one service request
            txnLine.setQuantity(1);
        } else {
            txnLine.setStatus(Status.DENIED);
        }
        return txnLine;
    }

    private TransactionLine populateTransactionLineForPurchase(PurchaseDataLineItem purchaseDataLineItem, int balance) {
        TransactionLine txnLine = new TransactionLine();
        txnLine.setTime(purchaseDataLineItem.getTimeStamp());
        txnLine.setType(Type.CREDIT);
        txnLine.setCreditPackage(purchaseDataLineItem.getCreditPackage().getName());
        txnLine.setStatus(Status.SUCCESS);
        txnLine.setQuantity(purchaseDataLineItem.getQuantity());
        txnLine.setBalance(balance);
        return txnLine;
    }

    public List<TransactionSummary> getTxnSummaryForAllUsers() {
        return txnSummaryForAllUsers;
    }
}
