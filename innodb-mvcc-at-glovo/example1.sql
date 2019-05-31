-- Example about concurrency & transaction isolation level

-- Base settup
drop schema mvcc;
create schema mvcc;
use mvcc;
CREATE TABLE `people` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO people (name) value('Alice');
SELECT * FROM people;

-- Window 2: repeatable read window
use mvcc;
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
START TRANSACTION;
SELECT * FROM people;

-- Window 3: uncommited read window
use mvcc;
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
START TRANSACTION;
SELECT * FROM people;

-- Window 4: repeatable read
use mvcc;
START TRANSACTION;
SELECT * FROM people;

-- Window 1: edit window
START TRANSACTION;
UPDATE people SET name = 'Bob' WHERE name = 'Alice';

-- Window 2:
SELECT * FROM people;  -- Expected result: val = Bob

-- Window 3: 
SELECT * FROM people;  -- Expected result: val = Alice

