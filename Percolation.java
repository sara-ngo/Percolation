import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {
    private int N;
    private boolean[][] grid;                       // transform a real number grid model to a boolean T/F file
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufBackwash;
    private int top;
    private int bottom;
    private int countOpen = 0;

    // Create an N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException();

        this.N = N;
        this.grid = new boolean[N][N];                   // create 2D grid
        ufBackwash = new WeightedQuickUnionUF(N * N + 2);        // backwash
        uf = new WeightedQuickUnionUF(N * N + 1);        // without bottom index
        top = 0;                                         // top virtual site
        bottom = N * N + 1;                              // bottom virtual site

        for (int i = 0; i <= N - 1; i++) {
            ufBackwash.union(top, i);
            ufBackwash.union(bottom, encode((N - 1), i));
            grid[i][i] = false;                          // initial all sites blocked
        }
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        int index = encode(row, col);
        if (row < 0 || col < 0) throw new IllegalArgumentException("Illegal Argument Exception");

        if(!isOpen(row, col)) {                           // check if not open, open then count number of opened sites
            grid[row][col] = true;
            countOpen++;

            if (row == 1) {                               // top row
                ufBackwash.union(top,index);
                uf.union(top,index);
            }
            if (row == N) {                               // bottom row
                ufBackwash.union(bottom, index);
            }

            if ((row - 1) >= 0 && isOpen(row - 1, col)) {       // check above
                uf.union(encode(row - 1, col), index);
                ufBackwash.union(encode(row - 1, col), index);
            }
            if ((col - 1) >= 0 && isOpen(row, col - 1)) {       // check left
                uf.union(encode(row, col - 1), index);
                ufBackwash.union(encode(row, col - 1), index);
            }
            if ((col + 1) < N && isOpen(row, col + 1)) {       // check right
                uf.union(encode(row, col + 1), index);
                ufBackwash.union(encode(row, col + 1), index);
            }
            if ((row + 1) < N && isOpen(row + 1, col)) {       // check below
                uf.union(encode(row + 1, col), index);
                ufBackwash.union(encode(row + 1, col), index);
            }
        }
    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N)
            throw new IllegalArgumentException("Illegal Argument Exception");
        return grid[row][col];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N)
            throw new IllegalArgumentException("Illegal Argument Exception");
        else if (isOpen(row, col) && (uf.find(top) == uf.find(encode(row, col)))) {
                return true;
        }
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return countOpen;
    }

    // Does the system percolate?
    public boolean percolates() {
        if (ufBackwash.find(top) == ufBackwash.find(bottom)) { // check if the top is connect with the bottom
            return true;
        }
        return false;
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N)
            throw new IllegalArgumentException("Illegal Argument Exception");
        else
            return (row * N + col + 1);         // convert grid coordinates to 1-D representative
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
