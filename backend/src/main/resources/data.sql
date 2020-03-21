CREATE TABLE `files` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` INT(255) NOT NULL,
	`json` varchar(255),
	`created` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `folders` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`files` INT NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `folders` ADD CONSTRAINT `folders_fk0` FOREIGN KEY (`files`) REFERENCES `files`(`id`);

