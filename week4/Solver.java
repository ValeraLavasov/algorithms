/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 17.01.2020
 *  Description: Implements A* search algorithm
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solution;
    private final int moves;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        solution = new Stack<>();
        MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<>();

        priorityQueue.insert(new SearchNode(initial, null, 0));
        twinPriorityQueue.insert(new SearchNode(initial.twin(), null, 0));
        SearchNode tempNode;
        SearchNode twinTempNode;

        while (true) {
            tempNode = priorityQueue.delMin();
            twinTempNode = twinPriorityQueue.delMin();

            if (tempNode.current.isGoal()) {
                moves = tempNode.moves;
                solvable = true;
                break;
            }

            if (twinTempNode.current.isGoal()) {
                moves = -1;
                solvable = false;
                break;
            }
            for (Board n : tempNode.current.neighbors()) {
                if (tempNode.previous == null || !tempNode.previous.current.equals(n)) {
                    priorityQueue.insert(new SearchNode(n, tempNode, tempNode.moves + 1));
                }
            }

            for (Board n : twinTempNode.current.neighbors()) {
                if (twinTempNode.previous == null || !twinTempNode.previous.current.equals(n)) {
                    twinPriorityQueue
                            .insert(new SearchNode(n, twinTempNode, twinTempNode.moves + 1));
                }
            }
        }

        if (moves != -1) {
            while (tempNode != null) {
                solution.push(tempNode.current);
                tempNode = tempNode.previous;
            }
        }

    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final int moves;
        private final int manhattan;
        private final SearchNode previous;
        private final Board current;

        public SearchNode(Board ct, SearchNode ps, int movesCount) {
            current = ct;
            previous = ps;
            moves = movesCount;
            manhattan = ct.manhattan();
        }

        public int compareTo(SearchNode other) {
            int thisPriority = manhattan + moves;
            int otherPriority = other.manhattan + other.moves;
            return Integer.compare(thisPriority, otherPriority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (solvable) {
            return solution;
        }
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
