package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to TransactionApp");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    public static void loadTransactions(String fileName) {
        try {
            // open reader
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String input;

            // read file
            while ((input = reader.readLine()) != null) {
                String[] tokens = input.split("\\|");
                LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                String description = tokens[2];
                String vendor = tokens[3];
                double price = Double.parseDouble(tokens[4]);

                // create/add products
                transactions.add(new Transaction(date, time, description, vendor, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // Prompt the user for deposit info
        System.out.println("\nAdd a Deposit");
        System.out.print("Deposit Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Deposit Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        // The amount should be a positive number.
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
            scanner.nextLine();
        }

        // Add to transactions
        transactions.add(new Transaction(new Deposit(date, time, vendor, amount)));

        //update transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write("\n" + date + "|" + time + "|Deposit|" + vendor + "|" + amount);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addPayment(Scanner scanner) {
        // Prompt user for payment info
        System.out.println("\nAdd a Payment");
        System.out.print("Payment Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Payment Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        // The amount should be a positive number
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
            scanner.nextLine();
        }

        amount *= -1;

        // Add payment to the `transactions` ArrayList
        transactions.add(new Transaction(new Payment(date, time, vendor, amount)));

        // Update transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME,true));
            writer.write("\n" + date + "|" + time + "|Payment|" + vendor + "|" + amount);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\nLedger");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // Table of all transaction
        printTableHeader();
        for (Transaction t: transactions) {
            printTable(t);
        }
    }

    private static void displayDeposits() {
        // Table of deposits
        printTableHeader();
        for (Transaction t: transactions) {
            if (t.getDescription().contains("deposit")) {
                printTable(t);
            }
        }
    }

    private static void displayPayments() {
        // Table of payments
        printTableHeader();
        for (Transaction t: transactions) {
            if (t.getDescription().contains("payment")) {
                printTable(t);
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\nReports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();
            LocalDate date = LocalDate.now();

            switch (input) {
                case "1":
                    // Current month
                    printTableHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().getMonth() == date.getMonth()) {
                            printTable(t);
                        }
                    }
                    break;
                case "2":
                    // Previous month
                    printTableHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().minusMonths(1).getMonth() == date.minusMonths(1).getMonth()) {
                            printTable(t);
                        }
                    }
                    break;
                case "3":
                    // Current year
                    printTableHeader();
                    for (Transaction t: transactions) {
                    if (t.getDate().getYear() == date.getYear()) {
                        printTable(t);
                    }
                }
                    break;
                case "4":
                    // Previous year
                    printTableHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().minusYears(1).getYear() == date.minusYears(1).getYear()) {
                            printTable(t);
                        }
                    }
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    System.out.println("Vendor Name: ");
                    String vendor = scanner.nextLine();
                    printTableHeader();
                    for (Transaction t: transactions) {
                        if (t.getVendor().equalsIgnoreCase(vendor)) {
                            printTable(t);
                        }
                    }
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

/*    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }*/
    public static void printTableHeader() {
        System.out.println(String.format("%s", "-------------------------------------------------------------------------------"));
        System.out.println(String.format("%-25s %-25s %-30s %-25s %-25s", "Date", "Time",
                "Description", "Vendor", "Amount"));
        System.out.println(String.format("%s", "-------------------------------------------------------------------------------"));
    }
    public static void printTable(Transaction t) {
            System.out.println(String.format("%-25s %-25s %-30s %-25s %-25s", t.getDate(), t.getTime(),
                    t.getDescription(), t.getVendor(), t.getAmount()));
    }
}
