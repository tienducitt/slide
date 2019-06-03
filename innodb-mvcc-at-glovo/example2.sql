-- Example about:
--  + how InnoDB handle update in cluster & secondary index
--  + the problem of long transaction with repeatable read

-- Create table
CREATE TABLE `tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);

-- Insert procedure
DELIMITER $$
CREATE PROCEDURE InsertToTbl(IN NumRows INT)
         BEGIN
             DECLARE i INT;
             SET i = 1;
             START TRANSACTION;
             WHILE i <= NumRows DO
                 INSERT INTO tbl (`value`) VALUES (1);
                 SET i = i + 1;
             END WHILE;
             COMMIT;
         END$$
DELIMITER ;

-- Insert 100.000 records
CALL InsertToTbl(100000);

-- Select query
SELECT SUM(`value`) FROM tbl;

-- Update procedure
DELIMITER $$
CREATE PROCEDURE UpdateTbl(IN nTime INT)
         BEGIN
             DECLARE i INT;
             SET i = 1;
             START TRANSACTION;
             WHILE i <= nTime DO
                 UPDATE tbl SET `value` = `value` + 1;
                 SET i = i + 1;
             END WHILE;
             COMMIT;
         END$$
DELIMITER ;

-- Update 100 times
CALL UpdateTbl(50);


-- Set transaction isolation level
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Buffer
select page_type,count(*) from information_schema.INNODB_BUFFER_PAGE group by page_type;