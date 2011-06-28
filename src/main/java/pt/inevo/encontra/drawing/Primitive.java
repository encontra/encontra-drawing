package pt.inevo.encontra.drawing;

import org.apache.batik.bridge.SVGPathElementBridge;
import org.apache.batik.css.dom.CSSOMSVGColor;
import org.apache.batik.css.engine.SVGCSSEngine;
import org.apache.batik.css.engine.StyleDeclaration;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.AbstractValue;
import org.apache.batik.css.engine.value.ComputedValue;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.RGBColorValue;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMLineElement;
import org.apache.batik.dom.svg.SVGOMMatrix;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.dom.svg.SVGOMPolylineElement;
import org.apache.batik.dom.svg.SVGOMStyleElement;
import org.apache.batik.dom.svg.SVGPathSupport;
import org.apache.batik.dom.svg.SVGPointShapeElement;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.apache.batik.svggen.SVGColor;
import org.apache.batik.util.SVG12Constants;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPathElement;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegArcAbs;
import org.w3c.dom.svg.SVGPathSegArcRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothRel;
import org.w3c.dom.svg.SVGPathSegLinetoAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalRel;
import org.w3c.dom.svg.SVGPathSegLinetoRel;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalRel;
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;
import org.w3c.dom.svg.SVGPathSegMovetoRel;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGStylable;

import pt.inevo.encontra.common.distance.HasDistance;
import pt.inevo.encontra.drawing.util.Color;
import pt.inevo.encontra.drawing.util.Functions;
import pt.inevo.encontra.geometry.Point;
import pt.inevo.encontra.geometry.Vector;
import pt.inevo.jcali.*;

import java.util.ArrayList;

//import pt.inevo.swig.nbtree.SWIGTYPE_p_Primitive;

public class Primitive implements HasDistance<Primitive> {


	// Constants uses in the detection of intersections
	public static final int COLLINEAR=2;

	private ArrayList<Point> _lst_points;	//!< List of points that make up the Primitive
	private int	_id;	//!< Id of the Primitive (root should be zero).

	   private double	_xmin,			//!< x minimum of the Primitive
				_xmax,			//!< x maximum of the Primitive
				_ymin,			//!< y minimum of the Primitive
				_ymax; 			//!< y maximum of the Primitive

	    private boolean _closed;
	    private double _borderWidth;

	    String _fillRule;
		boolean _fillNone;

	    private Color bordercolor;

	    private Color fillcolor;

	  public static boolean SAME_SIGNS( long a, long b ){
		  return (((long) (( long) a ^ ( long) b)) >= 0 );
	  }

		//  get functions
		public int getId() { return _id; }
		public void setId(int id) { _id = id;}

		public int getNumPoints() { return _lst_points.size(); }
		public Point getPoint(int i) { return _lst_points.get(i); }

		public double getXmin() { return _xmin; }
		public double getXmax() { return _xmax; }
		public double getYmin() { return _ymin; }
		public double getYmax() { return _ymax; }

	/** Default constructor.
	 */
	public Primitive() {
		initialise();
	}

	/** Constructor with id
	 *
	 * @param id	id of primitive
	 *
	 */
	public Primitive(int id) {
		initialise(id);
	}

	private void setStyle(SVGStylableElement elm) {
		StyleMap style=elm.getComputedStyleMap(null);

		if(style!=null) {
			Value fill=style.getValue(SVGCSSEngine.FILL_INDEX);
			if(fill instanceof RGBColorValue) {
				this.setFillColor(new Color((RGBColorValue)fill));
			}
			Value stroke=style.getValue(SVGCSSEngine.STROKE_INDEX);
			if(stroke instanceof RGBColorValue) {
				this.setBorderColor(new Color((RGBColorValue)stroke));
			}

			Value stroke_width=style.getValue(SVGCSSEngine.STROKE_WIDTH_INDEX);
			this.setBorderWidth(stroke_width.getFloatValue());
		}
	}

