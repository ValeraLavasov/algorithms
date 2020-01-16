/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 11.01.2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
        validatePoints(points);
        Point[] copyForOrigins = Arrays.copyOf(points, points.length);
        Point[] copyForIterations = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point originPoint = copyForOrigins[i];
            Point lineStart = null;
            int lineSize = 0;

            Arrays.sort(copyForIterations);
            Arrays.sort(copyForIterations, originPoint.slopeOrder());
            for (int k = 0; k < copyForIterations.length - 1; k++) {
                lineSize++;
                if (originPoint.slopeTo(copyForIterations[k]) == originPoint
                        .slopeTo(copyForIterations[k + 1])) {
                    if (lineSize == 1) {
                        lineStart = copyForIterations[k];
                    }
                    if (k + 1 == copyForIterations.length - 1) {
                        lineSize++;
                        if (lineSize >= 3 && lineStart.compareTo(originPoint) > 0) {
                            lines.add(new LineSegment(originPoint, copyForIterations[k + 1]));
                        }
                    }
                }
                else {
                    if (lineSize >= 3 && lineStart.compareTo(originPoint) > 0) {
                        lines.add(new LineSegment(originPoint, copyForIterations[k]));
                    }
                    lineSize = 0;
                }
            }
        }
    }    // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return segments().length;
    }     // the number of line segments

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
