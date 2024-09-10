package org.graviton.service.api.model;

public class CreditPackage {
    private String name;
    private double cost;
    private String currency;
    private int credit;

    public CreditPackage() {
    }

    public CreditPackage(String name, double cost, String currency, int credit) {
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
