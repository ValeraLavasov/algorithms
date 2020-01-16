/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 10.01.2020
 *  Description: Brute implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);
        // Sort should not be performed here
        // Arrays.sort(points);
        // Correct one
        // (10000, 0) -> (0, 10000)
        // (3000, 4000) -> (20000, 21000)
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int g = k + 1; g < points.length; g++) {
                        if (isCollinear(points[i], points[j], points[k], points[g])) {
                            // Sort them here by order e.g. left to right??
                            Point[] arr = { points[i], points[j], points[k], points[g] };
                            Arrays.sort(arr);
                            lines.add(new LineSegment(arr[0], arr[arr.length - 1]));
                        }
                    }
                }
            }
        }
    }  // finds all line segments containing 4 points

    public int numberOfSegments() {
        return segments().length;

    } // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] segments = lines.toArray(new LineSegment[lines.size()]);
        return segments;
    } // the line segments

    private void validatePoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null not permitted");

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("Null not permitted");
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Dups are not permitted");
                }
            }
        }
    }

    private boolean isCollinear(Point p1, Point p2, Point p3, Point p4) {
        double slope = p1.slopeTo(p2);
        return Double.compare(slope, p1.slopeTo(p3)) == 0
                && Double.compare(slope, p1.slopeTo(p4)) == 0;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
