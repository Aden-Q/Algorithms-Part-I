/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private static final int SPACE = 0;
    private int[][] blocks;
    private final int n;

    // construct a board from an n-by-n array of blocks
    // where blocks[i][j] = block in rowi, column j
    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[0].length; j++)
                this.blocks[i][j] = blocks[i][j];
    }

    // board dimension n
    public int dimension() { return n; }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int curBlock = blocks[i][j];
                if (curBlock != SPACE && !isInplace(i, j))
                    count++;
            }
        }

        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int curBlock = blocks[i][j];
                if (curBlock != SPACE && !isInplace(i, j))
                    manDist += Math.abs(i - row(curBlock)) + Math.abs(j - col(curBlock));
            }
        }

        return manDist;
    }

    private boolean isInplace(int row, int col) {
        int r = row(blocks[row][col]);
        int c = col(blocks[row][col]);
        return (r == row) && (c == col);
    }

    // return row which the block belongs to in the right order
    private int row(int block) { return (block-1) / n; }

    // return column which the block belongs to in the right order
    private int col(int block)  { return (block-1) % n; }

    // is this board the goal board?
    public boolean isGoal() { return hamming() == 0; }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n-1; j++) {
                int curBlock1 = this.blocks[i][j];
                int curBlock2 = this.blocks[i][j+1];
                if (curBlock1 != SPACE && curBlock2 != SPACE)
                    return new Board(swap(i, j, i, j+1));
            }
        }

        return null;
    }

    // swap two element in the original metric
    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] blocksCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                blocksCopy[i][j] = this.blocks[i][j];
        }
        int tmp = blocksCopy[row1][col1];
        blocksCopy[row1][col1] = blocksCopy[row2][col2];
        blocksCopy[row2][col2] = tmp;

        return blocksCopy;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        else if (y == null)
            return false;
        else if (y.getClass() != this.getClass())
            return false;
        else if (((Board)y).blocks.length != n)
            return false;
        else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (((Board)y).blocks[i][j] != this.blocks[i][j])
                        return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborList = new ArrayList<>();
        int spaceRow = 0;
        int spaceCol = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == SPACE) {
                    spaceRow = i;
                    spaceCol = j;
                    break;
                }
            }
        }

        if (spaceRow > 0) neighborList.add(new Board(swap(spaceRow, spaceCol, spaceRow-1, spaceCol)));
        if (spaceRow < n-1) neighborList.add(new Board(swap(spaceRow, spaceCol, spaceRow+1, spaceCol)));
        if (spaceCol > 0) neighborList.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol-1)));
        if (spaceCol < n-1) neighborList.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol+1)));

        return neighborList;
    }

    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(n);
        res.append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                stringAppend(res, i, j);
            if (i != n-1)
                res.append('\n');
        }

        return res.toString();
    }

    // string builder append according to number of digits
    private void stringAppend(StringBuilder res, int row, int col) {
        if (this.n < 11) {
            if (col == 0)
                res.append(String.format("%2d", blocks[row][col]));
            else
                res.append(String.format("%3d", blocks[row][col]));
        }
        else if (this.n < 32) {
            if (col == 0)
                res.append(String.format("%3d", blocks[row][col]));
            else
                res.append(String.format("%4d", blocks[row][col]));
        }
        else if (this.n < 101) {
            if (col == 0)
                res.append(String.format("%4d", blocks[row][col]));
            else
                res.append(String.format("%5d", blocks[row][col]));
        }
        else {
            if (col == 0)
                res.append(String.format("%5d", blocks[row][col]));
            else
                res.append(String.format("%6d", blocks[row][col]));
        }
    }

    // only used for unit test
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int num = in.readInt();
        int[][] blocks = new int[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board board = new Board(blocks);
        StdOut.println(board);
        // StdOut.println(board.hamming());
        // StdOut.println(board.manhattan());
        // StdOut.println(board.twin());
        // Iterator<Board> iter = board.neighbors().iterator();
        // for (Board bd : board.neighbors())
        //     StdOut.println(bd);
    }
}
