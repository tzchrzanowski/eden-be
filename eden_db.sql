-- MySQL dump 10.13  Distrib 8.1.0, for macos14.0 (arm64)
--
-- Host: localhost    Database: eden_db
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin','full admin access'),(2,'user','user type access'),(3,'accountant','can see all users and alter some of their attributes');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `profile_picture_url` varchar(999) DEFAULT NULL,
  `creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_date` timestamp NULL DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `role_id` int DEFAULT NULL,
  `left_child` int DEFAULT NULL,
  `right_child` int DEFAULT NULL,
  `parent` int DEFAULT NULL,
  `points` int DEFAULT NULL,
  `package_type` varchar(50) DEFAULT NULL,
  `money_amount` decimal(10,2) DEFAULT NULL,
  `direct_referral` int DEFAULT NULL,
  `monthly_points` int DEFAULT NULL,
  `cash_out` tinyint(1) DEFAULT NULL,
  `cash_out_details` varchar(200) DEFAULT NULL,
  `pairs_amount` int DEFAULT NULL,
  `gift_certificates_amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `left_child` (`left_child`),
  KEY `right_child` (`right_child`),
  KEY `parent` (`parent`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`left_child`) REFERENCES `users` (`id`),
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`right_child`) REFERENCES `users` (`id`),
  CONSTRAINT `users_ibfk_4` FOREIGN KEY (`parent`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'tzc','tzchrzanowski@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','Bartek','Ipsum','https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-10-12 03:09:45',NULL,1,1,14,NULL,2,0,'Diamond',0.00,1,0,0,'',0,0),(2,'Brt','bartek@bartek.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','Bartek','Dobraniecki','https://raw.githubusercontent.com/tzchrzanowski/img-wh/main/iron-man.jpg',NULL,NULL,1,1,1,3,NULL,78,'Gold',14200.00,1,0,0,'aa',5,7),(3,'user3','user1@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:33:58',NULL,1,2,4,5,2,168,'Silver',1800.00,2,0,0,'',0,0),(4,'user4','user4@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:40:13',NULL,1,2,6,7,3,165,'Gold',0.00,2,0,0,'',0,0),(5,'user5','user5@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:40:39',NULL,1,2,8,9,3,67,'Gold',0.00,2,0,0,'',0,0),(6,'user6','user6@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:43:10',NULL,1,2,NULL,NULL,4,46,'Silver',0.00,1,0,0,'',0,0),(7,'user7','user7@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:43:30',NULL,1,2,NULL,NULL,4,2,'Silver',0.00,1,0,1,'',0,0),(8,'user8','user8@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:44:27',NULL,1,2,NULL,NULL,5,44,'Silver',0.00,1,0,0,'',0,0),(9,'user9','user9@gmail.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm','User3',NULL,'https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-02 03:44:45',NULL,1,2,NULL,NULL,5,0,'Diamond',0.00,1,0,0,'',0,0),(14,'postUser','post@post.postdd','$2a$10$Tc159ZkIMsXuE7eu641sueeHI.Y/ZR82XQqXDUgGJcngTn86u4/OC','Post','User','https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-15 10:22:30',NULL,1,2,17,81,1,440,'Gold',2400.00,1,0,0,'',0,0),(17,'post User 3','postUser3@postUser3.com','$2a$10$3HSH9fIdbL.LKGsaIG4U6uQmFJxd8XK82hgC0L9xdB16lqwx6S27u','name3','surname3','https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-11-16 08:33:05',NULL,1,2,NULL,NULL,14,960,'Gold',0.00,2,0,1,'',0,0),(50,'acc','acc@acc.com','$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm',NULL,NULL,NULL,'2023-11-23 13:45:38',NULL,1,3,NULL,NULL,NULL,0,NULL,0.00,1,0,0,'',0,0),(81,'dwadwada11','dwa','$2a$10$k2Dl.6o1g9jtt5iWsRo0iOk2nT6Ywggo.mGJoLggIOB8.REh8N.v.','dwa','dwa','https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-12-13 11:53:30',NULL,1,2,82,NULL,14,0,'Silver',0.00,2,0,0,'',0,0),(82,'deeperUser1','1','$2a$10$expMJ1gQ3p2FMbMpF28ZwO2hc7HFPasNY3mhv/czcPvJHtN1RuPA2','1','1','https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png','2023-12-14 06:57:28',NULL,1,2,NULL,NULL,81,120,'Gold',0.00,2,0,0,'',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-16  6:25:17
