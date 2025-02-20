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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `fcm_token` varchar(255) DEFAULT NULL,
  `kakao_id` bigint DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'1@gmail.com','eVoCztapTUu-5uPvz5425T:APA91bHiMso6a58cjGcylZWfN_7QDwa1xPr0Uq504FBQGY4yq8vBJMi1UGl0PgLpGpsVwkoE6z3BtsRa7OkGPpH4MU8d2SCA-CWrjG4W2T7Rya7MzTC2MiM',NULL,'차현우','$2a$10$ztEDnRfn54Hav6a8CKmje.5L0xfR2Y1kFY2rTP39jNqHDrX8/YzHG'),(2,'2@gmail.com','eJBQuno3SPCuRx8ErW0VJY:APA91bFthDV1phwtmTFKJmgAoJ2jr7dLsdi7NCnNGZgjC-8L1VRvPeaZKZaTc2sox7OcCb0xZyKbE879byg7PBZaf2bracb_PyvWVy0eQLyJQpmCTi8Eg_M',NULL,'조성윤','$2a$10$vKncP5zqAgjY1Xxh1oMzzuQMvT6tmjvDzlr7HvxzuFhAVWbVEy386'),(3,'3@gmail.com',NULL,NULL,'김기훈','$2a$10$S0rY60EtYxmg0R5stDlF1eCZARZctuNvxWC8NspBOUo4iXi63bwIa'),(4,'4@gmail.com','fIIN1ptZQ0WzqE8skk37SS:APA91bFbSKjwMyIHo7DyM613V3DqT0QGDeggO_L-Pyhiqf61bE4jf09yHx0BBP1xHfdwOdAIfrU4z_k-glOGhdjGXIlrJW5VNNin6mYIgi26U0oFtSe7E4M',NULL,'서현석','$2a$10$7Tc/hmz7cybgX1f12bPXaO3MPp8mqbqOFoj4CzvbaHQ01NctmhJm2'),(5,'5@gmail.com',NULL,NULL,'경이현','$2a$10$0tYFQvhDzYpOPy24eLuFV.1rKNvjb9wD0C4YZwg23geO.OBpQoG5S'),(6,'6@gmail.com',NULL,NULL,'김성수','$2a$10$lSXK2BUomOdm6pwL3A/3AOwTVstjO0aJK3/9760TVMffzkZ82Asky'),(7,'7@gmail.com',NULL,NULL,'김정수','$2a$10$ABelQLiomWdpaZMyoFHXzeo83HTKUXJ.nyR6YirWUtpBnIwAqgyJ2'),(8,'8@gmail.com',NULL,NULL,'아이유','$2a$10$tllbKM.VdOTnHPzlMdjc0Obvo/F1OMN0zR2dFo0x2vb4f8SfrDfYK'),(9,'9@gmail.com',NULL,NULL,'반짝이는바지','$2a$10$qiPjWJs2Hs5OsYSnkv9VfurV9k5B4dBliiSFsfPfWwijX9dgE3gSG'),(10,'10@gmail.com',NULL,NULL,'싸피최고컨설턴트','$2a$10$vwbasPOyGmsB3S3GM.8jfu6zfx.ZfI510e9qZoK1PzNets2mphkKm'),(11,'11@gmail.com',NULL,NULL,'구미꿀주먹','$2a$10$W1K2kwNXYec7pA5HIwEAX.eC7eFNAkFbi0UQ.QxgHPFBK4LtHhaiy'),(12,'12@gmail.com',NULL,NULL,'배고픈취준생','$2a$10$2FPFOHplmyYx7cIRgOkWZ.axidK9w5wyvlEOXbAB.T1dDYmmk/gDm');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