    public Primitive(SVGOMLineElement line){
        initialise();

        setStyle(line);

        SVGElement root=(SVGElement)line.getOwnerDocument().getDocumentElement();
        SVGMatrix m=line.getTransformToElement(root);

        double x1=line.getX1().getBaseVal().getValue();
        double y1=line.getY1().getBaseVal().getValue();

        addPoint(x1, y1, m);

        double x2=line.getX2().getBaseVal().getValue();
        double y2=line.getY2().getBaseVal().getValue();

        addPoint(x2, y2, m);
    }

    public Primitive(SVGOMPolylineElement line){
        initialise();

        setStyle(line);

        SVGElement root=(SVGElement)line.getOwnerDocument().getDocumentElement();
        SVGMatrix m=line.getTransformToElement(root);

        SVGPoint point;
        for(int i=0;i<line.getPoints().getNumberOfItems();i++){
        	point=line.getPoints().getItem(i);
        	addPoint(point.getX(), point.getY(), m);
        }

    }

	public Primitive(SVGOMPathElement path)
	{
		initialise();

		setStyle(path);

		double prevx = 0;
		double prevy = 0;

		double x=0;
		double y=0;

		SVGElement root=(SVGElement)path.getOwnerDocument().getDocumentElement();
		SVGMatrix m=path.getTransformToElement(root);

		/*
		 *  A normalized path is composed only of absolute moveto, lineto and cubicto path segments (M, L and C). Using this subset, the path description can be represented with fewer segment types. Be aware that the normalized 'd' attribute will be a larger String that the original.
		 *	Relative values are transformed into absolute, quadratic curves are promoted to cubic curves, and arcs are converted into one or more cubic curves (one per quadrant).
		 */
		SVGPathSegList segList=path.getNormalizedPathSegList();

		for(int i=0;i<segList.getNumberOfItems();i++) {
			SVGPathSeg seg=segList.getItem(i);
			switch(seg.getPathSegType()) {
				// case 'm': case 'M': case 'l': case 'L': case 't': case 'T':
				case SVGPathSeg.PATHSEG_MOVETO_ABS: // M
					x=((SVGPathSegMovetoAbs)seg).getX();
					y=((SVGPathSegMovetoAbs)seg).getY();
					break;
				case SVGPathSeg.PATHSEG_LINETO_ABS: // L
					x=((SVGPathSegLinetoAbs)seg).getX();
					y=((SVGPathSegLinetoAbs)seg).getY();
					break;

				case SVGPathSeg.PATHSEG_CLOSEPATH: // z
					// do nothing
					continue;

					//case 'h': case 'H': case 'v': case 'V':
				case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS:
					x=((SVGPathSegLinetoHorizontalAbs)seg).getX();
					y=prevy;
					break;

				case SVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS:
					x=prevx;
					y=((SVGPathSegLinetoVerticalAbs)seg).getY();
					break;

				//case 's': case 'S':	case 'q': case 'Q':
				case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS:
					//x=((SVGPathSegCurvetoCubicSmoothAbs)seg).getX();
					//y=((SVGPathSegCurvetoCubicSmoothAbs)seg).getY();

					continue;
					//break;
				case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
					x=((SVGPathSegCurvetoCubicAbs)seg).getX();
					y=((SVGPathSegCurvetoCubicAbs)seg).getY();
					break;

				//case 'c': case 'C':
				//case SVGPathSeg.
				//	argc = 6; break;

				//case 'a': case 'A':
				case SVGPathSeg.PATHSEG_ARC_ABS:
					x=((SVGPathSegArcAbs)seg).getX();
					y=((SVGPathSegArcAbs)seg).getY();
					break;

				default:
					@SuppressWarnings("unused")
					int a=1;
					break;


			}

			if(seg.getPathSegType()==SVGPathSeg.PATHSEG_CLOSEPATH) {
	            if (getNumPoints()>0){
	                addPoint(getPoint(0).x,getPoint(0).y);
	            }
	        } else {

			    addPoint(x,y,m);
			    // save values
			    prevx = x;
			    prevy = y;
	        }
		}

	}

