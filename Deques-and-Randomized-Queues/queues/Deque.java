/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    // construct an empty deque

    private int n;
    private Node first, last;

    // inner class for linked-list implementation
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    public Deque() {
        this.n = 0;
        this.first = null;
        this.last = null;
    }

    // is the deque empty?
    public boolean isEmpty() { return size() == 0; }

    // return the number of items on the deque
    public int size() { return this.n; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("null argument!");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;
        if (oldfirst == null) this.last = first;
        else    oldfirst.prev = first;
        this.n += 1;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("null argument!");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        last.next = null;
        if (oldlast == null) this.first = last;
        else oldlast.next = last;
        this.n += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new java.util.NoSuchElementException("Deque is empty!");
        Item item = first.item;
        first = first.next;
        this.n -= 1;
        if (size() == 0)    last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size() == 0) throw new java.util.NoSuchElementException("Deque is empty!");
        Item item = last.item;
        last = last.prev;
        this.n -= 1;
        if (size() == 0)    first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() { return new ListIterator(); }

    // inner class for iterator
    private class ListIterator implements Iterator<Item> {
        // Node used to iterate
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No next element!");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() { throw new java.lang.UnsupportedOperationException("Unsupported operation!"); }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}