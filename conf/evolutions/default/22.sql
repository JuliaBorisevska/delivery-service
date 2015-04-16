# --- !Ups

UPDATE `user` SET `user_state_id`=1 WHERE `id` IN (1,2,3,4,5,6,7);
ALTER TABLE `user`
	CHANGE COLUMN `user_state_id` `user_state_id` INT(2) UNSIGNED NOT NULL AFTER `role_id`;

# --- !Downs

ALTER TABLE `user`
	CHANGE COLUMN `user_state_id` `user_state_id` INT(2) UNSIGNED NULL AFTER `role_id`;
UPDATE `user` SET `user_state_id`=null WHERE `id` IN (1,2,3,4,5,6,7);