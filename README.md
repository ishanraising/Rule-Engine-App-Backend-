# Rule Engine Application Backend

## Overview
This repository contains the backend for the Rule Engine Application, developed using Spring Boot and Java. The backend handles API requests for creating, modifying, and evaluating rules based on user attributes and conditions. It interacts with a MySQL database to store rules.

## Prerequisites

### 1. Install JDK
To run the application, you need to have the Java Development Kit (JDK) installed.

- **Download JDK**: 
  - Visit the [Oracle JDK Downloads](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) page.
  - Download the appropriate version for your operating system.

- **Installation**:
  - Follow the installation instructions specific to your operating system (Windows, macOS, Linux).
  - After installation, set the `JAVA_HOME` environment variable:
    - **Windows**:
      1. Open the Start menu and search for "Environment Variables."
      2. Click on "Edit the system environment variables."
      3. In the System Properties window, click on "Environment Variables."
      4. Under System Variables, click "New" and set `JAVA_HOME` to the JDK installation path (e.g., `C:\Program Files\Java\jdk-11.0.x`).
      5. Add `%JAVA_HOME%\bin` to the `Path` variable.
    - **macOS/Linux**:
      - Open the terminal and add the following line to your `~/.bash_profile` or `~/.bashrc` file:
        ```bash
        export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.x.jdk/Contents/Home
        ```
      - Save the file and run `source ~/.bash_profile` or `source ~/.bashrc`.

### 2. Install IntelliJ IDEA Community Edition
IntelliJ IDEA is a powerful IDE for Java development.

- **Download IntelliJ IDEA**:
  - Visit the [IntelliJ IDEA Downloads](https://www.jetbrains.com/idea/download/) page.
  - Select the Community version and download it.

- **Installation**:
  - Follow the installation instructions for your operating system.
  - Launch IntelliJ IDEA and set it up according to your preferences.

### 3. Install MySQL Server
MySQL is the database management system used for this application.

- **Download MySQL Server**:
  - Visit the [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/) page.
  - Select the appropriate version for your operating system.

- **Installation**:
  - Follow the installation instructions specific to your OS.
  - During installation, you will be prompted to set a root password. Make sure to remember this password as it will be needed to access the database.

### 4. Install MySQL Workbench
MySQL Workbench is a graphical user interface for managing MySQL databases.

- **Download MySQL Workbench**:
  - Visit the [MySQL Workbench Downloads](https://dev.mysql.com/downloads/workbench/) page.
  - Download the version compatible with your OS.

- **Installation**:
  - Follow the installation instructions for your operating system.

## Setting Up the Database
1. **Launch MySQL Server**: Start the MySQL server using the command line or the MySQL Workbench.
2. **Create Database**: 
   - Open MySQL Workbench and connect to your local MySQL server.
   - Run the following SQL command to create a new database:
     ```sql
     CREATE DATABASE ruleengine;
     ```
## Clone Repository
```bash
git clone https://github.com/ishanraising/Rule-Engine-App-Backend-
```


## Running the Application
1. **Open IntelliJ IDEA**.
2. **Import the Project**:
   - Click on "Open" and select the project directory.

3.  **Configure Application Properties**:
   - In the `src/main/resources/application.properties` file, configure the database connection:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/ruleengine
     spring.datasource.username=//your username 
     spring.datasource.password=//your password
     spring.jpa.hibernate.ddl-auto=update
     ```
     keep rest of the code in the file same 
     
4. **Run the Application**:
   - In the `src/main/java/com.example.ruleengine/RuleEngineApplication` file
   at top right corner there is run file option click on it and your application will run on port 8081
5. **Access the API**: 
   - Once the application is running, you can access the API at `http://localhost:8081/rule`.
## Key Directories and Files

/src/main/java/com/example/ruleengine/

RuleEngineApplication.java: The main Spring Boot application class. This serves as the entry point for the application, where the main method is located to launch the application.


`/src/main/java/com/example/ruleengine/controller/`
RuleController.java: This REST controller handles HTTP requests related to rules. It provides endpoints for creating, modifying, and evaluating rules, as well as retrieving rules from the database.

`/src/main/java/com/example/ruleengine/entity/`
Rule.java: The entity class representing a rule, including its attributes, operators, and conditions. This class defines the structure of a rule stored in the database.
RuleEvaluationResult.java: Represents the result of evaluating a rule against specific user attributes, storing the evaluation outcome and any relevant messages.

`/src/main/java/com/example/ruleengine/repository/`
RuleRepository.java: This interface extends JpaRepository or CrudRepository to facilitate CRUD operations on the Rule entity in the database.

`/src/main/java/com/example/ruleengine/service/`
RuleService.java: Contains the business logic for managing rules, including methods for creating, modifying, and evaluating rules. It serves as an intermediary between the controller and the repository.

`/src/main/java/com/example/ruleengine/exception/`
RuleNotFoundException.java: A custom exception that is thrown when a requested rule is not found in the database. This helps in handling errors gracefully.

`/src/main/resources/`
application.properties: This configuration file holds the database connection details (such as URL, username, and password) and other application properties, like the server port and any external API keys.

`/src/test/java/com/example/ruleengine/`
RuleEngineApplicationTests.java: Contains unit tests for various functionalities of the application. It includes test cases for creating, modifying, and evaluating rules to ensure the application behaves as expected.

`/src/test/java/com/example/ruleengine/ats/`
[AST Test Classes]: This directory contains test classes related to the Abstract Syntax Tree (AST) used in the rule engine. These tests focus on evaluating and validating the structure, operations, and behavior of the AST as it interprets and processes rule evaluation logic.

## Contact
For any questions or inquiries, please reach out to [Ishan Raising](ishanraising98@gmail.com).
