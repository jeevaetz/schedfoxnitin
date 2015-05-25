/*
 * GenericProcessDirectoryPanel.java
 *
 * Created on March 1, 2006, 11:30 AM
 */

package rmischedule.data_import.graphicalimportClasses;
import javax.swing.*;
/**
 *
 * @author  Ira Juneau
 */
public class GenericProcessDirectoryPanel extends javax.swing.JPanel implements ImportGenericType {
    
    protected ImportDataWindow parent;
    
    /** Creates new form GenericProcessDirectoryPanel */
    public GenericProcessDirectoryPanel() {
        initComponents();
    }
    
    public String getTitle() {
        return "Step 2: Which data would you like to process?";
    }
    public JPanel getComponent() {
        return this;
    }
    
    public void showMe() {
        
    }
    
    public void setParent(ImportDataWindow parent) {
        this.parent = parent;
    }
    
    public void doOnNext() {
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        controlPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.Y_AXIS));

        add(controlPanel, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel controlPanel;
    // End of variables declaration//GEN-END:variables
    
}
