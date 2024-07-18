# Telemedicine System

## Overview

The Telemedicine System is a comprehensive Java-based application developed using Spring technologies. It aims to revolutionize healthcare delivery by providing a platform for online consultations, managing patient health records, handling prescription management, and enabling seamless communication between doctors and patients.

## Key Features

### For Patients

- **User Registration and Authentication**: Patients can securely register and log in to the system.
- **Doctor Discovery**: View available doctors, their specialties, and schedules.
- **Appointment Management**: Book, reschedule, and cancel appointments with doctors online.
- **Consultation Interface**: Communicate with doctors via secure, real-time messaging during consultations.
- **Medical Records**: Access and update personal medical records, including viewing prescriptions and medical history.

### For Doctors

- **Secure Login**: Access the system securely with authentication.
- **Schedule Management**: View and manage consultation schedules.
- **Consultation Tools**: Conduct online consultations with access to patient medical histories.
- **Prescription Management**: Write and manage prescriptions directly within the system.
- **Medical Record Updates**: Update patient medical records after consultations.

### System-wide

- **Authentication and Security**: Utilizes Spring Security for robust authentication and session management, ensuring data privacy and user confidentiality.
- **Database Integration**: Integrated with PostgreSQL via Spring Data JPA, ensuring efficient data management and persistence.
- **RESTful API**: Implements RESTful services using Spring Web, providing a scalable and flexible API for seamless integration with frontend or other systems.
- **Real-time Updates**: Provides real-time updates on appointment statuses and ensures timely feedback to users.

## Business Benefits

- **Accessibility**: Improves access to healthcare services, particularly in remote or underserved areas.
- **Efficiency**: Streamlines the appointment booking process, reducing administrative overhead and wait times.
- **Security**: Ensures data integrity and patient confidentiality through secure authentication and session management.
- **Scalability**: Designed using Spring Boot, enabling easy scaling and future enhancements.
- **Compliance**: Adheres to healthcare data regulations and best practices, ensuring legal compliance and trustworthiness.

## API Endpoints

### Authentication

- `POST /auth/register`: Register a new user (patient or doctor).
- `POST /auth/login`: Authenticate user credentials and create a session.
- `POST /auth/logout`: Terminate the user session.

### Appointments

- `GET /doctors`: Retrieve a list of doctors with their schedules.
- `POST /appointments`: Book a new appointment with a doctor.
- `PUT /appointments/{id}`: Reschedule an existing appointment.
- `DELETE /appointments/{id}`: Cancel an appointment.
- `GET /appointments`: View appointment history for the patient.

### Medical Records and Prescriptions

- `GET /patients/{id}/records`: Retrieve the medical records of a patient.
- `PUT /patients/{id}/records`: Update the medical records of a patient.
- `GET /patients/{id}/prescriptions`: Retrieve prescriptions issued to a patient.
- `POST /patients/{id}/prescriptions`: Create a new prescription for a patient.

## Technology Stack

- **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **API Documentation**: Swagger

## Getting Started

To run the Telemedicine System locally:

1. Clone this repository.
2. Configure PostgreSQL database settings in `application.properties`.
3. Build and run the application using Gradle.
4. Access the API documentation via Swagger UI (`http://localhost:8090/swagger-ui.html`).
