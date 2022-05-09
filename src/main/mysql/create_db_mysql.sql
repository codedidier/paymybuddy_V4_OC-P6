-- -----------------------------------------------------
-- Schema pay_my_buddy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pay_my_buddy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
    `pay_my_buddy`;

-- -----------------------------------------------------
-- Table `pay_my_buddy`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`user`
(
    `id`
                       INT
                                    NOT
                                        NULL
        AUTO_INCREMENT,
    `email`
                       VARCHAR(60)  NOT NULL,
    `last_name`        VARCHAR(50)  NOT NULL,
    `first_name`       VARCHAR(100) NOT NULL,
    `password`         VARCHAR(255) NOT NULL,
    `balance`          DECIMAL(15,3) NULL DEFAULT NULL,
    `phone`            VARCHAR(12)  NULL DEFAULT NULL,
    `address_prefix`   VARCHAR(5)   NULL DEFAULT NULL,
    `address_number`   VARCHAR(6)   NULL DEFAULT NULL,
    `address_street`   VARCHAR(50)  NULL DEFAULT NULL,
    `zip`              VARCHAR(10)  NULL DEFAULT NULL,
    `city`             VARCHAR(50)  NULL DEFAULT NULL,
    `account_creation` DATETIME     NULL DEFAULT NULL,
    `last_update`      DATETIME     NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE`(`email` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`contact`
( `user_id`INT NOT NULL, `contact_id`INT NOT NULL,
    PRIMARY KEY ( `user_id`, `contact_id`),
    INDEX `user_contact_fk`(`contact_id` ASC) VISIBLE,
    INDEX `user_user_fk`( `user_id` ASC) VISIBLE,
    CONSTRAINT `user_contact_fk`
        FOREIGN KEY ( `contact_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`),
    CONSTRAINT `user_user_fk`
        FOREIGN KEY(`user_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_cs_0900_as_cs;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`transaction`
( `id` INT NOT NULL AUTO_INCREMENT,
    `user_debtor_id` INT NOT NULL,
    `user_creditor_id` INT NOT NULL,
    `description` VARCHAR(100) NULL DEFAULT NULL,
    `amount` DECIMAL(15,3) NULL DEFAULT NULL,
    `date` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `user_transaction_fk`(`user_creditor_id` ASC) VISIBLE,
    INDEX `user_transaction_fk1` (`user_debtor_id` ASC) VISIBLE,
    CONSTRAINT `user_transaction_fk`
        FOREIGN KEY (`user_creditor_id`)
            REFERENCES `pay_my_buddy`.`user` (`id`),
    CONSTRAINT `user_transaction_fk1`
        FOREIGN KEY (`user_debtor_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Create user with id=1 to save charge
-- -----------------------------------------------------
INSERT INTO pay_my_buddy.user (id, email, last_name, first_name, password, balance, phone, address_prefix,
                               address_number, address_street, zip, city, account_creation, last_update)
VALUES (1, 'paymybuddy', 'XXX', 'XXX', 'XXXX', null, null, null, null, null, null, null, null, null);

-- -----------------------------------------------------
-- Create Test DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pay_my_buddy_test
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pay_my_buddy_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
    `pay_my_buddy_test`;

-- -----------------------------------------------------
-- Table `pay_my_buddy_test`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_test`.`user`
(`id` INT NOT NULL AUTO_INCREMENT,
    `email`			   VARCHAR(60)  NOT NULL,
    `last_name`        VARCHAR(50)  NOT NULL,
    `first_name`       VARCHAR(100) NOT NULL,
    `password`         VARCHAR(255) NOT NULL,
    `balance`          DECIMAL(15,3) NULL DEFAULT NULL,
    `phone`            VARCHAR(12)  NULL DEFAULT NULL,
    `address_prefix`   VARCHAR(5)   NULL DEFAULT NULL,
    `address_number`   VARCHAR(6)   NULL DEFAULT NULL,
    `address_street`   VARCHAR(50)  NULL DEFAULT NULL,
    `zip`              VARCHAR(10)  NULL DEFAULT NULL,
    `city`             VARCHAR(50)  NULL DEFAULT NULL,
    `account_creation` DATETIME     NULL DEFAULT NULL,
    `last_update`      DATETIME     NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE`( `email` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pay_my_buddy_test`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_test`.`contact`
(`user_id` INT NOT NULL,
    `contact_id` INT NOT NULL,
    PRIMARY KEY ( `user_id`,`contact_id`),
    INDEX `user_contact_fk`(`contact_id` ASC) VISIBLE,
    INDEX `user_user_fk`(`user_id` ASC) VISIBLE,
    CONSTRAINT `user_contact_fk`
        FOREIGN KEY (`contact_id` )
            REFERENCES `pay_my_buddy`.`user` (`id`),
    CONSTRAINT `user_user_fk`
        FOREIGN KEY( `user_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_cs_0900_as_cs;


-- -----------------------------------------------------
-- Table `pay_my_buddy_test`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy_test`.`transaction`
(`id` INT NOT NULL AUTO_INCREMENT,
    `user_debtor_id` INT NOT NULL,
    `user_creditor_id` INT NOT NULL,
    `description`	VARCHAR(100) NULL DEFAULT NULL,
    `amount` DECIMAL(15,3)       NULL DEFAULT NULL,
    `date`   DATETIME     NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `user_transaction_fk`(`user_creditor_id` ASC ) VISIBLE,
    INDEX `user_transaction_fk1`(`user_debtor_id` ASC) VISIBLE,
    CONSTRAINT `user_transaction_fk`
        FOREIGN KEY (`user_creditor_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`),
    CONSTRAINT `user_transaction_fk1`
        FOREIGN KEY(`user_debtor_id`)
            REFERENCES `pay_my_buddy`.`user`(`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Create users for admin and test
-- -----------------------------------------------------
CREATE
    USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON pay_my_buddy.* TO
    'root'@'localhost';

CREATE
    USER IF NOT EXISTS 'root_test'@'localhost' IDENTIFIED BY 'root_test';
GRANT ALL PRIVILEGES ON pay_my_buddy_test.* TO
    'root_test'@'localhost';
FLUSH
    PRIVILEGES;