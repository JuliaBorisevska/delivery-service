# --- !Ups

ALTER TABLE `delivery_service`.`contact` ADD COLUMN `company_id` int(3) UNSIGNED NOT NULL AFTER `email`,
  ADD CONSTRAINT `contact_company_fk`
  FOREIGN KEY (`company_id` )
  REFERENCES `delivery_service`.`company` (`id` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION
, ADD INDEX `contact_company_fk_idx` (`company_id` ASC);

# --- !Downs

ALTER TABLE `delivery_service`.`contact` DROP INDEX `contact_company_fk`;
ALTER TABLE `delivery_service`.`contact` DROP COLUMN `company`;




