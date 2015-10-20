CREATE DATABASE  IF NOT EXISTS `forumdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `forumdb`;
-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: localhost    Database: forumdb
-- ------------------------------------------------------
-- Server version	5.6.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Follow`
--

DROP TABLE IF EXISTS `Follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Follow` (
  `followerID` int(11) unsigned NOT NULL,
  `followeeID` int(11) unsigned NOT NULL,
  KEY `followee_userID_idx` (`followeeID`),
  KEY `follower_userID_idx` (`followerID`),
  CONSTRAINT `followee_userID` FOREIGN KEY (`followeeID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `follower_userID` FOREIGN KEY (`followerID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Follow`
--

LOCK TABLES `Follow` WRITE;
/*!40000 ALTER TABLE `Follow` DISABLE KEYS */;
/*!40000 ALTER TABLE `Follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Forum`
--

DROP TABLE IF EXISTS `Forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Forum` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(70) NOT NULL,
  `short_name` varchar(60) NOT NULL,
  `userID` int(11) unsigned NOT NULL,
  PRIMARY KEY (`short_name`),
  UNIQUE KEY `short_name_UNIQUE` (`short_name`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `author_idx` (`userID`),
  CONSTRAINT `forum_authorID` FOREIGN KEY (`userID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Forum`
--

LOCK TABLES `Forum` WRITE;
/*!40000 ALTER TABLE `Forum` DISABLE KEYS */;
INSERT INTO `Forum` VALUES (2,'Forum I','forum1',5),(3,'Forum II','forum2',2),(4,'Форум Три','forum3',4),(1,'Forum With Sufficiently Large Name','forumwithsufficientlylargename',5);
/*!40000 ALTER TABLE `Forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Post`
--

DROP TABLE IF EXISTS `Post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Post` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `date` char(19) NOT NULL,
  `threadID` int(11) unsigned NOT NULL,
  `message` varchar(12500) NOT NULL,
  `authorID` int(11) unsigned NOT NULL,
  `forumID` int(11) unsigned NOT NULL COMMENT '\n',
  `parentID` int(11) unsigned DEFAULT NULL,
  `isApproved` tinyint(1) DEFAULT '0',
  `isHighlighted` tinyint(1) DEFAULT '0',
  `isEdited` tinyint(1) DEFAULT '0',
  `isSpam` tinyint(1) DEFAULT '0',
  `isDelited` tinyint(1) DEFAULT '0',
  `likes` int(11) unsigned DEFAULT '0',
  `dislikes` int(11) unsigned DEFAULT '0',
  `points` int(11) DEFAULT '0' COMMENT 'number of evaluations',
  `first_path` int(10) unsigned DEFAULT '0',
  `path` varchar(255) DEFAULT NULL,
  `count_of_children` int(11) unsigned DEFAULT '0',
  `delete_count` int(11) unsigned DEFAULT '0',
  `points_count` int(11) DEFAULT '0' COMMENT 'var for path',
  PRIMARY KEY (`id`),
  KEY `author_idx` (`authorID`),
  KEY `forum_idx` (`forumID`),
  KEY `thread_idx` (`threadID`),
  KEY `parent_idx` (`parentID`),
  CONSTRAINT `author` FOREIGN KEY (`authorID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `forum` FOREIGN KEY (`forumID`) REFERENCES `Forum` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `parent` FOREIGN KEY (`parentID`) REFERENCES `Post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `thread` FOREIGN KEY (`threadID`) REFERENCES `Thread` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Post`
--

LOCK TABLES `Post` WRITE;
/*!40000 ALTER TABLE `Post` DISABLE KEYS */;
INSERT INTO `Post` VALUES (1,'2014-01-01 00:00:01',3,'my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1',4,3,NULL,1,1,1,0,0,1,0,1,1,'',0,0,0),(2,'2014-01-01 00:02:01',3,'my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3my message 3',5,3,1,0,0,1,0,0,0,0,0,1,'0000001',0,0,0),(3,'2014-01-02 00:02:01',3,'my message 1',5,3,2,0,0,1,1,0,0,0,0,1,'00000010000001',0,0,0),(4,'2014-01-03 00:01:01',1,'my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1my message 1',3,4,NULL,1,0,0,0,0,0,0,0,4,'',0,0,0),(5,'2014-01-03 00:08:01',3,'my message 1',3,3,NULL,0,0,0,0,0,0,0,0,5,'',0,0,0);
/*!40000 ALTER TABLE `Post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Subscription`
--

DROP TABLE IF EXISTS `Subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Subscription` (
  `userID` int(11) unsigned NOT NULL,
  `threadID` int(11) unsigned NOT NULL,
  KEY `userID_idx` (`userID`),
  KEY `userID` (`userID`),
  KEY `subscribe_thread_idx` (`threadID`),
  CONSTRAINT `subscribe_threadID` FOREIGN KEY (`threadID`) REFERENCES `Thread` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `subscribe_userID` FOREIGN KEY (`userID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Subscription`
--

LOCK TABLES `Subscription` WRITE;
/*!40000 ALTER TABLE `Subscription` DISABLE KEYS */;
INSERT INTO `Subscription` VALUES (1,1),(1,4),(3,3),(4,1),(3,1);
/*!40000 ALTER TABLE `Subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Thread`
--

DROP TABLE IF EXISTS `Thread`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Thread` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `forumID` int(11) unsigned NOT NULL,
  `title` varchar(100) NOT NULL,
  `isClosed` tinyint(1) NOT NULL DEFAULT '0',
  `userID` int(11) unsigned NOT NULL,
  `date` char(19) NOT NULL,
  `message` varchar(13000) NOT NULL,
  `slug` varchar(100) NOT NULL,
  `isDelited` tinyint(1) DEFAULT '0',
  `likes` int(11) unsigned DEFAULT '0',
  `dislikes` int(11) unsigned DEFAULT '0',
  `points` int(11) DEFAULT '0',
  `posts` int(11) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `parent_forum_idx` (`forumID`),
  KEY `thread_aunhor_idx` (`userID`),
  CONSTRAINT `thread_authorID` FOREIGN KEY (`userID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `thread_forumID` FOREIGN KEY (`forumID`) REFERENCES `Forum` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Thread`
--

LOCK TABLES `Thread` WRITE;
/*!40000 ALTER TABLE `Thread` DISABLE KEYS */;
INSERT INTO `Thread` VALUES (1,4,'Thread With Sufficiently Large Title',1,4,'2014-01-01 00:00:01','hey hey hey hey!','Threadwithsufficientlylargetitle',1,0,0,0,0),(2,2,'Thread I',0,5,'2013-12-31 00:01:01','hey!','thread1',0,1,0,1,0),(3,3,'Thread II',0,2,'2013-12-30 00:01:01','hey hey!','newslug',0,0,0,0,0),(4,1,'Тред Три',0,5,'2013-12-29 00:01:01','hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! hey hey hey! ','thread3',0,0,0,0,0);
/*!40000 ALTER TABLE `Thread` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `about` varchar(5500) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `isAnonymous` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'example@mail.ru','user1','hello im user1','John',0),(2,'richard.nixon@example.com',NULL,NULL,NULL,1),(3,'example2@mail.ru','user2','Wowowowow','NewName',0),(4,'example3@mail.ru','user3','Wowowowow!!!','NewName2',0),(5,'example4@mail.ru','user4','hello im user4','Jim',0);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'forumdb'
--

--
-- Dumping routines for database 'forumdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-20 18:27:57