	public String getSVG() {
		String svg="";
		if (getNumPoints()>1) {

            Color bc = getBorderColor();
            Color fc = getFillColor();
            double bw = getBorderWidth();
			String fr = getFillRule();
			boolean fn = isFillNone();

            // svg content
            svg="<path ";

            if ( (fc._is_set==false  && fr.length() == 0) || fn ) {
                svg+="fill=\"none\" ";
            }else if(fc._is_set==false && fr.length() > 0)
				svg+="fill-rule=\"" + fr + "\"";

            svg+= "style=\"";
            if (bc._is_set==true) {
            	svg+= "stroke:rgb(" + bc._r + "," + bc._g + "," + (int)bc._b +"); ";
            }
            if (fc._is_set==true) {
            	svg+= "fill:rgb(" + (int)fc._r + "," + (int)fc._g + "," + (int)fc._b +"); ";
            }
            svg+= "stroke-width:"+ bw +"\" d=\"";  // p.getWidth() --gives to thick lines

			Point cip = getPoint(0); // GEEFT NULL !!!
			svg+="M " + cip.x + " " + cip.y + " ";

			for (int j=1; j<getNumPoints(); j++) {
				cip = getPoint(j);
				svg+= "L " + cip.x + " " + cip.y + " ";
			}
			svg+= "\" />" + '\n';
		}
		return svg;
	}

	/** initialises a new empty Primitive with id
	 *
	 * @param id	id of primitive
	 *
	 */
	public void initialise() { initialise(-1);}

	public void initialise(int id) {
	    _id = id;

	    bordercolor = new Color(false);
	    fillcolor = new Color(false);

	    setClosed(false);
	    setBorderWidth(1);

		setFillRule("");
		setFillNone(false);

	    _xmax = Integer.MIN_VALUE;//numeric_limits<int>::min();
		_ymax = Integer.MIN_VALUE;//numeric_limits<int>::min();
		_xmin = Integer.MAX_VALUE;//numeric_limits<int>::max();
		_ymin = Integer.MAX_VALUE;//numeric_limits<int>::max();

		_lst_points = new ArrayList<Point>();
	}

	/** Desctructor.
	 */
	/*
	Primitive::~Primitive() {
		int n_items = _lst_points->getNumItems();

		for (int i=0; i<n_items; i++)
			delete (*_lst_points)[i];

		delete _lst_points;

	    if (bordercolor != NULL) {
	        delete bordercolor;
	        bordercolor = NULL;
	    }

	    if (fillcolor != NULL) {
	        delete fillcolor;
	        fillcolor = NULL;
	    }
	}*/

	/** Add a new point to the Primitive's list of points.
	 *
	 * @param x	The x-coordinate of the new points.
	 * @param y	The y-coordinate of the new points.
	 */
	public void addPoint(double x, double y) {
		addPoint(x, y,null);
	}

	public void addPoint(double x, double y,SVGMatrix matrix) {
		 // add point
    	if(matrix!=null) {
    		SVGPoint point=new SVGOMPoint((float)x,(float)y);

    		point=point.matrixTransform(matrix);
    		x=point.getX();
    		y=point.getY();
    	}

	    // std::cout << "Debug: adding point (" << x << "," << y << ")" << std::endl;
		_lst_points.add( new Point(x,y) );

		if (x < _xmin) _xmin=x;
		if (x > _xmax) _xmax=x;
		if (y < _ymin) _ymin=y;
		if (y > _ymax) _ymax=y;
	}

	/** OBSOLETE SHOULD BE DONE BY USING DEJAFUNCTIONS
	 * Computes the maximum of the three values.
	 *
	 * @param v1	First value.
	 * @param v2	Second value.
	 * @param v3	Third value.
	 *
	 * @result	The maximum value.
	 */
	public double maximum(double v1, double v2, double v3) {
		double v = (v1 > v2) ? v1 : v2;
		v = (v3 > v) ? v3 : v;
		return v;
	}

