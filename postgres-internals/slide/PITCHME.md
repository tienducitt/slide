
# Postgresql internals

Duc Nguyen

---

## PostgreSQL Internals

<img src="postgres-internals/assets/PostgresQL_internals.png">

---

## Agenda

1.  Physical structure of database
2.  Table heap structure
3.  Tables & Indexes
4.  Query explain result: scan type

---

## Physical structure of databse

<img src="postgres-internals/assets/PostgresQL_physical_structure.png">

---

## Database directory

```sql
addresses.addresses=# SELECT datname, oid
                      FROM pg_database
                      WHERE datname = 'addresses.addresses';

       datname       |  oid
---------------------+-------
 addresses.addresses | 41024
(1 row)
```

<img src="postgres-internals/assets/database_base_folder.png">

---

## Table files

```sql
addresses.addresses=# SELECT relname, oid, relfilenode
                      FROM pg_class
                      WHERE relname = 'location_trees';

    relname     |  oid  | relfilenode
----------------+-------+-------------
 location_trees | 41058 |       41058
(1 row)
```

<img src="postgres-internals/assets/table_files.png">

---

## Indexes

```sql
addresses.addresses=# SELECT relname, oid, relfilenode
                      FROM pg_class
                      WHERE relname = 'location_trees_lazada_id';

         relname          |  oid  | relfilenode
--------------------------+-------+-------------
 location_trees_lazada_id | 41090 |       41090
(1 row)
```

<img src="postgres-internals/assets/index_files.png">

---

## Heap Table Structure

<img src="postgres-internals/assets/heap_table_file.png">
Page: a block of content (8KB default)

Line pointers: 4-byte number address to each tuple

+++

## Writing of a heap tuple. (1)

<img src="postgres-internals/assets/Tuple_before_insert.png">

+++

## Writing of a heap tuple. (2)

<img src="postgres-internals/assets/Tuple_after_insert.png">

---

## TID - Tuple identifier: (block, offset)

*   Block: block number of the page that contains the tuple
*   Offset: offset number of the line pointer that points to the tuples

--- 
## Table tuples - examples

<br>
<div class="left" style="float:left; font-style: italic">
    <ul style="list-style-type: none;">
        <li>1. INSERT Alice</li>
        <li>2. INSERT Bob</li>
        <li>3. INSERT Robert</li>
    </ul>
</div>
<div class="right">
    <table>
        <tr>
            <th>ctid</th>
            <th>id</th>
            <th>name</th>
        </tr>
        <tr>
            <td>(0,1)</td>
            <td>1</td>
            <td>Alice</td>
        </tr>
        <tr>
            <td>(0,2)</td>
            <td>2</td>
            <td>Bob</td>
        </tr>
        <tr>
            <td>(0,3)</td>
            <td>3</td>
            <td>Robert</td>
        </tr>
    </table>
</div>

+++

## Structure of a tuple

<img src="postgres-internals/assets/tuple_structure.png">
* t_xmin: txid of the transaction that inserted this tuple
* t_xmax: txid of the transaction that deleted this tuples. (t_xmax is 0 when tuple is ACTIVE)
* c_ctid: tuple id itself

+++

## MVCC - Multi-version Concurrency Control

Problem: someone reading data, while someone else is writing to it
Reader might see inconistent piece of data
MVCC: Allow reads & writes to happen concurrently

+++

## ACID - Isolation level

Read uncommitted
Read committed
Repeatable read
Seriablizable 

+++

## How can PostgresQL support MVCC?

+++

## MVCC

```sql
CREATE TABLE users
(id INTEGER
    CONSTRAINT location_trees_pkey PRIMARY KEY,
 name VARCHAR(100)
    CONSTRAINT users_name_unique UNIQUE (username)
);
```


+++

## MVCC - insert
<br>
<div class="left" style="float:left; font-style: italic">
    <ul style="list-style-type: none;">
        <li style="color: green;">1. INSERT Alice</li>
        <li style="color: green;">2. INSERT Bob</li>
    </ul>
