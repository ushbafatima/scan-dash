# ScanDash - Smart Shopping Management System

ScanDash is a Java-based retail management system that integrates RFID scanning capabilities for products and customer cards, providing a seamless shopping experience.

## Features

### Customer Interface
- Scan customer cards for authentication
- View and manage shopping cart
- Scan products to add to cart
- View detailed product descriptions by category:
  - Electronics
  - Appliances
  - Fresh Grocery
  - Packaged Products
  - Cosmetics
- Process bill payments

### Manager Interface
- Secure login system
- Inventory management:
  - Add new products
  - Remove products
  - Update product details
  - Track inventory levels
- Customer management:
  - Add new customers
  - Remove customers
  - Manage customer cards
- Sales tracking and reporting

## Technical Requirements

- Java JDK 23
- Maven
- PostgreSQL Database
- Arduino (for RFID scanning)
- Required Libraries:
  - FlatLaf 3.5.4 (UI Framework)
  - jSerialComm (Arduino Communication)
  - PostgreSQL JDBC Driver
  - JFreeChart (Sales Reporting)

## Usage

1. **Customer Flow**
   - Scan customer card
   - Enter PIN
   - Scan products to add to cart
   - View cart and checkout
   - Process payment

2. **Manager Flow**
   - Login with credentials
   - Access management interface
   - Perform inventory management
   - Handle customer management
   - View sales reports
