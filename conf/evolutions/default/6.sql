# --- !Ups

CREATE TABLE `delivery_service`.`user` (
  `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `contact_id` int(11) unsigned NOT NULL,
  `login` varchar(30) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int(2) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_contact_fk_idx` (`contact_id` ASC) ,
  INDEX `user_role_fk_idx` (`role_id` ASC) ,
  CONSTRAINT `user_contact_fk`
    FOREIGN KEY (`contact_id` )
    REFERENCES `delivery_service`.`contact` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `user_role_fk`
    FOREIGN KEY (`role_id` )
    REFERENCES `delivery_service`.`role` (`id` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

drop table if exists `delivery_service`.`user`;