/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() { this.pointSet = new TreeSet<Point2D>(); }

    // is the set empty?
    public boolean isEmpty() { return size() == 0; }

    // number of points in the set
    public int size() { return pointSet.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");

        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        final double eps = 0.000001;

        if (rect == null)
            throw new java.lang.IllegalArgumentException("RectHV rect is null!");

        TreeSet<Point2D> pointsInRange = new TreeSet<Point2D>();

        for (Point2D point : pointSet) {
            if (point.x() < rect.xmax() + eps && point.x() > rect.xmin() - eps && point.y() < rect.ymax() + eps && point.y() > rect.ymin() - eps) {
                pointsInRange.add(point);
            }
        }

        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");
        if (size() == 0)
            return null;

        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = new Point2D(1, 1);

        for (Point2D point : pointSet) {
            double curDist = point.distanceSquaredTo(p);
            if (curDist < minDist) {
                minDist = curDist;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        StdOut.println("Hello, world!");

        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

        // Point2D target = new Point2D(0.5, 0.5);
        // StdOut.println(brute.nearest(target));
    }
}