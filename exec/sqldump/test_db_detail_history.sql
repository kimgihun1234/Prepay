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
-- Table structure for table `detail_history`
--

DROP TABLE IF EXISTS `detail_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_history` (
  `detail_history_id` bigint NOT NULL AUTO_INCREMENT,
  `detail_price` int NOT NULL,
  `product` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `order_history_order_history_id` bigint DEFAULT NULL,
  PRIMARY KEY (`detail_history_id`),
  KEY `FKkqsp5758vrkrd44db1eawed59` (`order_history_order_history_id`),
  CONSTRAINT `FKkqsp5758vrkrd44db1eawed59` FOREIGN KEY (`order_history_order_history_id`) REFERENCES `order_history` (`order_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_history`
--

LOCK TABLES `detail_history` WRITE;
/*!40000 ALTER TABLE `detail_history` DISABLE KEYS */;
INSERT INTO `detail_history` VALUES (1,15000,'삼겹살',2,1),(2,15000,'삼겹살',10,2),(3,9000,'제일순살국밥',5,3),(4,8000,'제일국밥',1,4),(5,9000,'불고기비빔밥',3,5),(6,9000,'육회비빔밥',3,5),(7,4200,'둘둘둘커피',1,6),(8,3900,'제로슈가 복숭아아이스티',1,7),(9,5500,'복숭아아이스티',1,8),(10,5500,'복숭아아이스티',1,9),(11,12000,'삼겹살',4,16),(12,11000,'항정살',8,16),(13,15000,'특수부위',4,16),(15,8000,'제일순대국밥',1,18),(16,6000,'짜장면',1,19),(18,9000,'육회비빔밥',1,20),(19,12000,'까치복 정식',1,17),(20,5000,'소주',5,15),(21,5000,'맥주',3,15),(22,12000,'삼겹살',12,15),(23,9000,'특제일순대국',1,11),(24,5900,'딸기멜로 바닐라쉐이크',1,24),(25,5900,'딸기멜로 초코쉐이크',1,25),(26,3900,'제로슈가 복숭아아이스티',1,26),(27,5500,'제로슈가 감귤에이드',1,27),(28,4500,'아포카토',1,28),(29,4900,'돌체라떼',1,29),(30,4200,'둘둘둘커피',1,30),(31,4200,'둘둘둘커피',1,31),(32,4200,'둘둘둘커피',1,32),(33,3500,'아메리카노',1,33);
/*!40000 ALTER TABLE `detail_history` ENABLE KEYS */;
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
