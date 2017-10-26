#HSLIDE
# Introduction to Java

#HSLIDE
# Overview
- Java is **crossplatform** - 'Write Once, Run Anywhere' **(WORA)**
- Java is compiled to **Byte Code** (not to machine codes), which is executed by **Java Virtual Machine (JVM)**
- automatic memory management **(GC)**
- Java is **object-oriented**, **class-based**
- static strong safe typization
- concurrent

#HSLIDE
## Basic types
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
## if else
```java
if (18 == yourAge) {
    // у вас всё хорошо
} else if (yourAge > 18
           && yourAge <= 25) {
    // бывало и лучше
} else {
    // ¯\_(ツ)_/¯
}
```
#HSLIDE
## switch case
```java
switch (countOfApple) {
    case 1: // у нас есть 1 яблоко
        break;
    case 2: // у нас есть 2 яблока
        break;
    …
    default:
        // прочие случаи
        break;
}

```

#HSLIDE
## loops
```
while (expression) statement

do { statement } while (expression)

for (initialization; termination; increment)
    statement
```
**Examples:**
```java
int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
for (int i : digits) {
    System.out.println(“Digit: “ + i);
}
//Итерация для хипстеров
IntStream.range(0, 10).forEach(digit -> System.out.println(digit));

IntStream.range(0, 10).forEach(System.out::println);
```

#HSLIDE
# Methods
```java
public int getCountOfApples(List<Integer> boxes, Integer[] numberOfBoxes)
        throws Throwable {

    Integer sumOfApples = 0;
    for (Integer i : numberOfBoxes) {
        sumOfApples += boxes.get(i);
    }
    return sumOfApples;
}
```
**Method signature** – method name + argument list

Access modifier **public**  
Return type **int**  
Method name **getCountOfApples**  
Parameter list **( … )**  
Exception list **throws Throwable**  
Method body **{ … }**  

#HSLIDE
## Package
```java
package com.lazada.cs.helpcenter.entity;

public class Article {

}
```

## Import
```java
package com.lazada.cs.helpcenter.dao;

import com.lazada.cs.helpcenter.entity;

public class ArticleDAO {

}
```

#HSLIDE
# Dependencies
You can build your apps (or libraries) to a `jar` file (java archive)

To you a library, just import the necessary `jar` file

OR using a dependency management tool (`maven`,..)

#HSLIDE
# Collections
<img src="http://fresh2refresh.com/wp-content/uploads/2013/08/Java-Framework.png"/>

#HSLIDE
# Generics

#HSLIDE
# Error handling
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
<img src="https://www.tutorialspoint.com/java/images/exceptions1.jpg">

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
