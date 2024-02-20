create database welcome_service;
create user welcome_service_user with encrypted password 'welcome_service_service_password';
grant all privileges on database welcome_service to welcome_service_user;