</div>
<div class="right">
    <table>
        <tr>
            <th>xmin</th>
            <th>xmax</th>
            <th>ctid</th>
            <th>id</th>
            <th>name</th>
        </tr>
        <tr>
            <td>1</td>
            <td></td>
            <td>(0,1)</td>
            <td>1</td>
            <td>Alice</td>
        </tr>
        <tr>
            <td>2</td>
            <td></td>
            <td>(0,2)</td>
            <td>2</td>
            <td>Bob</td>
        </tr>
    </table>
</div>

+++

## MVCC - update

<br>
<div class="left" style="float:left; font-style: italic">
    <ul style="list-style-type: none;">
        <li>1. INSERT Alice</li>
        <li>2. INSERT Bob</li>
        <li style="color: green;">3. UPDATE Bob -> Robert</li>
    </ul>
</div>
<div class="right">
    <table>
        <tr>
            <th>xmin</th>
            <th>xmax</th>
            <th>ctid</th>
            <th>id</th>
            <th>name</th>
        </tr>
        <tr>
            <td>1</td>
            <td></td>
            <td>(0,1)</td>
            <td>1</td>
            <td>Alice</td>
        </tr>
        <tr>
            <td>2</td>
            <td>3</td>
            <td>(0,2)</td>
            <td>2</td>
            <td>Bob</td>
        </tr>
        <tr>
            <td>3</td>
            <td></td>
            <td>(0,3)</td>
            <td>3</td>
            <td>Robert</td>
        </tr>
    </table>
</div>

+++

## MVCC - delete

<br>
<div class="left" style="float:left; font-style: italic">
    <ul style="list-style-type: none;">
        <li>1. INSERT Alice</li>
        <li>2. INSERT Bob</li>
        <li>3. UPDATE Bob -> Robert</li>
        <li style="color: green;">4. DELETE Alice</li>
    </ul>
</div>
<div class="right">
    <table>
        <tr>
            <th>xmin</th>
            <th>xmax</th>
            <th>ctid</th>
            <th>id</th>
            <th>name</th>
        </tr>
        <tr>
            <td>1</td>
            <td>4</td>
            <td>(0,1)</td>
            <td>1</td>
            <td>Alice</td>
        </tr>
        <tr>
            <td>2</td>
            <td>3</td>
            <td>(0,2)</td>
            <td>2</td>
            <td>Bob</td>
        </tr>
        <tr>
            <td>3</td>
            <td></td>
            <td>(0,3)</td>
            <td>3</td>
            <td>Robert</td>
        </tr>
    </table>
</div>

+++

## MVCC - How repeatable read works?

+++

## Table bloat

Because each UPDATE creates a new tuple (and marks old tuples as deleted)
-> lots of UPDATEs will soon increase the tables's physical size

---

--- 
## Table
<img src="postgres-internals/assets/heap_table_illutrate.jpg">

---
## Indexes

Default indexes in PostgresQL is a **B-Tree**

+++ 

## B-Tree
<img src="postgres-internals/assets/b-tree.png">

--- 

## Indexes

-   **Primary indexes**: sorted by primary key and have pointer (tcip) points to the tuple 
-   Root node & inner nodes: contains keys & pointers to lower level nodes |
-   Leaf node contain keys and pointers to the heap (ctid) |

---

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Query explainer

*   Displays the execution plan that the PostgreSQL planner generates for the supplied statement |
*   How the table(s) will be scaned - by plain sequential scan / index scan/... |
*   What join algoritms will be used (in case of multiple tables are references) |
*   ... |

--- 
## Query explain - example

```sql
EXPLAIN ANALYSE SELECT * FROM users;
                 QUERY PLAN
---------------------------------------------
Seq Scan on location_trees  (cost=0.00..68.87 rows=1787 width=193) 
(actual time=0.012..7.674 rows=1787 loops=1)
 Planning time: 0.136 ms
 Execution time: 14.775 ms
(3 rows)
```

*Cost: cost to fetch 1 page (8 KB) from hard disk*

---
## Scan types
* Seq scan  |
* Bitmap index scan |
* Index scan    | 
* Index only scan   |

---

## Scan - Seq Scan (1)

```sql
EXPLAIN SELECT * FROM location_trees ;
                 QUERY PLAN
---------------------------------------------
 Seq Scan on location_trees  (cost=0.00..68.87 rows=1787 width=193)
(3 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## When a query can use index?

```sql
EXPLAIN SELECT * 
    FROM location_trees 
    WHERE parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f';
