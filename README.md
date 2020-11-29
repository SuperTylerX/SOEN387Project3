# SOEN387Project3
This is project 3 for SOEN387
## Team information
- Tianxiang Ying (40075013)
- Bowen Yang (40072133)
- Yanqi Zhang (40050276)
- Xuan Li (40066717)

## How to run it

1. Install JDK 8.0 and MySQL 5.6.
2. Excute the following SQL statement to create database schema.

```sql
create table if not exists attachment
(
    attach_id   int(8) auto_increment primary key references posts (post_id),
    attach_name varchar(255) null,
    attach_size int(64)      null,
    attach_mime varchar(255) null,
    attach_file mediumblob   null
);

create table if not exists posts
(
    post_id            int(8) auto_increment primary key,
    post_title         varchar(255)   null,
    post_content       varchar(10240) null,
    post_author_id     int(8)         null,
    post_created_date  bigint(13)     null,
    post_modified_date bigint(13)     null,
    post_attach_id     int(8)         null
);
```

## Description and Release Notes:

This is a small forum system called Concordia Forum 1.0. Currently, this version does not support user registration, you can only log in with the existing user name and password. The user registration function will be implemented in the next version. After logging in, you can create, delete, search, and modify posts. The forum supports uploading individual attachments when creat and modify posts. The attachment size should be no more than 5M.


This project is a Java Servlet project. Tomcat needs to be configured to run the project, you need to install and correctly configure [Tomcat 9.0](https://tomcat.apache.org/download-90.cgi) or above version for your IDE. For the project SDK, please install [Java 1.8](https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html) or the above version.


We use the MVC structure for the project. The model is responsible for maintaining application data and business logic. The view is a user interface of the application, which displays the data. The controller handles user's requests and renders appropriate View with Model data. Please check the code for more details.



