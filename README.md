Image storage service
-----------------------------------

This project intent is to

    CREATE/DELETE album 
    
    CREATE/Delete image in an album
    
    Get image/images from album
    
    Notify when the album and image is created or deleted

This microservice is RESTful API written in Java structured and designed using Spring Boot.

Technologies
--------------------
Spring Boot

Kafka 2.12

Zookeeper

Docker Compose to link the containers.

Minio

Checking out and Building 
----------------------------------

git clone <>

cd image-storage-service

mvn clean package

Installing docker and docker compose
-------------------------------------

Docker - https://docs.docker.com/install/
Docker compose - https://docs.docker.com/compose/install/

How to Run
------------------------------------------------

docker build . -t image-storage-service:1.0

docker-compose up

Running the application
----------------------------------------

For testing all of the CRUD actions, I recommend using Postman


Create Album 
-------------------

Example:
POST http://localhost:8080/albums
Header- user : mycomp
body- {"name":"hackathon"}
Response : String 

Delete Album 
--------------------

Example
DELETE http://localhost:8080/albums/hackathon/
Header- user:mycomp
Response : String

Upload Image to ALbum 
-----------------------------

Example:
Post http://localhost:8080/albums/hackathon/images
Header- user:mycomp
Body- Image file to upload
Response: {
  "name":"",
  "type":"",
  "size":""
}

DELETE image from album
------------------------------

Example
DELETE http://localhost:8080/albums/hackathon/images/${imagename}
Header- user:mycomp
Response String

Get image from album 
---------------------------------

Example
GET http://localhttp:8080/albums/hackathon/images/${imagename}
Header- user:mycomp
Response:{
  "name":"",
  "downloadUri":"",
  "lastModifiedTime":"",
  "size":""
}

Get images from album 
-------------------------------------

Example 
GET http://localhost:8080/albums/hackathon/images
Header- user:mycomp
Response:[{
  "name":"",
  "downloadUri":"",
  "lastModifiedTime":"",
  "size":""
}]
