/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 10.06.2019
 *  Description: Percolation class
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private int gridSize;
    private int gridMapSize;
    private int[] sitesState;
    private WeightedQuickUnionUF unionObj;
    private WeightedQuickUnionUF unionObj2;

    public Percolation(int n) {
        gridSize = n;
        if (gridSize <= 0) throw new IllegalArgumentException("Illegal Argument");
        gridMapSize = gridSize * gridSize;
        sitesState = new int[gridMapSize + 2];
        unionObj = new WeightedQuickUnionUF(gridMapSize + 2);
        unionObj2 = new WeightedQuickUnionUF(gridMapSize + 1);
        // I can open 0 and n+1 site for easy connectivity; Or there is no need to
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > gridSize) throw new IllegalArgumentException("Illegal Argument");
        if (col < 1 || col > gridSize) throw new IllegalArgumentException("Illegal Argument");

        if (!isOpen(row, col)) {
            int gridSiteNumber = siteNumber(row, col);
            sitesState[gridSiteNumber] = 1;
            // connect to top site
            if (row - 1 == 0) {
                unionObj.union(gridSiteNumber, 0);
                unionObj2.union(gridSiteNumber, 0);
            }
            else {
                if (isOpen(row - 1, col)) {
                    unionObj.union(siteNumber(row - 1, col), gridSiteNumber);
                    unionObj2.union(siteNumber(row - 1, col), gridSiteNumber);
                }
            }
            // connect to bottom site
            if (row + 1 == gridSize + 1) {
                unionObj.union(gridSiteNumber, gridMapSize + 1);
            }
            else if (row + 1 <= gridSize) {
                if (isOpen(row + 1, col)) {
                    unionObj.union(siteNumber(row + 1, col), gridSiteNumber);
                    unionObj2.union(siteNumber(row + 1, col), gridSiteNumber);
                }
            }
            // connect to right site
            if (col + 1 <= gridSize) {
                if (isOpen(row, col + 1)) {
                    unionObj.union(siteNumber(row, col + 1), gridSiteNumber);
                    unionObj2.union(siteNumber(row, col + 1), gridSiteNumber);
                }
            }
            // connect to left site
            if (col - 1 >= 1) {
                if (isOpen(row, col - 1)) {
                    unionObj.union(siteNumber(row, col - 1), gridSiteNumber);
                    unionObj2.union(siteNumber(row, col - 1), gridSiteNumber);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > gridSize) throw new IllegalArgumentException("Illegal Argument");
        if (col < 0 || col > gridSize) throw new IllegalArgumentException("Illegal Argument");

        return sitesState[siteNumber(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > gridSize) throw new IllegalArgumentException("Illegal Argument");
        if (col < 0 || col > gridSize) throw new IllegalArgumentException("Illegal Argument");

        return unionObj2.connected(0, siteNumber(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openCount = 0;
        for (int i = 1; i <= gridMapSize; i++) {
            if (sitesState[i] == 1) {
                openCount++;
            }
        }
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionObj.connected(0, gridMapSize + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }

    private int siteNumber(int row, int col) {
        return (row - 1) * gridSize + col;
    }
}
