CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(100) NOT NULL,
  `launch_date` datetime(6) NOT NULL,
  `price` decimal(65,2) NOT NULL,
  `title` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
);
