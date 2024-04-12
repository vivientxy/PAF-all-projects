CREATE TABLE `rsvp`.`rsvp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `confirmation_date` DATE NULL,
  `comment` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

INSERT INTO `rsvp`.`rsvp` (`id`, `full_name`, `email`, `confirmation_date`, `comment`) VALUES ('3', 'Mary', 'mary@email.com', '2006-03-22', 'comment');