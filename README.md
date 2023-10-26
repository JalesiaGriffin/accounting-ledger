# Financial Tracker

A CLI application for financial management. The user is given options to add payments, make payments, and view ledgers. Within the ledgers screen, the user is also able to run pre-defined reports as well as a custom search report.


## Overview

- [Setup](#setup)

- [Features](#features)

- [Demo](#demo)

- [Future Work](#future-work)

- [Acknowledgements](#acknowledgements)

## Setup
**Prerequisites**


Make sure Java and Maven are installed on your system.
You can check by running the following commands in your terminal:

```
java -version  
mvn -version
```
If they are not installed, download and install [Java](https://www.oracle.com/java/technologies/javase-downloads.html) and  [Maven](https://maven.apache.org/download.cgi).

**Clone The Repository**

In your terminal, navigate to the directory where you'll keep the project and clone this repository to your local machine. You can do this using the following command:
```
     git clone https://github.com/JalesiaGriffin/financial-tracker.git
```

## Features
- [Add Deposit](#add-deposit)
- [Add Payment](#add-payment)
- [Transactions Ledger](#transactions-ledger)
- [Deposit Ledger](#deposit-ledger)
- [Payment Ledger](#payment-ledger)
- [Reports Menu](#reports-menu)
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
  - By Vendor
  - Custom Search

### **Special Feature**
```
Public class Tables
```

Within this class I created 4 methods that allowed me to create tables for both
ledgers and reports. This enhanced my code because it allowed me to just
simply call on the class and a specific function as follows:
```
Tables.fillLedgerTable(t);
```
This eliminated repeat formatting and helped the main class maintain readability.
## Demo
### Home Screen
![home screen](imgs/home.gif)
### Add Deposit
![add deposit screen](imgs/add-deposit.gif)
### Add Payment
![add payment screen](imgs/add-payment.gif)
### Transactions Ledger
![ledger screen](imgs/ledger.gif)
### Deposit Ledger
![deposit ledger screen](imgs/deposit-ledger-screen.gif)
### Payment Ledger
![home screen](imgs/payment-ledger.gif)
### Reports Menu
![reports menu](imgs/reports-menu.gif)

## Future Work
- [ ] Expense Tracking (by categories)
- [ ] Budget Management
- [ ] User Registration

## Acknowledgements
[@RayMaroun](https://github.com/RayMaroun): Provided project skeleton

[@CallMeCJ_](https://github.com/CallMeCJUnderscore): Created sorting algorithm

[@WhiteFang34](https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println#comment6599876_5762502): ConsoleColors class

## Contact
Email: jalesiagriffin@outlook.com

Project Link:[https://github.com/JalesiaGriffin/financial-tracker](https://github.com/JalesiaGriffin/financial-tracker.git)

