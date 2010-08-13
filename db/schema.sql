create database garth;
use garth;
create table users (
	id int primary key auto_increment,
	user_name char(32) not null
);

create table organisms (
	id int primary key auto_increment,
	parent_id int,
	fitness double,
	genome binary(255)
);
create table breeders (
	id int primary key auto_increment,
	executable_path tinytext,
	short_name char(64),
	description tinytext
);
create table judges (
	id int primary key auto_increment,
	executable_path tinytext,
	short_name char(64),
	description tinytext
);
create table experiments (
	id int primary key auto_increment,
	user_id int not null,
	execution_time int,
	description tinytext
);
create table plugin_params (
	id int primary key auto_increment,
	parameter_name char(32) not null,
	parameter_type enum('string','number') not null,
	plugin_id int not null,
	plugin_type enum('breeder','judge') not null
);
create table processes (
	id int primary key auto_increment,
	experiment_id int not null,
	system_pid int not null,
	process_type enum('breeder','judge','zookeeper') not null,
	timestamp_for_start_of_execution timestamp,
	percent_complete float(4,2)
);
