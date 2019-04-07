/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements  Iterable<Item> {
    private Item[] queue;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.queue = (Item[]) new Object[1];
        this.n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return size() == 0; }

    // return the number of items on the randomized queue
    public int size() { return this.n; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("null argument!");
        if (size() == queue.length) resize(2 * queue.length);
        if (n == 0) {
            queue[n++] = item;
            return;
        }
        int randomPosition = StdRandom.uniform(n);
        Item temp = queue[randomPosition];
        queue[randomPosition] = item;
        queue[n++] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) throw new java.util.NoSuchElementException("queue is empty!");
        if (n == queue.length / 4) resize(queue.length / 2);
        int randomPosition = StdRandom.uniform(n);
        Item temp = queue[randomPosition];
        queue[randomPosition] = queue[--n];
        queue[n] = null;
        return temp;
    }

    private void resize(int capacity) {
        Item[] newQueue = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            newQueue[i] = queue[i];
        queue = newQueue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException("queue is empty!");
        int randomPosition = StdRandom.uniform(n);
        return queue[randomPosition];
    }

    // return an independent iterator over items in a random order
    public Iterator<Item> iterator() { return new ArrayIterator(); }

    // return an array iterator
    private class ArrayIterator implements Iterator<Item> {
        private int iterSum;
        private int[] randomIndices;

        public ArrayIterator() {
            this.iterSum = 0;
            randomIndices = new int[n];
            for (int i = 0; i < n; i++) {
                randomIndices[i] = i;
            }
            // index indices array
            StdRandom.shuffle(randomIndices);
        }

        public boolean hasNext() { return iterSum < n; }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No next element!");
            return queue[randomIndices[iterSum++]];
        }

        public void remove() { throw new java.lang.UnsupportedOperationException("Unsupported operation!"); }
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}