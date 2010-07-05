package pt.inevo.encontra.drawing.util;

import pt.inevo.jcali.*;

public class Functions {


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
	 * Checks if the value n is stored in vector v
	 *
	 * @param &v	a vector of integers
	 * @param n		an integer
	 *
	 * @return	true if n is in v
	 */
	/*
	public static boolean contains(std::vector<int> &v, int n) {

//	    std::cout << "count: " << v.size() << std::endl;

	    for (std::vector<int>::iterator i=v.begin(); i!=v.end(); i++) {
	        if (*i==n) return true;
	    }

	    return false;
	}*/
}
