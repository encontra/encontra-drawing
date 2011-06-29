package pt.inevo.encontra.drawing.descriptors.Topogeo;

import pt.inevo.encontra.geometry.Point;
import pt.inevo.encontra.geometry.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
* Utility class. Provides auxiliary functions for computing distances and
* other measures.
*
* @author Gabriel
*/
public class Util {
    private static NumberFormat timeFormatter = new DecimalFormat("#00");

    /**
     * Computes the distance between tow Points.
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The distance between the two points.
     */
    public static double distance(Point p1, Point p2) {
        return Math.sqrt( distanceSquared(p1, p2) );
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
     * Computes the length of the hypotenuse provied the two cateds.
     * @param dx Cated x.
     * @param dy Cated y.
     * @return The length of the hypotenuse.
     */
    public static double distance(double dx, double dy) {
        return Math.sqrt( distanceSquared(dx, dy) );
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

    /**
     * Converts a double into a string, according to the provided padding
     * and floating point precision configuration.
     * @param val the double value to be converted.
     * @param padding the padding.
     * @param precision the floating point precision.
     * @return the string containing the value.
     */
    public static String doubleToString(double val, int padding, int precision) {
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

        return decFormat.format(val);
    }

    /**
     * Converts a double array into a string.
     * @param arr the array to be represented as a string.
     * @param padding the number of zeros to be use at the left of the floating
     * point.
     * @param precision tje number of decimal values to be represented to the
     * right of the floating point.
     * @return the string representation of the descriptor.
     */
    public static String descriptorToString(double[] arr, int padding, int precision, boolean brackets) {
        if (arr == null)
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
        if (brackets)
            string.append("[");
        for (int i = 0; i < arr.length; i++) {
            string.append(decFormat.format(arr[i]));
            if (i < arr.length - 1)
                string.append(" ");
        }
        if (brackets)
            string.append("]");
        return string.toString().replace(',', '.');
    }

    public static String orderToString(short[] order) {
        if (order == null)
            return "null";
        StringBuilder string = new StringBuilder();
        string.append("{");
        for (int i = 0; i < order.length; i++) {
            if (i > 0)
                string.append(",");
            string.append(order[i]);
        }
        string.append("}");
        return string.toString();
    }

    /**
     * Converts a matrinx into a string.
     * @param mat the matrix to be converterd
     * @param padding the number of zeros to be use at the left of the floating
     * point.
     * @param precision tje number of decimal values to be represented to the
     * right of the floating point.
     * @param htmlNewLine true if the new line should be represented as html,
     * false otherwise.
     * @return the string representation of the matrix.
     */
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

    /**
     * Concatenates two arrays.
     * @param first the first array.
     * @param second the second array.
     * @return the array resulting from the concatenation.
     */
    public static double[] concatArrays(double[] first, double[] second) {
        double[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
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

    // TODO Gabe: documentar
    public static Point findPointArc(Point start, double rx, double ry, double axisRotation, boolean largeArcFlag, boolean sweepFlag, Point end, double t) {
        double x, y, dtheta, cx, cy, theta1;
        dtheta = (largeArcFlag ? 180.0 : 0.0) + (t * 180) * (sweepFlag ? 1 : -1);
        // Check:
        // http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
        // http://www.w3.org/TR/SVG/implnote.html#ArcImplementationNotes
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

//    // TODO Gabe: documentar
//    public static void saveComponentAsJPEG(Component myComponent, String filename) {
//       Dimension size = myComponent.getSize();
//       BufferedImage myImage =
//         new BufferedImage(size.width, size.height,
//         BufferedImage.TYPE_INT_RGB);
//       Graphics2D g2 = myImage.createGraphics();
//       myComponent.paint(g2);
//       try {
//         OutputStream out = new FileOutputStream(filename+".jpg");
//         ImageIO.write(myImage, "jpg", out);
//         out.close();
//       } catch (Exception e) {
//         System.out.println(e);
//       }
//     }
//
//    // TODO Gabe: documentar
//    public static void saveComponentAsPNG(Component myComponent, String filename) {
//       Dimension size = myComponent.getSize();
//       BufferedImage myImage =
//         new BufferedImage(size.width, size.height,
//         BufferedImage.TYPE_INT_RGB);
//       Graphics2D g2 = myImage.createGraphics();
//       myComponent.paint(g2);
//       try {
//         OutputStream out = new FileOutputStream(filename+".png");
////         PNGImageEncoder encode = = new
////         JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
////         encoder.encode(myImage);
//         ImageIO.write(myImage, "png", out);
//         out.close();
//       } catch (Exception e) {
//         System.out.println(e);
//       }
//     }

    /**
     * TODO Gabe: documentar
     * @return
     */
    public static String getNowDateLabel() {
        int hour = GregorianCalendar.getInstance().get(Calendar.HOUR);
        String ampm = (GregorianCalendar.getInstance().get(Calendar.AM_PM) == 1? "PM" : "AM");
        if (hour == 0)
            hour = 12;
        return
        timeFormatter.format(GregorianCalendar.getInstance().get(Calendar.YEAR)) + "-" +
        timeFormatter.format(GregorianCalendar.getInstance().get(Calendar.MONTH)) + "-" +
        timeFormatter.format(GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH)) + " T " +
        timeFormatter.format(hour) + "h" +
        timeFormatter.format(GregorianCalendar.getInstance().get(Calendar.MINUTE)) + "m" +
        timeFormatter.format(GregorianCalendar.getInstance().get(Calendar.SECOND)) + "s " +
        ampm;
    }

    /**
     * TODO Gabe: documentar
     * @param arlList
     */
    public static void removeDuplicate(ArrayList arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    /**
     * TODO Gabe: documentar
     * @param arlList
     */
    public static void removeDuplicateWithOrder(ArrayList arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        arlList.clear();
        arlList.addAll(newList);
    }

}
