package pt.inevo.encontra.drawing.swing;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Dimension2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.svg.SVGDocument;

public class SVGViewer {

	private SVGDocument _svg;

	// The frame.
    public JFrame frame;

 // The status label.
    protected JLabel label = new JLabel();

    // The SVG canvas.
    protected JSVGCanvas svgCanvas = new JSVGCanvas();

	public SVGViewer() {
		// Create a new JFrame.
        frame = new JFrame("Batik");

        // Add components to the frame.
        frame.getContentPane().add(createComponents());

        // Display the frame.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	closed();
            }
        });
        frame.setSize(400, 400);
        frame.setVisible(true);

	}

	public void setSVG(SVGDocument svg) {
		_svg=svg;

		svgCanvas.setDocument(svg);
	}

	public JComponent createComponents() {
        // Create a panel and add the button, status label and the SVG canvas.
        final JPanel panel = new JPanel(new BorderLayout());

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(label);

        panel.add("North", p);
        panel.add("Center", svgCanvas);

        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);


        /* Set the JSVGCanvas listeners.
        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loading...");
            }
            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loaded.");
            }
        });

        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildStarted(GVTTreeBuilderEvent e) {
                label.setText("Build Started...");
            }
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
            	Dimension2D size = svgCanvas.getSVGDocumentSize();
                label.setText("Build Done.");
                frame.pack();
            }
        });

        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
                label.setText("Rendering Started...");
            }
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                label.setText("");
            }
        });*/

        return panel;
    }

    private synchronized void closed() {
	    notify();
	}

	public synchronized void waitUntilClosed() {
	    try {
	      wait();
	    } catch (InterruptedException ex) {
	      // stop waiting on interruption
	    }
	  }
}

