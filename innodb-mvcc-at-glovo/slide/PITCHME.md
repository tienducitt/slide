
# MySQL InnoDB MVCC 101

Duc Nguyen (Rey)

---
## Agenda

1. What is MVCC
2. InnoDB general structure
3. Clustered Index & Secondary Index
4. Insert/Update/Delete
5. Demo

---
### First example

---
# 1. MVCC

---
### Concurrency
Multi transactions can read/write at the same time.
### Transaction Isolation
A transaction can only see the **valid** data according to the current transaction isolation level.

+++
### Transaction Isolation levels
* Uncommitted read
* Committed read
* Repeatable read
* Serializable

---
## Multi-Version Concurrency Controll (MVCC)

* A technique to support concurrency and isolation implemented in MySQL, PostgresQL, Oracle,... |
* By keeping multi versions of the same data, each write creates new version of data item while retaining the old version. |

---
## 2. InnoDB General Structure

---
## General structure
<img src="innodb-mvcc/assets/innodb_general_structure.png">

---
## Tablespace structure
<img src="innodb-mvcc/assets/innodb_tablespace_structure.png">

Note:
- Table's data can be store in a global tablespace or different tablespaces (based on `innodb_file_per_table` flag).
- Tablespace is a logical file which may have multiple physical files, it devided to segments, each segments have it own responsibilities (leaf node segment, non-leaf node segment, rollback segment).
- Each segment have many extents, each extent is 64 continuous pages). InnoDB features such as segments, read-ahead requests and the doublewrite buffer use I/O operations that read, write, allocate, or free data one extent at a time.
- 1 Pages is 16KB contains one or more rows
- If a record could not fit on a page, innodb save the remaining data in another space call overflow page and keep a pointer to it.

---
## Page structure

|Name|
|---|
|FIL Header (38)|
|Page Header (56)|
|Infinimum & Supermum Records|
|**User Records**|
|Free Space|
|Page Dictionary|
|FIL Trailer|

Note:
- TODO

---
## Record structure
|Name|
|---|
|Field start header|
|Extra header (6 bytes)|
|**Field contents**|

Note:
- TODO
- Record structure is different between secondary index & primary index.

+++
## Field start header
- List of numbers - where a field starts
- The size of each offset can be one or two bytes.

+++
## Extra header 
|Name|Size|Description|
|---|---|---|
|n_owned|4 bits|num records owned by this record in page dictionary|
|**delete-flag**|1 bit| 1 if record is deleted|
|n_fields|10 bits|num of fields|
|1byte_off_flag|1 bit| 1 if each Field Start Offset is 1 byte long|
|next|16 bits| pointer to the next record|

---
## 3. Clustered index & Secondary index

---
## 3. Clustered index
- Table itself is a B-Tree index ordered by primary key, user columns is attached to it |
- Root node & inner nodes: contains keys & pointers to lower level nodes |
- Leaf node contain primary keys and other columns |

+++
<img src="innodb-mvcc/assets/innodb_btree.png">

---
## Clustered index row structure
There are some system columns:
- **transaction_id**: the transaction id that insert this record
- **rollback_pointer**: pointer to the previous record version in rollback segment
- **row_id**: in case there are no primary key and also no unique key, rowId will be used behide the scense.

+++
## Record visualize
<img src="innodb-mvcc/assets/row_visualize.png">

+++
<img src="innodb-mvcc/assets/innodb_index_record_roll_sm.png">

---
## Secondary index page
* Secondary index is a B-Tree ordered by index fields, primary key is attached to it |
* No hidden fields, just index fields + primary key |
---
## General picture
<img src="innodb-mvcc/assets/innodb_general_picture.png">

---
## MVCC - Cluster index
* Insert: insert new row |
* Update: copy current row to rollback segment + update user fields + tx_id + rollback pointer |
* Delete: treated as an update where delete_flag bit in row header set as 1. |

Note:
- TODO: double check for delete, it will delete in place, does it copy data to rollback segment?

---
## MVCC Insert (1)
<img src="innodb-mvcc/assets/mvcc_insert_1.png" style="width: 90%;">

---
## MVCC Insert (2)
<img src="innodb-mvcc/assets/mvcc_insert_2.png" style="width: 90%;">

---
## MVCC update (1)
<img src="innodb-mvcc/assets/mvcc_update_1.png" style="width: 90%;">

---
## MVCC update (2)
<img src="innodb-mvcc/assets/mvcc_update_2.png" style="width: 90%;">

---
## MVCC Delete
<img src="innodb-mvcc/assets/mvcc_delete.png" style="width: 90%;">

---
### MVCC - Secondary index:
* Insert: insert new index record. |
* Update: mark current record as deleted, insert new one. |
* Delete: mark as deleted. |

---
## There are no visibility information in secondary index records !!! How MySQL make sure index data is visible for the current transaction?

+++
- MySQL covering index (it shown as `Using index` in extra column when you explain the where)
- PostgresQL `index-only scan`

+++
## Page Header
* PAGE_MAX_TRX_ID: the highest ID of a transaction which might have changed a record on the page (only set for secondary indexes) |
* If PAGE_MAX_TRX_ID of a page is smaller than "up_limit_id", all index records in that page is visible for all transaction. |
* Or else, InnoDB need to check for visibility in clustered index. |

---
## DEMO: MVCC & Isolation level

Note:
- TODO

---
## Take a way:
- Data store in MySQL as a B-tree: table as a clustered index, secondary index keep primary key. |
- Cluster index contains 2 hidden columns: tx_id, rollback_pointer and 1 bit in record header for delete mark. |
- Secondary index doesn't have visibility infomation at record level, it just have page level (by PAGE_MAX_TRX_ID) |
- Index only scan / Covering index is not really index "only scan" |

---
## Reference:
- [MySQL Documentation] (https://dev.mysql.com/doc/refman/8.0/en/innodb-introduction.html)
- [MySQL Internals Manual] (https://dev.mysql.com/doc/internals/en/)
- [Blog.jcole.us] (https://blog.jcole.us/innodb/)
- [Percona] (https://www.percona.com/blog/)
- [MySQL Server team]

---
## Thank you for listening!