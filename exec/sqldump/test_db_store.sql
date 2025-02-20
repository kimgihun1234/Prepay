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
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `store_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `store_description` varchar(255) DEFAULT NULL,
  `store_img_url` varchar(255) DEFAULT NULL,
  `store_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_register_permission` bit(1) NOT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'경북 구미시 인동가산로 20',36.1068,128.418,'티라미수가 맛있는 브랜드 투썸플레이스','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%88%AC%EC%8D%B8.png?alt=media&token=bc0289b4-5238-496b-af21-df276f9e7298','투썸플레이스 인동점','카페',_binary '\0'),(2,'경북 구미시 여헌로7길 46',36.1056,128.421,'저렴한 한돈구이집 한돈참숯꼬기 인동점입니다.','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%9C%EB%8F%88%EC%B0%B8%EC%86%A3%EA%BC%AC%EA%B8%B0.jpg?alt=media&token=9baea51b-2741-4e2d-b97b-891af5744608','한돈참숯꼬기 인동점','식당',_binary '\0'),(3,'경북 구미시 인동중앙로1길 16',36.1075,128.419,'든든한 가성비의 순대국밥집','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%A0%9C%EC%9D%BC%EC%88%9C%EB%8C%80.jpg?alt=media&token=e5c691ae-ed79-4447-aa69-78d46d130421','전국제일순대 인동직영점','식당',_binary '\0'),(4,'경북 구미시 진평2길 27',36.1027,128.421,'싸고 양 많은 중국집 차이앤웍','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%B0%A8%EC%9D%B4%EC%95%A4%EC%9B%8D.jpg?alt=media&token=04b0afe5-ab4f-42a2-be76-02f658a5ab10','차이앤웍','식당',_binary '\0'),(5,'경북 구미시 3공단3로 302',36.1096,128.416,'삼성전자2공장 내부의 이디야','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%9D%B4%EB%94%94%EC%95%BC.png?alt=media&token=0d871ece-63e5-4d39-8358-9551e9d2f319','삼성전자2공장 이디야','카페',_binary '\0'),(6,'경북 구미시 인동북길 43',36.1026,128.424,'제주도의 커피 브랜드 에이바우트커피 인동점','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%97%90%EC%9D%B4%EB%B0%94%EC%9A%B0%ED%8A%B8.png?alt=media&token=b8d99438-ddcd-45ed-9a99-0bd67794c9de','에이바우트커피 인동점','카페',_binary '\0'),(7,'경북 구미시 인동북길 67',36.1007,128.425,'복어조리명장의 백년가게 복터진집 본점','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EB%B3%B5%ED%84%B0%EC%A7%84%EC%A7%91.jpg?alt=media&token=018967cf-e0f1-4d6d-bc2a-b80e17750769','복터진집 본점','식당',_binary '\0'),(8,'경북 구미시 인동가산로 5',36.1082,128.418,'간단하게 먹기 좋은 핵밥 구미인동점','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%B5%EB%B0%A5.png?alt=media&token=55a847d5-7032-404b-ad36-9f6a8b5e389b','핵밥 구미인동점','식당',_binary '\0'),(9,'경북 구미시 인동가산로 14',36.1071,128.418,'스타일링 받기 좋은 글로우업헤어','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EB%AF%B8%EC%9A%A9%EC%8B%A4.png?alt=media&token=8277b516-8a8a-41e1-98ee-408c43ca33f3','글로우업헤어','미용실',_binary '\0'),(10,'경북 구미시 인동중앙로1길 12',36.1073,128.42,'맛 좋은 한식집 교반인동점','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EA%B5%90%EB%B0%98.png?alt=media&token=32ea8e3a-52cb-448c-836d-d76406f3e71f','교반인동점','식당',_binary '\0');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-20 21:39:48
