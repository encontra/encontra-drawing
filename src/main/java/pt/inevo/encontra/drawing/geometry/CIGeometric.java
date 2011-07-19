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
        //feat = new CIEvaluate();
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
            result.add(1 / CIEvaluate.Tl_Pch.evaluate(sc) );
            result.add(1 / CIEvaluate.Pch2_Ach.evaluate(sc));

            // Other special ones
            result.add(CIEvaluate.Her_Wer.evaluate(sc));

            // Area ratios
            result.add( CIEvaluate.Alq_Aer.evaluate(sc));
            result.add(CIEvaluate.Ach_Aer.evaluate(sc));
            result.add(CIEvaluate.Alq_Ach.evaluate(sc));
            result.add(CIEvaluate.Alt_Alq.evaluate(sc));
            result.add( CIEvaluate.Alt_Ach.evaluate(sc));

            // Perimeter ratios
            result.add(CIEvaluate.Plq_Pch.evaluate(sc));
            result.add(CIEvaluate.Plt_Pch.evaluate(sc));
            result.add(CIEvaluate.Pch_Per.evaluate(sc));

        } else {
            // Special ratios
            result.add(1 / CIEvaluate.Tl_Pch.evaluate(sc));
            result.add( 1 / CIEvaluate.Pch2_Ach.evaluate(sc));

            // Other special ones
            result.add( CIEvaluate.Her_Wer.evaluate(sc));

            // Area ratios
            result.add( CIEvaluate.Ach_Aer.evaluate(sc));
            result.add( CIEvaluate.Alq_Ach.evaluate(sc));
            result.add( CIEvaluate.Alt_Alq.evaluate(sc));
            result.add( CIEvaluate.Alt_Ach.evaluate(sc));
            result.add( CIEvaluate.Alq_Aer.evaluate(sc));

            // Perimeter ratios
            result.add( CIEvaluate.Plq_Pch.evaluate(sc));
            result.add( CIEvaluate.Plt_Pch.evaluate(sc));
            result.add( CIEvaluate.Pch_Per.evaluate(sc));

            result.add( CIEvaluate.Hollowness.evaluate(sc) / 10);

            result.add( CIEvaluate.Ach_Abb.evaluate(sc));
            result.add( CIEvaluate.Alt_Abb.evaluate(sc));
            result.add( CIEvaluate.Pch_Pbb.evaluate(sc));
            result.add( CIEvaluate.Aer_Abb.evaluate(sc));
            result.add( CIEvaluate.Alq_Abb.evaluate(sc));
        }

        for(int i=0;i<result.size();i++)
            if(result.get(i)>1)
                result.set(i, new Double(1));
        return result;
    }
}
