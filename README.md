OrderCraft – Deployment Manual
1. Introduction
This document provides step-by-step instructions for deploying and running the OrderCraft application. It is intended for developers, system administrators, and technical users responsible for application setup and maintenance.
________________________________________
2. System Requirements
Ensure the following software and tools are installed and properly configured before proceeding:
Required Tools & Technologies
•	Java Development Kit (JDK) – Version 8
•	Oracle Database – Version 11g
•	Angular Framework – Compatible with the provided source code
•	Spring Boot – Backend framework (embedded server enabled)
•	Visual Studio Code – For Angular (frontend) development
•	Eclipse IDE – For Spring Boot (backend) development
________________________________________
3. Application Architecture Overview
•	Frontend: Angular (runs on a local development server)
•	Backend: Spring Boot (embedded server)
•	Database: Oracle 11g
________________________________________
4. Deployment Steps
Follow the steps below to successfully run the application:
Step 1: Environment Setup
•	Install all required tools listed in Section 2.
•	Verify environment variables for Java and Angular CLI are correctly configured.
Step 2: Import Source Code
•	Open Visual Studio Code and import the Angular frontend source code.
•	Open Eclipse IDE and import the Spring Boot backend project.
Step 3: Validate Source Code
•	Ensure there are no compilation or dependency errors in either the frontend or backend projects.
•	Resolve any missing dependencies before proceeding.
Step 4: Run the Frontend Application
•	Open a terminal or command prompt.
•	Navigate to the Angular project directory.
•	Execute the following command:
•	ng serve
•	Wait until the Angular development server starts successfully.
Step 5: Run the Backend Application
•	Since Spring Boot uses an embedded server, no external server setup is required.
•	Run the Spring Boot application directly from Eclipse.
•	Application startup may take a few minutes.
Step 6: Access the Application
•	Once both frontend and backend are running:
o	Open a web browser.
o	Enter the URL using the port number displayed by Angular (commonly http://localhost:4200).
•	The OrderCraft application will be available for use.
________________________________________
5. Database Configuration
Use the following credentials to connect to the Oracle database:
•	Username: IMS
•	Password: 1234
________________________________________
6. Source Code Repository
The complete source code for the OrderCraft application is available at:
Git Repository:
Charshini Bandreddi / OrderCraft_FE_Squad3 · GitLab (Frontend).
Charshini Bandreddi / Ordercraft_Squad3 · GitLab (Backend).

________________________________________
7. Completion Confirmation
After successfully completing the above steps, the OrderCraft application will be fully operational and ready for use.

