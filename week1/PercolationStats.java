/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 10.06.2019
 *  Description: Stats calculator for Percolations
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialsResults;
    private int trialsCount;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Illegal Argument");
        trialsCount = trials;
        trialsResults = new double[trials];

        for (int i = 1; i <= trialsCount; i++) {
            Percolation percolationSym = new Percolation(n);
            while (!percolationSym.percolates()) {
                percolationSym.open(StdRandom.uniform(1, n + 1),
                                    StdRandom.uniform(1, n + 1));
            }
            trialsResults[i - 1] = percolationSym.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialsResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(trialsCount);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(trialsCount);
    }

    // test client (see below)
    public static void main(String[] args) {
        int nSize = Integer.parseInt(args[0]);
        int trialsCount = Integer.parseInt(args[1]);

        PercolationStats obj = new PercolationStats(nSize, trialsCount);

        StdOut.println("mean = " + obj.mean());
        StdOut.println("stddev = " + obj.stddev());
        StdOut.println("95% confidence interval = [" + obj.confidenceLo() + "," + obj.confidenceHi()
                               + "]");
    }
}
