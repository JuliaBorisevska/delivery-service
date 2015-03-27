# --- !Ups

INSERT INTO `company` (`title`) VALUES ('Самоцветы');
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Александр', 'Петров', 'Сергеевич', '1980-01-09', 1);
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Дмитрий', 'Волков', 'Владимирович', '1975-12-12', 1);
INSERT INTO `role` (`title`) VALUES ('Администратор');
INSERT INTO `role` (`title`) VALUES ('Супервизор');
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (1, 'alex', '$2a$10$4C1eoxIIHqV/Bc.xC4lKauq887Z/QKbW6sOkXoqTvVYmUpY.x0ufu', 2);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (2, 'volkov', '$2a$10$l1IJ1OrMd4nCAj8/fkkcIOdmlGcdVLCmyOxwVLcnBITnlZEBgsr8K', 1);

# --- !Downs

DELETE FROM `company`;
DELETE FROM `contact`;
DELETE FROM `role`;
DELETE FROM `user`;