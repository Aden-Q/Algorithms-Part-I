/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Solver {
    private PQBoard lastPQBoard;

    private class PQBoard implements Comparable<PQBoard> {
        private int numMoves;
        private Board board;
        private PQBoard prev;

        public PQBoard(Board cur, PQBoard prevBoard) {
            this.board = cur;
            this.prev = prevBoard;
            if (prevBoard == null)   numMoves = 0;
            else    numMoves = prev.numMoves + 1;
        }

        public int compareTo(PQBoard that) { return this.board.manhattan() + this.numMoves - that.board.manhattan() - that.numMoves; }
    }


    // find a solution to the initial board (using A* algorithm)
    public Solver(Board initial) {
        if (initial == null)    throw new java.lang.IllegalArgumentException("Null argument!");

        MinPQ<PQBoard> pq;
        MinPQ<PQBoard> pqTwin;  // because either pq or pqTwin is solvable
        PQBoard lastTwin = null;
        pq = new MinPQ<>();
        pq.insert(new PQBoard(initial, null));
        pqTwin = new MinPQ<>();
        pqTwin.insert(new PQBoard(initial.twin(), null));
        this.lastPQBoard = null;

        // solve the original and its twin (either can be solved but not both)
        while (lastPQBoard == null && lastTwin == null) {
            lastPQBoard = pqSolve(pq);
            lastTwin = pqSolve(pqTwin);
        }
    }

    private PQBoard pqSolve(MinPQ<PQBoard> p) {
        if (p == null || p.isEmpty())
            return null;
        PQBoard bestBoard = p.delMin();
        if (bestBoard.board.isGoal())
            return bestBoard;
        else {
            for (Board bd : bestBoard.board.neighbors()) {
                if (bestBoard.prev == null || !bd.equals(bestBoard.prev.board)) {
                    p.insert(new PQBoard(bd, bestBoard));
                }
            }
        }

        return null;    // return null if not solved yet
    }

    // is the initial board solvable?
    public boolean isSolvable() { return lastPQBoard != null; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return lastPQBoard.numMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> boardStack = new Stack<>();
        Stack<Board> resStack = new Stack<>();
        PQBoard curPQBoard = lastPQBoard;   // ensure immutability
        while (curPQBoard != null) {
            boardStack.add(curPQBoard.board);
            curPQBoard = curPQBoard.prev;
        }
        while (!boardStack.isEmpty())
            resStack.push(boardStack.pop());

        return resStack;
    }

    // unit test client, solve a slider puzzle
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
