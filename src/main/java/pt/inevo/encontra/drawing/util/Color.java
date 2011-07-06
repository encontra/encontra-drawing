package pt.inevo.encontra.drawing.util;

import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.RGBColorValue;

public class Color {

    // undefined is used when parsing colors to HSV
    public static double UNDEFINED = -1.0;


    //attributes
    public double hue,            //!< hue
            saturation,            //!< saturation
            value;         //!< value/intensity

    public int red,   //!< red
            green,   //!< green
            blue,   //!< blue
            alpha = 255;  //alpha value

    public boolean isSet;

    public Color(RGBColorValue color) {
        FloatValue r_val = (FloatValue) color.getRed();
        FloatValue g_val = (FloatValue) color.getGreen();
        FloatValue b_val = (FloatValue) color.getBlue();

        setcolors((int) r_val.getFloatValue(), (int) g_val.getFloatValue(), (int) b_val.getFloatValue(), 1, true);
    }

    public Color(Color colorValue, boolean isSet) {
//        this.red = colorValue.getRed();
//        this.green = colorValue.getGreen();
//        this.blue = colorValue.getBlue();
//        this.isSet = isSet;
        this(colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue(), colorValue.getAlpha(), isSet);
    }

    public Color(Color colorValue) {
        this(colorValue, true);
    }

    /**
     * Default constructor.
     *
     * @param str Hex string
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

        setcolors(seq[0], seq[1], seq[2], 1, true);
    }

    /**
     * Converts a string in hexadecimal notation into byte.
     *
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

    /**
     * Default constructor.
     *
     * @param r      Value of red between 0-255.
     * @param g      Value of green between 0-255.
     * @param b      Value of blue between 0-255.
     * @param isSet Bool defines it the color has been set
     */
    public Color(int r, int g, int b, int a, boolean isSet) {
        setcolors(r, g, b, a, isSet);
    }

    public Color(int r, int g, int b, int a) {
        this(r, g, b, a, true);
    }

    /**
     * Constructor to create transparent color, by setting param is_set to false.
     *
     * @param isSet Bool defines it the color has been set
     */
    public Color(boolean isSet) {
        setcolors(0, 0, 0, 1, isSet);
    }

    //----------------------- methods -----------------------

    /**
     * Sets rgb and hsv-colors
     *
     * @param r Value of red between 0-255.
     * @param g Value of green between 0-255.
     * @param b Value of blue between 0-255.
     * @param a Alpha Value
     */
    void setcolors(int r, int g, int b, int a, boolean isSet) {

//		       std::cout << " ik kom hier en zet kleur op r:" << (int)r << " g:" << (int)g << " b:"  << (int)b << " is_set:"  << (int)is_set << std::endl;

        red = r;
        green = g;
        blue = b;
        this.isSet = isSet;
        this.alpha = (a > 1)? a / 255 : a;

        //normalise
        double r_n = r / 255.0; //
        double g_n = g / 255.0; // De modo aos valores ficarem no intervalo [0,1]
        double b_n = b / 255.0; //

        double mx = Functions.max(r_n, g_n, b_n);
        double mn = Functions.min(r_n, g_n, b_n);

        double delta = mx - mn;

        value = mx;

        //calculate saturation: saturation is 0 if r, g and b are all 0
        if (mx == 0.0) {
            saturation = 0.0;
        } else {
            saturation = delta / mx;
        }

        if (saturation == 0.0) {
            hue = UNDEFINED;
        } else {
            if (r == mx) {   // between yellow and magenta [degrees]
                hue = 60.0 * (g_n - b_n) / delta;
            } else if (g == mx) { // between cyan and yellow
                hue = 120.0 + 60.0 * (b_n - r_n) / delta;
            } else if (b == mx) {  // between magenta and cyan
                hue = 240.0 + 60.0 * (r_n - g_n) / delta;
            }

            if (hue < 0.0) hue += 360.0;
        }

    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public boolean isSet() {
        return isSet;
    }

    public void set(boolean isSet) {
        this.isSet = isSet;
    }

    public boolean equals(Color o) {
        return this.red == o.getRed() && this.green == o.getGreen() && this.blue == o.getBlue();
    }
}
