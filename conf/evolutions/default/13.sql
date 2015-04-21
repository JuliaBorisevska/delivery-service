# --- !Ups

ALTER TABLE `status` CHANGE COLUMN `title` `title` VARCHAR(30) NOT NULL AFTER `id`;
ALTER TABLE `contact` CHANGE COLUMN `midle_name` `middle_name` VARCHAR(20) NULL DEFAULT NULL AFTER `last_name`;
	
# --- !Downs

ALTER TABLE `status` CHANGE COLUMN `title` `title` VARCHAR(20) NOT NULL AFTER `id`;
ALTER TABLE `contact` CHANGE COLUMN `middle_name` `midle_name` VARCHAR(20) NULL DEFAULT NULL AFTER `last_name`;