	/** OBSOLETE SHOULD BE DONE BY USING DEJAFUNCTIONS
	 * Computes the minimum of the three values.
	 *
	 * @param v1	First value.
	 * @param v2	Second value.
	 * @param v3	Third value.
	 *
	 * @result	The minimum value.
	 */
	public double minimum(double v1, double v2, double v3) {
		double v = (v1 < v2) ? v1 : v2;
		v = (v3 < v) ? v3 : v;
		return v;
	}


	/**
	 * Checks whether the current primitive is completely in the primitive passed to it.
	 *
	 * @param pol	The other Primitive.
	 *
	 * @return	True iff this Primitive is entirely inside the other Primitive, false otherwise.
	 */
	public boolean isInPrimitive(Primitive p_j) {
		int num_points_p_i = this._lst_points.size();

		for (int i=0; i<num_points_p_i; i++) {

			if ( p_j.getNumPoints() > 0 ) {

	            if ( !p_j.pointIn(getPoint(i)) ) {
					return false;
	            }
			}
		}
		return true;
	}


	/** Checks whether a point lies within a Primitive
	 *
	 * Note: This function is based on code from
	 * [http://www.acm.org/pubs/tog/GraphicsGems/gemsiv/ptpoly_haines/]
	 *
	 * @param pt	The Point that's tested against the Primitive.
	 *
	 * @return	Returns true iff the point is inside the Primitive.
	 */
	public boolean pointIn( Point pt) {
		int	crossings ;
		int	j;
		boolean yflag0, yflag1, inside_flag, xflag0 ;
		double ty, tx;
		Point vtx0, vtx1;

		int numverts=this.getNumPoints();
		tx = pt.x;
		ty = pt.y;

		vtx0 = this.getPoint(numverts-1);
		/* get test bit for above/below X axis */
		yflag0 = ( vtx0.y >= ty ) ;
		vtx1 = this.getPoint(0);
		int conta_pontos=0;

		crossings = 0 ;

		for ( j = numverts+1 ; (--j)>0 ; ) {

			yflag1 = ( vtx1.y >= ty ) ;
			/* check if endpoints straddle (are on opposite sides) of X axis
			 * (i.e. the Y's differ); if so, +X ray could intersect this edge.
			 */
			if ( yflag0 != yflag1 ) {
				xflag0 = ( vtx0.x >= tx ) ;
				/* check if endpoints are on same side of the Y axis (i.e. X's
				 * are the same); if so, it's easy to test if edge hits or misses.
				 */
				if ( xflag0 == ( vtx1.x >= tx ) ) {

					/* if edge's X values both right of the point, must hit */

					if ( xflag0 ) crossings += ( yflag0 ? -1 : 1 ) ;
				} else {
					/* compute intersection of polygon segment with +X ray, note
					 * if >= point's X; if so, the ray hits it.
					 */
					if ( (vtx1.x - (vtx1.y-ty)*
						( vtx0.x-vtx1.x)/(vtx0.y-vtx1.y)) >= tx ) {
							crossings += ( yflag0 ? -1 : 1 ) ;
					}
				}
			}

			/* move to next pair of vertices, retaining info as possible */
			yflag0 = yflag1 ;
			vtx0 = vtx1 ;
			conta_pontos++;
			if(conta_pontos < numverts)
				vtx1 = this.getPoint(conta_pontos);
		}
		/* test if crossings is not zero */
		inside_flag = (crossings != 0) ;

		return inside_flag;
	}

