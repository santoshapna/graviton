package org.graviton.service.api.model.output;

public class TransactionLine {
    private long time;
    private Type type;
    private String service;
    private String creditPackage;
    private Status status;
    private int quantity;
    /*assuming that balance can fit in int*/
    private int balance;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCreditPackage() {
        return creditPackage;
    }

    public void setCreditPackage(String creditPackage) {
        this.creditPackage = creditPackage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
