# --- !Ups

INSERT INTO `order` (`status_id`, `user_id`, `customer_contact_id`, `recipient_contact_id`, `order_date`, `description`, `total_price`) VALUES (1, 4, 4, 5, '2015-01-10 18:19:30', 'букет свадебный №1234', 125000);
INSERT INTO `order` (`status_id`, `user_id`, `customer_contact_id`, `recipient_contact_id`, `order_date`, `description`, `total_price`) VALUES (1, 4, 4, 5, '2015-01-30 19:10:42', 'букет №234', 96000);
INSERT INTO `order` (`status_id`, `user_id`, `customer_contact_id`, `recipient_contact_id`, `order_date`, `description`, `total_price`) VALUES (1, 4, 4, 5, '2015-03-04 13:19:42', '1 роза + ленточка', 35000);
INSERT INTO `order` (`status_id`, `user_id`, `customer_contact_id`, `recipient_contact_id`, `order_date`, `description`, `total_price`) VALUES (1, 4, 4, 5, '2015-04-02 11:19:42', '5 красных роз', 75000);

# --- !Downs

DELETE FROM `order`;