-- Example about concurrency & transaction isolation level

-- Base settup
CREATE TABLE `tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `val` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO tbl (val) value(1);

-- Window 2: repeatable read window
START TRANSACTION;
SELECT * FROM tbl;
-- Window 3: uncommited read window
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
START TRANSACTION;
SELECT * FROM tbl;

-- Window 1: edit window
START TRANSACTION;
UPDATE tbl SET val = 2 WHERE id = 1;

-- Window 2:
SELECT * FROM tbl;  -- Expected result: val = 1

-- Window 3: 
SELECT * FROM tbl;  -- Expected result: val = 2

