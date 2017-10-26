#HSLIDE
# Introduction to Java

#HSLIDE
# Agenda

1. Overview
2. Primitive type & wrapper classes
3. Operators & flow control
4. Package & Import
5. Build and dependencies
6. Collection Framework
7. Exception handling
8. Thread and concurrency

#HSLIDE
# Overview
- Java is **crossplatform** - 'Write Once, Run Anywhere' **(WORA)**
- Java is compiled to **Byte Code** (not to machine codes), which is executed by **Java Virtual Machine (JVM)**
- automatic memory management **(GC)**
- Java is **object-oriented**, **class-based**
- static strong safe typization
- concurrent

#HSLIDE
## Flow control
### Loops:
- while Loop
- do while
- for loop
### If else
- if ... else if ... else
- switch case

#HSLIDE
## Operators
|Operator type  |Operator                   |
|---------------|---------------------------|
|Assignment     | =, +=, *= …^=             |
|Arithmetical   | +, -, *, /, %             |
|Relational     | <, >, <=, >=, ==, !=      |
|Logical        | &&, &#124;&#124;          |
|Bitwise        | &, &#124;, ^, >>, <<, >>> |
|Unary          | ++, --, +, -, !           |
|Relational2    | instanceof                |


#HSLIDE
## Primitive types
| Type          | Size          | Range             |
| ------------- |:-------------:| -----------------:|
| boolean       | undefined*    | true/false        |
| byte          | 1 byte        | -128-127          |
| char          | 2 bytes       | \u0000-\uffff     |
| short         | 2 bytes       | -32768 - 32767    |
| int           | 4 bytes       | -2^31 - (2^31)-1  |
| long          | 8 bytes       | -2^63 - (2^63)-1  |
| float         | 4 bytes       | IEEE 754          |
| double        | 8 bytes       | IEEE 754          |
| **reference** | system bitness|                   |
* Not defined by specification, but actually 1 byte in hotspot.

#HSLIDE
## Wrapper classes
<img src="https://www.ntu.edu.sg/home/ehchua/programming/java/images/OOP_WrapperClass.png" />

#HSLIDE
## String
- String
```java
String greeting = "Hello" + "world!";
```
- StringBuilder
```java
StringBuilder builder = new StringBuilder();
builder.append("Hello ");
builder.append("World!");
builder.toString();
```
- StringBuffer
```java
StringBuffer buffer = new StringBuffer();
buffer.append("Hello world");
buffer.toString();
```

#HSLIDE
## Package
```java
package com.lazada.cs.helpcenter.entity; <----

public class Article {

}
```

## Import
```java
package com.lazada.cs.helpcenter.dao;

import com.lazada.cs.helpcenter.entity; <----

public class ArticleDAO {

}
```

#HSLIDE
# Dependencies

- You can build your apps (or libraries) to a `jar` file (java archive)

- To you a library, just import the necessary `jar` file

- OR using a dependency management tool (`maven`,..)


#HSLIDE
# Generics

#HSLIDE
# Collections
<img src="http://fresh2refresh.com/wp-content/uploads/2013/08/Java-Framework.png" style="width: 800px"/>

#HSLIDE
# Exception handling try-catch-finally
```java
try {
    // do something
} catch (SQLException ex) {
    _logger.error(ex.getMessage(), ex)
} catch (Exception ex) {
    _logger.error(ex.getMessage(), ex)
} finally {
    conn.close();
}
```

#HSLIDE
# Exception hierarchy
<img src="https://www.tutorialspoint.com/java/images/exceptions1.jpg" style="width: 800px">

#HSLIDE
# Concurrency: Thread


#HSLIDE
# Annotation
# Reflection

#HSLIDE
# You should know
## `==` and `Equals`
## String concatenate
## `Equals` & `HashMap`

#HSLIDE
# Resources:
