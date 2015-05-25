/*
 * DefaultFooterDelegate.java
 *
 * Created on September 28, 2006, 9:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import java.awt.*;
import java.awt.font.*;
import javax.swing.*;

/**
 * A simple footer that draws a line at the bottom of the page, and displays the page
 * number below that line.
 *
 * Somewhat of a misnomer as the default footer delegate for a document is actually null.
 * The PrintDocument class provides a static factory method to obtain an instance of this
 * class so you can easily add this simple type of footer to your document if you wish.
 *
 * @author shawn
 */
class DefaultFooterDelegate implements IPrintFooter {

    public JPanel getFooter(int pageNum) {
        return new DefaultFooter(pageNum);
    }
    
    private class DefaultFooter extends JPanel implements ITotalPages {
        
        private JLabel pageLabel;
        
        public DefaultFooter(int pageNum) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBackground(Color.WHITE);
            this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 0, 0, 0), new java.awt.Color(0, 0, 0)));
            this.pageLabel = new JLabel("Page " + pageNum + " of ");
            this.pageLabel.setFont(pageLabel.getFont().deriveFont(Font.PLAIN));
            this.pageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            this.add(pageLabel);            
        }
        
        public void setTotalPages(int numPages) {
            this.pageLabel.setText(this.pageLabel.getText() + numPages);
        }
    }
    
}
