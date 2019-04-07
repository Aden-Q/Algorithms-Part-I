/* *****************************************************************************
 *  Name: Zecheng Qian
 *  Date: Dec.10 2018
 *  Description: Percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte BLOCKED = 1;
    private static final byte OPEN = 2;
    private static final byte CONNECT_TO_TOP = 4;
    private static final byte CONNECT_TO_BOTTTOM = 8;

    private byte[] siteStatus;
    private int sideLength;
    private WeightedQuickUnionUF uf;
    private int numOpen;
    private boolean percolated;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal Argument, n must be greater than 0!");
        }
        else {
            numOpen = 0;
            percolated = false;
            sideLength = n;
            siteStatus = new byte[n*n + 1];
            uf = new WeightedQuickUnionUF(n*n + 1);
            // all blocks in the initial state
            for (int i = 1; i < n*n + 1; i++)
                siteStatus[i] = BLOCKED;
        }
    }

    // convert 2D index to 1D index
    private int indexConvert(int row, int col) {
        return (row-1)*sideLength + col;
    }

    // open one entry if it is not open
    public void open(int row, int col) {
        if (row > sideLength || col > sideLength || row < 1 || col < 1) {
            throw new IllegalArgumentException("Illegal Argument!");
        }

        byte[] rootStatus = {OPEN, OPEN, OPEN, OPEN};
        int rootIndex;
        int index = indexConvert(row, col);
        int adjacent;
        if (siteStatus[index] == BLOCKED) {
            if (row == 1)
                siteStatus[index] = CONNECT_TO_TOP;
            else if (row == sideLength)
                siteStatus[index] = CONNECT_TO_BOTTTOM;
            else
                siteStatus[index] = OPEN;
            numOpen++;
        }

        if (row > 1) {
            adjacent = indexConvert(row - 1, col);
            if (siteStatus[adjacent] != BLOCKED) {
                rootStatus[0] = siteStatus[uf.find(adjacent)];
                uf.union(index, adjacent);
            }
        }
        if (row < sideLength) {
            adjacent = indexConvert(row+1, col);
            if (siteStatus[adjacent] != BLOCKED) {
                rootStatus[1] = siteStatus[uf.find(adjacent)];
                uf.union(index, adjacent);
            }
        }
        if (col > 1) {
            adjacent = indexConvert(row, col-1);
            if (siteStatus[adjacent] != BLOCKED) {
                rootStatus[2] = siteStatus[uf.find(adjacent)];
                uf.union(index, adjacent);
            }
        }
        if (col < sideLength) {
            adjacent = indexConvert(row, col+1);
            if (siteStatus[adjacent] != BLOCKED) {
                rootStatus[3] = siteStatus[uf.find(adjacent)];
                uf.union(index, adjacent);
            }
        }

        rootIndex = uf.find(index);
        for (int i = 0; i < 4; i++) {
            if (siteStatus[rootIndex] == CONNECT_TO_TOP) {
                if (siteStatus[index] == CONNECT_TO_BOTTTOM)
                    percolated = true;
                break;
            }
            else if (rootStatus[i] == CONNECT_TO_TOP || siteStatus[index] == CONNECT_TO_TOP) {
                if (siteStatus[rootIndex] == CONNECT_TO_BOTTTOM)
                    percolated = true;
                siteStatus[rootIndex] = CONNECT_TO_TOP;
            }
            else if (rootStatus[i] == CONNECT_TO_BOTTTOM || siteStatus[index] == CONNECT_TO_BOTTTOM) {
                siteStatus[rootIndex] = CONNECT_TO_BOTTTOM;
            }
        }

        int countBottom = 0;
        int countTop = 0;
        for (int i = 0; i < 4; i++) {
            if (rootStatus[i] == CONNECT_TO_BOTTTOM)
                countBottom++;
            if (rootStatus[i] == CONNECT_TO_TOP)
                countTop++;
        }
        if (siteStatus[index] == CONNECT_TO_BOTTTOM)
            countBottom++;
        if (siteStatus[index] == CONNECT_TO_TOP)
            countTop++;
        if (countBottom > 0 && countTop > 0)
            percolated = true;
    }

    // judge open or not
    public boolean isOpen(int row, int col) {
        if (row > sideLength || col > sideLength || row < 1 || col < 1) {
            throw new IllegalArgumentException("Illegal Argument!");
        }

        int index = indexConvert(row, col);
        return !(siteStatus[index] == BLOCKED);
    }

    // full or not
    public boolean isFull(int row, int col) {
        if (row > sideLength || col > sideLength || row < 1 || col < 1) {
            throw new IllegalArgumentException("Illegal Argument!");
        }

        int index = indexConvert(row, col);

        return siteStatus[uf.find(index)] == CONNECT_TO_TOP;
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        if (sideLength == 1 && numberOfOpenSites() == 1)
            return true;
        else
            return percolated;
    }

    public static void main(String[] args) {

    }
}