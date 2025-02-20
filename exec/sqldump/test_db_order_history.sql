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
-- Table structure for table `order_history`
--

DROP TABLE IF EXISTS `order_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_history` (
  `order_history_id` bigint NOT NULL AUTO_INCREMENT,
  `company_dinner` bit(1) NOT NULL,
  `order_date` bigint NOT NULL,
  `refund_requested` bit(1) NOT NULL,
  `total_price` int NOT NULL,
  `with_draw` bit(1) NOT NULL,
  `store_store_id` bigint DEFAULT NULL,
  `team_team_id` bigint DEFAULT NULL,
  `user_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`order_history_id`),
  KEY `FKccac92pcqr10g73t6ldov79cs` (`store_store_id`),
  KEY `FK96kq0n6oi64s9ml2vpr1rroj` (`team_team_id`),
  KEY `FKpx0tr8vgt1ibaeh2k0xif8lys` (`user_user_id`),
  CONSTRAINT `FK96kq0n6oi64s9ml2vpr1rroj` FOREIGN KEY (`team_team_id`) REFERENCES `team` (`team_id`),
  CONSTRAINT `FKccac92pcqr10g73t6ldov79cs` FOREIGN KEY (`store_store_id`) REFERENCES `store` (`store_id`),
  CONSTRAINT `FKpx0tr8vgt1ibaeh2k0xif8lys` FOREIGN KEY (`user_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_history`
--

LOCK TABLES `order_history` WRITE;
/*!40000 ALTER TABLE `order_history` DISABLE KEYS */;
INSERT INTO `order_history` VALUES (1,_binary '\0',1739843154859,_binary '\0',30000,_binary '',2,1,3),(2,_binary '',1739743154859,_binary '\0',150000,_binary '\0',2,1,1),(3,_binary '',1739643154859,_binary '\0',45000,_binary '',3,1,5),(4,_binary '\0',1739543154859,_binary '\0',8000,_binary '',3,1,1),(5,_binary '',1739443154859,_binary '\0',54000,_binary '',10,1,5),(6,_binary '\0',1739833154123,_binary '\0',4200,_binary '',6,2,11),(7,_binary '\0',1739843154859,_binary '\0',3900,_binary '',6,2,12),(8,_binary '\0',1739843154859,_binary '\0',5500,_binary '',1,5,7),(9,_binary '\0',1739843154859,_binary '\0',5500,_binary '',1,5,6),(11,_binary '\0',1739943154859,_binary '\0',9000,_binary '',3,1,3),(15,_binary '',1740002354859,_binary '\0',184000,_binary '',2,1,2),(16,_binary '',1740088754859,_binary '\0',196000,_binary '',2,1,4),(17,_binary '\0',1740005154859,_binary '\0',12000,_binary '',2,1,6),(18,_binary '\0',1740061554859,_binary '\0',8000,_binary '',3,1,1),(19,_binary '\0',1740047954859,_binary '\0',6000,_binary '',4,1,3),(20,_binary '\0',1740034354859,_binary '\0',9000,_binary '',2,1,5),(21,_binary '\0',1740100754859,_binary '\0',500000,_binary '\0',2,1,1),(22,_binary '\0',1740461554859,_binary '\0',6500,_binary '',1,3,1),(23,_binary '\0',1739743154859,_binary '\0',4500,_binary '',1,3,1),(24,_binary '\0',1739843154859,_binary '\0',5900,_binary '',6,2,1),(25,_binary '\0',1739883154859,_binary '\0',5900,_binary '',6,2,2),(26,_binary '\0',1739943154859,_binary '\0',3900,_binary '',6,2,3),(27,_binary '\0',1739863154859,_binary '\0',5500,_binary '',6,2,4),(28,_binary '\0',1739833154859,_binary '\0',4500,_binary '',6,2,5),(29,_binary '\0',1739813154859,_binary '\0',4900,_binary '',6,2,6),(30,_binary '\0',1739848154859,_binary '\0',4200,_binary '',6,2,7),(31,_binary '\0',1739343154859,_binary '\0',4200,_binary '',6,2,8),(32,_binary '\0',1739543154859,_binary '\0',4200,_binary '',6,2,9),(33,_binary '\0',1739883154859,_binary '\0',3500,_binary '',6,2,10);
/*!40000 ALTER TABLE `order_history` ENABLE KEYS */;
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
