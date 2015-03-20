# --- First database schema

# --- !Ups

CREATE TABLE `delivery_service`.`contact` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT UNIQUE,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `midle_name` varchar(20),
  `birth_date` date,
  `email` varchar(20),
  `town` varchar(20),
  `street` varchar(50),
  `house` int(3) unsigned,
  `flat` int(3) unsigned,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

drop table if exists `delivery_service`.`contact`;

