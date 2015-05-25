/*
 * DefaultHeaderDelegate.java
 *
 * Created on September 28, 2006, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import java.awt.*;
import javax.swing.*;

/**
 * A simple header delegate that will display the title passed to it at the
 * top of every page in a bold, 16pt font.
 *
 * Somewhat of a misnomer as the default header delegate for a document is actually null.
 * The PrintDocument class provides a static factory method to obtain an instance of this
 * class so you can easily add this simple type of header to your document if you wish.
 *
 * @author shawn
 */
class DefaultHeaderDelegate implements IPrintHeader {
    
    private String headerTitle;
    
    /** Creates a new instance of DefaultHeaderDelegate */
    public DefaultHeaderDelegate(String title) {
        this.headerTitle = title;
    }

    public JPanel getHeader(int pageNum) {
        return new DefaultHeader(this.headerTitle);
    }
    
    private class DefaultHeader extends JPanel {
        
        private JLabel titleLabel;

        public DefaultHeader(String title) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBackground(Color.WHITE);
            this.titleLabel = new JLabel(title);
            this.titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));           
            this.add(titleLabel);
        }
    }
    
}
