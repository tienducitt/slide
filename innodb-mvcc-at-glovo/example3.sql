-- Create table
CREATE TABLE `tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `val` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `tbl_val` (`val`)
);

-- Insert procedure
DELIMITER $$
CREATE PROCEDURE InsertToTbl(IN NumRows INT)
         BEGIN
             DECLARE i INT;
             SET i = 1;
             START TRANSACTION;
             WHILE i <= NumRows DO
                 INSERT INTO tbl (`val`) VALUES (1);
                 SET i = i + 1;
             END WHILE;
             COMMIT;
         END$$
DELIMITER ;

-- Update procedure
DELIMITER $$
CREATE PROCEDURE UpdateTbl(IN nTime INT)
         BEGIN
             DECLARE i INT;
             SET i = 1;
             START TRANSACTION;
             WHILE i <= nTime DO
                 UPDATE tbl SET `val` = `val` + 1;
                 SET i = i + 1;
             END WHILE;
             COMMIT;
         END$$
DELIMITER ;

-- Insert 100k rows
CALL InsertToTbl(100000);

-- Window 1: repeatable read 
START TRANSACTION;
SELECT COUNT(*) FROM tbl;
-- Window 2: committed read 
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT COUNT(*) FROM tbl;
-- Window 3: show the table size, update 100k records 100 times
SHOW TABLE STATUS WHERE name = 'tbl' \G;
START TRANSACTION;
CALL UpdateTbl(50);

-- Window 1: select count(*) should take a long time 
SELECT COUNT(*) FROM tbl;  
-- Window 2: select count(*) is faster
SELECT COUNT(*) FROM tbl;  

-- Window 3: show the table size again, this time both table & index size is so big 
SHOW TABLE STATUS WHERE name = 'tbl';

-- Window 4: using index / not using index performance 
SELECT count(*) FROM tbl USE INDEX (`tbl_val`) ;
SELECT count(*) FROM tbl IGNORE INDEX (`tbl_val`) ;
