/* *****************************************************************************
 *  Name: Zecheng Qian
 *  Date: Dec.20 2018
 *  Description: Monte Carlo simulation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int sideLength;
    private double meanThreshold;
    private double stddevThreshold;
    private double confidenceLow;
    private double condidenceHigh;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Illegal Arguments!");
        }
        else {
            sideLength = n;
            double[] thresholds = new double[trials];
            for (int i = 0; i < trials; i++) {
                thresholds[i] = oneTrial();
            }
            meanThreshold = StdStats.mean(thresholds);
            stddevThreshold = StdStats.stddev(thresholds);
            double diff = (1.96 * stddevThreshold) / Math.sqrt(trials);
            confidenceLow = meanThreshold - diff;
            condidenceHigh = meanThreshold + diff;
        }
    }

    private double oneTrial() {
        Percolation sample = new Percolation(sideLength);
        int numOpen = 0;
        while (!sample.percolates()) {
            int row = StdRandom.uniform(sideLength) + 1;
            int col = StdRandom.uniform(sideLength) + 1;
            if (!sample.isOpen(row, col)) {
                sample.open(row, col);
                numOpen++;
            }
        }

        return 1.0 * numOpen / (sideLength*sideLength);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanThreshold;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevThreshold;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return condidenceHigh;
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolations = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + percolations.mean());
        StdOut.println("stddev                  = " + percolations.stddev());
        StdOut.println("95% confidence interval = "
                               + percolations.confidenceLo() + ", "
                               + percolations.confidenceHi());
    }
}