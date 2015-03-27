# --- !Ups

CREATE TABLE `delivery_service`.`order_history` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `order_id` int(11) UNSIGNED NOT NULL,
  `status_id` int(2) UNSIGNED NOT NULL,
  `user_id` int(2) UNSIGNED NOT NULL,
  `modification_date` datetime NOT NULL,
  `user_comment` varchar(100),
  PRIMARY KEY (`id`),
  INDEX `order_history_status_fk_idx` (`status_id` ASC) ,
  INDEX `order_history_user_fk_idx` (`user_id` ASC) ,
  INDEX `order_history_order_fk_idx` (`order_id` ASC) ,
  CONSTRAINT `order_history_status_fk`
    FOREIGN KEY (`status_id` )
    REFERENCES `delivery_service`.`status` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `order_history_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `delivery_service`.`user` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
	CONSTRAINT `order_history_order_fk`
    FOREIGN KEY (`order_id` )
    REFERENCES `delivery_service`.`order` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

# --- !Downs

drop table if exists `delivery_service`.`order_history`;
