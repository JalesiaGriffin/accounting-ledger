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

    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        transactions.sort(Transaction.TransactionDate);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n" + ConsoleColors.WHITE_BOLD_BRIGHT + ConsoleColors.PURPLE_BACKGROUND
                    + "Welcome to TransactionApp" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.WHITE_BOLD + "D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    public static void loadTransactions(String FILE_NAME) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // Prompt the user for deposit info
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Add a Deposit" + ConsoleColors.RESET);
        System.out.print("Deposit Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Deposit Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextDouble();

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
            writer.write(date + "|" + time + "|Deposit|" + vendor + "|" + amount);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addPayment(Scanner scanner) {
        // Prompt user for payment info
        System.out.println(ConsoleColors.GREEN_BOLD + "\nAdd a Payment" + ConsoleColors.RESET);
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
            writer.write(date + "|" + time + "|Payment|" + vendor + "|" + amount);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(ConsoleColors.BLUE_BOLD + "\nLedger" + ConsoleColors.RESET);
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
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            Tables.fillLedgerTable(t);
        }
    }

    private static void displayDeposits() {
        // Table of deposits
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            if (t.getDescription().toLowerCase().contains("deposit")) {
                Tables.fillLedgerTable(t);
            }
        }
    }

    private static void displayPayments() {
        // Table of payments
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            if (t.getDescription().toLowerCase().contains("payment")) {
                Tables.fillLedgerTable(t);
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(ConsoleColors.YELLOW_BOLD + "\nReports" + ConsoleColors.RESET);
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Filter Transaction by Date");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();
            LocalDate date = LocalDate.now();
            LocalDate previousMonth = date.minusMonths(1);
            LocalDate previousYear = date.minusYears(1);

            switch (input) {
                case "1":
                    // Current month
                    Tables.reportsHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().getYear() == date.getYear()) {
                            if (t.getDate().getMonthValue() == date.getMonthValue()) {
                                Tables.fillReportsTable(t);
                            }
                        }
                    }
                    break;
                case "2":
                    // Previous month
                    Tables.reportsHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().getYear() == date.getYear()) {
                            if (t.getDate().getMonthValue() == previousMonth.getMonthValue()) {
                                Tables.fillReportsTable(t);
                            }
                        }
                    }
                    break;
                case "3":
                    // Current year
                    Tables.reportsHeader();
                    for (Transaction t: transactions) {
                    if (t.getDate().getYear() == date.getYear()) {
                        Tables.fillReportsTable(t);
                    }
                }
                    break;
                case "4":
                    // Previous year
                    Tables.reportsHeader();
                    for (Transaction t: transactions) {
                        if (t.getDate().getYear() == previousYear.getYear()) {
                            Tables.fillReportsTable(t);
                        }
                    }
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    System.out.println("Vendor Name: ");
                    String vendor = scanner.nextLine();

                    Tables.reportsHeader();
                    for (Transaction t: transactions) {
                        if (t.getVendor().equalsIgnoreCase(vendor)) {
                            Tables.fillReportsTable(t);
                        }
                    }
                    break;
                case "6":
                    System.out.println("\nCustom Search (insert field(s) to filter)");
                    System.out.print("Start Date: ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

                    System.out.print("Start Date: ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

                    System.out.println("Description: ");
                    String description = scanner.nextLine();

                    System.out.println("Vendor: ");
                    vendor = scanner.nextLine();

                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        boolean found = false;

        Tables.reportsHeader();
        for (Transaction t: transactions) {
            if (t.getDate().isBefore(endDate) && t.getDate().isAfter(startDate)) {
                    Tables.fillReportsTable(t);
                    found = true;
                }
            }

        if (found == false) {
            System.out.println("no results.");
        }
    }

/*    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        for (Transaction t: transactions) {
            if (t.getVendor().contains(vendor)) {
                Tables
            }
        }
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }*/

}
