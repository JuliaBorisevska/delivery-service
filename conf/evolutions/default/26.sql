# --- !Ups

ALTER TABLE `delivery_service`.`contact` MODIFY `email` varchar(50);
INSERT INTO `contact` (`first_name`, `last_name`, `middle_name`, `birth_date`, `company_id`, `email`) VALUES ('Артем', 'Седельник', 'Викторович', '1970-12-15', 2, 'antonkw.mail@gmail.com');
INSERT INTO `contact` (`first_name`, `last_name`, `middle_name`, `birth_date`, `company_id`, `email`) VALUES ('Владимир', 'Ефимчик', 'Сергеевич', '1980-03-18', 2, 'antonkw.mail@gmail.com');
INSERT INTO `contact` (`first_name`, `last_name`, `middle_name`, `birth_date`, `company_id`, `email`) VALUES ('Александр', 'Захаров', 'Андреевич', '1968-02-15', 2, 'antonkw.mail@gmail.com');

# --- !Downs

DELETE FROM `contact` WHERE `id` IN (8,9,10);