# --- !Ups

INSERT INTO `order_history` (`order_id`, `status_id`, `user_id`, `modification_date`) VALUES (3, 2, 5, '2015-03-21 14:26:10');
INSERT INTO `order_history` (`order_id`, `status_id`, `user_id`, `modification_date`) VALUES (3, 3, 5, '2015-03-25 15:36:13');
INSERT INTO `order_history` (`order_id`, `status_id`, `user_id`, `modification_date`) VALUES (3, 4, 5, '2015-03-25 20:40:13');
UPDATE `order` SET `status_id`=4 WHERE  `id`=3

# --- !Downs

DELETE FROM `order_history`;
UPDATE `order` SET `status_id`=1 WHERE  `id`=3