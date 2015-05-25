/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProblemContactTypePanel.java
 *
 * Created on Feb 17, 2011, 1:41:39 PM
 */

package rmischedule.client.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JScrollPane;
import rmischedule.components.graphicalcomponents.PrettyButton;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.Contact;
import schedfoxlib.model.EmailContact;
import schedfoxlib.model.PhoneContact;
import schedfoxlib.model.ProblemSolverContacts;

/**
 *
 * @author user
 */
public class ProblemContactTypePanel<T extends Contact> extends javax.swing.JPanel {

    private String title = "Unfilled in title";
    private String companyId;
    private int branchId;
    private ArrayList<T> users;

    private boolean isMaximized;

    private HashMap<T, ContactLabel> contacts =
            new HashMap<T, ContactLabel>();

    private ArrayList<ProblemSolverContacts> lastContacts;
    private String valueInDb;
    private boolean isEmailContact;

    private JScrollPane myScrollPane;

    public ProblemContactTypePanel(String typeOfContact, String valueInDb, String companyId, 
            ArrayList<ProblemSolverContacts> lastContacts, JScrollPane myScroll) {
        this.companyId = companyId;
        this.title = typeOfContact;
        this.valueInDb = valueInDb;
        this.lastContacts = lastContacts;
        isMaximized = false;
        this.myScrollPane = myScroll;
        initComponents();

        this.isEmailContact = true;
    }

    /** Creates new form ProblemContactTypePanel */
    public ProblemContactTypePanel(String typeOfContact, String valueInDb, String companyId, 
            int branchId, ArrayList<ProblemSolverContacts> lastContacts, JScrollPane myScroll) {
        this(typeOfContact, valueInDb, companyId, lastContacts, myScroll);
        this.companyId = companyId;
        this.branchId = branchId;
        this.isEmailContact = true;
        this.refreshPanel(true);

        
    }

    public ProblemContactTypePanel(String typeOfContact, String valueInDb, String companyId, ArrayList<T> contacts, 
            ArrayList<ProblemSolverContacts> lastContacts, boolean isEmailContact, JScrollPane myScroll) {
        this(typeOfContact, valueInDb, companyId, lastContacts, myScroll);

        this.users = contacts;
        this.isEmailContact = isEmailContact;
        this.refreshPanel(false);

    }

    public String getValueInDb() {
        return this.valueInDb;
    }

    public void togglePanel() {
        GridLayout panelLayout = (GridLayout)this.contentPanel.getLayout();
        int rows = (int)Math.ceil((double)contentPanel.getComponentCount() / (double)panelLayout.getColumns());

        int newHeight = rows * 20;
        if (isMaximized) {
            newHeight = 0;
        }
        isMaximized = !isMaximized;
        contentPanel.setPreferredSize(new Dimension((int)contentPanel.getPreferredSize().getWidth(), newHeight));
        contentPanel.setMaximumSize(new Dimension((int)contentPanel.getMaximumSize().getWidth(), newHeight));
        contentPanel.setMinimumSize(new Dimension((int)contentPanel.getMaximumSize().getWidth(), newHeight));

        setPreferredSize(new Dimension((int)getPreferredSize().getWidth(), ((int)titlePanel.getPreferredSize().getHeight() + (int)contentPanel.getPreferredSize().getHeight())));
        setMaximumSize(new Dimension((int)getMaximumSize().getWidth(), ((int)titlePanel.getMaximumSize().getHeight() + (int)contentPanel.getMaximumSize().getHeight())));
        setMinimumSize(new Dimension((int)getMinimumSize().getWidth(), ((int)titlePanel.getMinimumSize().getHeight() + (int)contentPanel.getMinimumSize().getHeight())));

        Container myParent = this.getParent();
        if (myScrollPane != null && isMaximized) {
            myScrollPane.getViewport().setViewPosition(this.getLocation());
        }
        myParent.repaint();
        this.revalidate();
    }

    protected ContactLabel getContactLabel(Object obj) {
        if (this.isEmailContact) {
            return new EmailLabel(obj);
        } else {
            return new PhoneLabel(obj);
        }
    }

