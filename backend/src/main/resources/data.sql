# DROP TABLE IF EXISTS `files`;
CREATE TABLE `files` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	`json` varchar(255),
	`created` DATETIME,
	PRIMARY KEY (`id`)
);

# DROP TABLE IF EXISTS `folders`;
CREATE TABLE `folders` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`files` INT,
	PRIMARY KEY (`id`)
);

ALTER TABLE `folders` ADD CONSTRAINT `folders_fk0` FOREIGN KEY (`files`) REFERENCES `files`(`id`);

