-- -----------------------------------------------------
-- Clear all tables
-- -----------------------------------------------------
TRUNCATE `pay_my_buddy_test`.`user`;
TRUNCATE `pay_my_buddy_test`.`contact`;
TRUNCATE `pay_my_buddy_test`.`transaction`;

-- -----------------------------------------------------
-- populate DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- table user
-- -----------------------------------------------------
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (1, 'paymybuddy', 'XXX', 'XXX', 'XXXX', null, null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (2, 'mail2@mail.com', 'Nom2', 'Prenom2', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '5000',
        null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (3, 'mail3@mail.com', 'Nom3', 'Prenom3', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '20', null,
        null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (4, 'mail4@mail.com', 'Nom4', 'Prenom4', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '998.475', null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (5, 'mail5@mail.com', 'Nom5', 'Prenom5', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '1500.58',
        null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (8, 'mailvalid@mail.com', 'Nom3', 'Prenom6', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '500000', null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_test.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (7, 'mailtest@mail.com', 'NomTest', 'PrenomTest', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '999999999.999', null, null, null, null, null, null, null, null);

-- -----------------------------------------------------
-- table contact
-- -----------------------------------------------------
INSERT INTO `pay_my_buddy_test`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '3');
INSERT INTO `pay_my_buddy_test`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '4');
INSERT INTO `pay_my_buddy_test`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '5');


-- -----------------------------------------------------
-- table transaction
-- -----------------------------------------------------
INSERT INTO `pay_my_buddy_test`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`)
VALUES ('1', '2', '3', 'gazon', '20', '2022-05-08 11:59:00');
INSERT INTO `pay_my_buddy_test`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`)
VALUES ('2', '2', '4', 'tabac', '50', '2022-05-08 12:59:00');
INSERT INTO `pay_my_buddy_test`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`)
VALUES ('3', '8', '2', 'préparatif anniv', '10000', '2022-05-08 12:59:20');
INSERT INTO `pay_my_buddy_test`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`)
VALUES ('4', '2', '3', 'fleurs', '40', '2022-05-08 14:39:50');

