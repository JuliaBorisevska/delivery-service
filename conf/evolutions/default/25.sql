# --- !Ups

UPDATE `order` SET `process_mng_id`=6;
UPDATE `order` SET `delivery_mng_id`=7;
ALTER TABLE `order`
	CHANGE COLUMN `process_mng_id` `process_mng_id` INT(2) UNSIGNED NOT NULL AFTER `user_id`;
ALTER TABLE `order`
	CHANGE COLUMN `delivery_mng_id` `delivery_mng_id` INT(2) UNSIGNED NOT NULL AFTER `process_mng_id`;

# --- !Downs

ALTER TABLE `order`
	CHANGE COLUMN `process_mng_id` `process_mng_id` INT(2) UNSIGNED NULL AFTER `user_id`;
ALTER TABLE `order`
	CHANGE COLUMN `delivery_mng_id` `delivery_mng_id` INT(2) UNSIGNED NULL AFTER `process_mng_id`;
UPDATE `order` SET `process_mng_id`=null;
UPDATE `order` SET `delivery_mng_id`=null;