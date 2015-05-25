/*
 * EditNoteTypes.java
 *
 * Created on October 3, 2005, 8:47 AM
 */

package rmischedule.misc;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.creamtec.ajaxswing.AjaxSwingManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import schedfoxlib.model.ClientContactType;


/**
 *
 * @author  Ira Juneau
 */
public class EditClientContactTypes extends javax.swing.JInternalFrame {
    
    public static final Color  mouseOverColor   = new Color(175, 175, 255);
    public static final Color  selectedColor    = new Color(0, 235, 30);
    public static final Border mouseOverBorder  = new CompoundBorder(new LineBorder(mouseOverColor, 1),
                                                                     new EtchedBorder());
    public static final Border selectedBorder   = new CompoundBorder(new LineBorder(selectedColor, 1),
                                                                     new EtchedBorder());
    public static final Border emptyBorder      = new CompoundBorder(new EmptyBorder(1, 1, 1, 1),
                                                                     new EtchedBorder());
    
    private ArrayList<ClientContactType> contacts;
    private String companyId;
    
    /** Creates new form EditNoteTypes */
    public EditClientContactTypes(String compId) {
        initComponents();
        this.contacts = new ArrayList<ClientContactType>();
        this.companyId = compId;
        this.loadData();
    }
    
    
    private Connection getConnection() { 
        Connection con = new Connection();
        con.setCompany(this.companyId);
        return con;
    }
   
    
    private void clearData() {
        this.noteListPanel.removeAll();
        this.contactTypeTxt.setText("");
    }
    
    
    private void loadData() {
        get_all_client_contact_types_query query = new get_all_client_contact_types_query();
        Connection con = this.getConnection();
        query.setPreparedStatement(new Object[]{});
        Record_Set rs = con.executeQuery(query);

        this.contacts.clear();
        for(int i = 0; i < rs.length(); i++) {
            this.contacts.add(new ClientContactType(rs));
            rs.moveNext();
        }

        for (int c = 0; c < this.contacts.size(); c++) {
            this.noteListPanel.add(new ClientContactListPanel(this.contacts.get(c)));
        }
    }
    
    
    private void refreshData() {
        this.clearData();
        this.loadData();
        this.noteListPanel.revalidate();
        this.noteListPanel.repaint();
    }
    
