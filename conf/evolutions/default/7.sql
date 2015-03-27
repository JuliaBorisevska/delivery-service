# --- !Ups

CREATE TABLE `delivery_service`.`order` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `status_id` int(2) UNSIGNED NOT NULL,
  `user_id` int(2) UNSIGNED NOT NULL,
  `customer_contact_id` int(11) unsigned NOT NULL,
  `recipient_contact_id` int(11) unsigned NOT NULL,
  `order_date` datetime NOT NULL,
  `description` varchar(100) NOT NULL,
  `total_price` double unsigned NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `order_status_fk_idx` (`status_id` ASC) ,
  INDEX `order_user_fk_idx` (`user_id` ASC) ,
  INDEX `order_customer_contact_fk_idx` (`customer_contact_id` ASC) ,
  INDEX `order_recipient_contact_fk_idx` (`recipient_contact_id` ASC) ,
  CONSTRAINT `order_status_fk`
    FOREIGN KEY (`status_id` )
    REFERENCES `delivery_service`.`status` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `order_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `delivery_service`.`user` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
	CONSTRAINT `order_customer_contact_fk`
    FOREIGN KEY (`customer_contact_id` )
    REFERENCES `delivery_service`.`contact` (`id` )
    ON DELETE CASCADE 
    ON UPDATE NO ACTION,
  CONSTRAINT `order_recipient_contact_fk`
    FOREIGN KEY (`recipient_contact_id` )
    REFERENCES `delivery_service`.`contact` (`id` )
    ON DELETE CASCADE 
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

drop table if exists `delivery_service`.`order`;