	/**
	 * This is the Douglas-Peucker recursive simplification routine.
	 * It just marks vertices that are part of the simplified polyline
	 * for approximating the polyline subchain v[j] to v[k].
	 *
	 * @param rel_tol	The relative tolerance.
	 * @param v[]	    Polyline array of vertex points to be optimized.
	 * @param j		    Index for the subchain v[j] to v[k].
	 * @param k	    	Index for the subchain v[j] to v[k].
	 * @param mk[]	    Array of markers matching vertex array v[], which should be kept
	 */
	public void simplifyDP( double rel_tol, Point[] v, int j, int k, int[] mk ) {
		int i;

	    if (k <= j+1) // there is nothing to simplify
	        return;

	    // check for adequate approximation by segment S from v[j] to v[k]
	    int	maxi  = j;			// index of vertex farthest from S
	    double maxd2 = 0;			// distance squared of farthest vertex
	    double tol2  = rel_tol * rel_tol;	// tolerance squared

	    Point	SP0   = v[j];
	    Point SP1   = v[k];		// segment from v[j] to v[k]

	    Vector  u=new Vector(SP1, SP0);		// segment direction vector
	    double  cu = Vector.dot(u,u);		// segment length squared

	    // test each vertex v[i] for max distance from S
	    // compute using the Feb 2001 Algorithm's dist_Point_to_Segment()
	    // Note: this works in any dimension (2D, 3D, ...)
	    Vector	w;
	    Point	Pb=new Point();                // base of perpendicular from v[i] to S

	    double	b,
				cw,
				dv2;        // dv2 = distance v[i] to S squared

	    for (i = j+1; i < k; i++) {
	        // compute distance squared
	        w  = new Vector(v[i], SP0);
	        cw = Vector.dot(u,w);

	        //calculate dv2
	        if ( cw <= 0 ) {            //behind SP0
	            dv2 = v[i].DistanceTo(SP0);

	        } else if ( cu <= cw ) {    //nearer to SP1
	            dv2 = v[i].DistanceTo(SP1);

	        } else {                    //
	            b    = cw / cu;
				u.setDX( u.getDX() * (int)b);
				u.setDY( u.getDY() * (int)b);
	            Pb.x = SP0.x + u.getDX();
	            Pb.y = SP0.y + u.getDY();
	            dv2  = v[i].DistanceTo(Pb);
	        }

	        // test with current max distance squared
	        if (dv2 > maxd2) {
	            // v[i] is a new max vertex
	            maxi  = i;
	            maxd2 = dv2;
	        }
	    }

		// Check whether the error is worse than the tolerance.
	    if (maxd2 > tol2) {
	        // split the polyline at the farthest vertex from S
	        mk[maxi] = 1;      // mark v[maxi] for the simplified polyline

	        // recursively simplify the two subpolylines at v[maxi]
	        simplifyDP( rel_tol, v, j, maxi, mk );  // polyline v[j] to v[maxi]
	        simplifyDP( rel_tol, v, maxi, k, mk );  // polyline v[maxi] to v[k]
	    }
	}


