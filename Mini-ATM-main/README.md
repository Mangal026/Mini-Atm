# Mini-ATM
💳 Java ATM Management System (Swing + JDBC + MySQL)
This is a Java-based ATM simulation system built using Swing for GUI and JDBC with MySQL for data persistence. It allows users to register with their PIN, log in securely, manage their account balance, and view detailed transaction history — all through a friendly graphical interface.

📌 Key Features
👤 User Registration: New users can register by entering their name, 4-digit PIN, and initial balance.

🔐 Secure Login: Login is done using a 4-digit PIN. Duplicate PINs are prevented.

💵 Deposit & Withdrawal: Users can perform basic ATM operations such as depositing and withdrawing money.

💰 Real-Time Balance Check: Users can instantly check their current account balance.

📜 Transaction History: Each transaction (with timestamp) is recorded in the MySQL database and displayed in a scrollable dialog.

🔁 Session Management: Includes proper logout functionality to clear the current session.

🧑‍💻 Technologies Used
Technology	Purpose
Java	Core programming language
Swing	GUI development
JDBC	Java-MySQL database connectivity
MySQL	Database to store users and transactions

🗄 Database Schema
Two main tables:

users:

pin (INT, Primary Key)

username (VARCHAR)

balance (DOUBLE)

transactions:

id (INT, AUTO_INCREMENT)

pin (INT)

message (VARCHAR)

time (DATETIME)

🛠 How It Works
On first launch, users can register a new account.

After registration, users can log in using their PIN.

The main ATM panel allows:

Checking current balance

Depositing funds

Withdrawing funds

Viewing transaction history

All operations are logged in the database with timestamps.

The user can log out, returning to the login screen.

📦 Project Structure Highlights
SystematizedATMApp.java: Main application file that contains:

GUI components for registration, login, and ATM operations.

Database functions for user registration, login validation, balance updates, and transaction history.

Local User class to manage current session.

🧪 Prerequisites to Run
JDK installed (Java 8 or later)

MySQL installed and running locally

JDBC connector JAR added to classpath

IDE like IntelliJ or Eclipse (optional)

🚀 Getting Started
bash
Copy
Edit
git clone https://github.com/your-username/java-atm-system.git
cd java-atm-system
Import atmdb.sql (if provided) into your MySQL.

Update DB credentials in the source file:

java
Copy
Edit
static final String DB_USER = "your-username";
static final String DB_PASS = "your-password";
Run SystematizedATMApp.java.
