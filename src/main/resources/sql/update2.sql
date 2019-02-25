CREATE TABLE `pre_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(128) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `private_key` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4