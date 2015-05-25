/*
 * PrintPreviewInternalFrame.java
 *
 * Created on September 27, 2006, 9:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import javax.swing.*;
import java.awt.*;

/**
 * Just a print preview that is an internal frame.
 *
 * @author shawn
 */
public class PrintPreviewInternalFrame extends JInternalFrame {
    
    private PrintPreviewPanel printPanel;
    
    /** Creates a new instance of PrintPreviewInternalFrame */
    public PrintPreviewInternalFrame(PrintDocument doc) {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setFrameIcon(null);        
        this.getContentPane().setLayout(new BorderLayout());
        this.printPanel = new PrintPreviewPanel(doc);
        this.getContentPane().add(this.printPanel, BorderLayout.CENTER);
        this.setTitle("Print Preview -- " + doc.getDocumentName());
    }
    
    public void displayPrintPreview() {
        this.setVisible(true);
        try {
            this.setMaximum(true);
            this.setSelected(true);
        } catch(Exception ex) { }
        this.printPanel.displayPrintPreview();
    }
    
}
