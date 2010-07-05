package pt.inevo.encontra.drawing.util;

import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.RGBColorValue;

public class Color {
	
	// undefined is used when parsing colors to HSV
	public static double UNDEFINED=-1.0;

	
		//attributes
	public	   double	_h,	        //!< hue
				    _s,	    	//!< saturation
				    _v; 		//!< value/intensity
		    
	public int _r,   //!< red
		                  _g,   //!< green
		                  _b;   //!< blue
		    
	public boolean _is_set;

	
	public Color(RGBColorValue color) {
		FloatValue r_val=(FloatValue)color.getRed();
		FloatValue g_val=(FloatValue)color.getGreen();
		FloatValue b_val=(FloatValue)color.getBlue();
		
		setcolors((int)r_val.getFloatValue(),(int)g_val.getFloatValue(),(int)b_val.getFloatValue(),true);
	
	}
	/** Default constructor.
    *
    * @param str		Hex string
    *
    */
   public Color(String str) {
	   int len = str.length();

		if (len % 2 != 0) {
			throw new NumberFormatException("Illegal length of string in hexadecimal notation.");
		} 
		int numOfOctets = len / 2;
		int[] seq = new int[numOfOctets];

		for (int i = 0; i < numOfOctets; i++) {
			String hex = str.substring(i * 2, i * 2 + 2);

			seq[i] = parseByte(hex);
		} 

       setcolors(seq[0],seq[1],seq[2],true);
   }

   /**
	 * Converts a string in hexadecimal notation into byte.
	 * @param hex string in hexadecimal notation
	 * @return a byte (1bytes)
	 */
	private int parseByte(String hex) throws NumberFormatException {
		if (hex == null) {
			throw new IllegalArgumentException("Null string in hexadecimal notation.");
		} 
		if (hex.equals("")) {
			return 0;
		} 
		Integer num = Integer.decode("0x" + hex);
		int n = num.intValue();

		if (n > 255 || n < 0) {
			throw new NumberFormatException("Out of range for byte.");
		} 
		return n;
	}

		    /** Default constructor.
		    *
		    * @param r			Value of red between 0-255.
		    * @param g			Value of green between 0-255.
		    * @param b			Value of blue between 0-255.
		    * @param is_set	Bool defines it the color has been set
		    *
		    */
		   public Color(int r,int g,int b, boolean is_set) {
		       setcolors(r,g,b,is_set);
		   }

		   /** Constructor to create transparent color, by setting param is_set to false.
		    *
		    * @param is_set	Bool defines it the color has been set
		    *
		    */
		   public Color(boolean is_set) {
		       setcolors(0,0,0,is_set);
		   }

		
		   //----------------------- methods ----------------------- 


		   /**
		    * Sets rgb and hsv-colors 
		    *
		    * @param r	Value of red between 0-255.
		    * @param g	Value of green between 0-255.
		    * @param b	Value of blue between 0-255.
		    *
		    */
		   void setcolors(int r, int g,int b, boolean is_set) {

//		       std::cout << " ik kom hier en zet kleur op r:" << (int)r << " g:" << (int)g << " b:"  << (int)b << " is_set:"  << (int)is_set << std::endl;

		       _r = r;
		       _g = g;
		       _b = b;
		       _is_set = is_set;
		       
		       //normalise
		       double r_n = r / 255.0; //
		   	double g_n = g / 255.0; // De modo aos valores ficarem no intervalo [0,1]
		   	double b_n = b / 255.0; //

		       double mx = Functions.max(r_n,g_n,b_n);
		   		double mn = Functions.min(r_n,g_n,b_n);

		       double delta = mx-mn;

		       _v = mx;

		       //calculate saturation: saturation is 0 if r, g and b are all 0
		       if (mx == 0.0) {
		           _s = 0.0;
		       } else {
		           _s = delta / mx;
		       }

		   	if (_s == 0.0){
		   		_h = UNDEFINED;
		   	} else {
		           if (r == mx) {   // between yellow and magenta [degrees]
		   			_h = 60.0 * (g_n - b_n) / delta;
		           } else if (g == mx) { // between cyan and yellow
		   			_h = 120.0 + 60.0 * (b_n - r_n) / delta;
		           } else if (b == mx) {  // between magenta and cyan
		   			_h = 240.0 + 60.0 * (r_n - g_n) / delta;
		           }

		   		if (_h < 0.0) _h += 360.0;
		   	}

		   }
		public int get_r() {
			return _r;
		}
		public void set_r(int _r) {
			this._r = _r;
		}
		public int get_g() {
			return _g;
		}
		public void set_g(int _g) {
			this._g = _g;
		}
		public int get_b() {
			return _b;
		}
		public void set_b(int _b) {
			this._b = _b;
		}
		public boolean is_set() {
			return _is_set;
		}
		public void set(boolean _is_set) {
			this._is_set = _is_set;
		}

		public boolean equals(Color o) {
			return this._r==o.get_r() && this._g==o.get_g() && this._b==o.get_b();
		}
		
		
}
