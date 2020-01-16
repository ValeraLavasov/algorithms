/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 05.01.2020
 *  Description: Permutation example based of Rand Queue
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rDeq = new RandomizedQueue<String>();
        int outputSize = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            rDeq.enqueue(StdIn.readString());
        }
        for (int i = 0; i < outputSize; i++) {
            StdOut.println(rDeq.dequeue());
        }
    }
}
