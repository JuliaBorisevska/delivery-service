# --- !Ups

CREATE TABLE `delivery_service`.`company` (
  `id` int(3) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(30) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

drop table if exists `delivery_service`.`company`;