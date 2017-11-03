#HSLIDE
# Collections in Java

#HSLIDE
## Agenda

1. List
2. Queue
3. Map
4. Set

#HSLIDE
## Collections
<img src="http://fresh2refresh.com/wp-content/uploads/2013/08/Java-Framework.png" style="height: 450px"/>

#HSLIDE
## List
Represents an ordered list of objects, you can access the elements of a List in a specific order, or by index.
```java
boolean add(E e);
E get(int index);
E set(int index, E element);
boolean remove(int index);
boolean contains(Object o);
```

#HSLIDE
## List implementations

1. ArrayList
2. LinkedList
3. Vector

#HSLIDE
## ArrayList
* Auto **resizeable-array** implementation of List interface

#HSLIDE
### ArrayList. Complexity

|  contains  | add   | get   |  set  | remove |
|:----------:|:-----:|:-----:|:-----:|:------:|
| O(n)       | O(1)* |  O(1) |  O(1) | O(n)   |

#HSLIDE
## ArrayList. History
Before Java 7u40
```java
    /**
    Constructs an empty list with an initial capacity of ten.
    */
    public ArrayList() {
        this(10);
    }
```
Memory leak
```java
    public class MyClass {
        private List<AnotherObject> list = new ArrayList<>();
    }
```

#HSLIDE
## ArrayList. History
Java 7u40
```java
    public ArrayList() {
        super();
        this.elementData = EMPTY_ELEMENTDATA;
    }
```

#HSLIDE
## LinkedList
Doubly-linked list implementation
```java
    public class LinkedList<E> ... {
        Node<E> first;
        Node<E> last;
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
```

#HSLIDE
## LinkedList. Complexity

|  contains  | add   | get   |  set  | remove |
|:----------:|:-----:|:-----:|:-----:|:------:|
| O(n)       | O(1)  |  O(n) |  O(n) | O(n)   |

#HSLIDE
## Queue
```java
boolean add(E e);
E peek();
E poll();
E remove();
```

#HSLIDE
## Queue. implementations
1. LinkedList
2. PriorityQueue

#HSLIDE
## PriorityQueue
An unbounded queue based on heap data structure.
The head of this queue is the least element according to the specified ordering

#HSLIDE
## Heap
<img src="http://coopsoft.com/ar/i/heapArray.png" style="width: 500px;"></img>

#HSLIDE
## Heap. Complexity
|  add      | peek   |  poll        |  new  |
|:---------:|:------:|:------------:|:-----:|
| O(log(n)) | O(1)   |  O(log(n))   |  O(n) |

#HSLIDE
## Map
Collections of key-value pair.
Key is unique
```java
V put(K key, V value);
V get(Object key);
void clear();
Set<K> keySet()
Set<Map.Entry<K,V>> entrySet();
```

#HSLIDE
## Map. implementations
1. HashMap
2. LinkedHashMap
3. TreeMap

#HSLIDE
## HashMap
O(1) time complexity map based on **hashing mechanism**

#HSLIDE
## HashMap. Complexity
|  containsKey  | get   | put   | remove |
|:----------:|:-----:|:-----:|:------:|
| O(1)       | O(1)  |  O(1) | O(1)  |


#HSLIDE
## Hashing Collection
Hashing collections like HashMap, HashSet,... work base on **hashing mechanism**.

- How hashing mechanism work?

- How HashMap work internally?

#HSLIDE
## HashMap. Internals
<img src="https://raw.githubusercontent.com/rybalkinsd/atom/master/lecture03/presentation/assets/img/hashmap.png" />

#HSLIDE
## equals and hashCode functions
```java
    public class Object {
        public boolean equals(Object obj) {
            return (this == obj);
        }
        public native int hashCode();
    }
```

#HSLIDE
## general contract of hashCode
- Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode method must consistently return the same integer
- a.equals(b) => a.hashCode() == b.hashCode()
- !a.equals(b) => that's fine if a.hashCode() == b.hashCode()

#HSLIDE
## Always override hashCode when you override equals

#HSLIDE
## How to implement hashCode properly?
Read effective java 2nd Chapter 3. Item 9

#HSLIDE
## LinkedHashMap
- HashMap is not guarantee any order when you loop through it!
- LinkedHashMap extends from HashMap and a provide insert order

#HSLIDE
## TreeMap
A map implementations based on Red-Black Tree

#HSLIDE
## TreeMap
<img src="https://upload.wikimedia.org/wikipedia/commons/6/66/Red-black_tree_example.svg" style="height: 500px;"/>

#HSLIDE
## TreeMap. Complexity
|  containsKey  | get   | put   | remove |
|:----------:|:-----:|:-----:|:------:|
| O(log(n))       | O(log(n))  |  O(log(n)) | O(log(n))  |

#HSLIDE
## Set
- Collection of **unique** items.
- In java, almost every Set is **implemented by map** internally with key is the item of the map, value is a dummy object.
```java
    boolean add(E e);
    boolean contains(Object o);
```

#HSLIDE
## Set implementations
1. HashSet ~ HashMap
2. LinkHashSet ~ LinkedHashSet
3. TreeSet ~ TreeMap

#HSLIDE
## Thank you!
