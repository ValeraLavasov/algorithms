/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 23.01.2020
 *  Description: Point set bassed off red-black BST
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private final SET<Point2D> bts;

    public PointSET() {
        bts = new SET<Point2D>();
    } // construct an empty set of points

    public boolean isEmpty() {
        return bts.isEmpty();
    } // is the set empty?

    public int size() {
        return bts.size();
    } // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        bts.add(p);
    } // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return bts.contains(p);
    } // does the set contain point p?

    public void draw() {
        for (Point2D p : bts)
            p.draw();
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> range = new Stack<>();
        for (Point2D p : bts) {
            if (rect.contains(p)) range.push(p);
        }
        return range;
    } // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (bts.isEmpty()) return null;

        Point2D nearestPoint = bts.min();
        double nearestDistance = p.distanceSquaredTo(nearestPoint);

        for (Point2D point : bts) {
            double distance = p.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                nearestPoint = point;
                nearestDistance = distance;
            }
        }
        return nearestPoint;
    } // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.1, 0.3));
        set.draw();
    } // unit testing of the methods (optional)
}
