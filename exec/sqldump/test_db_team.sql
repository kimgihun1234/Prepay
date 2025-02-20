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
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `team_id` bigint NOT NULL AUTO_INCREMENT,
  `code_gen_date` bigint NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `count_limit` int NOT NULL,
  `daily_price_limit` int NOT NULL,
  `gen_date` bigint NOT NULL,
  `public_team` bit(1) NOT NULL,
  `team_img_url` varchar(255) DEFAULT NULL,
  `team_message` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  `team_password` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  KEY `FK7ab8b9m84sk9vvpmrn6b6tpjk` (`user_id`),
  CONSTRAINT `FK7ab8b9m84sk9vvpmrn6b6tpjk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,0,'#21c8f1',0,12000,1739700507000,_binary '\0',NULL,'오늘 걷지 않으면 내일은 뛰어야한다!','기훈솔루션',NULL,1),(2,0,'#9f75b9',0,6000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%95%84%EC%9D%B4%EC%9C%A0-%20%EB%B9%84%EC%9C%A8%EC%A1%B0%EC%A0%95.jpg?alt=media&token=46862700-5f3b-4029-b26a-8bab1170f991%EC%95%84%EC%9D%B4%EC%9C%A0','구미 비공식 아이유 서포터즈 아이유애나입니다.\n이번 아이유 구미 콘서트를 기념하여 팬분들께 음료를 제공하기로 결정하였습니다.?\n많은 이용 부탁드려요~ \n제공 매장은 에이바우트 커피 인동점입니다. \n많은 분들이 이용하실 수 있도록 인당 한도는 6000원으로 지정하였습니다.\n질서 있는 이용 부탁드려요~ ?','아이유애나',NULL,8),(3,0,'#e89800',0,7000,1739700507000,_binary '\0',NULL,'일일 1골드 B형 가보자!','알고리즘 스터디',NULL,1),(4,0,'#777777',0,10000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%9D%B4%EB%94%94%EC%95%BC%20%ED%8C%80.png?alt=media&token=8d998bf7-535d-4670-bbff-a953f673a9e7','힘들겠지만 오늘도 파이팅….','구미 취준생 모여라',NULL,10),(5,0,'#0c4da2',0,10000,1739700507000,_binary '\0',NULL,'삼성전자 개발팀을 위한 선결제 그룹입니다.','삼성전자 개발팀',NULL,8),(6,0,'#21c8f1',0,10000,1739700507000,_binary '\0','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%B5%EB%B0%A5.png?alt=media&token=55a847d5-7032-404b-ad36-9f6a8b5e389b','싸피 12기 파이팅','핵밥 먹자용',NULL,8),(7,0,'#21c8f1',0,20000,1739700507000,_binary '\0','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%82%B4%EB%A1%B1%EB%93%9C%EB%AF%B8%EB%87%BD.jpg?alt=media&token=dd832637-36ae-4831-89bf-794b2c9ed68c','머리를 자릅시다.','살롱드미뇽',NULL,5),(8,0,'#21c8f1',0,6000,1739700507000,_binary '\0',NULL,'책 읽고 밥 먹어요','독서동아리',NULL,3),(9,0,'#21c8f1',0,10000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%BD%94%EB%94%A9%20%EC%82%AC%EC%A7%84.jpg?alt=media&token=ca47efab-6448-424c-bde7-acab57701796','모각코 ㄱㄱㄱ 같이 코딩해요','코딩하는사람들',NULL,2),(10,0,'#21c8f1',0,5000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%88%AC%EC%8D%B8.png?alt=media&token=bc0289b4-5238-496b-af21-df276f9e7298','커피 마시고 좋은 하루 보내세요','투썸 같이먹어요',NULL,11),(11,0,'#21c8f1',0,25000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%8B%A0%EC%A7%B1%EA%B5%AC.png?alt=media&token=7b1159e1-415d-4a16-b40d-c3712d4c32fa','치맥 한번 하시죠','치킨 먹자용',NULL,4),(12,0,'#21c8f1',0,8500,1739700507000,_binary '\0','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%8B%A0%EC%A7%B1%EA%B5%AC.png?alt=media&token=b3d50446-00fe-455e-','구미 밥친구 모임','구미친구들',NULL,9),(13,0,'#4285F4',0,9000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EA%B5%90%EB%B0%98.png?alt=media&token=32ea8e3a-52cb-448c-836d-d76406f3e71f','교반 먹고 코딩해요!','코딩 마스터즈',NULL,2),(14,0,'#4285F4',0,10000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/220071_128201_620.jpg?alt=media&token=0d652583-1b83-4333-bda1-36afa46e89c3','열운 합시다!','헬스 메이트',NULL,2),(15,0,'#4285F4',0,13000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%98%A4%EB%91%A5%EC%9D%B4.png?alt=media&token=be3628bd-d6c9-4ef6-b8ac-18e7f0e7c424','모두 같이 먹어요~','함께 먹어요',NULL,2),(16,0,'#4285F4',0,5000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EA%B0%95%EC%95%84%EC%A7%80.jpg?alt=media&token=02792633-babd-4cc7-beee-3e4beeffcb32','커피 한잔으로 시작하세용','행복 나눔 카페',NULL,2),(17,0,'#4285F4',0,6000,1739700507000,_binary '','https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%8A%A4%ED%8F%B0%EC%A7%80%EB%B0%A5.webp2.jpg?alt=media&token=7af14adc-8b17-46f0-b902-7d174e9c4d97','힘든 하루 모두 힙냅시다!','모두 화이팅 하세요!',NULL,2);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
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