```
**We have parent_id index, would it use index to answer this query? And Why?**

---

## Selectivity

### = total unique value / total value

---
## Scan - Bitmap index scan

```sql
EXPLAIN 
SELECT * 
FROM location_trees 
WHERE parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f';
                 QUERY PLAN
---------------------------------------------
 Bitmap Heap Scan on location_trees  (cost=4.38..35.08 rows=13 width=193)
   Recheck Cond: (parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid)
   ->  Bitmap Index Scan on location_trees_parent_id_name_unique  (cost=0.00..4.38 rows=13 width=0)
         Index Cond: (parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Scan - Index scan
```sql
EXPLAIN 
SELECT * 
FROM location_trees 
WHERE parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f';
                 QUERY PLAN
---------------------------------------------
 Index Scan using location_trees_parent_id_name_unique on location_trees  
(cost=0.28..13.50 rows=13 width=193)
   Index Cond: (parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid)
(2 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Scan - Index only scan
```sql
EXPLAIN SELECT id FROM location_trees ORDER BY id LIMIT 1;
                 QUERY PLAN
---------------------------------------------
 Limit  (cost=0.28..0.43 rows=1 width=16)
   ->  Index Only Scan using location_trees_pkey on location_trees  
   (cost=0.28..275.08 rows=1787 width=16)
(2 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Scan with order by id
```sql
EXPLAIN ANALYSE SELECT * 
FROM uploaded_tracking_numbers 
WHERE tracking_list_id = 95 
    AND active = true 
ORDER BY id 
LIMIT 1;
                 QUERY PLAN
---------------------------------------------
 Limit  (cost=0.42..0.48 rows=1 width=48) (actual time=72.065..72.066 rows=1 loops=1)
   ->  Index Scan using uploaded_tracking_numbers_pkey on uploaded_tracking_numbers  (
       cost=0.42..36049.55 rows=643133 width=48) (actual time=72.064..72.064 rows=1 loops=1)
         Filter: (active AND (tracking_list_id = 95))
         Rows Removed by Filter: 257283
 Planning time: 0.129 ms
 Execution time: 72.089 ms
(6 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Scan without order
```sql
EXPLAIN ANALYSE SELECT * 
FROM uploaded_tracking_numbers 
WHERE tracking_list_id = 95 
AND active = true  LIMIT 1;
                 QUERY PLAN
---------------------------------------------
 Limit  (cost=0.00..0.03 rows=1 width=48) (actual time=34.305..34.305 rows=1 loops=1)
   ->  Seq Scan on uploaded_tracking_numbers  (cost=0.00..21056.86 rows=643133 width=48) 
   (actual time=34.304..34.304 rows=1 loops=1)
         Filter: (active AND (tracking_list_id = 95))
         Rows Removed by Filter: 256908
 Planning time: 0.087 ms
 Execution time: 34.331 ms
(6 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Scan with the same other with index
```sql
EXPLAIN ANALYSE SELECT * 
FROM uploaded_tracking_numbers 
WHERE tracking_list_id = 95 
    AND active = true 
ORDER BY tracking_list_id, active 
LIMIT 1;
                 QUERY PLAN
---------------------------------------------
 Limit  (cost=0.42..0.50 rows=1 width=48) (actual time=0.596..0.597 rows=1 loops=1)
   ->  Index Scan using uploaded_tracking_numbers_tracking_list_id_active_index 
   on uploaded_tracking_numbers  (cost=0.42..45247.79 rows=643133 width=48) 
   (actual time=0.595..0.595 rows=1 loops=1)
         Index Cond: ((tracking_list_id = 95) AND (active = true))
         Filter: active
 Planning time: 0.140 ms
 Execution time: 0.616 ms
(6 rows)
```

+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## What's next?

- MVCC (Multi-version concurrent control) |
- Isolation level |
- Locking |
- Shared Buffer |
- WAL (Write Ahead log) |

---

## Reference

*   PostgreSQL Documentation
*   [The Internals of PostgreSQL](https://github.com/gitpitch/gitpitch/wiki/Slide-Markdown)
*   [Grokking Vietnam](https://www.facebook.com/grokking.vietnam/)

--- 
# Thank you for listening