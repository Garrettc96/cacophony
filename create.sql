create table user (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
create table user (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
create table c_users (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
create table c_users (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
create table c_users (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
create table c_users (created_at timestamp(6) with time zone, email varchar(255), id varchar(255) not null, name varchar(255), primary key (id));
alter table if exists c_users add column password varchar(255);
alter table if exists c_users add column username varchar(255);
