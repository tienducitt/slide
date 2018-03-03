#HSSIDE

# Postgresql internals

Duc Nguyen

#HSLIDE

## PostgreSQL Internals

<img src="postgres-internals/assets/PostgresQL_internals.png">

#HSLIDE

## Agenda

1.  Physical structure of database
2.  Table heap and Tuple structure
3.  MVCC (Multi-version concurrent control)
4.  Tables & Indexes
5.  Query explain: type of scan method
6.  Index only scan

#HSLIDE

## Physical structure of databse

<img src="postgres-internals/assets/PostgresQL_physical_structure.png">

#HSLIDE

## Database

```sql
addresses.addresses=# SELECT datname, oid FROM pg_database WHERE datname = 'addresses.addresses';
       datname       |  oid
---------------------+-------
 addresses.addresses | 41024
(1 row)
```

<img src="postgres-internals/assets/database_base_folder.png">

#HSLIDE

## Table

```sql
addresses.addresses=# SELECT relname, oid, relfilenode FROM pg_class WHERE relname = 'location_trees';
    relname     |  oid  | relfilenode
----------------+-------+-------------
 location_trees | 41058 |       41058
(1 row)
```

<img src="postgres-internals/assets/table_files.png>

#HSLIDE

## Index

```sql
addresses.addresses=# SELECT relname, oid, relfilenode FROM pg_class WHERE relname = 'location_trees_lazada_id';
         relname          |  oid  | relfilenode
--------------------------+-------+-------------
 location_trees_lazada_id | 41090 |       41090
(1 row)
```

<img src="postgres-internals/assets/index_files.png>

#HSlIDE

## Heap Table Structure

<img src="postgres-internals/assets/heap_table_file.png">
Page: a block of content (8KB default)
Line pointers: 4-byte number address to each tuple

#HSlIDE

## B-Tree Index

*   <Image>: what is a B-tree

#HSlIDE

## B-Tree index in postgreSQL

*   Root node & inner nodes: contains keys & pointers to lower level nodes
*   Leaf node contain keys and pointers to the heap (ctid)
*   When table has new tuples, new tuple is added to index tree

#HSlIDE

## MVCC (Multi version concurrent control)

Problem: someone reading data, while someone else is writing to it
Reader might see inconistent piece of data
MVCC: Allow reads & writes to happen concurrently

#HSlIDE

## MVCC - table

*   Example of how postgres insert, update, delete a record
    -> xmin, xmax

#HSlIDE

## MVCC - insert

#HSlIDE

## MVCC - update

#HSlIDE

## MVCC - delete

#HSlIDE

## Table bloat

Because each UPDATE creates a new tuple (and marks old tuples as deleted)
-> lots of UPDATEs will soon increase the tables's physical size

#HSlIDE

## Query explainer

*   displays the execution plan that the PostgreSQL planner generates for the supplied statement
*   how the table(s) will be scaned - by plain sequential scan / index scan - what join algoritms will be used (in case of multiple tables are references)

```sql
EXPLAIN ANALYSE SELECT * FROM users;
```

#HSLIDE

## Estimate Cost

//TODO: add the table from Postgres explaining EXPLAIN

#HSlIDE

## Scan - Seq Scan

#HSlIDE

## Scan - Bitmap index scan

#HSLIDE

## Scan - Index scan

#HSLIDE

## Scan - Index only scan

#HSLIDE

## Joins - Nested Loop

#HSLIDE

## Joins - Merge

#HSLIDE

## Joins - Hash join

#HSLIDE

## Reference

*   PostgreSQL Documentation
*   The Internals of PostgreSQL
*   Grokking: PostgreSQL Internals workshop
