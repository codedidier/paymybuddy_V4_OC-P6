-- -----------------------------------------------------
-- Schema pay_my_buddy_v5
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pay_my_buddy_v5` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;

USE `pay_my_buddy_v5` ;

-- -----------------------------------------------------
-- Table `pay_my_buddy_v5`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_v5`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(60) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `first_name` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `balance` DECIMAL(15,2) NULL DEFAULT NULL,
  `phone` VARCHAR(12) NULL DEFAULT NULL,
  `address_number` VARCHAR(6) NULL DEFAULT NULL,
  `address_prefix` VARCHAR(30) NULL DEFAULT NULL,
  `address_street` VARCHAR(50) NULL DEFAULT NULL,
  `zip` VARCHAR(10) NULL DEFAULT NULL,
  `city` VARCHAR(50) NULL DEFAULT NULL,
  `account_creation` DATETIME NULL DEFAULT NULL,
  `last_update` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pay_my_buddy_v5`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_v5`.`contact` (
  `user_id` INT NOT NULL,
  `contact_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `contact_id`),
  INDEX `user_contact_fk` (`contact_id` ASC) VISIBLE,
  INDEX `user_user_fk` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_contact_fk`
    FOREIGN KEY (`contact_id`)
    REFERENCES `pay_my_buddy_v5`.`user` (`id`),
  CONSTRAINT `user_user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `pay_my_buddy_v5`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_cs_0900_as_cs;


-- -----------------------------------------------------
-- Table `pay_my_buddy_v5`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_v5`.`transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_debtor_id` INT NOT NULL,
  `user_creditor_id` INT NOT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  `amount` DECIMAL(15,2) NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  `charge` DECIMAL(15,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_transaction_fk` (`user_creditor_id` ASC) VISIBLE,
  INDEX `user_transaction_fk1` (`user_debtor_id` ASC) VISIBLE,
  CONSTRAINT `user_transaction_fk`
    FOREIGN KEY (`user_creditor_id`)
    REFERENCES `pay_my_buddy_v5`.`user` (`id`),
  CONSTRAINT `user_transaction_fk1`
    FOREIGN KEY (`user_debtor_id`)
    REFERENCES `pay_my_buddy_v5`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Create user with id=1 to save charge
-- -----------------------------------------------------
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                               address_number, address_street, zip, city, account_creation, last_update)
VALUES (1, 'paymybuddy', 'Stockage', 'des charges', 'XXXX', null, null, null, null, null, null, null, null, null);

-- -----------------------------------------------------
-- Create Test DB
-- -----------------------------------------------------
-- -----------------------------------------------------
-- table user
-- -----------------------------------------------------
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (2, 'mail2@mail.com', 'Nom2', 'Prenom2', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '5000.00',
        null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (3, 'mail3@mail.com', 'Nom3', 'Prenom3', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '20.00', null,
        null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (4, 'mail4@mail.com', 'Nom4', 'Prenom4', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '998.47', null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (5, 'mail5@mail.com', 'Nom5', 'Prenom5', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG', '1500.58',
        null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (6, 'mailvalid@mail.com', 'Nom3', 'Prenom6', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '500000', null, null, null, null, null, null, null, null);
INSERT INTO pay_my_buddy_v5.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                                    address_number, address_street, zip, city, account_creation, last_update)
VALUES (7, 'mailtest@mail.com', 'NomTest', 'PrenomTest', '$2a$10$qiVpUqQKa5Truu0j3QpRvuJVTpiTyWbyDGR.NvSG5hFBEH8N3hFaG',
        '999999999.99', null, null, null, null, null, null, null, null);

-- -----------------------------------------------------
-- table contact
-- -----------------------------------------------------
INSERT INTO `pay_my_buddy_v5`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '3');
INSERT INTO `pay_my_buddy_v5`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '4');
INSERT INTO `pay_my_buddy_v5`.`contact` (`user_id`, `contact_id`)
VALUES ('2', '5');


-- -----------------------------------------------------
-- table transaction
-- -----------------------------------------------------
INSERT INTO `pay_my_buddy_v5`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`,`charge`)
VALUES ('1', '2', '3', 'cadeau anniv', '20.00', '2022-05-02 11:59:00', 0.10);
INSERT INTO `pay_my_buddy_v5`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`,`charge`)
VALUES ('2', '2', '4', 'achat cafe', '50.00', '2022-05-02 12:59:00', 0.25);
INSERT INTO `pay_my_buddy_v5`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`,`charge`)
VALUES ('3', '6', '2', 'place cine', '10000.00', '2022-05-02 12:59:20', 50.00);
INSERT INTO `pay_my_buddy_v5`.`transaction` (`id`, `user_debtor_id`, `user_creditor_id`, `description`, `amount`,
                                               `date`,`charge`)
VALUES ('4', '2', '3', 'resto', '40.00', '2022-05-02 14:39:50', 0.20);
SET FOREIGN_KEY_CHECKS = 1;

-- -----------------------------------------------------
-- Create users for admin and test
-- -----------------------------------------------------
CREATE
    USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON pay_my_buddy_v5.* TO
    'root'@'localhost';

FLUSH
    PRIVILEGES;