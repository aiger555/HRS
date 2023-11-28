
-- Table for patients
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    phone_number INT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    room INT NOT NULL
);

-- Table for doctors
CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    doctor VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    qualification VARCHAR(255) NOT NULL,
    room INT NOT NULL
);

-- Table for appointments
CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- Table for users (assuming doctors and patients share the same table)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    qualification VARCHAR(255),
    room INT
);
-- Inserting sample data into doctors table
INSERT INTO doctors (doctor, specialization, qualification, room)
VALUES
    ('Dr. Smith', 'Cardiologist', '2000', 3),
    ('Dr. Johnson', 'Orthopedic Surgeon', '2002', 2),
    ('Dr. White', 'Pediatrician', '2008', 4);

-- Inserting sample data into patients table
INSERT INTO patients (name, surname, phone_number, reason, room)
VALUES
    ('John', 'Doe', 123456789, 'Regular Checkup', 1),
    ('Jane', 'Smith', 987654321, 'Orthopedic Consultation', 2),
    ('Alice', 'Johnson', 555555555, 'Pediatric Consultation', 3);

-- Sample data for testing
INSERT INTO users (username, password, role, name, specialization, qualification, room)
VALUES
    ('doctor1', 'password1', 'DOCTOR', 'Dr. Smith', 'Cardiologist', '2003', 1),
    ('doctor2', 'password2', 'DOCTOR', 'Dr. Johnson', 'Orthopedic Surgeon', 'MD', 4),
    ('patient1', 'password1', 'PATIENT', 'John Doe', NULL, NULL, 2),
    ('patient2', 'password2', 'PATIENT', 'Jane Doe', NULL, NULL, 3);
-- Inserting appointments for patients and doctors
INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES
(1, 1, '2023-12-01'),
(2, 2, '2023-12-02'),
(3, 1, '2023-12-03'),
(4, 2, '2023-12-04');
