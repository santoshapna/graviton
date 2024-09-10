package org.graviton.service.api.model.output;

public class OutPutJsonFields {
    private static final String USER = "User";
    private static final String TXNS = "Transactions";
    private static final String TIME = "Time";
    private static final String TYPE = "Type";
    private static final String SERVICE = "Service";
    private static final String CREDIT_PACKAGE = "Package";
    private static final String STATUS = "Status";
    private static final String QUANTITY = "Quantity";
    private static final String BALANCE = "Balance";

    public static String getOutputFiledForUser() {
        return USER;
    }
    public static String getOutputFiledForTxns() {
        return TXNS;
    }
    public static String getOutputFiledForTime() {
        return TIME;
    }
    public static String getOutputFiledForType() {
        return TYPE;
    }
    public static String getOutputFiledForService() {
        return SERVICE;
    }
    public static String getOutputFiledForPackage() {
        return CREDIT_PACKAGE;
    }
    public static String getOutputFiledForStatus() {
        return STATUS;
    }
    public static String getOutputFiledForQuantity() {
        return QUANTITY;
    }
    public static String getOutputFiledForBalance() {
        return BALANCE;
    }

}
