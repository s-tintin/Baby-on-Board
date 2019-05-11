-- Version 1.0
-- MySQL

-- Create database
CREATE DATABASE baby_db;

-- Using database
USE baby_db;

-- Customer table
CREATE TABLE `customer` (
	`id` int NOT NULL AUTO_INCREMENT,
    `user_name` char(25) NOT NULL UNIQUE,
    `password` char(20) NOT NULL,
    `full_name` char(100) DEFAULT NULL,
    `email` char(100) DEFAULT NULL,
    `phone` char(20) DEFAULT NULL,
    PRIMARY KEY (id)
);

-- Dumping data for customer table
INSERT INTO `customer` (`user_name`,`password`,`full_name`,`email`,`phone`) VALUES ("admin","admin","Administrator","ooadgroup5utd@gmail.com","+1-468-555-1234");

-- Age group table
CREATE TABLE `age_group` (
	`id` int NOT NULL,
	`name` char(25) NOT NULL UNIQUE,
	`description` char(100) NOT NULL,
 	PRIMARY KEY (id)
);

-- Dumping data for age group table
INSERT INTO `age_group` (`id`,`name`,`description`) VALUES (1, "New Born", "0-2 Months"), (2, "Infant", "2 Months - 1 Year"), (3, "Toddler", "1-5 Years");

-- Change auto increment for age group table
ALTER TABLE `age_group` AUTO_INCREMENT=4;

-- Subscription table
CREATE TABLE `subscription` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` char(50) NOT NULL,
	`age_group` int NOT NULL,
	`created_by` int NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_subscription_created_by FOREIGN KEY (`created_by`) REFERENCES `customer`(`id`),
	CONSTRAINT fk_subscription_age_group FOREIGN KEY (`age_group`) REFERENCES `age_group`(`id`)
);

-- Dumping data for subscription table
INSERT INTO `subscription` (`id`,`name`,`age_group`,`created_by`) VALUES (1,"Premium",1,1), (2,"Premium",2,1), (3,"Premium",3,1), 
(4,"Standard",1,1), (5,"Standard",2,1), (6,"Standard",3,1), (7,"Economical",1,1), (8,"Economical",2,1), (9,"Economical",3,1);

-- Change auto increment for subscription table
ALTER TABLE `age_group` AUTO_INCREMENT=10;

-- Product table
CREATE TABLE `product` (
	`id` int NOT NULL,
	`name` char(100) NOT NULL,
	`brand` char(100) NOT NULL,
	`category` char(100) NOT NULL,
	`quantity` char(20) NOT NULL,
	`price` float(5,2) NOT NULL,
	PRIMARY KEY (id)
);

-- Dumping data for product table
INSERT INTO `product` (`id`,`name`,`brand`,`category`,`quantity`,`price`) VALUES (1,"Cerelac Stage-1","Nestle","Instant Cereal","2lbs",17.50),
(2,"Baby Cereal","Gerber","Instant Cereal","2lbs",12.00),
(3,"Creamy Porridge","Hipp","Instant Cereal","2lbs",9.50),
(4, "Snug N Dry","Huggies","Diapers","20nos",25.00),
(5, "Swaddlers","Pampers","Diapers","20nos",21.50),
(6, "Disposable","Luvs","Diapers","20nos",15.50);

-- Product age group mapping table
CREATE TABLE `product_age_group_mapping` (
	`id` int NOT NULL AUTO_INCREMENT,
	`product_id` int NOT NULL,
	`age_group_id` int NOT NULL,
	PRIMARY KEY (id),
	KEY(`product_id`,`age_group_id`),
	CONSTRAINT fk_product_age_group_mapping_product_id FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
	CONSTRAINT fk_product_age_group_mapping_age_group_id FOREIGN KEY (`age_group_id`) REFERENCES `age_group`(`id`)
);

-- Dumping data for product age group mapping table
INSERT INTO `product_age_group_mapping` (`product_id`,`age_group_id`) VALUES (1,2), (1,3), (2,2), (2,3), (3,2), (3,3), (4,1), (4,2),
(5,1), (5,2), (6,1), (6,2);


-- Subscription product mapping table
CREATE TABLE `subscription_product_mapping` (
	`id` int NOT NULL AUTO_INCREMENT,
	`subscription_id` int NOT NULL,
	`product_id` int NOT NULL,
	`quantity` int NOT NULL,
	PRIMARY KEY (id),
	KEY(`subscription_id`,`product_id`),
	CONSTRAINT fk_subscription_product_mapping_subscription_id FOREIGN KEY (`subscription_id`) REFERENCES `subscription`(`id`),
	CONSTRAINT fk_subscription_product_mapping_product_id FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
);

-- Dumping data for subscription product mapping table
INSERT INTO `subscription_product_mapping` (`subscription_id`,`product_id`,`quantity`) VALUES (5,2,2), (6,2,2), (2,1,2), (3,1,2), (8,3,2), (9,3,2),
(4,5,4), (5,5,4), (1,4,4), (2,4,4), (7,6,4), (8,6,4);

-- Customer subscription mapping table
CREATE TABLE `customer_subscription_mapping` (
	`id` int NOT NULL AUTO_INCREMENT,
	`customer_id` int NOT NULL,
	`subscription_id` int NOT NULL,
	`frequency` char(25) NOT NULL,
	`quantity` int NOT NULL,
	`duration` int NOT NULL,
	`start_date` timestamp NOT NULL,
	`status` boolean NOT NULL DEFAULT 1, 
	PRIMARY KEY (id),
	CONSTRAINT fk_customer_subscription_mapping_customer_id FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`),
	CONSTRAINT fk_customer_subscription_mapping_subscription_id FOREIGN KEY (`subscription_id`) REFERENCES `subscription`(`id`)
);

-- Transaction table
CREATE TABLE `transaction` (
	`id` int NOT NULL AUTO_INCREMENT,
	`transaction_date` timestamp NOT NULL,
	`payment_mode` char(100) NOT NULL,
	`address` char(200) NOT NULL,
	`amount` float(10,2) NOT NULL,
	`card_name` char(50),
	`card_no` char(20),
	`card_expiration` char(7),
	`card_cvv` char(3),
	PRIMARY KEY(id)
);

-- Transaction Customer Subscription mapping table
CREATE TABLE `transaction_customer_subscription_mapping` (
	`id` int NOT NULL AUTO_INCREMENT,
	`transaction_id` int NOT NULL,
	`customer_subscription_id` int NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_transaction_customer_subscription_mapping_cust_sub_id FOREIGN KEY (`customer_subscription_id`) REFERENCES `customer_subscription_mapping`(`id`),
	CONSTRAINT fk_transaction_customer_subscription_mapping_transaction_id FOREIGN KEY (`transaction_id`) REFERENCES `transaction`(`id`)
);

-- Cart table
CREATE TABLE `cart` (
	`id` int NOT NULL AUTO_INCREMENT,
	`subscription_id` int NOT NULL,
	`customer_id` int NOT NULL,
	`price` float(5,2) NOT NULL,
	`quantity` int NOT NULL,
	UNIQUE KEY `uniq_cart_sub_id_cust_id` (`subscription_id`,`customer_id`),
	PRIMARY KEY(id),
	CONSTRAINT fk_cart_subscription_id FOREIGN KEY (`subscription_id`) REFERENCES `subscription`(`id`),
	CONSTRAINT fk_cart_customer_id FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`)
);