	/** Simplifies a polyline.
	 *
	 * This method first removes vertices whose removal will result in an
	 * error no greater that specified by rel_tol. It then applies the Douglas-Peucker
	 * algorithm to the result of the first simplification.
	 *
	 * @param rel_tol   Approximation tolerance.
	 */
	public void poly_simplify(  double rel_tol )
	{
		int n = getNumPoints();	// the original number of Points in the Primitive

//	    std::cout << " poly_simplify: Polygon NrPoints=" << n << std::endl;
	    if (n!=0)  {

		    // misc counters
	        int	i  = 0, // index of points in original primitive
			    k  = 1, // index of points in reduced primitive
			    pv = 0; // index of previously added point to reduced primitive

	       double tol2 = (rel_tol * rel_tol);	// tolerance squared
	        Point [] vt	= new Point[n];		// vertex-buffer, contains the candidates of points to simplify
	        int [] mk	= new int[n];				// marker-buffer, used by simplifyDP to mark result of final points

		    // Clear the array
		    for ( i = 0; i < n; i++ )
			    mk[i] = 0;

	        // Vertex Reduction within tolerance of prior vertex cluster
	        vt[0] = _lst_points.get(0);	// Add V[0] to the vertext-buffer.

		    // Add the important points to the vertex-buffer. I.e. remove all
		    // really small lines.
	        for (i=1; i<n; i++) {
			    // If the distance between two consecutive points is greater
			    // then or equal to the specified tolerance, add the current vertext to the
			    // vertex-buffer and set the pv to the current index.

	            if ( _lst_points.get(i).DistanceTo( _lst_points.get(pv)) > tol2 ) {
		            vt[k++] = _lst_points.get(i);
		            pv = i;
	            }
	        }

		    // Add the last point if it wasn't added already.
	        if (pv < n-1)
	            vt[k++] = _lst_points.get(n-1);

	        // Douglas-Peucker polyline simplification
	        mk[0] = 1;      // mark the first
	        mk[k-1] = 1;    // and last vertices
	        simplifyDP(rel_tol, vt, 0, k-1, mk);

	        CIListNode <CIPoint> it;
		    // Clear the original list of CIPoints. Copies are stored in vt[]
		    // TODO Clear the list!
	        //for (it = _lst_points.getHeadPosition(); it != null; _lst_points.getNextItem(it))
			    //delete _lst_points->getItemAt(it);

		    //delete _lst_points;
		    _lst_points = new ArrayList<Point>();


	        // Copy marked vertices to the output simplified polyline
		    for (i=0; i < k; i++) {
	            if ( mk[i] > 0)
				    _lst_points.add(vt[i]);
	        }


		    // If the CIPoints of this Primitive just form a small line,
		    // delete its points so it won't be used in further calculations.
	        if (this.getNumPoints() == 2) {
	            if (getPoint(0).DistanceTo(getPoint(1)) <= tol2) {

				    /* TODO Delete here also!

				     int i;
				    for (i=0; i < this->getNumPoints(); i++)
					    delete (*_lst_points)[i];

		            delete _lst_points;
					*/
		            _lst_points = new ArrayList<Point>();
	            }
	        }

	        // TODO - Clean up.
	        //delete []vt;
	        //delete []mk;
	    }
	}


	/**
	 * Returns a human readable representation of this primitive.
	 */
	public String toString() {

		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("List of points in this primitive:");

		int i;
		for(i = 0; i < this.getNumPoints(); i++) {
			Point tempPoint = this.getPoint(i);

			stringBuffer.append(" (" + tempPoint.x + ", " + tempPoint.y + ")");
		}


		return stringBuffer.toString();
	}

	/**
	 * Computes the shortest distance between points of this Primitive with
	 * the Primitive passed to it.
	 *
	 * @param other the Primitive to compute this Primitive's distance to
	 * @return the shortest distance (between points)
	 */
	public double getShortestDistance(Primitive other) {
		double currentLowestDistance = Double.POSITIVE_INFINITY;// numeric_limits<double>::infinity();

		int i; int j;
		for(i = 0; i < this.getNumPoints(); i++) {
			for (j = 0; j < other.getNumPoints(); j++) {
	            double distance = this.getPoint(i).DistanceTo(other.getPoint(j));

				if (distance < currentLowestDistance) {
					currentLowestDistance = distance;
				}
			}
		}
		return currentLowestDistance;
	}

	/**
	 * Returns the height of this Primitive.
	 */
	public double getHeight() {
		return Math.abs(_ymax-_ymin);
	}

	/**
	 * Returns the width of this Primitive.
	 */
	public double getWidth() {
		return Math.abs(_xmax-_xmin);
	}

	/**
	 * Returns the polygon diagonal of this primitive.
	 */
	public double getDiagonalLength() {
	    return Functions.dist(getHeight(),getWidth());
	}


