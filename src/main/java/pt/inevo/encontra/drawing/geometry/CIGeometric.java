package pt.inevo.encontra.drawing.geometry;

import pt.inevo.jcali.CIEvaluate;
import pt.inevo.jcali.CIScribble;
import pt.inevo.jcali.CIStroke;

import java.util.ArrayList;

public class CIGeometric {

    private CIEvaluate feat;
    private CIScribble sc;	//!< Scribbles, are made up of strokes.
    private CIStroke stroke;	//!< Strokes, are made up of points.

    public CIGeometric() {
        feat = new CIEvaluate ();
        sc = null;
        stroke = null;
    }

    public void newScribble()
    {
        sc = new CIScribble();
        stroke = null;
    }

    public void newStroke()
    {
        if (stroke !=null) {
            sc.addStroke(stroke);
        }
        stroke = new CIStroke();
    }

    public void addPoint(double x, double y)
    {
        stroke.addPoint(x, y, 0);
    }

    public ArrayList<Double> geometricFeatures(){
        return geometricFeatures(true);
    }

    public ArrayList<Double> geometricFeatures(boolean cad)
    {
        ArrayList<Double> result=new ArrayList<Double>();

        sc.addStroke(stroke);

        /* What does cad do? Nobody knows, we only use cad=true. ~Alper */
        if (cad) {
            // Special ratios
            result.add(1 / feat.Tl_Pch(sc) );
            result.add(1 / feat.Pch2_Ach(sc));

            // Other special ones
            result.add(feat.Her_Wer (sc));

            // Area ratios
            result.add( feat.Alq_Aer(sc));
            result.add(feat.Ach_Aer(sc));
            result.add(feat.Alq_Ach(sc));
            result.add(feat.Alt_Alq(sc));
            result.add( feat.Alt_Ach(sc));

            // Perimeter ratios
            result.add(feat.Plq_Pch(sc));
            result.add(feat.Plt_Pch(sc));
            result.add(feat.Pch_Per(sc));

        } else {
            // Special ratios
            result.add(1 / feat.Tl_Pch(sc));
            result.add( 1 / feat.Pch2_Ach(sc));

            // Other special ones
            result.add( feat.Her_Wer (sc));

            // Area ratios
            result.add( feat.Ach_Aer(sc));
            result.add( feat.Alq_Ach(sc));
            result.add( feat.Alt_Alq(sc));
            result.add( feat.Alt_Ach(sc));
            result.add( feat.Alq_Aer(sc));

            // Perimeter ratios
            result.add( feat.Plq_Pch(sc));
            result.add( feat.Plt_Pch(sc));
            result.add( feat.Pch_Per(sc));

            result.add( feat.Hollowness(sc) / 10);

            result.add( feat.Ach_Abb(sc));
            result.add( feat.Alt_Abb(sc));
            result.add( feat.Pch_Pbb(sc));
            result.add( feat.Aer_Abb(sc));
            result.add( feat.Alq_Abb(sc));
        }

        for(int i=0;i<result.size();i++)
            if(result.get(i)>1)
                result.set(i, new Double(1));
        return result;
    }
}
