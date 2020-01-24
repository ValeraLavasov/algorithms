/* *****************************************************************************
 *  Name: Valerii Lavasov
 *  Date: 23.01.2020
 *  Description: Kd implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private Node root;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;       // size of all subtrees

        public Node(Point2D point, RectHV rec, Node left, Node right, int s) {
            p = point;
            rect = rec;
            lb = left;
            rt = right;
            size = s;
        } // construct an empty set of points
    }

    public KdTree() {

    } // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    } // is the set empty?

    public int size() {
        return size(root);
    } // number of points in the set

    private int size(Node x) {
        if (x == null) return 0;

        return x.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), null, null, 1);
        root = insertNode(root, p, 1, null, 0);
    } // add the point to the set (if it is not already in the set)

    private Node insertNode(Node x, Point2D p, int level, Node parent, int direction) {
        if (x == null) {
            double minX;
            double minY;
            double maxX;
            double maxY;
            // direction: 0 - left/bottom; 1 -right/up
            if (level % 2 == 0) {
                // Horizontal split
                // this should be parents x
                if (direction == 0) {
                    minX = parent.rect.xmin();
                    maxX = parent.p.x();
                }
                else {
                    minX = parent.p.x();
                    maxX = parent.rect.xmax();
                }
                maxY = parent.rect.ymax();
                minY = parent.rect.ymin();
            }
            else {
                // set as X // vertival split
                if (direction == 0) {
                    minY = parent.rect.ymin();
                    maxY = parent.p.y();
                }
                else {
                    minY = parent.p.y();
                    maxY = parent.rect.ymax();
                }
                maxX = parent.rect.xmax();
                minX = parent.rect.xmin();
            }
            return new Node(p, new RectHV(minX, minY, maxX, maxY), null, null, 1);
        }
        double thatC;
        double thisC;

        if (level % 2 == 0) {
            // set as Y
            thatC = p.y();
            thisC = x.p.y();
        }
        else {
            // set as X
            thatC = p.x();
            thisC = x.p.x();
        }
        if (thatC < thisC) x.lb = insertNode(x.lb, p, level + 1, x, 0);
        else if (p.compareTo(x.p) == 0) return x;
        else x.rt = insertNode(x.rt, p, level + 1, x, 1);

        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return containsPoint(root, p, 1);
    } // does the set contain point p?

    private boolean containsPoint(Node x, Point2D p, int level) {
        if (x == null) return false;
        double thatC;
        double thisC;
        boolean result;

        if (level % 2 == 0) {
            // set as Y
            thatC = p.y();
            thisC = x.p.y();
        }
        else {
            // set as X
            thatC = p.x();
            thisC = x.p.x();
        }

        if (thatC < thisC) result = containsPoint(x.lb, p, level + 1);
        else if (p.compareTo(x.p) == 0) result = true;
        else result = containsPoint(x.rt, p, level + 1);

        return result;
    }

    public void draw() {
        drawFrom(root, 1);
    } // draw all points to standard draw

    private void drawFrom(Node x, int level) {
        if (x == null) return;
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            // vertical
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        else {
            //  horizontal
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x.p.x(), x.p.y());

        drawFrom(x.lb, level + 1);
        drawFrom(x.rt, level + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> range = new Stack<>();
        findRangePoints(range, root, rect);
        return range;
    } // all points that are inside the rectangle (or on the boundary)

    private void findRangePoints(Stack<Point2D> storage, Node x, RectHV rect) {
        if (x == null) return;
        if (x.rect.intersects(rect)) {
            findRangePoints(storage, x.rt, rect);
            findRangePoints(storage, x.lb, rect);
            if (rect.contains(x.p)) storage.push(x.p);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        // return findNearest(p, root, null, Double.POSITIVE_INFINITY);
        return findNearest(p, root, null, Double.POSITIVE_INFINITY, 1);
    } // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D findNearest(Point2D point, Node x, Point2D champion, double champDistance,
                                int level) {
        if (x.p.distanceSquaredTo(point) < champDistance) {
            champion = x.p;
            champDistance = champion.distanceSquaredTo(point);
        }
        if (level % 2 == 0) {
            // Horizontal split
            // So to select correct order I need to add here choose the sub-tree that is on the same side
            if (point.y() < x.p.y()) {
                // go bot
                if (x.lb != null) {
                    champion = findNearest(point, x.lb, champion, champDistance, level + 1);
                    champDistance = champion.distanceSquaredTo(point);
                }
                if (x.rt != null && x.rt.rect.distanceSquaredTo(point) < champDistance) {
                    champion = findNearest(point, x.rt, champion, champDistance, level + 1);
                }
            }
            else {
                // go up
                if (x.rt != null) {
                    champion = findNearest(point, x.rt, champion, champDistance, level + 1);
                    champDistance = champion.distanceSquaredTo(point);
                }
                if (x.lb != null && x.lb.rect.distanceSquaredTo(point) < champDistance) {
                    champion = findNearest(point, x.lb, champion, champDistance, level + 1);
                }
            }
        }
        else {
            // Vertical split
            if (point.x() < x.p.x()) {
                // go left
                if (x.lb != null) {
                    champion = findNearest(point, x.lb, champion, champDistance, level + 1);
                    champDistance = champion.distanceSquaredTo(point);
                }
                if (x.rt != null && x.rt.rect.distanceSquaredTo(point) < champDistance) {
                    champion = findNearest(point, x.rt, champion, champDistance, level + 1);
                }
            }
            else {
                // go right
                if (x.rt != null) {
                    champion = findNearest(point, x.rt, champion, champDistance, level + 1);
                    champDistance = champion.distanceSquaredTo(point);
                }
                if (x.lb != null && x.lb.rect.distanceSquaredTo(point) < champDistance) {
                    champion = findNearest(point, x.lb, champion, champDistance, level + 1);
                }
            }
        }
        return champion;
    }
    // private Point2D findNearest(Point2D point, Node x, Point2D champion, double champDistance) {
    //     StdOut.println(x.p);
    //     if (x.p.distanceSquaredTo(point) < champDistance) {
    //         champion = x.p;
    //         champDistance = champion.distanceSquaredTo(point);
    //     }
    //     // So to select correct order I need to add here choose the sub-tree that is on the same side
    //     if (x.lb != null && x.lb.rect.distanceSquaredTo(point) < champDistance) {
    //         champion = findNearest(point, x.lb, champion, champDistance);
    //         champDistance = champion.distanceSquaredTo(point);
    //     }
    //     if (x.rt != null && x.rt.rect.distanceSquaredTo(point) < champDistance) {
    //         champion = findNearest(point, x.rt, champion, champDistance);
    //     }
    //
    //     return champion;
    // }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        StdOut.println(tree.isEmpty());
        tree.insert(new Point2D(0.7, 0.2));
        StdOut.println(tree.size());
        tree.insert(new Point2D(0.5, 0.4));
        StdOut.println(tree.size());
        StdOut.println(tree.isEmpty());
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        StdOut.println(tree.size());
        StdOut.println(tree.contains(new Point2D(0.1, 0.4)));
        StdOut.println(tree.contains(new Point2D(0.3, 0.4)));
        tree.draw();
    } // unit testing of the methods (optional)
}
