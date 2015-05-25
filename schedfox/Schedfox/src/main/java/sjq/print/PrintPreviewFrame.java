/*
 * PrintPreviewFrame.java
 *
 * Created on September 27, 2006, 3:05 PM
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
public class PrintPreviewFrame extends JFrame {
    
    private PrintPreviewPanel printPanel;
    
    /** Creates a new instance of PrintPreviewFrame */
    public PrintPreviewFrame(PrintDocument doc) {
        super();
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
