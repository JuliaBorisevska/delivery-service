# --- !Ups

ALTER TABLE `delivery_service`.`user` ADD COLUMN `user_state_id` int(2) UNSIGNED AFTER `role_id`,
  ADD CONSTRAINT `user_user_state_fk`
  FOREIGN KEY (`user_state_id` )
  REFERENCES `delivery_service`.`user_state` (`id` )
  ON DELETE RESTRICT
  ON UPDATE NO ACTION
, ADD INDEX `user_user_state_fk_idx` (`user_state_id` ASC);

# --- !Downs

ALTER TABLE `delivery_service`.`user` DROP INDEX `user_user_state_fk`;
ALTER TABLE `delivery_service`.`user` DROP COLUMN `user_state_id`;