    private void saveContactType(ClientContactType clientType) {
        save_client_contact_type_query mySaveQuery = new save_client_contact_type_query();
        boolean update = true;
        Connection con = this.getConnection();
        if (clientType.getClientContactType() == null || clientType.getClientContactType() == 0) {
            update = false;
            get_next_client_contact_type_id_query mySeqQuery = new get_next_client_contact_type_id_query();
            mySeqQuery.setPreparedStatement(new Object[]{});
            Record_Set seq = con.executeQuery(mySeqQuery);
            clientType.setClientContactType(seq.getInt("val"));
        }

        mySaveQuery.setObject(clientType, update);
        con.executeQuery(mySaveQuery);

        this.clearData();
        this.refreshData();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        noteListPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        newNoteLabel = new javax.swing.JLabel();
        contactTypeTxt = new javax.swing.JTextField();
        AddNoteTypeBtn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Edit Client Contact Types");

        noteListPanel.setLayout(new javax.swing.BoxLayout(noteListPanel, javax.swing.BoxLayout.Y_AXIS));
        getContentPane().add(noteListPanel, java.awt.BorderLayout.CENTER);

        controlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Add New Client Contact"));
        controlPanel.setMaximumSize(new java.awt.Dimension(32767, 80));
        controlPanel.setMinimumSize(new java.awt.Dimension(10, 80));
        controlPanel.setPreferredSize(new java.awt.Dimension(10, 80));
        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.LINE_AXIS));

        newNoteLabel.setText("Client Contact Type:");
        controlPanel.add(newNoteLabel);

        contactTypeTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        contactTypeTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contactTypeTxtKeyTyped(evt);
            }
        });
        controlPanel.add(contactTypeTxt);

        AddNoteTypeBtn.setText("Add Type");
        AddNoteTypeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNoteTypeBtnActionPerformed(evt);
            }
        });
        controlPanel.add(AddNoteTypeBtn);

        getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-400)/2, (screenSize.height-300)/2, 400, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void contactTypeTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactTypeTxtKeyTyped
        if(evt.getKeyChar() == '\n') {
            this.AddNoteTypeBtn.doClick();
        }
    }//GEN-LAST:event_contactTypeTxtKeyTyped

    private void AddNoteTypeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNoteTypeBtnActionPerformed
        if(this.contactTypeTxt.getText().trim().length() > 0) {
            ClientContactType contactType = new ClientContactType();
            contactType.setContactType(contactTypeTxt.getText());
            saveContactType(contactType);
        }
    }//GEN-LAST:event_AddNoteTypeBtnActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddNoteTypeBtn;
    private javax.swing.JTextField contactTypeTxt;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel newNoteLabel;
    private javax.swing.JPanel noteListPanel;
    // End of variables declaration//GEN-END:variables
    
    
    private class ClientContactListPanel extends JPanel implements MouseListener, FocusListener, KeyListener {
        
        private JTextField  noteTypeTextField;
        private JLabel      deleteLabel;
        private boolean     selected;
        private String oldValue = "";
        private ClientContactType contactType;
        
        public ClientContactListPanel(ClientContactType type) {
            this.initComponents();
            this.noteTypeTextField.setText(type.getContactType());
            this.contactType = type;
            this.selected = false;
        }
        
        
        private void deleteNoteType() {
            delete_contact_type_query query = new delete_contact_type_query();
            Connection con = getConnection();
            con.prepQuery(query);
            try {
                query.setPreparedStatement(new Object[]{contactType.getClientContactType()});
                con.executeQuery(query);
                refreshData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        
        public void setSelected(boolean b) {
            this.selected = b;
            this.noteTypeTextField.setEditable(b);            
            this.noteTypeTextField.getCaret().setVisible(b);
            if(b) {
                this.oldValue = this.noteTypeTextField.getText();
                this.setBorder(EditClientContactTypes.selectedBorder);
                this.noteTypeTextField.setSelectionStart(0);
                this.noteTypeTextField.setSelectionEnd(this.noteTypeTextField.getText().length());
            } else {
                this.noteTypeTextField.setText(this.oldValue);
                this.setBorder(EditClientContactTypes.emptyBorder);
            }
        }
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() == this.noteTypeTextField && e.getClickCount() == 2) {
                this.setSelected(true);
            } else if(e.getSource() == this.deleteLabel) {
                if(JOptionPane.showConfirmDialog(this, "Are you SURE you want to delete this contact type?", "Confirm Delete",
                                                 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    this.deleteNoteType();
                }
            }
        }


        public void focusLost(FocusEvent e) {
            if(e.getSource() == this.noteTypeTextField && this.selected) {
                this.setSelected(false);
            }
        }

        
        public void mouseEntered(MouseEvent e) {
            if(!this.selected) {
                this.setBorder(EditClientContactTypes.mouseOverBorder);
            }
        }

        
        public void mouseExited(MouseEvent e) {
            if(!this.selected) {
                this.setBorder(EditClientContactTypes.emptyBorder);
            }
        }

        
        public void keyPressed(KeyEvent e) {
            if(e.getSource() == this.noteTypeTextField) {
                if(e.getKeyCode() == e.VK_ENTER) {
                    if (noteTypeTextField.getText().length() > 0) {
                        this.contactType.setContactType(noteTypeTextField.getText());
                        saveContactType(this.contactType);
                    }
                } else if(e.getKeyCode() == e.VK_ESCAPE) {
                    this.setSelected(false);
                }
            }        
        }
        
        
        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) { }
        public void focusGained(FocusEvent e) { }
        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }        
        
        
        private void initComponents() {
            this.noteTypeTextField = new JTextField();
            this.noteTypeTextField.setEditable(false);
            this.noteTypeTextField.setBorder(BorderFactory.createEmptyBorder());
            this.noteTypeTextField.addMouseListener(this);
            this.noteTypeTextField.addFocusListener(this);
            this.noteTypeTextField.addKeyListener(this);
            this.noteTypeTextField.setToolTipText("Double-click to edit");
            
            this.deleteLabel = new JLabel();
            this.deleteLabel.setIcon(Main_Window.Garbage_Full_16x16);
            this.deleteLabel.addMouseListener(this);
            this.deleteLabel.setToolTipText("Click to delete note type");
            
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setBorder(EditClientContactTypes.emptyBorder);
            this.setMaximumSize(new Dimension(32767, 24));
            this.add(this.noteTypeTextField);
            this.add(this.deleteLabel);
            if(AjaxSwingManager.isAjaxSwingRunning()){
            	this.noteTypeTextField.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        //saveNoteType();
                    }
                });
            }
        } 
    }
}
