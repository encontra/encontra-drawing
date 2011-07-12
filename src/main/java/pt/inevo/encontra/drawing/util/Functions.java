package pt.inevo.encontra.drawing.util;

import pt.inevo.encontra.geometry.Point;
import pt.inevo.encontra.geometry.Vector;
import pt.inevo.jcali.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class Functions {

    private static NumberFormat timeFormatter = new DecimalFormat("#00");

	/** Computes the distance between two CIPoints.
	 *
	 * @param p1	The first CIPoint.
	 * @param p2	The second CIPoint.
	 *
	 * @return	The distance between the points.
	 */
	public static double dist(CIPoint p1, CIPoint p2) {

		return Math.sqrt( dist_squared(p1,p2) );
	}


	/**
	 * Computes the squared distance between two Points.
	 *
	 * @param p1	First point.
	 * @param p2	Second point.
	 *
	 * @return	The squared distance between p1 and p2.
	 */
	public static double dist_squared(CIPoint p1, CIPoint p2) {
		double dx, dy;

		dx = (p1.x) - (p2.x);
		dy = (p1.y) - (p2.y);

	    return dist_squared(dx, dy);
	}


	/**
	 * Calculates the inner product between this and another Vector.
	 * 
	 * @param dx	delta x.
	 * @param dy	delta y
	 *
	 * @return	The distance between the delta's.
	 */
	public static double dist(double dx, double dy) {

		return Math.sqrt( dist_squared(dx ,dy) );
	}


	/**
	 * Computes the squared distance between two Points.
	 *
	 * @param dx	delta x.
	 * @param dy	delta y
	 *
	 * @return	The squared distance between the delta's.
	 */
	public static double dist_squared(double dx, double dy) {

		return ( (dx*dx) + (dy*dy) );
	}


	/**
	 * Returns the maximum of three doubles
	 *
	 * @param v1	Value 1.
	 * @param v2	Value 2.
	 * @param v3	Value 3.
	 *
	 * @return	the maximum of the three doubles
	 */
	public static double max(double v1, double v2, double v3) {
		double v = (v1 > v2) ? v1 : v2;
		v = (v3 > v) ? v3 : v;
		return v;
	}

	/**
	 * Returns the minimum of three doubles
	 *
	 * @param v1	Value 1.
	 * @param v2	Value 2.
	 * @param v3	Value 3.
	 *
	 * @return	the minimum of the three doubles
	 */
	public static double min(double v1, double v2, double v3) {
		double v = (v1 < v2) ? v1 : v2;
		v = (v3 < v) ? v3 : v;
		return v;
	}

    /**
     * TODO Gabe: documentar
     * @param p1
     * @param p2
     * @return
     */
    public static double distanceSquared(Point p1, Point p2) {
        double dx, dy;

	dx = (p1.getX()) - (p2.getX());
	dy = (p1.getY()) - (p2.getY());

        return distanceSquared(dx, dy);
    }

    /**
     * TODO Gabe: documentar
     * @param dx
     * @param dy
     * @return
     */
    public static double distanceSquared(double dx, double dy) {
        return ( (dx*dx) + (dy*dy) );
    }

    // TODO Gabe: documentar
    public static Point findPointQuadraticBezier(Point P0, Point P1, Point P2, double t) {
        return P0.scale(Math.pow((1-t), 2)).translate(P1.scale(2*(1-t)*t)).translate(P2.scale(Math.pow(t, 2)));
    }

    // TODO Gabe: documentar
    public static Point findPointCubicBezier(Point P0, Point P1, Point P2, Point P3, double t) {
        return P0.scale(Math.pow((1-t), 3)).translate(P1.scale(3*Math.pow((1-t), 2)*t)).
                translate(P2.scale(3*(1-t)*Math.pow(t, 2))).translate(P3.scale(Math.pow(t, 3)));
    }

    // TODO Gabe: documentar
    public static Point reflectControlPoint(Point P2, Point P3) {
        return P3.subtract(P2.subtract(P3));
    }

    // TODO Gabe: documentar
    public static ArrayList<Point> findPointsQuadraticBezier(Point P0, Point P1, Point P2, int n){
        double inc = 1/n;
        ArrayList<Point> points = new ArrayList<Point>();
        for (double i = 0; i <= 1; i += inc) {
            points.add(findPointQuadraticBezier(P0, P1, P2, i));
        }
        return points;
    }

    // TODO Gabe: documentar
    public static ArrayList<Point> findPointsCubicBezier(Point P0, Point P1, Point P2, Point P3, int n){
        double inc = (double)1/n;
        ArrayList<Point> points = new ArrayList<Point>();
        for (double i = 0; i <= 1; i += inc) {
            points.add(findPointCubicBezier(P0, P1, P2, P3, i));
        }
        return points;
    }

    /**
     * Check:
     * http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
     * http://www.w3.org/TR/SVG/implnote.html#ArcImplementationNotes
     * @param start
     * @param rx
     * @param ry
     * @param axisRotation
     * @param largeArcFlag
     * @param sweepFlag
     * @param end
     * @param t
     * @return
     */
    public static Point findPointArc(Point start, double rx, double ry, double axisRotation, boolean largeArcFlag, boolean sweepFlag, Point end, double t) {
        double x, y, dtheta, cx, cy, theta1;
        dtheta = (largeArcFlag ? 180.0 : 0.0) + (t * 180) * (sweepFlag ? 1 : -1);
        //x2 = (cos(fi) * rx cos(theta1 + delta*theta) + cx) - (sin(fi) * rx cos(theta1 + delta*theta) + cx)
        //y2 = (sin(fi) * ry sin(theta1 + delta*theta) + cy) + (cos(fi) * ry sin(theta1 + delta*theta) + cy)

        // Start of Evil Math
        double x1p = Math.cos(axisRotation) * ((start.getX() - end.getX())/2.0) +
                Math.sin(axisRotation) * ((start.getY() - end.getY())/2);
        double y1p = - Math.sin(axisRotation) * ((start.getX() - end.getX())/2.0) +
                Math.cos(axisRotation) * ((start.getY() - end.getY())/2);

        double cp = Math.sqrt(
                (Math.pow(rx, 2) * Math.pow(ry, 2) -
                 Math.pow(rx, 2) * Math.pow(y1p, 2) -
                 Math.pow(ry, 2) * Math.pow(x1p, 2))
                /
                (Math.pow(rx, 2) * Math.pow(y1p, 2) +
                 Math.pow(ry, 2) * Math.pow(x1p, 2))) * (largeArcFlag == sweepFlag ? -1 : 1);
        double cxp = cp * (rx*y1p/ry);
        double cyp = cp * -(ry*x1p/rx);

        cx = Math.cos(axisRotation) * cxp - Math.sin(axisRotation) * cyp + (start.getX() + end.getX())/2.0;
        cy = Math.sin(axisRotation) * cxp + Math.cos(axisRotation) * cyp + (start.getY() + end.getY())/2.0;

        Vector v1 = new Vector();
        v1.setDX(1.0);
        v1.setDY(0.0);

        Vector v2 = new Vector();
        v2.setDX((x1p - cxp)/(float)rx);
        v2.setDY((y1p-cyp)/(float)ry);

        theta1 = v1.angle(v2);

        // End of Evil Math

        x = (Math.cos(axisRotation) * rx * Math.cos(theta1 + dtheta) + cx) -
                (Math.sin(axisRotation) * rx * Math.cos(theta1 + dtheta) + cx);
        y = (Math.sin(axisRotation) * ry * Math.sin(theta1 + dtheta) + cy) +
                (Math.cos(axisRotation) * ry * Math.sin(theta1 + dtheta) + cy);
        return new Point(x, y);
    }

    // TODO Gabe: documentar
    public static ArrayList<Point> findPointsArc(Point start, double rx, double ry, double axisRotation, boolean largeArcFlag, boolean sweepFlag, Point end, int n) {
        double inc = (double)1/n;
        ArrayList<Point> points = new ArrayList<Point>();
        for (double i = 0; i <= 1; i += inc) {
            points.add(findPointArc(start, rx, ry, axisRotation, largeArcFlag, sweepFlag, end, i));
        }
        return points;
    }

    public static String matrixToString(double[][] mat, int padding, int precision, boolean htmlNewLine) {
        if (mat == null)
            return "null";

        String format = "0";
        for (int i = 1; i < padding; i++) {
            format += "0";
        }
        for (int i = 0; i < precision; i++) {
            if (i == 0)
                format += ".";
            format += "0";
        }
        DecimalFormat decFormat = new DecimalFormat(format);

        StringBuilder string = new StringBuilder();
        for (int i = 0; i < mat.length; i++) {
            string.append("|");
            for (int j = 0; j < mat[i].length; j++) {
                string.append(decFormat.format(mat[i][j]));
                if (j < mat[j].length - 1)
                    string.append(" ");
                }
            string.append("|");
            if (i < mat.length - 1)
                string.append((htmlNewLine ? "<br>" : System.getProperty("line.separator")));
        }
        return string.toString();
    }
}
