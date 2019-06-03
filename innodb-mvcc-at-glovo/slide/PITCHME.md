
# MySQL InnoDB Internal 101

-- Duc Nguyen (Rey)

---
## Agenda

1. What is MVCC
2. InnoDB General On Disk Format
3. Clustered & Secondary Indexes
4. MVCC & Insert/Update/Delete
5. Bonus examples

---
# 1. MVCC

---
### Concurrency
Multi transactions can read/write at the same time.
### Transaction isolation levels
A transaction can only see the **valid** data according to the current transaction isolation level.

+++
### Transaction Isolation levels
* READ UNCOMMITTED: can see uncommitted data |
* READ COMMITTED: can see last committed data |
* REPEATABLE READ (*): read commited data at it was on start of transaction |
* SERIALIZABLE: read block write, write block read |

---
### Multi-Version Concurrency Controll (MVCC)

* A technique to support concurrency and isolation implemented in many RDBMS: MySQL, PostgresQL, Oracle,... |
* By keeping information about old versions of changed rows, each write creates new version of data item while retaining the old version. |

---
## 2. InnoDB General On Disk Format

Note: 
- In order to understand the MVCC, we need to know how MySQL store data on disk, it is the core design that effect other parts of MySQL.
- In-memory state is a reflect/cache version of on disk data.

---
### General structure
<img src="innodb-mvcc/assets/innodb_general_structure.png">
Note:
- .frm files contain metadata about the table structure.
- .idb files contain the table data itself
- Occording to innodb_file_per_tables flags, MySQL can use 1 single System table space for all the table, or use different table spaces for each table.

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

+++ 
### Page Header
|Name|Size|Meaning|
|---|---|---|
| PAGE_N_DIR_SLOTS | 2 | number of directory slots in the Page Directory part; initial value = 2 |
| PAGE_HEAP_TOP | 2 | record pointer to first record in heap |
| PAGE_N_HEAP | 2 | number of heap records; initial value = 2 |
| PAGE_FREE | 2 | record pointer to first free record |
| PAGE_GARBAGE | 2 | "number of bytes in deleted records" |
| PAGE_LAST_INSERT | 2 | record pointer to the last inserted record |
| PAGE_DIRECTION | 2 | either PAGE_LEFT, PAGE_RIGHT, or PAGE_NO_DIRECTION |
| PAGE_N_DIRECTION | 2 | number of consecutive inserts in the same direction, e.g. "last 5 were all to the left" |
| PAGE_N_RECS | 2 | number of user records |
| * PAGE_MAX_TRX_ID * | 8 | * the highest ID of a transaction which might have changed a record on the page (only set for secondary indexes) * |
| PAGE_LEVEL | 2 | level within the index (0 for a leaf page) |
| PAGE_INDEX_ID | 8 | identifier of the index the page belongs to |
| PAGE_BTR_SEG_LEA | F | 10 | "file segment header for the leaf pages in a B-tree" (this is irrelev- ant here)
| PAGE_BTR_SEG_TOP |1 0 | "file segment header for the non-leaf pages in a B-tree" (this is ir- relevant here) |

+++
## Page Header
* PAGE_MAX_TRX_ID: the highest ID of a transaction which might have changed a record on the page (only set for secondary indexes) |
* If PAGE_MAX_TRX_ID of a page is smaller than "up_limit_id", all index records in that page is visible for all transaction. |
* Or else, InnoDB need to check for visibility in clustered index. |

---
## Record structure
|Name|
|---|
|Field start header|
|Extra header (6 bytes)|
|**Field data**|

Note:
- TODO
- Record structure is different between secondary index & primary index.
- Consider building an image of the B-Tree

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

TODO: draw a illustration about primary index, non-leaf-node page, leaf-node pages.

+++
<img src="innodb-mvcc/assets/innodb_btree.png">

---
## Clustered index row structure
There are some system columns:
- **transaction_id**: the transaction id that inserts this record
- **rollback_pointer**: pointer to the previous record version in rollback segment

+++
## Record visualize
<img src="innodb-mvcc/assets/row_visualize.png">

+++
<img src="innodb-mvcc/assets/innodb_index_record_roll_sm.png">

---
## Secondary index page
* Secondary index is a B-Tree ordered by index fields, primary key is attached to it |
* No hidden fields, just index fields + primary key |

+++
TODO: draw an image

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
### 5. Bonus

---
### Bonus 1: super long transaction & isolation level.
TODO:
- Add live map postmorten example
- Run example 2

--- 
### Bonus 2:
- MySQL index covering (extra: Using index in explain query result)
- PostgresQL index-only-scan

+++
### There are no visibility information in secondary index records !!! How MySQL make sure index data is visible for the current transaction?

---
## Take a way:
- Data store in MySQL as a B-tree: table as a clustered index, secondary index keep primary key. |
- For MVCC, cluster index contains 2 hidden columns: tx_id, rollback_pointer and 1 bit in record header for delete mark. |
- Secondary index doesn't have visibility infomation at record level, it just have page level (by PAGE_MAX_TRX_ID) |
- Index only scan / Covering index is not really "index only scan" |
- Long transaction and Repeatable read can polute your database TODO: improve it.

---
## Reference:
- [MySQL Documentation] (http://kambing.ui.ac.id/onnopurbo/library/library-sw-hw/linux-howto/mysql/internals-en.pdf)
- [MySQL Internals Manual] (https://dev.mysql.com/doc/internals/en/)
- [Blog.jcole.us] (https://blog.jcole.us/innodb/)
- [Percona] (https://www.percona.com/blog/)
- [MySQL Server team]

---
## Thank you for listening!