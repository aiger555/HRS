
create table doctors(
id serial primary key,
doctor varchar(50) ,
specialization varchar(100),
qualification int ,
room int);

insert  into doctors (doctor, specialization, qualification, room)
values
        ('Yasmiin Sharshenova', 'Dermatologist', 2000, 2),
        ('Aizirek Ibraimova', 'ENT', 2007, 3),
        ('Aiperi Zhenishova', 'Traumatologist', 2015, 4),
        ('Jyldyz Altmyshbaeva', 'Dietitian', 2020, 3),
        ('Aigerim Nuralieva','Dentist', 1990, 5),
        ('Samira Mirbekova', 'Cardiologist', 2003, 1);

create table patients (
id serial primary key,
name varchar(20),
surname varchar(30),
phone_number int ,
reason_of_visiting varchar(200),
room int );

insert into patients ( name, surname, phone_number, reason_of_visiting, room)
values 
 ('Marina', 'Kubanychbekov', 771458943, 'Dermatologist:Hair loss or thinning.', 2),
 ('Askhat', 'Daiyrbekov', 778012856, 'Traumatologist:Sports injuries.', 4),
 ('Beksultan', 'Faizullaev', 502678323, 'ENT:Sleep apnea or snoring issues.', 3),
 ('Nargiza', 'Mamytova', 704200300, 'Dietitian:Food allergies or intolerances.', 3),
 ('Tilek', 'Bolotov', 500322677, 'Cardiologist: Shortness of breath.', 1),
 ('Nurai', 'Nurlanovna', 555074272, 'Dermatologist:Acne or persistent skin breakouts.',2),
 ('Adinai', 'Samudinova', 500469746, 'Dentist:Toothache or dental pain.',5);

ALTER TABLE patients
ADD COLUMN doctor_id INT;

ALTER TABLE patients
ADD CONSTRAINT fk_doctor_id
FOREIGN KEY (doctor_id)
REFERENCES doctors(id);

insert into patients ( name, surname, phone_number, reason_of_visiting, room, doctor_id)
values 
 ('Marina', 'Kubanychbekov', 771458943, 'Dermatologist:Hair loss or thinning.', 2, 1),
 ('Askhat', 'Daiyrbekov', 778012856, 'Traumatologist:Sports injuries.', 4, 3 ),
 ('Beksultan', 'Faizullaev', 502678323, 'ENT:Sleep apnea or snoring issues.', 3, 2),
 ('Nargiza', 'Mamytova', 704200300, 'Dietitian:Food allergies or intolerances.', 3, 4),
 ('Tilek', 'Bolotov', 500322677, 'Cardiologist: Shortness of breath.', 1, 6),
 ('Nurai', 'Nurlanovna', 555074272, 'Dermatologist:Acne or persistent skin breakouts.', 2, 1),
 ('Adinai', 'Samudinova', 500469746, 'Dentist:Toothache or dental pain.', 5, 5 );
 

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date TIMESTAMP(0), 
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

insert INTO appointments (patient_id, doctor_id, appointment_date)
VALUES
    (1, 1, '2023-11-27 10:00:00'),
    (2, 3, '2023-11-28 15:30:00'),
    (3, 2, '2023-11-29 09:45:00'),
    (4, 4, '2023-11-30 14:15:00'),
    (5, 6, '2023-12-01 11:30:00'),
    (6, 1, '2023-12-02 16:00:00'),
    (7, 5, '2023-12-03 08:00:00');
