-- Window 1
DROP schema test_long_transaction;
CREATE schema test_long_transaction;
USE test_long_transaction;

CREATE TABLE `tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `val1` int(11) NOT NULL DEFAULT '0',
  `val2` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_tbl_val2` (`val2`)
);

-- Insert procedure
DELIMITER $$
CREATE PROCEDURE InsertToTbl(IN NumRows INT)
         BEGIN
             DECLARE i INT;
             SET i = 1;
             START TRANSACTION;
                WHILE i <= NumRows DO
                    INSERT INTO tbl (`val1`, `val2`) VALUES (1, 1);
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
                    UPDATE tbl SET `val1` = `val1` + 1, `val2` = `val2` + 1;
                    SET i = i + 1;
                END WHILE;
             COMMIT;
         END$$
DELIMITER ;

-- Insert 100k rows
CALL InsertToTbl(100000);
SELECT SUM(val1) FROM tbl;
SHOW TABLE STATUS WHERE name = 'tbl' \G;


-- Window 2: repeatable read 
use test_long_transaction;
START TRANSACTION;
SELECT SUM(val1) FROM tbl;

-- Window 3: committed read 
use test_long_transaction;
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT SUM(val1) FROM tbl;

-- Window 1:
START TRANSACTION;
CALL UpdateTbl(50);

-- Window 1: select count(*) should take a long time 
SELECT SUM(val1) FROM tbl;
-- Window 2: select count(*) is faster
SELECT SUM(val1) FROM tbl; 

-- Window 3: show the table size again, this time both table & index size is so big 
SHOW TABLE STATUS WHERE name = 'tbl' \G;

-- Window 4: using index / not using index performance, result: using index is slower because the index tree is poluted with many row versions
SELECT SUM(val2) FROM tbl USE INDEX (`idx_tbl_val2`) ;
SELECT SUM(val2) FROM tbl IGNORE INDEX (`idx_tbl_val2`) ;
