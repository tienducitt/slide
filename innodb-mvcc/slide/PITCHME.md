
# Understand MySQL InnoDB MVCC

Duc Nguyen

---
## Agenda

1. What is MVCC
2. InnoDB general structure
3. Clustered Index
4. Secondary Index

---
# 1. MVCC

---
### Concurrency
Multi transaction can read/write at the same time.
### Transaction Isolation
A transaction can only see the **valid** data according to the current transaction isolation level.

---
<img src="innodb-mvcc/assets/innodb_mvcc_example.jpg" style="height: 90%">

---
## Multi-Version Concurrency Controll (MVCC)

* A technique to support concurrency and isolation implemented in MySQL, PostgresQL, Oracle,... |
* Keeping multi versions of the same data, each write creates new version of data item while retaining the old version. |

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
- Tablespace have many segments, each segments have it own responsibilities (leaf node segment, non-leaf node segment, rollback segment).
- Each segment have many extents (64 pages). InnoDB features such as segments, read-ahead requests and the doublewrite buffer use I/O operations that read, write, allocate, or free data one extent at a time.
- 1 Pages is 16KB contains one or more rows
- if a record could not fit on a page, innodb save the remaining data in another space call overflow page and keep a pointer to it.

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

---
## Record structure
|Name|
|---|
|Field start header|
|Extra header (6 bytes)|
|**Field contents**|

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
## Clustered index
- Table itself is a B-Tree index ordered by primary key, user columns is attached to it.

---
## Clustered index row structure
- `transaction_id`: the transaction id that insert this record
- `rollback_pointer`: pointer to the previous record version in rollback segment
- `rowID`: in case there are no primary key and also no unique key, rowId will be used behide the scense.

---
## Record visualize:
<img src="innodb-mvcc/assets/row_visualize.png">

---
## MVCC
* Insert: insert new row |
* Update: copy current row to rollback segment + update user fields + tx_id + rollback pointer |
* Delete: treated as an update where delete_flag bit in row header set as 1. |

---
## MVCC Insert (1)
<img src="innodb-mvcc/assets/mvcc_insert_1.png" style="width: 100%;">

---
## MVCC Insert (2)
<img src="innodb-mvcc/assets/mvcc_insert_2.png">

---
## MVCC update (1)
<img src="innodb-mvcc/assets/mvcc_update_1.png">

---
## MVCC update (2)
<img src="innodb-mvcc/assets/mvcc_update_2.png">

---
## MVCC Delete
<img src="innodb-mvcc/assets/mvcc_delete.png">

---
## Const scan type

---
## Secondary index page
* Secondary index is a B-Tree index ordered by index fields, primary key is attached to it |
*-* No hidden fields, just index fields + primary key |

---
## Secondary index:
* Insert: insert new index record. |
* Update: mark current record as deleted, insert new one. |
* Delete: mark as deleted. |

---
## There are no visibility information in secondary index records !!!

---
## Page Header
* PAGE_MAX_TRX_ID: the highest ID of a transaction which might have changed a record on the page (only set for secondary indexes) |
* If PAGE_MAX_TRX_ID of a page is smaller than `up_limit_id`, all index records in that page is visible for all transaction. |
* Or else, InnoDB need to check for visibility in clustered index. |

---
## DEMO

---
## Reference:
- MySQL Documentation
- MySQL Internals Manual
- Percona
- Blog.jcole.us

---
## Thank you for listening!