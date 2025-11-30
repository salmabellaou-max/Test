# MyWelly - Healthcare Appointment Booking Platform

![MyWelly Logo](https://img.shields.io/badge/MyWelly-Healthcare-2d6a4f?style=for-the-badge)

MyWelly is a comprehensive healthcare appointment booking platform designed for Morocco. It connects patients with doctors and laboratories, providing a seamless experience for booking appointments, managing medical records, and leaving reviews.

## Features

### For Patients
- **Easy Registration & Login** with secure authentication
- **Advanced Search** - Find doctors by name, specialty, location (Moroccan cities), and price
- **Book Appointments** with real-time availability
- **Modify or Cancel** appointments
- **Leave Reviews** and rate doctors (1-5 stars)
- **Email Notifications** for appointment reminders
- **Profile Management** - Update email, change password, delete account
- **View Appointment History** - Track completed, cancelled, and upcoming appointments

### For Doctors
- **Professional Registration** with specialty, location, and consultation fees
- **Dashboard** showing upcoming appointments and statistics
- **View Patient Details** including phone numbers
- **Cancel Appointments** with mandatory reason
- **View Reviews** from patients
- **Update Location** and other profile information
- **Performance Metrics** - Average rating and total reviews

### For Laboratories
- **Laboratory Registration** with location and working hours
- **Dashboard** with rating statistics
- **View Reviews** from patients
- **Profile Management**

## Technology Stack

- **Backend:** Java 17, Spring Boot 3.1.5
- **Security:** Spring Security with BCrypt password encoding
- **Database:** H2 (development), MySQL (production ready)
- **Frontend:** Thymeleaf, HTML5, CSS3
- **Build Tool:** Maven

## Prerequisites

- Java JDK 17 or higher
- Maven 3.6+
- Git

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/salmabellaou-max/Test.git
cd Test
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access H2 Database Console (Optional)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/mywelly`
- Username: `sa`
- Password: (leave empty)

## Test Accounts

The application comes pre-populated with sample data for testing:

### Patient Account
- **Email:** `patient@test.com`
- **Password:** `password123`

### Doctor Accounts
| Email | Password | Name | Specialty | Location | Fee (MAD) |
|-------|----------|------|-----------|----------|-----------|
| dr.hassan@mywelly.ma | doctor123 | Dr. Hassan Alami | Cardiology | Casablanca | 500 |
| dr.fatima@mywelly.ma | doctor123 | Dr. Fatima Zahra | Dermatology | Rabat | 400 |
| dr.mohammed@mywelly.ma | doctor123 | Dr. Mohammed Bennis | Pediatrics | Marrakech | 350 |
| dr.amina@mywelly.ma | doctor123 | Dr. Amina Idrissi | Orthopedics | Tangier | 600 |
| dr.youssef@mywelly.ma | doctor123 | Dr. Youssef El Amrani | Neurology | Agadir | 700 |
| dr.sara@mywelly.ma | doctor123 | Dr. Sara Benjelloun | General Practice | Laayoune | 300 |
| dr.karim@mywelly.ma | doctor123 | Dr. Karim Benkirane | Cardiology | Guelmim | 450 |
| dr.zineb@mywelly.ma | doctor123 | Dr. Zineb Chakir | Dermatology | Ben Guerir | 380 |

### Laboratory Accounts
| Email | Password | Name | Location |
|-------|----------|------|----------|
| lab.biotech@mywelly.ma | lab123 | BioTech Lab Casablanca | Casablanca |
| lab.medanalysis@mywelly.ma | lab123 | MedAnalysis Rabat | Rabat |
| lab.lifelab@mywelly.ma | lab123 | LifeLab Marrakech | Marrakech |

## Complete Testing Guide

### Test 1: Patient Registration & Login

1. **Register a New Patient:**
   - Navigate to `http://localhost:8080`
   - Click "Sign Up"
   - Select "Patient" from dropdown
   - Fill in all required fields:
     - Email: `testpatient@email.com`
     - Password: `test123`
     - Full Name: `Test Patient`
     - Date of Birth: `1995-01-01`
     - Gender: `Male`
     - ID Number: `TP123456`
     - Phone: `+212612999999`
     - Username: `testpatient`
   - Click "Create Account"
   - Verify you're redirected to login page with success message

2. **Login:**
   - Enter email: `testpatient@email.com`
   - Enter password: `test123`
   - Click "Login"
   - Verify you're redirected to patient dashboard

### Test 2: Search Functionality (All Filters)

1. **Login as Patient** (`patient@test.com` / `password123`)
2. **Click "Find Doctors"** in navigation
3. **Test Search by Name:**
   - Type "Hassan" in search box
   - Click "Search"
   - Verify Dr. Hassan Alami appears

4. **Test Filter by Specialty:**
   - Select "Cardiology" from Specialty dropdown
   - Verify only Cardiology doctors appear

5. **Test Filter by City:**
   - Select "Casablanca" from City dropdown
   - Verify only Casablanca-based doctors appear

6. **Test Filter by Price:**
   - Enter Min Price: `300`
   - Enter Max Price: `500`
   - Verify only doctors with fees between 300-500 MAD appear

7. **Test Combined Filters:**
   - Specialty: "Cardiology"
   - City: "Casablanca"
   - Min Price: `400`
   - Max Price: `600`
   - Verify correct filtering

### Test 3: View Doctor Profile

1. From search results, click "View Profile" on any doctor
2. Verify all information is displayed:
   - Doctor name, specialty, location
   - Rating and review count
   - Phone number
   - Working hours
   - Consultation fee
   - Certificates
   - Patient reviews

### Test 4: Book Appointment

1. **From Doctor Profile:**
   - Click "Book Appointment"
   - Select a future date (e.g., tomorrow)
   - Select a time slot (e.g., "10:00")
   - Click "Confirm Booking"
   - Verify success message and redirect to dashboard

2. **Verify Appointment Appears:**
   - Go to Patient Dashboard
   - Verify appointment appears in "Upcoming Appointments"
   - Verify it shows: doctor name, specialty, location, date, time, fee

3. **Test Double Booking Prevention:**
   - Try to book same doctor, same date, same time
   - Verify error message: "Time slot already booked"

### Test 5: Cancel Appointment (Patient)

1. **From Patient Dashboard:**
   - Find an upcoming appointment
   - Click "Cancel Appointment"
   - Confirm cancellation in popup
   - Verify success message

2. **Verify Cancellation:**
   - Verify appointment moves to "Appointment History"
   - Verify status shows "CANCELLED_BY_PATIENT"

### Test 6: Leave a Review

1. **From Appointment History:**
   - Find a completed appointment (you may need to wait 2 hours or manually update database)
   - Click "Leave a Review"

2. **Submit Review:**
   - Select rating (e.g., 5 stars)
   - Enter comment: "Excellent doctor, very professional"
   - Click "Submit Review"
   - Verify success message

3. **Verify Review Appears:**
   - Go to doctor's profile page
   - Verify your review appears in "Patient Reviews"
   - Verify star rating is displayed

### Test 7: Profile Management (Patient)

1. **Update Email:**
   - Go to Profile
   - Change email to: `newemail@test.com`
   - Click "Update Email"
   - Verify success message

2. **Change Password:**
   - Enter new password: `newpass123`
   - Confirm password: `newpass123`
   - Click "Change Password"
   - Verify success message
   - Logout and login with new password
   - Verify login works with new password

3. **Delete Account:**
   - Go to Profile
   - Scroll to "Danger Zone"
   - Click "Delete Account"
   - Confirm deletion
   - Verify redirect to homepage
   - Try to login with deleted account
   - Verify login fails

### Test 8: Doctor Dashboard & Features

1. **Login as Doctor** (`dr.hassan@mywelly.ma` / `doctor123`)

2. **View Dashboard:**
   - Verify statistics are displayed (upcoming appointments, reviews, rating)
   - Verify upcoming appointments list shows patient details

3. **Cancel Appointment (Doctor):**
   - Click "Cancel Appointment" on any upcoming appointment
   - Enter cancellation reason: "Emergency surgery scheduled"
   - Click "Confirm Cancellation"
   - Verify success message
   - Verify appointment is removed from list

4. **Update Location:**
   - Go to Profile
   - Change location to different city (e.g., "Rabat")
   - Click "Update Location"
   - Verify success message
   - Login as patient and search
   - Verify doctor appears in new city

### Test 9: Laboratory Dashboard

1. **Login as Laboratory** (`lab.biotech@mywelly.ma` / `lab123`)

2. **View Dashboard:**
   - Verify laboratory name is displayed
   - Verify statistics (reviews, rating)
   - Verify recent reviews section

3. **View Profile:**
   - Click "Profile"
   - Verify all laboratory information is displayed

### Test 10: Logout Functionality

1. **From any account:**
   - Click "Logout" in navigation
   - Verify redirect to homepage
   - Verify cannot access protected pages
   - Try to access `http://localhost:8080/patient/dashboard`
   - Verify redirect to login page

### Test 11: Security Features

1. **Test Account Lockout:**
   - Try to login with wrong password 5 times
   - Verify account gets locked after 5 attempts
   - Verify appropriate error message

2. **Test No-Show Blocking:**
   - Manually set patient's `no_show_count` to 3 in database
   - Try to login as that patient
   - Verify account is blocked

3. **Test Authentication:**
   - Try to access patient dashboard without login: `http://localhost:8080/patient/dashboard`
   - Verify redirect to login page

## Database Schema

The application uses the following main entities:

- **Users** - Stores user authentication data
- **Patients** - Patient-specific information
- **Doctors** - Doctor profiles with specialties and fees
- **Laboratories** - Laboratory information
- **Appointments** - Appointment bookings and history
- **Reviews** - Patient reviews for doctors and labs

## Design Features

- **Color Scheme:** Professional green and white theme
- **Responsive Design:** Works on desktop and mobile
- **MyWelly Logo:** Heart-shaped logo representing healthcare
- **User-Friendly Interface:** Intuitive navigation and clean layout

## Configuration

### Database Configuration

The application uses H2 database by default. To switch to MySQL:

1. Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mywelly
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

2. Create MySQL database:

```sql
CREATE DATABASE mywelly;
```

### Email Configuration

Update email settings in `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Project Structure

```
Test/
├── src/
│   ├── main/
│   │   ├── java/com/mywelly/
│   │   │   ├── config/           # Security & Data initialization
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── MyWellyApplication.java
│   │   └── resources/
│   │       ├── static/css/      # Stylesheets
│   │       ├── templates/       # Thymeleaf templates
│   │       └── application.properties
├── pom.xml
└── README.md
```

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Database Locked
```bash
# Stop all running instances
# Delete data/mywelly.mv.db file
# Restart application
```

## Contributing

This is an academic project for the MyWelly healthcare platform.

## Team

- **Safar Fatima Ezzahra**
- **Elansari Zineb**
- **Bellaou Salma**

**Supervisor:** Fouad Imane

## License

This project is developed as part of an academic assignment.

## Support

For issues or questions, please contact the development team.

---

**MyWelly - Transforming Healthcare in Morocco**
