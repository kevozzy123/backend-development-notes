import java.util.LinkedList;

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

        // remove pointers with prev
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        // remove pointers with next
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        return element;
    }

    Node node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
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
