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
            // INITIALIZE READER
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String input;

            // READ INPUT
            while ((input = reader.readLine()) != null) {
                String[] tokens = input.split("\\|");
                LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                String description = tokens[2];
                String vendor = tokens[3];
                double price = Double.parseDouble(tokens[4]);

                // CREATE & ADD PRODUCTS
                transactions.add(new Transaction(date, time, description, vendor, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // PROMPT USER FOR DEPOSIT PROPERTIES
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Add a Deposit" + ConsoleColors.RESET);
        System.out.print("Deposit Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Deposit Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        // VALIDATION (positive number)
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
        }

        // ADD DEPOSIT TO `transactions` ArrayList
        transactions.add(new Transaction(date, time, description, vendor, amount));

        //  WRITE DEPOSIT TO transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addPayment(Scanner scanner) {
        // PROMPTS USER FOR PAYMENT
        System.out.println(ConsoleColors.GREEN_BOLD + "\nAdd a Payment" + ConsoleColors.RESET);
        System.out.print("Payment Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Payment Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();


        // VALIDATION (positive number)
        while (amount < 0) {
            System.out.println("Invalid. Enter a positive value.");
            System.out.print("Amount: ");
            amount = scanner.nextDouble();
        }

        amount *= -1;

        // ADD PAYMENT TO `transactions` ArrayList
        transactions.add(new Transaction(date, time, description, vendor, amount));

        // WRITE PAYMENT TO transactions.csv
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME,true));
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
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
        // TABLE OF ALL TRANSACTIONS
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            Tables.fillLedgerTable(t);
        }
    }

    private static void displayDeposits() {
        // TABLE OF DEPOSITS
        Tables.ledgerHeader();
        for (Transaction t: transactions) {
            if (t.getAmount() > 0) {
                Tables.fillLedgerTable(t);
            }
        }
    }

    private static void displayPayments() {
        // TABLE OF PAYMENTS
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

            switch (input) {
                case "1" -> monthToDate(date);
                case "2" -> previousMonth(date);
                case "3" -> currentYear(date);
                case "4" -> previousYear(date);
                case "5" -> byVendor(scanner);
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    public static void monthToDate(LocalDate date) {
        boolean found = false;

        // PRINTS REPORT
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

        // PRINTS ONLY IF NO FOUND RESULT
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }

    public static void previousMonth (LocalDate date) {
        boolean found = false;
        LocalDate previousMonth = date.minusMonths(1);

        // PRINTS REPORT
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

        // PRINTS ONLY IF NO FOUND RESULT
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }

    public static void currentYear(LocalDate date) {
        boolean found = false;

        // PRINTS REPORT
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Current Year" + ConsoleColors.RESET);
        Tables.reportsHeader();
        for (Transaction t : transactions) {
            if (t.getDate().getYear() == date.getYear()) {
                Tables.fillReportsTable(t);
                found = true;
            }
        }

        // PRINTS ONLY IF NO FOUND RESULT
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }

    public static void previousYear(LocalDate date) {
        boolean found = false;
        LocalDate previousYear = date.minusYears(1);

        // PRINTS REPORT IF USER FOUND
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Previous Year" + ConsoleColors.RESET);
        Tables.reportsHeader();
        for (Transaction t : transactions) {
            if (t.getDate().getYear() == previousYear.getYear()) {
                Tables.fillReportsTable(t);
                found = true;
            }
        }

        // PRINTS ONLY IF NO FOUND RESULTS
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }

    public static void byVendor(Scanner scanner) {
        boolean found = false;

        // PROMPT FOR USER NAME
        System.out.print("Vendor Name: ");
        String vendor = scanner.nextLine();
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + vendor + ConsoleColors.RESET);

        // PRINTS REPORT IF VENDOR FOUND
        Tables.reportsHeader();
        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                Tables.fillReportsTable(t);
                found = true;
            }
        }

        // PRINTS ONLY IF NO FOUND RESULTS
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }

    public static void customSearch(Scanner scanner) {
        System.out.println("\nCustom Search (insert field(s) to filter)");


        // START DATE
        System.out.print("Start Date (yyyy-MM-dd) or 'null': ");                // prompts user for start date
        String userStartDateInput = scanner.nextLine();

        while (userStartDateInput.equals("")){                                  // forces user to use valid date format
            System.out.println("Invalid Date Format.");
            System.out.print("Start Date (yyyy-MM-dd) or 'null': ");
            userStartDateInput = scanner.nextLine();
        }

        LocalDate startDate = null;                                             // checks if start date field is empty
        if (!userStartDateInput.equalsIgnoreCase("null")) {
            startDate = LocalDate.parse(userStartDateInput, DATE_FORMATTER);
        }


        // END DATE
        System.out.print("End Date (yyyy-MM-dd) or 'null': ");                  // prompts user for end date
        String userEndDateInput = scanner.nextLine();

        while (userEndDateInput.equals("")){                                  // forces user to use valid date format
            System.out.println("Invalid Date Format.");
            System.out.print("Start Date (yyyy-MM-dd) or 'null': ");
            userEndDateInput = scanner.nextLine();
        }

        LocalDate endDate = null;
        if (!userEndDateInput.equalsIgnoreCase("null")) {             // checks if end date field is empty
            endDate = LocalDate.parse(userEndDateInput, DATE_FORMATTER);
        }


        // DESCRIPTION
        System.out.print("Description or press enter: ");
        String description = scanner.nextLine();


        // VENDOR
        System.out.print("Vendor or press enter: ");
        String vendor = scanner.nextLine();


        // AMOUNT
        System.out.print("Amount or 'null': ");
        String userAmountInput = scanner.nextLine();
        double amount = 0;

        if (!userAmountInput.equalsIgnoreCase("null")) {                // checks if amount is empty
            amount = Double.parseDouble(userAmountInput);
        }

        // CREATE REPORTS HEADER
        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Custom Search" + ConsoleColors.RESET);
        Tables.reportsHeader();

        boolean found = false;

        for (Transaction t : transactions) {
            if ((startDate == null || t.getDate().isAfter(startDate.minusDays(1))) &&
                    (endDate == null || t.getDate().isBefore(endDate.minusDays(1))) &&
                    (description.equals("") || t.getDescription().contains(description)) &&
                    (vendor.equals("") || t.getVendor().contains(vendor)) &&
                    (amount == 0 || t.getAmount() == amount)) {
                Tables.fillReportsTable(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println(ConsoleColors.RED_BOLD + "no results." + ConsoleColors.RESET);
        }
    }
}