/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {
    private ArrayList<LineSegment> segmentArray;
    private Point[] pointsCopy;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // exceptions
        if (points == null)
            throw new java.lang.IllegalArgumentException("Null argument!");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException("Null argument!");
            for (int j = i-1; j >= 0; j--) {
                if (points[j].compareTo(points[i]) == 0)
                    throw new java.lang.IllegalArgumentException("Duplicate values!");
            }
        }

        // main routine
        segmentArray = new ArrayList<>();
        pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int p = 0; p < points.length-3; p++)
            for (int q = p+1; q < points.length-2; q++)
                for (int r = q+1; r < points.length-1; r++)
                    for (int s = r+1; s < points.length; s++) {
                        if (pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(pointsCopy[r]))
                            if (pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(pointsCopy[s]))
                                segmentArray.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                    }
    }

    // the number of line segments
    public int numberOfSegments() { return segmentArray.size(); }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegs = new LineSegment[segmentArray.size()];
        return segmentArray.toArray(lineSegs);
    }

    public static void main(String[] args) {
        // readin data from files
        String filename = args[0];
        In in = new In(filename);
        int num = in.readInt();
        int count = 0;
        Point[] points = new Point[num];
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            points[count++] = new Point(x, y);
        }
        BruteCollinearPoints brute = new BruteCollinearPoints(points);

        // visualize the result
        StdDraw.clear();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.show();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdOut.println(brute.numberOfSegments());
        for (LineSegment line : brute.segments()) {
            line.draw();
        }
        for (Point point: points) {
            point.draw();
        }
        StdDraw.show();
    }
}
