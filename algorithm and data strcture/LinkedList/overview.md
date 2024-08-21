# Linked List

## Definition:
> In computer science, a **linked list** is a linear collection of data elements whose order is not given by their physical placement in memory. Instead, each element points to the next. 


There are a few types of linked list:
* Singly Linked List: Each element only knows the next element.
![image-20221110083407176](../../imgs/image-20221110083407176.png)
* Doubly Linked List: Each element knows both the previous and the next elements.
![image-20221110083427372](../../imgs\image-20221110083427372.png)
* Circular Linked List: In a typical linked list, the tail node points to null, but in a circular linked list, the tail points to the head node.
![image-20221110083538273](../../imgs\image-20221110083538273.png)
Understanding singly linked list is suffcient for most leetcode questions, however, some harder ones do require doubly linked list and circular linked list.


> There is also a special type of node within a linked list called a Sentinel node, also known as a Dummy node. It doesn't store data and is usually used as a head or tail node to simplify boundary conditions, as shown in the diagram below.
![image-20221110084611550](../../imgs\image-20221110084611550.png)

## Advantages of using LinkedList
* Dynamic Size: Linked lists can grow or shrink dynamically, which makes them more flexible compared to arrays.
* Efficient Insertions/Deletions: Insertions and deletions can be done in constant time, provided you have the reference to the node.
**Random Access Performance**
Searching by index has a time complexity of $O(n)$.
Insertion or Deletion Performance

Starting position: $O(1)$
Ending position: $O(1)$ if the tail node is known, $O(n)$ if the tail node is unknown.
Middle position: Index search time + $O(1)$

### Singly Linked List
Every linked list needs to have a series of Node class that stores the value and the pointer to the next node. One such node needs to be the head, namely the first element of the list, or the dummy node as mentioned ealier.

Let's first take a look at a linked list implementation without the dummy node.

#### SinglyLinkedList without dummy head
**Define Node class**
```java
public class SinglyLinkedList {
    
    private Node head;
    
    private static class Node {
        int value;
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
```

**Add item to the beginning of the list**
```java
public class SinglyLinkedList {
    // ...
    public void addFirst(int value) {
		this.head = new Node(value, this.head);
    }
}
```

**Iterate through the list**
```java
public class SinglyLinkedList {
    // ...
    public void loop() {
        Node curr = this.head;
        while (curr != null) {
            // do something
            curr = curr.next;
        }
    }
}
```

It's also possible to iterate the list using the Iterator pattern
```java
public class LinkedList implements Iterable<Integer> {
    private class NodeIterator implements Iterable<Integer> {
        Node curr = head;

        public boolean hasNext() {
            return curr != null;
        } 

        public Integer next() {
            int value = curr.value;
            curr = curr.next;
            return value;
        }
    }

    public Iterator iterator() {
        return new NodeIterator();
    }
}
```

**Add to tail**
```java
public class LinkedList {
    // ...

    private Node findLast() {
        if(this.head == null) {
            return null;
        }
        Node curr = head;
        while(curr.next != null) {
            curr = curr.next;
        }
        return curr;
    }

    public void addLast(int value) {
        Node last = findLast();
        if(last == null) {
            addFirst(value);
            return;
        }
        last.next = new Node(value, null);
    }
}
```

**Access elements by index**
```java
public class SinglyLinkedList {
    // ...

    private Node findNode(int index) {
        int i = 0;
        for(Node curr = head; curr != null; curr = curr.next, i++) {
            if(i == index) {
                return curr;
            }
        }
        return null;
    }

    public int get(int index) {
        Node node = findNode(index);
        if(node != null) {
            return node.value;
        }
        throw new IllegalArgumentException("Illegal index");
    }
}
```

**Insert element into linked list**
```java
public class SinglyLinkedList {
    // ...

    public void insert(int index, int value) {
        // find the previous node
        Node prev = findprev(index - 1);
        if(prev == null) {
            throw new IllegalArgumentException();
        }
        prev.next = new Node(value, prev.next);
    }
}
```
**Why this "prev.next = new Node(value, prev.next)" works:**
* Order of Operations: When we create the new node with new Node(value, prev.next), prev.next is evaluated first. So, the new node is created with a reference to the next node (the node that was originally after prev).
* Reassigning prev.next: After the new node is created, prev.next is reassigned to point to this new node.

**Delete an element**
```java
public class SinglyLinkedLis {
    // ...

    public void remove(int index) {
        if(index == 0) {
            if(this.head != null) {
                // simply break the connection between head and its next node
                this.head = this.head.next;
                return;
            } else {
                throw new IllegalArgumentException();
            }
        }
        Node prev = findNode(index - 1);
        Node curr;
        if(prev != null && (curr = prev.next) != null) {
            prev.next = curr.next;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
```

#### SinglyLinkedList with dummy
Personally, I find most linked list problems more intuitive with a dummy node implemented. This is how I approach almost every leetcode's linked list questions as well. We can simplify our linkedList using the dummy node.
The value of the dummy usually doesn't matter, therefore we can give it whatever we like. 

**Initialize the dummy**
```java
public class SinglyLinkedListSentinel {
    // ...
    private Node dummy = new Node(Integer.MIN_VALUE, null);
}
```

