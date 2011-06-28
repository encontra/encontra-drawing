package pt.inevo.encontra.drawing.geometry;

import pt.inevo.jcali.CIEvaluate;
import pt.inevo.jcali.CIScribble;
import pt.inevo.jcali.CIStroke;

import java.util.ArrayList;

public class CIGeometric {
    private static int MAX_FEAT1 = 11;
    private static int MAX_FEAT2 = 17;


    private CIEvaluate _feat;
    private CIScribble _sc;	//!< Scribbles, are made up of strokes.
    private CIStroke _stroke;	//!< Strokes, are made up of points.
    Double[] _result;


    public CIGeometric() {
        _feat = new CIEvaluate ();
        _sc = null;
        _stroke = null;
        //_result=new Double[MAX_FEAT2 + 1];
    }

    /*
     CIGeometric::~CIGeometric()
     {
         if (_feat) delete _feat;
         if (_sc) delete _sc;
     }*/

    public void newScribble()
    {
        _sc = new CIScribble();
        _stroke = null;
    }

    public void newStroke()
    {
        if (_stroke!=null) {
            _sc.addStroke(_stroke);
        }
        _stroke = new CIStroke();
    }

    public void addPoint(double x, double y)
    {
        _stroke.addPoint(x, y, 0);
    }

    public ArrayList<Double> geometricFeatures(){
        return geometricFeatures(true);
    }

    public ArrayList<Double> geometricFeatures(boolean cad)
    {
        ArrayList<Double> result=new ArrayList<Double>();

        _sc.addStroke(_stroke);

        /* What does cad do? Nobody knows, we only use cad=true. ~Alper */
        if (cad) {
            //result.add(new Double(MAX_FEAT1));
            // Special ratios
            result.add(1 / _feat.Tl_Pch(_sc) );
            result.add(1 / _feat.Pch2_Ach(_sc));

            // Other special ones
            result.add(_feat.Her_Wer (_sc));

            // Area ratios
            result.add( _feat.Alq_Aer(_sc));
            result.add(_feat.Ach_Aer(_sc));
            result.add(_feat.Alq_Ach(_sc));
            result.add(_feat.Alt_Alq(_sc));
            result.add( _feat.Alt_Ach(_sc));

            // Perimeter ratios
            result.add(_feat.Plq_Pch(_sc));
            result.add(_feat.Plt_Pch(_sc));
            result.add(_feat.Pch_Per(_sc));

            // _result[4] = _feat->pch_psc (_sc);



            //for (int i=1; i<= MAX_FEAT1; i++)
            //	if (_result[i] > 1)
            //		_result[i] = new Double(1);

        } else {
            //result.add(new Double(MAX_FEAT2));
            // Special ratios
            result.add(1 / _feat.Tl_Pch(_sc));
            result.add( 1 / _feat.Pch2_Ach(_sc));

            // Other special ones
            result.add( _feat.Her_Wer (_sc));

            // Area ratios
            result.add( _feat.Ach_Aer(_sc));
            result.add( _feat.Alq_Ach(_sc));
            result.add( _feat.Alt_Alq(_sc));
            result.add( _feat.Alt_Ach(_sc));
            result.add( _feat.Alq_Aer(_sc));

            // Perimeter ratios
            result.add( _feat.Plq_Pch(_sc));
            result.add( _feat.Plt_Pch(_sc));
            result.add( _feat.Pch_Per(_sc));

            result.add( _feat.Hollowness(_sc) / 10);

            result.add( _feat.Ach_Abb(_sc));
            result.add( _feat.Alt_Abb(_sc));
            result.add( _feat.Pch_Pbb(_sc));
            result.add( _feat.Aer_Abb(_sc));
            result.add( _feat.Alq_Abb(_sc));
            //	_result[18] = _feat->Diag_er (_sc)/sqrt(2)*0.01;
            //	_result[13] = _feat->Hbb_Wbb (_sc);
            //	_result[17] = _feat->Plt_Pbb (_sc);
            //	_result[20] = _feat->Per_Pbb (_sc);
            //	_result[21] = _feat->Plq_Pbb (_sc);

            // _result[13] = _feat->Plt_Per (_sc); // N�o � usada no CALI
            // _result[14] = _feat->Alt_Aer (_sc); // N�o � usada no CALI
            // _result[15] = _feat->Plq_Per (_sc); // N�o � usada no CALI
            // _result[16] = _feat->Plt_Plq (_sc); // N�o � usada no CALI
            //	_result[17] = _feat->Hm_Wbb (_sc);
            //	_result[18] = _feat->Vm_Hbb (_sc);
            // _result[4] = _feat->pch_psc (_sc);
            // _result[] = _feat->Ns(_sc) / 100;
            // _result[] = _feat->Pch_Ns_Tl (_sc) / 100;

            /*
               for (int i=1; i<= MAX_FEAT2; i++) {
                   if (_result[i] > 1) {
                       _result[i] = new Double(1);
                   }
               }*/
        }

        for(int i=0;i<result.size();i++)
            if(result.get(i)>1)
                result.set(i, new Double(1));
        return result;
    }
}
