package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");
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

        // The amount should be a positive number.
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
        }

        // Add to transactions
        transactions.add(new Transaction(new Deposit(date, time, vendor, amount)));

        //update transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(date + "|" + time + "|Deposit|" + vendor + "|" + amount + "\n");
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


        // The amount should be a positive number
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
        }

        amount *= -1;

        // Add payment to the `transactions` ArrayList
        transactions.add(new Transaction(new Payment(date, time, vendor, amount)));

        // Update transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME,true));
            writer.write(date + "|" + time + "|Payment|" + vendor + "|" + amount + "\n");
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
                case "A" -> {
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "All Transactions" + ConsoleColors.RESET);
                    displayLedger();
                }
                case "D" -> {
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Deposits" + ConsoleColors.RESET);
                    displayDeposits();
                }
                case "P" -> {
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Payments" + ConsoleColors.RESET);
                    displayPayments();
                }
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
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
            if (t.getAmount() > 0) {
                Tables.fillLedgerTable(t);
            }
        }
    }

    private static void displayPayments() {
        // Table of payments
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            if (t.getAmount() < 0) {
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
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();
            LocalDate date = LocalDate.now();
            LocalDate previousMonth = date.minusMonths(1);
            LocalDate previousYear = date.minusYears(1);
            boolean found = false;

            switch (input) {// Current month
                case "1":
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Current Month" + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if (t.getDate().getYear() == date.getYear()) {
                            if (t.getDate().getMonthValue() == date.getMonthValue()) {
                                Tables.fillReportsTable(t);
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                // Previous month
                case "2":
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Previous Month" + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if (t.getDate().getYear() == date.getYear()) {
                            if (t.getDate().getMonthValue() == previousMonth.getMonthValue()) {
                                Tables.fillReportsTable(t);
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                // Current year
                case "3":
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Current Year" + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if (t.getDate().getYear() == date.getYear()) {
                            Tables.fillReportsTable(t);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                // Previous year
                case "4":
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Previous Year" + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if (t.getDate().getYear() == previousYear.getYear()) {
                            Tables.fillReportsTable(t);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                // By Vendor
                case "5":
                    System.out.print("Vendor Name: ");
                    String vendor = scanner.nextLine();
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + vendor + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if (t.getVendor().equalsIgnoreCase(vendor)) {
                            Tables.fillReportsTable(t);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                // Custom search
                case "6":
                    System.out.println("\nCustom Search (insert field(s) to filter)");
                    System.out.print("Start Date (yyyy-MM-dd) or 'null': ");
                    String userStartDateInput = scanner.nextLine();
                    LocalDate startDate = null;
                    if (!userStartDateInput.equalsIgnoreCase("null")) {
                        startDate = LocalDate.parse(userStartDateInput, DATE_FORMATTER);
                    }
                    System.out.print("End Date (yyyy-MM-dd) or 'null': ");
                    String userEndDateInput = scanner.nextLine();
                    LocalDate endDate = null;
                    if (!userEndDateInput.equalsIgnoreCase("null")) {
                        endDate = LocalDate.parse(userEndDateInput, DATE_FORMATTER);
                    }
                    System.out.print("Description or press enter: ");
                    String description = scanner.nextLine();
                    System.out.print("Vendor or press enter: ");
                    vendor = scanner.nextLine();
                    System.out.print("Amount or 'null': ");
                    String userAmountInput = scanner.nextLine();
                    double amount = 0;
                    if (!userAmountInput.equalsIgnoreCase("null")) {
                        amount = Double.parseDouble(userAmountInput);
                    }
                    System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Custom Search" + ConsoleColors.RESET);
                    Tables.reportsHeader();
                    for (Transaction t : transactions) {
                        if ((startDate == null || t.getDate().isAfter(startDate.minusDays(1))) &&
                                (endDate == null || t.getDate().isBefore(endDate.minusDays(1))) &&
                                (description == null || t.getDescription().contains(description)) &&
                                (vendor == null || t.getVendor().contains(vendor)) &&
                                (amount == 0 || t.getAmount() == amount)) {
                            Tables.fillReportsTable(t);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
                    }
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}