	/**
	 * Returns the size covered by the area of the polygon
	 */
	public double getAreaSize() {
	    //return -1 if it is a (poly-)line (so when it hasn't got an area)
	    if (!isClosed()) return -1;

	    int last = this.getNumPoints() - 1;
	    if (last>0) {
	        // if last point isn't the first point, then add it
	        if (((this.getPoint(0).x)!=(this.getPoint(last).x)) ||
	        	((this.getPoint(0).y)!=(this.getPoint(last).y))) {
	            this.addPoint((this.getPoint(0).x),(this.getPoint(0).y));
	        }
	    }

	    //this can't be a polygon, because it exist of only 1 or 2 points!
	    if ((this.getNumPoints()) < 3) {
	        this.setClosed(false);
	        return -1;
	    }

	    double area=0;
		for(int i = 0; i < ((this.getNumPoints()) - 1); i++) {
			area += ((this.getPoint(i).x) * (this.getPoint(i+1).y)) - ((this.getPoint(i).y) * (this.getPoint(i+1).x));
		}
	    area = area/2;

	    return Math.abs(area);
	}

	/**
	 * Returns the perimeter for an polygon or the total length of the line of a polyline
	 */
	public double getPerimeter() {

	    int numPoints = this.getNumPoints();
	    double perim=0;

	    for (int i = 0; i < numPoints - 1; i++) {
	        perim += this.getPoint(i).DistanceTo(this.getPoint(i+1));
	    }

	    return perim;

	}

	boolean isFillNone(){return _fillNone;}
	void setFillNone(boolean sf){_fillNone = sf;}

	void setBorderWidth(float w){_borderWidth = w;}
	void setFillRule(String fr){_fillRule=fr;}

	String getFillRule(){return _fillRule;}

	public Color getBorderColor() {

	    return bordercolor;
	}

	public Color setBorderColor(Color c) {

	    bordercolor = c;

	    return bordercolor;
	}

	public Color getFillColor() {

	    return fillcolor;
	}

	public Color setFillColor(Color c) {
	    fillcolor = c;

	    return fillcolor;
	}

	public boolean isClosed(){
	    return _closed;
	}

	public void setClosed(boolean closed){
	    _closed=closed;
	}

	public double getBorderWidth(){
	    return _borderWidth;
	}

	public void setBorderWidth(int w){
	    _borderWidth = w;
	}


	//returns true if one primitive intersects or is inside the other
	public boolean boundingBoxesCollide(Primitive p_j) {

	    int conditions = 0;

	    //test on x-values
	    if (
	        ( (p_j.getXmin() > this.getXmin()) && (p_j.getXmin() < this.getXmax()) ) || //xmin of j is within x of this
	        ( (p_j.getXmax() > this.getXmin()) && (p_j.getXmax() < this.getXmax()) ) || //xmax of j is within x of this
	        ( (this.getXmin() > p_j.getXmin()) && (this.getXmin() < p_j.getXmax()) ) || //xmin of this is within x of j
	        ( (this.getXmax() > p_j.getXmin()) && (this.getXmax() < p_j.getXmax()) )    //xmax of this is within x of j
	    ) {
	        conditions++;
	    }
	    //test on y-values
	    if (
	        ( (p_j.getYmin() > this.getYmin()) && (p_j.getYmin() < this.getYmax()) ) || //ymin of j is within y of this
	        ( (p_j.getYmax() > this.getYmin()) && (p_j.getYmax() < this.getYmax()) ) || //ymin of j is within y of this
	        ( (this.getXmin() > p_j.getXmin()) && (this.getXmin() < p_j.getXmax()) ) || //ymin of this is within y of j
	        ( (this.getXmax() > p_j.getXmin()) && (this.getXmax() < p_j.getXmax()) )    //ymax of this is within y of j
	    ) {
	        conditions++;
	    }


	    //true if both tests pass
	    return (conditions>1);
	}

	public void removeAllPoints() {
		/*
	    int n_items = _lst_points.getNumItems();

	    int i=0;

	    for (i=0; i<n_items; i++) {
			delete (*_lst_points)[i];
	    }

		delete _lst_points;*/
		_lst_points = new ArrayList<Point>();
	}

