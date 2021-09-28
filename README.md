# video_blog_api

Restful Monolithic Design using Spring Boot

This maven project is first created from spring.io.
Postgres
---------------
* Postgres 11 is used as database.
* video blog Database with user, role is kept under ./resources/db/video_management_system.sql.
* neet to execute the video_management_system.sql before running the project.
* User Info, Video Items & relations are recorded in the database. (database credentials are in appliantion.properties file)

Redis(Memory storage)
----------------------
* Neet to install redis (Redis credentials are in appliantion.properties file)
* Redis maintain key, value pair with TTL.
* JWT token expirations customization is handled thourg Redis TTL feature.

Lombok Plugin
-------------------
* Need to Install lombok plugin.

Swagger-UI
---------------
* Added swagger to test REST API from browser.

Log4j
---------------
* Used Log4japi to write logs.


Functions
----------------
 * Implemented JWT authentication (Spring Security)
 * Managing Videos (CRUD Operation)
 * Managing Videos watch, like/dislike count, summary & details  (CRUD Operation)
 * User registration & login.
 * User Identification
