# --- !Ups

CREATE TABLE `delivery_service`.`phone` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `country_code` int(3) unsigned NOT NULL,
  `operator_code` int(3) unsigned NOT NULL,
  `basic_number` int(7) unsigned NOT NULL,
  `phone_type_id` int(1) UNSIGNED NOT NULL,
  `user_comment` VARCHAR(100),
  `contact_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `phone_contact_fk_idx` (`contact_id` ASC) ,
  INDEX `phone_phone_type_fk_idx` (`phone_type_id` ASC) ,
  CONSTRAINT `phone_contact_fk`
    FOREIGN KEY (`contact_id` )
    REFERENCES `delivery_service`.`contact` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `phone_phone_type_fk`
    FOREIGN KEY (`phone_type_id` )
    REFERENCES `delivery_service`.`phone_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

# --- !Downs

drop table if exists `delivery_service`.`phone`;