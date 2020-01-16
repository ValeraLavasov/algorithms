/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 05.01.2020
 *  Description: Rand Queue implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] storage;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        storage = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Wrong Argument");
        if (size == storage.length) resize(2 * storage.length);
        storage[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("No Such Elelment");
        int randNum = StdRandom.uniform(size);
        size--;
        Item item = storage[randNum];
        storage[randNum] = storage[size];
        storage[size] = null;
        if (size > 0 && size == storage.length / 4) resize(storage.length / 2);
        return item;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = storage[i];
        storage = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("No Such Elelment");
        return storage[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // implement deque iterator for interface
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] randMap = StdRandom.permutation(size);
        private int index = size;

        public boolean hasNext() {
            return index != 0;
        }

        // Map Implementation
        public Item next() {
            if (index == 0)
                throw new NoSuchElementException("No Such Element");
            --index;
            return storage[randMap[index]];
        }

        public void remove() {
            throw new UnsupportedOperationException("Unsupported Operation Exception");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rDeq = new RandomizedQueue<String>();
        StdOut.println(rDeq.isEmpty());
        rDeq.enqueue("AA");
        StdOut.println(rDeq.size());
        StdOut.println("-----");
        rDeq.enqueue("BB");
        rDeq.enqueue("CC");
        rDeq.enqueue("DD");
        rDeq.enqueue("EER");
        StdOut.println(rDeq.size());
        StdOut.println("-----");
        StdOut.println(rDeq.dequeue());
        StdOut.println("-----");
        StdOut.println(rDeq.sample());
        StdOut.println(rDeq.sample());
        StdOut.println(rDeq.sample());
        StdOut.println(rDeq.sample());
        StdOut.println("-----");
        for (String str : rDeq) {
            StdOut.println(str);
        }
    }

}
