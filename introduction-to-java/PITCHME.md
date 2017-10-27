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
## Overview
- Java is **crossplatform** - 'Write Once, Run Anywhere' **(WORA)**
- Java is compiled to **Byte Code** (not to machine codes), which is executed by **Java Virtual Machine (JVM)**
- automatic memory management **(GC)**
- Java is **object-oriented**, **class-based**
- static strong safe typization
- concurrent

#HSLIDE
## Flow control
* Loops:
    - while Loop
    - do while
    - for loop
* If else
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
<img src="https://www.ntu.edu.sg/home/ehchua/programming/java/images/OOP_WrapperClass.png" style="height: 450px"/>

#HSLIDE
## String
- String is immutable
```java
String greeting = "Hello";
greeting += " World!";
```
- StringBuilder is not immutable, not thread-safe
```java
StringBuilder builder = new StringBuilder();
builder.append("Hello ");
builder.append("World!");
builder.toString();
```
- StringBuffer: thread-safe
```java
StringBuffer buffer = new StringBuffer();
buffer.append("Hello world");
buffer.toString();
```

#HSLIDE
## Package
//TODO: add image
```java
package com.lazada.cs.helpcenter.entity;

public class Article {

}
```

#HSLIDE
## Import
Import 1 file from a pa
```java
package com.lazada.cs.helpcenter.dao;

import com.lazada.cs.helpcenter.entity.Article;

public class ArticleDAO {

}
```
Import all file from a package
```java
import com.lazada.cs.helpcenter.entity.*;
```

#HSLIDE
## Dependencies

- You can build your apps (or libraries) to a `jar` file (java archive)

- To you a library, just import the necessary `jar` file

- OR using a dependency management tool (`maven`,..)


#HSLIDE
## Array
An array is a collection of variables of the same type
```java
int[] intArray;
intArray = new int[10];
int[]   ints2 = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
int[][] mArray = new int[10][20];

intArray[0] = 0;
```

#HSLIDE
## Collections
<img src="http://fresh2refresh.com/wp-content/uploads/2013/08/Java-Framework.png" style="height: 450px"/>


#HSLIDE
## Generics & Collections
```java
public interface List<E> extends Collection<E> {
    E get(int index);
    ...
}
```
Declare a List
```java
List<Integer> list = new ArrayList<Integer>();  // ArrayList is implementation of List based on Array
```

#HSLIDE
## Exception handling try-catch-finally
```java
try {
    // SQL query
} catch (SQLException ex) {
    _logger.error(ex.getMessage(), ex)
} catch (Exception ex) {
    throw err
} finally {
    conn.close();
}
```

#HSLIDE
### Exceptions hierarchy
<img src="https://raw.githubusercontent.com/rybalkinsd/atom/master/lecture03/presentation/assets/img/exception.png" alt="exception" style="width: 700px;"/>


#HSLIDE
##Checked exceptions must be handled (catch it or throws)


#HSLIDE
## Concurrency: Thread
```java
Thread thread = new Thread();
thread.start();
// thread.run() will not create new thread, it will run on the current thread
```
This example doesn't specify any code for the thread to execute. The thread will stop again right away after it is started.

#HSLIDE
## Concurrency: #1 Subclass of Thread
```java
public class MyThread extends Thread {

    public void run(){
       System.out.println("MyThread running");
    }
  }
```
To create and start the above thread
```java
MyThread myThread = new MyThread();
  myTread.start();
```

#HSLIDE
## Concurrency: #1 Implement runnable
```java
public class MyRunnable implements Runnable {

    public void run(){
       System.out.println("MyRunnable running");
    }
  }
```

```java
Thread thread = new Thread(new MyRunnable());
  thread.start();
```

#HSLIDE
## Concurrency: synchronized
```java
public synchronized void add(int value){
      this.count += value;
  }
```

```java
public void add(int value){

    synchronized(this){
       this.count += value;   
    }
  }
```

#HSLIDE
## Concurrency: more...
- Volatile
- ThreadLocal
- lock()/wait()/notity()/notityAll()
- Data structure

#HSLIDE
## Annotation
- Kind of comment or meta data
- These annotaions can then be processed at compiled time or runtime by Java Reflection
```java
@Entity(name="table_article")
public class Article {
    @Id
    @Column(name="id_article")
    private int id;

    @Override
    public String toString() {
        return "Bla";
    }
}
```

#HSLIDE
## Reflection
Make it possible to inspect classes, interfaces, fields and methods at runtime

```java
Method[] methods = MyObject.class.getMethods();

for(Method method : methods){
    System.out.println("method = " + method.getName());
}
```

#HSLIDE
## Resources:
- http://tutorials.jenkov.com/: really nice tutorials
- Java docs: sure
- http://javarevisited.blogspot.com/: java interview questions
- Java source code, grepcode.com: document can lie, code cannot
- Book: Effective Java ! (2nd 2008, 3rd is coming by the end of this year!!!)
