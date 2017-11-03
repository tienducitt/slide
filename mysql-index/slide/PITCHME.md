#HSLIDE
## MySQL - From index to selectivity and covering index
ReydentX

#HSLIDE
## Agenda
1. What is an index?
2. Table scan & Index scan
3. Index Selectivity
4. Covering index

#HSLIDE
## What is an index?
- Data structures that storage engines use to find rows quickly

#HSLIDE
## Index
- Speed up SELECTs
- Slows down writes
- More disk space used

#HSLIDE
## B-Tree indexes
- Table itself is a B-tree (cluster index) order by primary key, non-primary columns is attached to it.
- Secondary index is a B-Tree order by `index key`, primary key is attached to it.

#HSLIDE
## B+-Tree in MySQL
<img src="/assets/b-tree.jpg">

#HSLIDE
## Does MySQL alway use index?

#HSLIDE
## Query explainer

#HSLIDE
## Selectivity

#HSLIDE
## "Table scan" & "Index scan"?

#HSLIDE
## Two ways of scan
- Follow the tree, from root to leaf
- Full scan the tree (all the leaf nodes)

#HSLIDE
## Explain
- In which order tables are read
- What type of read operations are made
- Which indexes could have been used
- How tables refer to each other
- How many rows the optimizer estimates to retrieve from table

#HSLIDE
## Explain. type column
- const
- ref
- index
- all

#HSLIDE
## Explain type. Const
```sql
    explain select * from employees where emp_no=11111 \G;
```
```
           id: 1
  select_type: SIMPLE
        table: employees
   partitions: NULL
         type: const
possible_keys: PRIMARY
          key: PRIMARY
      key_len: 4
          ref: const
         rows: 1
     filtered: 100.00
        Extra: NULL
```

#HSLIDE
##Explain type. ref
```sql
    explain select * from employees where first_name="Shahab" \G;
```

```
           id: 1
  select_type: SIMPLE
        table: employees
   partitions: NULL
         type: ref
possible_keys: employee_first_name
          key: employee_first_name
      key_len: 16
          ref: const
         rows: 244
     filtered: 100.00
        Extra: NULL
```

#HSLIDE
##Expain type. index
```sql
    explain select emp_no from employees where first_name like '%y' \G;
```

```
           id: 1
  select_type: SIMPLE
        table: employees
   partitions: NULL
         type: index
possible_keys: NULL
          key: employee_first_name
      key_len: 16
          ref: NULL
         rows: 299113
     filtered: 11.11
        Extra: Using where; Using index
```

#HSLIDE
##Explain type all
```sql
    explain select * from employees where year(hire_date) = 2017 \G;
```
```
           id: 1
  select_type: SIMPLE
        table: employees
   partitions: NULL
         type: ALL
possible_keys: NULL
          key: NULL
      key_len: NULL
          ref: NULL
         rows: 299113
     filtered: 100.00
        Extra: Using where
```

#HSLIDE
## Should we create index in a low selectivity columns?

#HSLIDE
## Covering index
- All the columns need for the query is exist in the index
- Just read data from index without accessing table rows

#HSLIDE
## Covering index
- Index size is really small compare to cluster index!

#HSLIDE
## Deferrer Join
This query will not use covering index
```sql
SELECT SQL_NO_CACHE * FROM document WHERE enable=1 ORDER BY id LIMIT 60000,1;
```

But this one will
```sql
SELECT sql_no_cache *
    FROM document d
        JOIN
            (SELECT id FROM document WHERE enable=1 ORDER BY id LIMIT 60000,1) de
        ON d.id = de.id;
```

#HSLIDE
## One more thing. Multicolumn index?
- MySQL can use multiple-column indexes for queries that test all the columns in the index or query that test the left most columns

#HSLIDE
## Examples

#HSLIDE
## Summary

#HSLIDE
## Thank you
