# --- !Ups

INSERT INTO `phone_type` (`title`) VALUES ('Мобильный');
INSERT INTO `phone_type` (`title`) VALUES ('Домашний');

# --- !Downs

DELETE FROM `phone_type`;