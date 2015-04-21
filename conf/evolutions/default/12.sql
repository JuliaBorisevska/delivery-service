# --- !Ups

UPDATE `role` SET `title`='ADMIN' WHERE  `id`=1;
UPDATE `role` SET `title`='SUPERVISOR' WHERE `id`=2;
INSERT INTO `role` (`title`) VALUES ('ORDER_MNG');
INSERT INTO `role` (`title`) VALUES ('PROCESS_MNG');
INSERT INTO `role` (`title`) VALUES ('DELIVERY_MNG');
INSERT INTO `company` (`title`) VALUES ('Ландыш');
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Евгений', 'Ветров', 'Анатольевич', '1970-10-15', 2);
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Артем', 'Андреев', 'Владимирович', '1981-05-18', 2);
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Александра', 'Душевина', 'Андреевна', '1968-02-15', 2);
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Ольга', 'Хомич', 'Петровна', '1985-07-19', 2);
INSERT INTO `contact` (`first_name`, `last_name`, `midle_name`, `birth_date`, `company_id`) VALUES ('Николай', 'Корнеев', 'Матвеевич', '1987-09-10', 2);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (3, 'admin', '$2a$10$TDUNEiDoU6HkAy0WEqScheKIHv8hRzDwMxfcKfycyc8gydgJK8hri', 1);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (4, 'ordermanager', '$2a$10$IM.0k2.3elJlLqA3WX2L7OenRhKWFtQQVf9lP71nKTFKpXySyvy3O', 3);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (5, 'supervisor', '$2a$10$Lp1fzli2VmDDJkoG3xup6ugiqqTGRf2HwWU74uxVExmoE01hsKhee', 2);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (6, 'processmanager', '$2a$10$3mdkwXqcpjjI5pazEnI42ulKy3f8/t3qxS4Zqa3zLKRZcz9ZeCY2.', 4);
INSERT INTO `user` (`contact_id`, `login`, `password`, `role_id`) VALUES (7, 'deliverymanager', '$2a$10$A.rE9wn2LyA2shUHa.kS1O1RlNgo24PIBP/vtKTIIPaoHmxl7M4zy', 5);

# --- !Downs

UPDATE `role` SET `title`='Администратор' WHERE  `id`=1;
UPDATE `role` SET `title`='Супервизор' WHERE `id`=2;
DELETE FROM `company` WHERE `id`=2;
DELETE FROM `contact` WHERE `id` IN (3,4,5,6,7);
DELETE FROM `role` WHERE `id` IN (3,4,5);
DELETE FROM `user` WHERE `id` IN (3,4,5,6,7);