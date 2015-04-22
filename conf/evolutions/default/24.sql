# --- !Ups

ALTER TABLE `delivery_service`.`order` ADD COLUMN `process_mng_id` INT(2) UNSIGNED NULL AFTER `user_id`,
  ADD CONSTRAINT `order_process_mng_fk`
  FOREIGN KEY (`process_mng_id` )
  REFERENCES `delivery_service`.`user` (`id` )
  ON DELETE RESTRICT
  ON UPDATE NO ACTION
, ADD INDEX `order_process_mng_fk_idx` (`process_mng_id` ASC);
ALTER TABLE `delivery_service`.`order` ADD COLUMN `delivery_mng_id` INT(2) UNSIGNED NULL AFTER `process_mng_id`,
  ADD CONSTRAINT `order_delivery_mng_fk`
  FOREIGN KEY (`delivery_mng_id` )
  REFERENCES `delivery_service`.`user` (`id` )
  ON DELETE RESTRICT
  ON UPDATE NO ACTION
, ADD INDEX `order_delivery_mng_fk_idx` (`delivery_mng_id` ASC);

# --- !Downs

ALTER TABLE `order` DROP COLUMN `process_mng_id`, DROP FOREIGN KEY `order_process_mng_fk`;
ALTER TABLE `order` DROP COLUMN `delivery_mng_id`, DROP FOREIGN KEY `order_delivery_mng_fk`;