/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;


public class KdTree {

    private static class Node {
        private Point2D p;  // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private boolean flip;  // coordinate flip or not, set x-split as default
        private Node lb;    // the left/bottom subtree
        private Node rt;    // the right/top subtree

        public Node(Point2D p, RectHV rect, boolean flip) {
            this.p = p;
            this.rect = rect;
            this.flip = flip;
            this.lb = null;
            this.rt = null;
        }
    }

    private Node root;
    private int size;
    private double nearestDist;
    private Point2D nearestPoint;

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
        this.nearestDist = Double.POSITIVE_INFINITY;
        this.nearestPoint = null;
    }

    // is the tree empty?
    public boolean isEmpty() { return size() == 0; }

    // number of points in the tree
    public int size() { return this.size; }

    // add the point to the set (if it is not already in the tree), iterative version done!
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");

        final double eps = 0.000001;
        if (this.root == null) {
            this.root = new Node(p, new RectHV(0, 0, 1, 1), false);
            this.size++;
        }
        else {
            Node cur = root;
            boolean flip = cur.flip;

            while (!cur.p.equals(p)) {
                if (!flip) {
                    if (p.x() < cur.p.x()-eps) {
                        if (cur.lb != null)
                            cur = cur.lb;
                        else {
                            RectHV parent = cur.rect;
                            double newXmin = parent.xmin();
                            double newXmax = cur.p.x();
                            double newYmin = parent.ymin();
                            double newYmax = parent.ymax();
                            cur.lb = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax),
                                              true);
                            this.size++;
                            return;
                        }
                    }
                    else {
                        if (cur.rt != null)
                            cur = cur.rt;
                        else {
                            RectHV parent = cur.rect;
                            double newXmin = cur.p.x();
                            double newXmax = parent.xmax();
                            double newYmin = parent.ymin();
                            double newYmax = parent.ymax();
                            cur.rt = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax),
                                              true);
                            this.size++;
                            return;
                        }
                    }
                }
                else {
                    if (p.y() < cur.p.y()-eps) {
                        if (cur.lb != null)
                            cur = cur.lb;
                        else {
                            RectHV parent = cur.rect;
                            double newXmin = parent.xmin();
                            double newXmax = parent.xmax();
                            double newYmin = parent.ymin();
                            double newYmax = cur.p.y();
                            cur.lb = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax),
                                              false);
                            this.size++;
                            return;
                        }
                    }
                    else {
                        if (cur.rt != null)
                            cur = cur.rt;
                        else {
                            RectHV parent = cur.rect;
                            double newXmin = parent.xmin();
                            double newXmax = parent.xmax();
                            double newYmin = cur.p.y();
                            double newYmax = parent.ymax();
                            cur.rt = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax),
                                              false);
                            this.size++;
                            return;
                        }
                    }
                }
                flip = !flip;
            }
        }
    }

    // does the tree contains point p? easy iterative implementation
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");
        if (root == null)
            return false;

        final double eps = 0.0000001;
        Node cur = root;
        boolean flip = cur.flip;
        while (cur != null) {
            if (p.equals(cur.p))
                return true;
            else if (!flip) {
                if (p.x() < cur.p.x()-eps)
                    cur = cur.lb;
                else
                    cur = cur.rt;
            }
            else {
                if (p.y() < cur.p.y()-eps)
                    cur = cur.lb;
                else
                    cur = cur.rt;
            }
            flip = !flip;
        }

        return false;
    }

    // draw all points to standard draw, easy recursive version
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null)
            return;
        node.p.draw();
        draw(node.lb);
        draw(node.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("RectHV rect is null!");

        if (this.root == null)
            return new ArrayList<Point2D>();
        else
            return range(this.root, rect);
    }

    private ArrayList<Point2D> range(Node cur, RectHV rect) {
        ArrayList<Point2D> pointTree = new ArrayList<Point2D>();
        if (cur.rect.intersects(rect)) {
            if (rect.contains(cur.p))
                pointTree.add(cur.p);
            if (cur.lb != null)
                pointTree.addAll(range(cur.lb, rect));
            if (cur.rt != null)
                pointTree.addAll(range(cur.rt, rect));
        }

        return pointTree;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("Point2D p is null!");

        if (root == null)
            return null;
        else {
            this.nearestDist = Double.POSITIVE_INFINITY;
            nearest(root, p);
        }

        return this.nearestPoint;
    }

    private void nearest(Node cur, Point2D p) {
        if (cur == null)
            return;

        double curDist = cur.p.distanceSquaredTo(p);
        if (curDist < nearestDist) {
            nearestDist = curDist;
            nearestPoint = cur.p;
        }

        if (!cur.flip) {
            // first left then right or first right then left
            if (p.x() < cur.p.x()) {
                nearest(cur.lb, p);
                // may need improving (distance)
                if (cur.rt != null && cur.rt.rect.distanceSquaredTo(p) < nearestDist)
                    nearest(cur.rt, p);
            }
            else {
                nearest(cur.rt, p);
                // may need improving (distance)
                if (cur.lb != null && cur.lb.rect.distanceSquaredTo(p) < nearestDist)
                    nearest(cur.lb, p);
            }
        }
        else {
            if (p.y() < cur.p.y()) {
                nearest(cur.lb, p);
                // may need improving (distance)
                if (cur.rt != null && cur.rt.rect.distanceSquaredTo(p) < nearestDist)
                    nearest(cur.rt, p);
            }
            else {
                nearest(cur.rt, p);
                // may need improving (distance)
                if (cur.lb != null && cur.lb.rect.distanceSquaredTo(p) < nearestDist)
                    nearest(cur.lb, p);
            }
        }
    }

    // main entrance
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdtree.draw();
        StdDraw.show();

        // Point2D p = new Point2D(0.5, 0.5);
        // Point2D q = new Point2D(0.5, 0.5);
        // StdOut.println(p.equals(q));
    }
}