	/* TODO - Indagare
	vector<pair<double,double>> Dejasvg::calculateEllipse(double x, double y, double a, double b, double angle, double finalX, double finalY, int steps){

		if(steps == NULL){
			steps = 36;
		}

		vector<pair<double,double>> points;

		double beta = (-angle/180)*PI;
		double sinbeta = sin(beta);
		double cosbeta = cos(beta);

		double center_x;

		if(x>=finalX)
			center_x = x - a*cos(angle);
		else
			center_x = x + a*cos(angle);

		double center_y = y - b*sin(angle);

		double cos_t_initial_d = ((x - center_x)/a);
		int cos_t_initial;

		if(cos_t_initial_d < 0)
			cos_t_initial = (int)(cos_t_initial_d - 0.5);
		else
			cos_t_initial = (int)(cos_t_initial_d + 0.5);

		int initial_angle = (int)((acos((double)cos_t_initial)*180)/PI);

		double cos_t_final_d = ((finalX - center_x)/a);
		int cos_t_final;

		if(cos_t_final_d < 0)
			cos_t_final = (int)(cos_t_final_d - 0.5);
		else
			cos_t_final = (int)(cos_t_final_d + 0.5);

		int final_angle = (int)((acos((double)cos_t_final)*180)/PI);

		int diff_angle = abs(final_angle - initial_angle);



		double stop_condition;

		if(diff_angle == 0)
			stop_condition = 360;
		else if(initial_angle > final_angle){
			final_angle = initial_angle + 180;

		}


		for(double i = initial_angle; i < final_angle; i+= (diff_angle/steps)){

			double alpha = (i/180)*PI;
			double sinalpha = sin(alpha);
			double cosalpha = cos(alpha);

			double X = center_x + ((a*cosalpha*cosbeta) - (b*sinalpha*sinbeta));
			double Y = center_y + ((a*cosalpha*sinbeta) + (b*sinalpha*cosbeta));

			std::pair<double,double> point;
			point.first = X;
			point.second = Y;
			points.push_back(point);
		}

		std::pair<double,double>point;
		point.first = finalX;
		point.second = finalY;
		points.push_back(point);

	  return points;

	}*/

	void discretize(SVGOMPathElement path,int steps) {
		float length=SVGPathSupport.getTotalLength(path);
		float delta=length/steps;
		for(float distance=0;distance<=length;distance+=delta){
			SVGPoint p=SVGPathSupport.getPointAtLength(path, distance);
		}
	}


	/* TODO - Indagare
	std::vector<std::pair<double,double>> Dejasvg::calculateCubicBezier(double X0, double Y0, double X1, double Y1, double X2, double Y2, double X3, double Y3, int steps){

		double X,Y;

		std::vector<std::pair<double,double>> points;

		for(double t = 0; t <= 1; t += ((double)1/steps)){

			X = X0 - 3*t*X0 + 3*pow(t,2)*X0 - pow(t,3)*X0 + 3*t*X1 - 6*pow(t,2)*X1 + 3*pow(t,2)*X2 - 3*pow(t,3)*X2 + pow(t,3)*X3;
			Y = Y0 - 3*t*Y0 + 3*pow(t,2)*Y0 - pow(t,3)*Y0 + 3*t*Y1 - 6*pow(t,2)*Y1 + 3*pow(t,2)*Y2 - 3*pow(t,3)*Y2 + pow(t,3)*Y3;


			X = pow((1-t),3)*X0 + 3*t*pow((1-t),2)*X1 + 3*pow(t,2)*(1-t)*X2 + pow(t,3)*X3;
			Y = pow((1-t),3)*Y0 + 3*t*pow((1-t),2)*Y1 + 3*pow(t,2)*(1-t)*Y2 + pow(t,3)*Y3;

			std::pair<double,double> point;
			point.first = X;
			point.second = Y;

			points.push_back(point);
		}

		return points;
	}*/

    @Override
    public double getDistance(Primitive other) {
        return this.getShortestDistance(other);
    }
}
