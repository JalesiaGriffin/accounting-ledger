# Financial Tracker

A CLI application for financial management. The user is given options to add payments, make payments, and view ledgers. Within the ledgers screen, the user is also able to run pre-defined reports as well as a custom search report.


## Overview

- [Setup](#setup)

- [Features](#features)

- [Demo](#demo)

- [Future Work](#features)

- [Thanks](#thanks)

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
- Add Deposit
- Add Payment
- Reports
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
  - By Vendor
  - Custom Search
- Transactions Ledger
- Deposit Ledger
- Payment Ledger

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
### Ledger
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

## Thanks

