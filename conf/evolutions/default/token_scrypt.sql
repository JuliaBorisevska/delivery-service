# --- !Ups

ALTER TABLE `delivery_service`.`user` ADD COLUMN `token` VARCHAR(50)
AFTER `email`;

# --- !Downs

ALTER TABLE `delivery_service`.`user` DROP COLUMN `token`;