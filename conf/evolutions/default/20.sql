# --- !Ups

INSERT INTO `user_state` (`title`) VALUES ('active');
INSERT INTO `user_state` (`title`) VALUES ('inactive');

# --- !Downs

DELETE FROM `user_state`;