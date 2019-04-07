/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segArray;
    private List<Point> startPoints;
    private List<Point> endPoints;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        segArray = new ArrayList<>();
        startPoints = new ArrayList<>();
        endPoints = new ArrayList<>();
        // make a copy to avoid modifying the original array
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Point[] slopeSet = new Point[points.length];
        // sort the array based on slope order, n passes
        // for each pass, examine at most three points a time (otherwise update stableSlope)
        for (Point basePoint : points) {
            Arrays.sort(pointsCopy, basePoint.slopeOrder());
            slopeSet[0] = basePoint;
            int count = 1;
            double stableSlope = Double.NEGATIVE_INFINITY;
            for (int i = 1; i < points.length; i++) {
                double curSlope = basePoint.slopeTo(pointsCopy[i]);
                if (Double.compare(curSlope, stableSlope) > 0) {
                    if (count >= 4) {
                        Arrays.sort(slopeSet, 0, count);
                        addSegment(slopeSet[0], slopeSet[count-1]);
                        count = 1;
                        slopeSet[count-1] = basePoint;
                    }
                    stableSlope = curSlope;
                    count = 1;
                }
                slopeSet[count++] = pointsCopy[i];
            }
            if (count >= 4) {
                Arrays.sort(slopeSet, 0, count);
                addSegment(slopeSet[0], slopeSet[count-1]);
            }
        }
    }

    // use a List to avoid duplicate segments (HashMap is optimal)
    private void addSegment(Point start, Point end) {
        for (int i = 0; i < startPoints.size(); i++) {
            if (start.compareTo(startPoints.get(i)) == 0) {
                if (end.compareTo(endPoints.get(i)) == 0)
                    return;
            }
        }
        startPoints.add(start);
        endPoints.add(end);
        segArray.add(new LineSegment(start, end));
    }

    // the number of line segments
    public int numberOfSegments() { return segArray.size(); }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegs = new LineSegment[segArray.size()];
        return segArray.toArray(lineSegs);
    }

    // unit test (optional)
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
        FastCollinearPoints fast = new FastCollinearPoints(points);

        // visualize the result
        StdDraw.clear();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (LineSegment line : fast.segments()) {
            line.draw();
        }
        for (Point point: points) {
            point.draw();
        }
        StdDraw.show();
    }
}
