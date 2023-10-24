package com.pluralsight;

public class Tables {
    public static void ledgerHeader() {
        System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------------------------"));
        System.out.println(String.format("%-25s %-25s %-30s %-25s %-25s", "Date", "Time",
                "Description", "Vendor", "Amount"));
        System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------------------------"));
    }

    public static void fillLedgerTable(Transaction t) {
        System.out.println(String.format("%-25s %-25s %-30s %-25s %-25s", t.getDate(), t.getTime(),
        t.getDescription(), t.getVendor(), t.getAmount()));
    }

    public static void reportsHeader() {
        System.out.println(String.format("%s", "-------------------------------------------------------------"));
        System.out.println(String.format("%-25s %-25s %-25s", "Date", "Vendor", "Amount"));
        System.out.println(String.format("%s", "-------------------------------------------------------------"));
    }

    public static void fillReportsTable(Transaction t) {
        System.out.println(String.format("%-25s %-25s %-25s", t.getDate(), t.getVendor(), t.getAmount()));
    }
}
