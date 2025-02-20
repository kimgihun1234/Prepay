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
-- Table structure for table `team_store`
--

DROP TABLE IF EXISTS `team_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_store` (
  `team_store_id` bigint NOT NULL AUTO_INCREMENT,
  `team_store_balance` int NOT NULL,
  `store_store_id` bigint DEFAULT NULL,
  `team_team_id` bigint DEFAULT NULL,
  PRIMARY KEY (`team_store_id`),
  KEY `FK6tdd4gc1xvc59wc2052byl2g8` (`store_store_id`),
  KEY `FK3p9s08jrv3sannwx8udx3u1mv` (`team_team_id`),
  CONSTRAINT `FK3p9s08jrv3sannwx8udx3u1mv` FOREIGN KEY (`team_team_id`) REFERENCES `team` (`team_id`),
  CONSTRAINT `FK6tdd4gc1xvc59wc2052byl2g8` FOREIGN KEY (`store_store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_store`
--

LOCK TABLES `team_store` WRITE;
/*!40000 ALTER TABLE `team_store` DISABLE KEYS */;
INSERT INTO `team_store` VALUES (1,200000,1,5),(2,1654000,5,4),(3,95315000,6,2),(4,540000,2,1),(5,450000,3,1),(6,76000,4,1),(7,223600,7,1),(8,500000,10,1),(9,50000,1,3),(10,50000,6,3),(11,50000,3,3),(12,50000,6,9),(13,100000,1,10),(14,50000,4,11),(15,120000,10,13),(16,120000,7,14),(17,240000,4,15),(18,300000,5,16),(19,150000,3,17);
/*!40000 ALTER TABLE `team_store` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-20 21:39:46
