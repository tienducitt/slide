
# POSTGRESQL Scan type IN DEPTH

Duc Nguyen

---
## Agenda

1. Background knowledege
2. Explainer
3. Scan type

---
## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---

## Problem
### Input
- Heap table
- Index (optional)
- Query
- Cost constants: seq_page_cost, rand_page_cost, cpu_tuple_cost,...

### Output
Find the fastest way to solve the query!

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
* Index scan    | 
* Bitmap index scan |
* Index only scan   |

---
## Sequence scan 
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
## Scan - Bitmap index scan (2)
```sql
EXPLAIN 
SELECT * 
FROM location_trees 
WHERE parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f' OR parent_id='0548adeb-4091-4a49-a8eb-c1f7e9b383e9';
                QUERY PLAN
---------------------------------------------
 Bitmap Heap Scan on location_trees  (cost=8.76..53.12 rows=26 width=192)
   Recheck Cond: ((parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid) 
   OR (parent_id = '0548adeb-4091-4a49-a8eb-c1f7e9b383e9'::uuid))
   ->  BitmapOr  (cost=8.76..8.76 rows=26 width=0)
         ->  Bitmap Index Scan on location_trees_parent_id_name_unique  (cost=0.00..4.38 rows=13 width=0)
               Index Cond: (parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid)
         ->  Bitmap Index Scan on location_trees_parent_id_name_unique  (cost=0.00..4.38 rows=13 width=0)
               Index Cond: (parent_id = '0548adeb-4091-4a49-a8eb-c1f7e9b383e9'::uuid)
```
+++

## Tables & Indexes visualize
<img src="postgres-internals/assets/table_and_index.png">

---
## Scan - Bitmap index scan (3)
```sql
EXPLAIN 
SELECT * 
FROM location_trees 
WHERE parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f' AND lazada_id='m';
                QUERY PLAN
---------------------------------------------
 Bitmap Heap Scan on location_trees  (cost=9.07..13.08 rows=1 width=192)
   Recheck Cond: ((parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid) 
   AND ((lazada_id)::text = 'm'::text))
   ->  BitmapAnd  (cost=9.07..9.07 rows=1 width=0)
         ->  Bitmap Index Scan on location_trees_parent_id_name_unique  (cost=0.00..4.38 rows=13 width=0)
               Index Cond: (parent_id = 'a64fe8f7-ae02-41d6-81b2-8c2a83fdb48f'::uuid)
         ->  Bitmap Index Scan on location_trees_lazada_id  (cost=0.00..4.44 rows=22 width=0)
               Index Cond: ((lazada_id)::text = 'm'::text)
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

## Reference

*   PostgreSQL Documentation
*   [The Internals of PostgreSQL](https://github.com/gitpitch/gitpitch/wiki/Slide-Markdown)
*   [dba.stackexchange.com](https://dba.stackexchange.com/questions/119386/understanding-bitmap-heap-scan-and-bitmap-index-scan?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa)
---
Thanks for listening
---