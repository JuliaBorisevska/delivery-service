
# --- !Ups

CREATE TABLE `delivery_service`.`phone_type` (
  `id` int(1) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(15) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

# --- !Downs

drop table if exists `delivery_service`.`phone_type`;

