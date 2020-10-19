# Percolation Project

# Problem 1 (Percolation.java)
The model:
We model a percolation system using an n-by-n grid of sites. Each site is either open
or blocked. A full site is an open site that can be connected to an open site in the top row via a
chain of neighboring (left, right, up, down) open sites. We say the system percolates if there is a
full site in the bottom row. In other words, a system percolates if we fill all open sites connected
to the top row and that process fills some open site on the bottom row.
Test: input10.txt and input10-no.txt
Time complexity: O(n^2)
Backwash problem solved.

# Problem 2 (PercolationStats.java)
Using Monte Carlo simulation to write a program estimate p*
