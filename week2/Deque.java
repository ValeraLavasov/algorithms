/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 03.01.2020
 *  Description: Dequeue class for Algorithms class
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int dequeSize = 0;

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return dequeSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Unsupported Operation Exception");

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst == null) last = first;
        else oldFirst.previous = first;
        dequeSize++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Unsupported Operation Exception");
        // Need to handle empty deck cases
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (oldLast == null) first = last;
        else oldLast.next = last;
        dequeSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Empty");

        Item item = first.item;
        first.item = null;
        first = first.next;
        if (first == null) last = null;
        else first.previous = null;
        dequeSize--;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Empty");

        Item item = last.item;
        last.item = null;
        last = last.previous;
        if (last == null) first = null;
        else last.next = null;
        dequeSize--;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // implement deque iterator for interface
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException("No Such Element");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Unsupported Operation Exception");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deq = new Deque<String>();
        StdOut.println(deq.isEmpty());
        deq.addFirst("A");
        StdOut.println(deq.size());
        deq.addFirst("0");
        deq.addLast("B");
        deq.addLast("C");
        StdOut.println(deq.size());
        for (String str : deq) {
            StdOut.println(str);
        }
        StdOut.println(deq.removeFirst());
        StdOut.println(deq.removeLast());
    }

}