First off, we need to make a minor adjustment to findNode() and findLast().
Instead of starting the index at 0, **we now initialize it at -1**, because now we are starting at the dummy node that is one index ahead of the head.
Also, **findLast()** now will always return something. This will make our life easier as you will see later.
```java
public class SinglyLinkedListSentinel {
    // ...


    private Node findNode(int index) {
        int i = -1;
        for (Node curr = this.dummy; curr != null; curr = curr.next, i++) {
            if (i == index) {
                return curr;
            }
        }
        return null;
    }

    private Node findLast() {
        Node curr;
        for (curr = this.head; curr.next != null; ) {
            curr = curr.next;
        }
        return curr;
    }
}
```

```java
public class SinglyLinkedListSentinel {
    public void addLast(int value) {
        Node last = findLast();
        // before:
        // if(last == null) {
        //     this.head = new Node(value, null);
        //     return;
        // }

        last.next = new Node(value, null);
    }

    public void insert(int index, int value) {
        // if(index == 0) {
        //     this.head = new Node(value, this.head);
        //     return;
        // }

        // we no longer need to zero check, because now index 0 would return the dummy node
        Node prev = findNode(index - 1);
        if(prev != null) {
            prev.next = new Node(value, prev.next);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void remove(int index) {
        // we no longer need to zero check, because now index 0 would return the dummy node
        // if(index == 0) {
        //     if(head != null) {
        //         this.head = this.head.next;
        //     } else {
        //         throw new IllegalArgumentException();
        //     }
        // }

        Node prev = findNode(index - 1);
        Node curr;
        if(prev != null && (curr = prev.next) != null) {
            prev.next = curr.next;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addFirst(int value) {
        // Before:
        // this.head = new Node(value, this.head);

        this.dummy.next = new Node(value, this.dummy.next);
    }
}
```

### Doubly Linked List
Since java's implementation of linked list is using a doubly linked list without any dummy nodes, I'm going to do that as well.
Below is an simplified summary version of java.utils implementation
```java
public class MyLinkedList {

    Node first;
    Node last;
    int size = 0;

    private static class Node {
        int item;
        Node prev;
        Node next;

        public Node(Node prev, int item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public void addLast(int value) {
        linkLast(value);
    }

    public void addFirst(int value) {
        linkFirst(value);
    }

    // Start searching from first
    public int indexOf(int value) {
        int index = 0;
        for (Node node = first; node != null; node = node.next) {
            if (value == node.item)
                return index;
            index++;
        }
        return -1;
    }

    // Start searching from last
    public int lastIndexOf(int value) {
        int index = size - 1;
        for (Node node = last; node != null; node = node.prev) {
            if (value == node.item)
                return index;
            index--;
        }
        return -1;
    }

    // Without specifying index, new elements are added to the end
    public boolean add(int value) {
        addLast(value);
        return true;
    }

    public void add(int index, int element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    public boolean remove(int value) {
        for (Node node = first; node != null; node = node.next) {
            if (node.item == value) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    // added a second param just to prevent it from overloading the other remove()
    public int remove(int index, Object obj) {
        if (index >= 0 && index < size) {
            return unlink(node(index));
        }
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /*
     * before: first -> last
     * after: newNode -> first -> last
     */
    public void linkFirst(int val) {
        // old first, this will become the second element
        Node f = first;
        Node newNode = new Node(null, val, first);
        // assign new node to be first
        first = newNode;
        // if first was null, last also becomes newNode. Else, link newNode to old first
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    /*
     * before: first -> last(next = null)
     * after: first -> last -> newNode
     */
    private void linkLast(int value) {
        // old last, this will become the second last element
        Node l = last;
        Node newNode = new Node(last, value, null);
        last = newNode;

        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    void linkBefore(int value, Node node) {
        final Node prev = node.prev;
        final Node newNode = new Node(prev, value, node);
        node.prev = newNode;
        if (prev == null) {
            // We could do linkFirst(), but that wouldn't be necessary
            first = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
    }

    int unlink(Node node) {
        final int element = node.item;
        final Node prev = node.prev;
        final Node next = node.next;

        if (prev == null) {
            first = next; // If the node is the first node, update first to next
        } else {
            prev.next = next; // Otherwise, bypass the node by linking previous to next
            node.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        return element;
    }

    /*
     * Return the node at the specified index.
     * Use linear search from the nearest end of the list.
     */
    Node node(int index) {
        if (index < (size/2)) {
            Node x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }
}
```

## Leetcode Questions

### 206. Reverse Linked List
[https://leetcode.com/problems/reverse-linked-list](https://leetcode.com/problems/reverse-linked-list/description/)

#### Solution 1:
The most simple one. We just need to create a new list. The downside is that this will take additional O(N) memory as we create new nodes
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode node = null;
        ListNode curr = head;
        while(curr != null) {
            node = new ListNode(curr.val, node);
            curr = curr.next;
        }
        return node;
    }
}
```

#### Solution 2:
This approach doesn't require additional memory but can be a bit more challenging to grasp.
In each iteration, we reverse the direction of the pointer by making the current node point to the previous one. As a result, instead of 1→2→3→∅, the list becomes ∅←1←2←3.

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while(curr != null) {
            // Temporarily store the next node
            ListNode next = curr.next;
            // Reverse the current node's pointer to the previous node
            curr.next = prev;
            // Move prev and curr one step forward
            prev = curr;
            curr = next;
        }
        return prev;
    }
}
```