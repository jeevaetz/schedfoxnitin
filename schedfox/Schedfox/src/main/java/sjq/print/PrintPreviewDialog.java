/*
 * PrintPreviewDialog.java
 *
 * Created on September 27, 2006, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author shawn
 */
public class PrintPreviewDialog extends JDialog {
    
    private PrintPreviewPanel printPanel;
    
    /** Creates a new instance of PrintPreviewDialog */
    public PrintPreviewDialog(PrintDocument doc, Frame owner) {
        super(owner);
        this.init(doc);
    }
    
    public PrintPreviewDialog(PrintDocument doc, Dialog owner) {
        super(owner, false);
        this.init(doc);
    }
    
    public PrintPreviewDialog(PrintDocument doc) {
        super();
        this.init(doc);
    }
    
    private void init(PrintDocument doc) {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.printPanel = new PrintPreviewPanel(doc);
        this.getContentPane().add(this.printPanel, BorderLayout.CENTER);
        this.setTitle("Print Preview -- " + doc.getDocumentName());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, screenSize.width, screenSize.height);
    }
    
    public void displayPrintPreview() {
        this.setVisible(true);
        this.printPanel.displayPrintPreview();
    }    
    
    
}
