# Telemedicine System

## Description

The Telemedicine System is a Java-based application developed using Spring technologies. It facilitates online consultations between doctors and patients, manages patient health records, handles prescription management, and provides a secure platform for communication.

## Business Case

Telemedicine systems play a crucial role in modern healthcare by improving accessibility and efficiency. This project aims to provide developers with insights into essential features of telemedicine systems, focusing on security, data integrity, and user interaction.

## Architecture & Technologies Used

### Backend Framework

- **Spring Boot**: Facilitates rapid development and deployment of Java applications.

### Security

- **Spring Security**: Provides authentication and secures API endpoints.

### Data Handling

- **Spring Data JPA**: Simplifies data access and persistence.

### API

- **RESTful services using Spring Web**: Processes data for doctors and patients.

### Database Schema

- **Patients**: id, name, email, phone, medical_history
- **Doctors**: id, name, specialization, schedule
- **Appointments**: id, patient_id, doctor_id, date, time, status
- **Prescriptions**: id, patient_id, doctor_id, medication, dosage, duration

## User Stories

### Patients

- Register and log in to the system.
- View available doctors and their schedules.
- Book online consultations.
- Communicate with doctors during consultations.
- Receive and view prescriptions and medical records.

### Doctors

- Log in to the system.
- View their schedules.
- Conduct online consultations.
- Access patient medical history.
- Write prescriptions and update medical records.

### System Requirements

- Validate user credentials securely.
- Manage user sessions to ensure data protection.
- Process appointments and prescriptions accurately and securely.
- Provide real-time updates on appointment statuses.

## API Endpoints

### Authentication and Session Management

- **POST /auth/register**: Register a new user.
- **POST /auth/login**: Authenticate user and create a session.
- **POST /auth/logout**: Terminate the session.

### Appointment Operations

- **GET /doctors**: List all doctors and their schedules.
- **POST /appointments**: Book a new online consultation.
- **PUT /appointments/{id}**: Reschedule an existing consultation.
- **DELETE /appointments/{id}**: Cancel a consultation.
- **GET /appointments**: View consultation history.

### Medical Records and Prescriptions

- **GET /patients/{id}/records**: View patient's medical records.
- **PUT /patients/{id}/records**: Update patient's medical records.
- **GET /patients/{id}/prescriptions**: View patient's prescriptions.
- **POST /patients/{id}/prescriptions**: Create a new prescription.

## Acceptance Criteria

### Spring Boot Application Setup

- Configure a Spring Boot application with Gradle.
- Include dependencies for Spring Boot, Spring Data JPA, Spring Security, and Spring Web.

### Database and Entity Configuration

- Define JPA entities for Patients, Doctors, Appointments, and Prescriptions.
- Set up a PostgreSQL database for data storage.

### User Authentication

- Implement secure login functionality using email and password authentication.
- Manage user sessions securely.

### Appointment and Prescription Processing

- Develop API endpoints to handle appointment bookings, rescheduling, and cancellations.
- Implement functionalities for doctors to manage patient records and prescriptions.

### API Documentation

- Use Swagger for documenting API endpoints.
- Include detailed descriptions of request/response models, error messages, and status codes.

### Testing

- Write integration tests to validate critical functionalities.
- Ensure robust error handling and graceful recovery from exceptions.

## Getting Started

To run the Telemedicine System locally:

1. Clone this repository.
2. Set up a PostgreSQL database and configure the connection in `application.properties`.
3. Build the project using Gradle.
4. Run the application.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- Inspired by the need for accessible healthcare solutions.
- Thanks to the Spring Boot and Java communities for their robust frameworks and libraries.

