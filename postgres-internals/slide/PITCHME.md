# Postgresql internals

Duc Nguyen

---

## PostgreSQL Internals

<img src="postgres-internals/assets/PostgresQL_internals.png">

---

## Agenda

1.  Physical structure of database
2.  Table heap and Tuple structure
3.  MVCC (Multi-version concurrent control)
4.  Tables & Indexes
5.  Query explain: type of scan method
6.  Index only scan

---

## Physical structure of databse

<img src="postgres-internals/assets/PostgresQL_physical_structure.png">

---

## Database

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

## Index

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

---

## TID - Tuple identifier: (block, offset)

Used to identify a tuple within a table.

*   Block: block number of the page that contains the tuple
*   Offset: offset number of the line pointer that points to the tuples
    Example: (0, 2)

---

## Writing of a heap tuple. (1)

<img src="postgres-internals/assets/Tuple_before_insert.png">

---

## Writing of a heap tuple. (2)

<img src="postgres-internals/assets/Tuple_after_insert.png">

---

## Structure of a tuple

<img src="postgres-internals/assets/tuple_structure.png">
* t_xmin: txid of the transaction that inserted this tuple
* t_xmax: txid of the transaction that deleted this tuples. (t_xmax is 0 when tuple is ACTIVE)
* c_ctid: tuple id itself

---

## MVCC - Tables & indexes

-   Record data is stored in tuple |
-   Primary indexes sorted by primary key and have pointer (tcip) points to the tuple |
-   Indexes in PostgresQL is a b-tree |
-   Root node & inner nodes: contains keys & pointers to lower level nodes |
-   Leaf node contain keys and pointers to the heap (ctid) |
-   When table has new tuples, new tuple is added to index tree |

---

## MVCC - Tables & Indexes visualize

I need a picture here to illutrate the heap table, primary key, secondary key

---

## Overral of Sequential scan and index scan

---

## MVCC - Multi-version Concurrency Control

Problem: someone reading data, while someone else is writing to it
Reader might see inconistent piece of data
MVCC: Allow reads & writes to happen concurrently

---
## ACID - Isolation level
Read uncommitted
Read committed
Repeatable read
Seriablizable 

---

## How can PostgresQL support MVCC?

---

## MVCC

```sql
CREATE TABLE users
(id INTEGER
    CONSTRAINT location_trees_pkey PRIMARY KEY,
 name VARCHAR(100)
    CONSTRAINT users_name_unique UNIQUE (username)
);
```


---

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

---

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

---

## MVCC - delete

<br>
<div class="left" style="float:left; font-style: italic">
    <ul style="list-style-type: none;">
        <li>1. INSERT Alice</li>
        <li>2. INSERT Bob</li>
        <li>3. UPDATE Bob -> Robert</li>
        <li style->4. DELETE Alice</li>
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

---

## MVCC - How repeatable read works?

---

## Table bloat

Because each UPDATE creates a new tuple (and marks old tuples as deleted)
-> lots of UPDATEs will soon increase the tables's physical size

---

## Query explainer

*   displays the execution plan that the PostgreSQL planner generates for the supplied statement
*   how the table(s) will be scaned - by plain sequential scan / index scan - what join algoritms will be used (in case of multiple tables are references)

```sql
EXPLAIN ANALYSE SELECT * FROM users;
```

---

## Estimate Cost

//TODO: add the table from Postgres explaining EXPLAIN

---

## Scan - Seq Scan

---

## Scan - Bitmap index scan

---

## Scan - Index scan

---

## Scan - Index only scan

---

## Joins - Nested Loop

---

## Joins - Merge

---

## Joins - Hash join

---

## Index only scan

---

## Reference

*   PostgreSQL Documentation
*   The Internals of PostgreSQL
*   Grokking: PostgreSQL Internals workshop
