# --- !Ups

ALTER TABLE `user` DROP FOREIGN KEY `user_contact_fk`;
ALTER TABLE `user` ADD CONSTRAINT `user_contact_fk` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;

# --- !Downs

ALTER TABLE `user` DROP FOREIGN KEY `user_contact_fk`;
ALTER TABLE `user` ADD CONSTRAINT `user_contact_fk` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON UPDATE NO ACTION ON DELETE RESTRICT;