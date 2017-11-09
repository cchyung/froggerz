# Create Users Table
CREATE TABLE `froggerz`.`Users` (
  `id` INT NOT NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `wins` INT NULL,
  PRIMARY KEY (`id`));
  
  # Create Rankings Table
  CREATE TABLE `froggerz`.`Rankings` (
  `user_id` INT NOT NULL,
  `position` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `position_UNIQUE` (`position` ASC),
  CONSTRAINT `userfk`
    FOREIGN KEY (`user_id`)
    REFERENCES `froggerz`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


