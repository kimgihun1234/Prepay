CREATE DATABASE  IF NOT EXISTS `test_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `test_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 43.202.43.14    Database: test_db
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user_team`
--

DROP TABLE IF EXISTS `user_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_team` (
  `user_team_id` bigint NOT NULL AUTO_INCREMENT,
  `is_like` bit(1) NOT NULL,
  `position` bit(1) NOT NULL,
  `privilege` bit(1) NOT NULL,
  `usage_count` int NOT NULL,
  `used_amount` int NOT NULL,
  `team_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_team_id`),
  KEY `FK6d6agqknw564xtsa91d3259wu` (`team_id`),
  KEY `FKd6um0sk8hyytfq7oalt5a4nph` (`user_id`),
  CONSTRAINT `FK6d6agqknw564xtsa91d3259wu` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`),
  CONSTRAINT `FKd6um0sk8hyytfq7oalt5a4nph` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_team`
--

LOCK TABLES `user_team` WRITE;
/*!40000 ALTER TABLE `user_team` DISABLE KEYS */;
INSERT INTO `user_team` VALUES (1,_binary '\0',_binary '',_binary '',0,0,1,1),(2,_binary '\0',_binary '\0',_binary '\0',0,0,1,2),(3,_binary '\0',_binary '\0',_binary '\0',0,0,1,3),(4,_binary '\0',_binary '\0',_binary '\0',0,0,1,4),(5,_binary '\0',_binary '\0',_binary '\0',0,0,1,5),(6,_binary '\0',_binary '\0',_binary '\0',0,0,1,6),(7,_binary '\0',_binary '',_binary '',0,0,2,8),(8,_binary '\0',_binary '\0',_binary '\0',0,0,2,9),(9,_binary '\0',_binary '\0',_binary '\0',0,0,2,11),(10,_binary '\0',_binary '\0',_binary '\0',0,0,2,12),(11,_binary '\0',_binary '\0',_binary '\0',0,0,2,5),(12,_binary '\0',_binary '\0',_binary '\0',0,0,2,6),(13,_binary '\0',_binary '',_binary '',0,0,3,1),(14,_binary '\0',_binary '\0',_binary '\0',0,0,3,2),(15,_binary '\0',_binary '\0',_binary '\0',0,0,3,3),(16,_binary '\0',_binary '\0',_binary '\0',0,0,3,4),(17,_binary '\0',_binary '\0',_binary '\0',0,0,3,5),(18,_binary '\0',_binary '\0',_binary '\0',0,0,3,6),(19,_binary '\0',_binary '',_binary '',0,0,4,10),(20,_binary '\0',_binary '\0',_binary '\0',0,0,4,2),(21,_binary '\0',_binary '\0',_binary '\0',0,0,4,3),(22,_binary '\0',_binary '\0',_binary '\0',0,0,4,4),(23,_binary '\0',_binary '\0',_binary '\0',0,0,4,5),(24,_binary '\0',_binary '\0',_binary '\0',0,0,4,6),(25,_binary '\0',_binary '\0',_binary '\0',0,0,4,1),(26,_binary '\0',_binary '',_binary '',0,0,5,7),(27,_binary '\0',_binary '\0',_binary '\0',0,0,5,2),(28,_binary '\0',_binary '\0',_binary '\0',0,0,5,3),(29,_binary '\0',_binary '\0',_binary '\0',0,0,5,4),(30,_binary '\0',_binary '\0',_binary '\0',0,0,5,5),(31,_binary '\0',_binary '\0',_binary '\0',0,0,5,6),(32,_binary '\0',_binary '\0',_binary '\0',0,0,5,1),(34,_binary '\0',_binary '',_binary '',0,0,13,5),(35,_binary '\0',_binary '',_binary '',0,0,14,2),(36,_binary '\0',_binary '',_binary '',0,0,15,4),(37,_binary '\0',_binary '',_binary '',0,0,16,1),(38,_binary '\0',_binary '',_binary '',0,0,17,3),(39,_binary '\0',_binary '\0',_binary '\0',0,0,2,1),(40,_binary '\0',_binary '\0',_binary '\0',0,0,10,1),(41,_binary '\0',_binary '\0',_binary '\0',0,0,2,2),(42,_binary '\0',_binary '\0',_binary '\0',0,0,14,1);
/*!40000 ALTER TABLE `user_team` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-20 21:39:47
