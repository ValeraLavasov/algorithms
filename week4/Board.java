/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 17.01.2020
 *  Description: Board class for Slider Puzzle A solver
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Objects;

public class Board {
    // implements Iterable<Board>
    private final int boardSize;
    // I can change this to char to use even less memory!
    private final short[][] board;
    // private final Stack<Board> neighbors = new Stack<>();
    private Board twinBoard;
    // private int hamming = 0;
    // private int manhattan = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        boardSize = tiles.length;
        board = new short[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = (short) tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        // Check if all tiles are at place 1/2/3 && 4/5/6 && 7/8/0
        // Otherwise count missplaced tiles; This can be cached//saved
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0) {
                    int goalX = (board[i][j] - 1) / boardSize;
                    int goalY = (board[i][j] - 1) % boardSize;
                    if (goalX != i || goalY != j) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // row diff + col diff for each missplaced item
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0) {
                    int goalX = (board[i][j] - 1) / boardSize;
                    int goalY = (board[i][j] - 1) % boardSize;
                    count += Math.abs(goalX - i) + Math.abs(goalY - j);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Objects.deepEquals(board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // if (!neighbors.isEmpty()) {
        //     return neighbors;
        // }
        Stack<Board> neighbors = new Stack<>();
        boolean zeroPosition = false;
        int zeroX = 0;
        int zeroY = 0;
        for (int i = 0; i < boardSize; i++) {
            if (zeroPosition) break;
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    zeroPosition = true;
                    zeroX = i;
                    zeroY = j;
                    break;
                }
            }
        }

        if (zeroX == 0) {
            if (zeroY == 0) {
                // 2 options: right, down;
                // right
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
            }
            else if (zeroY == boardSize - 1) {
                // 2 options: left, down
                // left
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
            }
            else {
                // 3 options: left, down, right;
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
            }
            // down
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX + 1, zeroY));
        }
        else if (zeroX == boardSize - 1) {
            if (zeroY == 0) {
                // 2 options: up, right;
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
            }
            else if (zeroY == boardSize - 1) {
                // 2 options: up, left;
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
            }
            else {
                // 3 options: left, up, right;
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
                neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
            }
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX - 1, zeroY));
        }
        else if (zeroY == 0) {
            // 3 options: up, down, right;
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX - 1, zeroY));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX + 1, zeroY));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
        }
        else if (zeroY == boardSize - 1) {
            // 3 options: up, down, left;
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX - 1, zeroY));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX + 1, zeroY));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
        }
        else {
            // it is somewhere on the board with all 4 options
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY - 1));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX, zeroY + 1));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX - 1, zeroY));
            neighbors.push(buildNeighbor(zeroX, zeroY, zeroX + 1, zeroY));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twinBoard != null) {
            return twinBoard;
        }
        // generate 2 arbitrary random elements
        int randi = StdRandom.uniform(boardSize);
        int randj = StdRandom.uniform(boardSize);
        int ri = StdRandom.uniform(boardSize);
        int rj = StdRandom.uniform(boardSize);

        while (board[randi][randj] == 0) {
            randi = StdRandom.uniform(boardSize);
            randj = StdRandom.uniform(boardSize);
        }

        // I need to check if not the same indexies
        while (board[ri][rj] == 0 || (ri == randi && rj == randj)) {
            ri = StdRandom.uniform(boardSize);
            rj = StdRandom.uniform(boardSize);
        }
        // This can be moved to method for board array generation
        int[][] copyBoard = new int[boardSize][boardSize];
        // copy arrays
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        // swap boards
        int swap = board[randi][randj];
        copyBoard[randi][randj] = board[ri][rj];
        copyBoard[ri][rj] = swap;

        twinBoard = new Board(copyBoard);
        return twinBoard;
    }

    private Board buildNeighbor(int zeroX, int zeroY, int swapX, int swapY) {
        int[][] copyBoard = new int[boardSize][boardSize];
        // copy arrays
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        // swap boards
        int swap = board[zeroX][zeroY];
        copyBoard[zeroX][zeroY] = board[swapX][swapY];
        copyBoard[swapX][swapY] = swap;

        return new Board(copyBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] smallBoard = { { 1, 2, 5 }, { 0, 7, 4 }, { 3, 8, 6 } };
        int[][] smallBoard = {
                { 4, 0, 11, 12 }, { 1, 15, 9, 13 }, { 14, 6, 10, 3 }, { 5, 8, 7, 2 }
        };
        Board test = new Board(smallBoard);
        StdOut.println(test);
        StdOut.println("-----");
        StdOut.println(test.hamming());
        StdOut.println(test.manhattan());
        StdOut.println(test.isGoal());
        StdOut.println(test.twin());
        for (Board n : test.neighbors()) {
            StdOut.println("-----");
            StdOut.println(n);
        }
    }

}