    public void refreshPanel(boolean fetchResults) {
        if (fetchResults) {
            UserController userController = new UserController(this.companyId);
            try {
                users = new ArrayList<T>();
                ArrayList<Contact> contacts = userController.getUsersByType(title, branchId + "");
                for (int c = 0; c < contacts.size(); c++) {
                    users.add((T)contacts.get(c));
                }
            } catch (Exception e) {
                users = new ArrayList<T>();
            }
        }

        //Add labels select ones selected last time CC sent, if applicable.
        for (int u = 0; u < users.size(); u++) {
            ContactLabel userLabel = this.getContactLabel(users.get(u));
            userLabel.addCheckboxListener(new CheckboxActionListener());
            contacts.put(users.get(u), userLabel);
            for (int l = 0; l < this.lastContacts.size(); l++) {
                if (lastContacts.get(l).getContactId() == users.get(u).getPrimaryId() &&
                        lastContacts.get(l).getContactType().equals(this.valueInDb)) {
                    userLabel.setChecked(true);
                }
            }
            contentPanel.add(userLabel);
        }
        selectedText.setText(getSelectedText());
    }

    public ArrayList<T> getSelectedContacts() {
        Iterator<T> currContact = contacts.keySet().iterator();
        ArrayList<T> selectedContacts = new ArrayList<T>();
        while (currContact.hasNext()) {
            T contact = currContact.next();
            if (contacts.get(contact).isChecked()) {
                selectedContacts.add(contact);
            }
        }
        return selectedContacts;
    }

    public String getMyTitle() {
        if (this.title.equals("Sales")) {
            return "Business Development";
        }
        return this.title;
    }

    public String getSelectedText() {
        int numberSelected = getSelectedContacts().size();
        int totalNumber = contentPanel.getComponentCount();
        if (numberSelected == 0) {
            titlePanel.setBackground(PrettyButton.redPanelColor);
            return "No contacts selected";
        } else if (numberSelected == totalNumber) {
            titlePanel.setBackground(PrettyButton.bluePanelColor);
            return "All contacts selected";
        } else {
            titlePanel.setBackground(PrettyButton.yellowColor);
            return numberSelected + " contacts selected";
        }
    }

    private void toggleSelectAll() {
        for (int u = 0; u < users.size(); u++) {
            ContactLabel userLabel = contacts.get(users.get(u));
            userLabel.setChecked(selectAllCheckbox.isSelected());
        }
        selectedText.setText(getSelectedText());
    }

    private class CheckboxActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedText.setText(getSelectedText());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        selectedText = new javax.swing.JLabel();
        selectAllCheckbox = new javax.swing.JCheckBox();
        contentPanel = new javax.swing.JPanel();

        setBorder(null);
        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(0, 24));
        setPreferredSize(new java.awt.Dimension(451, 24));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        titlePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        titlePanel.setMaximumSize(new java.awt.Dimension(32767, 22));
        titlePanel.setMinimumSize(new java.awt.Dimension(0, 22));
        titlePanel.setPreferredSize(new java.awt.Dimension(451, 22));
        titlePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                titlePanelMouseClicked(evt);
            }
        });
        titlePanel.setLayout(new javax.swing.BoxLayout(titlePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText(getMyTitle());
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jLabel1.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(120, 17));
        jLabel1.setPreferredSize(new java.awt.Dimension(120, 17));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        titlePanel.add(jLabel1);

        selectedText.setText(getSelectedText());
        selectedText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 50, 0, 0));
        selectedText.setMaximumSize(new java.awt.Dimension(4000, 16));
        selectedText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectedTextMouseClicked(evt);
            }
        });
        titlePanel.add(selectedText);

        selectAllCheckbox.setText("Select All");
        selectAllCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        selectAllCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllCheckboxActionPerformed(evt);
            }
        });
        titlePanel.add(selectAllCheckbox);

        add(titlePanel);

        contentPanel.setLayout(new java.awt.GridLayout(0, 4));
        add(contentPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void titlePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titlePanelMouseClicked
        this.togglePanel();
    }//GEN-LAST:event_titlePanelMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.togglePanel();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void selectedTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectedTextMouseClicked
        this.togglePanel();
    }//GEN-LAST:event_selectedTextMouseClicked

    private void selectAllCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllCheckboxActionPerformed
        toggleSelectAll();
    }//GEN-LAST:event_selectAllCheckboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox selectAllCheckbox;
    private javax.swing.JLabel selectedText;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables

}
