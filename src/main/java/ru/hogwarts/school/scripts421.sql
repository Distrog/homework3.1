--шаг 1
--Возраст студента не может быть меньше 16 лет.
alter table student add constraint age_constraint check(age>=16);
--Имена студентов должны быть уникальными и не равны нулю.
alter table student alter column name set not null;
alter table student add constraint name_unique unique(name);
--Пара “значение названия” - “цвет факультета” должна быть уникальной.
alter table faculty add constraint name_and_color_unique unique(name,color);
--При создании студента без возраста ему автоматически должно присваиваться 20 лет.
alter table student alter age set default 20;

--шаг 2
create table cars(
id serial primary key,
mark varchar(20),
model varchar(20),
price numeric(10,2)
);
create table persons(
name text primary key,
age integer,
has_car_license boolean,
car_id integer references cars(id)
);

--шаг3
select student.name, student.age, faculty.name from student
inner join faculty on student.faculty_id=faculty.id;



