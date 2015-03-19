CREATE DATABASE `delivery_service`
    CHARACTER SET 'utf8'
    COLLATE 'utf8_general_ci';	
	
CREATE TABLE `delivery_service`.`contact` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT UNIQUE,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `midle_name` varchar(20),
  `birth_date` date,
  `email` varchar(20),
  `town` varchar(20),
  `street` varchar(50),
  `house` int(3) unsigned,
  `flat` int(3) unsigned,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `delivery_service`.`phone_type` (
  `id` int(1) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(15) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

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

CREATE TABLE `delivery_service`.`status` (
  `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

CREATE TABLE `delivery_service`.`status_changes` (
  `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `current_status_id` int(2) UNSIGNED NOT NULL,
  `available_status_id` int(2) UNSIGNED NOT NULL,
 PRIMARY KEY (`id`),
 INDEX `status_changes_current_status_fk_idx` (`current_status_id` ASC) ,
 INDEX `status_changes_available_status_fk_idx` (`available_status_id` ASC) ,
 CONSTRAINT `service_user_current_status_fk`
    FOREIGN KEY (`current_status_id` )
    REFERENCES `delivery_service`.`status` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
 CONSTRAINT `service_user_available_status_fk`
    FOREIGN KEY (`available_status_id` )
    REFERENCES `delivery_service`.`status` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;		

CREATE TABLE `delivery_service`.`role` (
  `id` int(2) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `title` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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




