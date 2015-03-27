# --- !Ups

CREATE TABLE `delivery_service`.`status` (
  `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

# --- !Downs

drop table if exists `delivery_service`.`status`;