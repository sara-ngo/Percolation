import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int N, T;
    private double[] thresholds;
    private double stddev;
    private double mean;
    private double lo;
    private double hi;
    public double sumOfDifferenceSquared;
    private static final double confidenceConst = 1.96;

    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException("Illegal Argument Exception");
        this.N = N;
        this.T = T;
        this.thresholds = new double[T];

        int allSites = N * N;

        for (int i = 0; i < T; i++) {
            Percolation temp = new Percolation(N);
            int openedSites = 0;

            while (!temp.percolates()) {
                int row = StdRandom.uniform(0,N);
                int col = StdRandom.uniform(0,N);

                if (!temp.isOpen(row,col)) {
                    temp.open(row, col);
                    openedSites++;
                }
            }
            thresholds[i] = (double)openedSites / (double)allSites;
        }
    }

    // Sample mean of percolation threshold.
    public double mean() {
        mean = StdStats.mean(thresholds);
        return mean;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        stddev = StdStats.stddev(thresholds);
        return stddev;
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        lo = mean - (confidenceConst * stddev)/ Math.sqrt(thresholds.length);
        return lo;
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        hi = mean + (confidenceConst * stddev) / Math.sqrt(thresholds.length);
        return hi;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
