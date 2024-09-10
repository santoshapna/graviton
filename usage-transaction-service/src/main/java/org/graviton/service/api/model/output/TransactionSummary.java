package org.graviton.service.api.model.output;

import java.util.List;

public class TransactionSummary {
    private String user;
    private List<TransactionLine> transactions;

    public TransactionSummary(String user, List<TransactionLine> transactions) {
        this.user = user;
        this.transactions = transactions;
    }

    public String getUser() {
        return user;
    }

    public List<TransactionLine> getTransactions() {
        return transactions;
    }
}
