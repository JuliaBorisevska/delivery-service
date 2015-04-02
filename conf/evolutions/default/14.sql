# --- !Ups

INSERT INTO `status` (`title`) VALUES ('Новый');
INSERT INTO `status` (`title`) VALUES ('Принят');
INSERT INTO `status` (`title`) VALUES ('В обработке');
INSERT INTO `status` (`title`) VALUES ('Готов к доставке');
INSERT INTO `status` (`title`) VALUES ('Доставка');
INSERT INTO `status` (`title`) VALUES ('Не может быть выполнен');
INSERT INTO `status` (`title`) VALUES ('Отменен');
INSERT INTO `status` (`title`) VALUES ('Закрыт');

# --- !Downs

DELETE FROM `status`;