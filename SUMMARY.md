# Summary

## About the Application

The application is a file management API built using Kotlin, Spring Boot and MongoDB. It provides endpoints for 
uploading, downloading, retrieving metadata, and deleting files. The API supports various file types and allows users 
to interact with files using unique tokens.

### Features

- **File Upload**: Users can upload files along with metadata such as name, content type, source, and expiration time.
- **File Download**: Users can download files by providing the corresponding token. The API returns the file content 
along with appropriate headers.
- **File Metadata**: Users can retrieve metadata for multiple files by providing their tokens. The API returns metadata 
including file name, size, content type, and additional custom metadata.
- **File Deletion**: Users can delete files by providing the token. The API removes the file from storage and returns a 
success response.

### How to Run

#### Starting the database: 
     docker-compose up -d

#### Configurations:
See `variables.env` file and do modifications if needed.

#### Usage
In development add

    127.0.0.1    filedb
to your `/etc/hosts` file.

For basic auth, username is `admin` and password is `hunter2`

#### Start from CLI
    ./do.sh start

## Comments

The assignment provided a good opportunity for me to understand the technologies used at Mercans on a daily basis.
The Acceptance Criteria was clear and well-defined, making it easy to understand the requirements. But it would be more 
informative if it was mentioned what is the file size (small or large) that we're expected to store. Here, I assumed 
the file size is large.

## Which part of the assignment took the most time and why?

This was the first time I used Kotlin, and it took some time for me to understand the concepts and syntax.
Also, the first time I used MongoDB. I had to spend time to figure out what are the approaches that I can use and 
how to do the implementation.

## What You learned

I learned the basics of Kotlin and also got some idea about how MongoDB stores files in database using GridFS.

## TODOs

- Implement pagination and filtering for endpoints that return lists of metadata to improve performance and user 
experience.
- Add validations for input fields.
