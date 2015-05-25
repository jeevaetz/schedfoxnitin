/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GenericMessageBoard.java
 *
 * Created on Jun 23, 2010, 10:23:14 AM
 */
package rmischedule.messagingboard;

import rmischeduleserver.mysqlconnectivity.queries.messageBoard.GetEmailMessages;
import rmischeduleserver.mysqlconnectivity.queries.messageBoard.EmailFolderQuery;
import rmischeduleserver.mysqlconnectivity.queries.messageBoard.GetAllContacts;
import rmischeduleserver.mysqlconnectivity.queries.messageBoard.GetSentMessages;
import rmischeduleserver.mysqlconnectivity.queries.messageBoard.MarkAsReadQuery;
import rmischeduleserver.mysqlconnectivity.queries.messageBoard.DeleteMessageQuery;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultListModel;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.Company;
import rmischedule.security.User;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;

/**
 *
 * @author vnguyen
 */
public class GenericMessageBoard extends javax.swing.JInternalFrame {

    @Override
    public void setIcon(boolean b) throws PropertyVetoException {
        super.setIcon(b);
    }
    Vector<Company> myCompanies = Main_Window.getActiveListOfCompanies();
    //Message Containers
    Vector<MessageNote> currMsgBox = new Vector();
    HashMap<String, Vector<MessageNote>> folderToMsg = new HashMap<String, Vector<MessageNote>>();
    //listbox index to FolderName
    HashMap<Integer, String> indToFolder = new HashMap<Integer, String>();
    // combobox --> companyId
    HashMap<Integer, String> indToComp = new HashMap<Integer, String>();
    //folder ID 2 WAY HASHES
    HashMap<Integer, Integer> indToDbInd = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> dbToInd = new HashMap<Integer, Integer>();
    //Contact Hashes companyId --> Vector of people in that company
    HashMap<String, Vector<Contact>> compIdToContacts = new HashMap<String, Vector<Contact>>();
    Vector<Contact> allContacts = new Vector<Contact>();
    private User myUser;
    private Connection myConn = new Connection();

    /** Creates new form GenericMessageBoard */
    public GenericMessageBoard() {
    }

    public void setInformation(User tempUser) {
        this.myUser = tempUser;

        for (int i = 0; i < this.myCompanies.size(); i++) {
            this.indToComp.put(new Integer(i + 1), this.myCompanies.get(i).getId());
        }
        this.setName("SchedFox Email System");
        initComponents();
        if (this.myCompanies.size() < 2) {
            this.getCompanyComboBox().setVisible(false);
        } else {
            this.getCompanyComboBox().setVisible(true);
            this.getCompanyComboBox().removeAllItems();
            this.getCompanyComboBox().addItem("ALL");
            for (int i = 0; i < this.myCompanies.size(); i++) {
                this.getCompanyComboBox().addItem(this.myCompanies.get(i).getName());
            }
        }

        this.loadData();
    }

    private void loadData() {
        this.getCompanyComboBox().setSelectedIndex(0);
        this.folderToMsg.clear();
        this.indToFolder.clear();
        this.currMsgBox.clear();
        this.indToDbInd.clear();
        this.dbToInd.clear();
        //this.initialTestData();
        getMessages();
        //initialTestData();
        this.jList1.setSelectedIndex(0);
    }

    private void fillFolders(Record_Set rs) {

        //will be replaced by actual query later
        DefaultListModel listModel = new DefaultListModel();
        int i = 0;
        do {
            String folder = rs.getString("foldername");
            this.indToFolder.put(i, folder);
            int dbInd = Integer.parseInt(rs.getString("folderid"));
            this.indToDbInd.put(i, dbInd);
            this.dbToInd.put(dbInd, i);
            listModel.addElement(folder);
            i++;
        } while (rs.moveNext());
        //need to change to no slection
        this.jList1.setModel(listModel);
    }

    private void getMessages() {

        RunQueriesEx myDataPull = new RunQueriesEx();
        myDataPull.add(EmailFolderQuery.getInstance());
        Vector<String> compIds = new Vector<String>();
        for (Company c : this.myCompanies) {
            compIds.add(c.getId());
        }
        myDataPull.add(new GetAllContacts(compIds));
        for (int i = 0; i < this.myCompanies.size(); i++) {
            myDataPull.add(new GetEmailMessages(this.myUser.getUserId(), this.myCompanies.get(i).getDB()));
            myDataPull.add(new GetSentMessages(this.myUser.getUserId(), this.myCompanies.get(i).getDB()));
        }
        ArrayList<Record_Set> rs = getMyConn().executeQueryEx(myDataPull);
        this.fillFolders(rs.get(0));
        this.fillAddressBook(rs.get(1));

        for (int i = 2; i < rs.size(); i++) {
            int compInd = (i / 2) - 1;
            if (i % 2 == 0) {
                //incoming
                procIncomingMessageSet(rs.get(i), this.myCompanies.get(compInd));
            } else {
                //recorded outgoing
                procSendBox(rs.get(i), this.myCompanies.get(compInd));
            }
        }

    }

    public void addMessages(Vector<MessageNote> notes) {
        for (MessageNote n : notes) {
            if (this.getCompanyComboBox().getSelectedIndex() == 0
                    || this.indToComp.get(this.getCompanyComboBox().getSelectedIndex()).compareToIgnoreCase(n.getCompanyId()) == 0) {
                this.currMsgBox.add(n);
                this.jPanel4.add(n);
            }
        }
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
    }

    public void addMessage(MessageNote n) {
        this.currMsgBox.add(n);
        this.jPanel4.add(n);
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        button4 = new java.awt.Button();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        btnComposeMail = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnMarkAsRead = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        companyComboBox = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        MsgPanel = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        msgBoxPane = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        button1.setLabel("button1");

        button4.setLabel("button4");

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Internal Email System");
        setMinimumSize(new java.awt.Dimension(57, 50));
        setName("MessageBoardFrame"); // NOI18N
        setOpaque(true);
        setPreferredSize(new java.awt.Dimension(500, 251));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jToolBar1.add(jPanel2);

        btnComposeMail.setText("Compose Mail");
        btnComposeMail.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnComposeMail.setFocusable(false);
        btnComposeMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnComposeMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnComposeMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComposeMailActionPerformed(evt);
            }
        });
        jToolBar1.add(btnComposeMail);

        btnDelete.setText("Delete");
        btnDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDelete);

        btnMarkAsRead.setText("Mark As Read");
        btnMarkAsRead.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMarkAsRead.setFocusable(false);
        btnMarkAsRead.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMarkAsRead.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMarkAsRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarkAsReadActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMarkAsRead);

        btnRefresh.setText("Refresh");
        btnRefresh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRefresh.setFocusable(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefresh);

        jCheckBox1.setText("Select All");
        jCheckBox1.setFocusable(false);
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jCheckBox1);
        jToolBar1.add(jPanel5);

        companyComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        companyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyComboBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(companyComboBox);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jPanel1.setAutoscrolls(true);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));
        MsgPanel.setViewportView(jPanel4);

        jPanel1.add(MsgPanel);

        jSplitPane1.setRightComponent(jPanel1);
        jSplitPane1.setLeftComponent(jPanel3);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "INBOX", "SENT MAIL", "DRAFT", "TRASH" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList1.setAutoscrolls(false);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                msgBoxChange(evt);
            }
        });
        msgBoxPane.setViewportView(jList1);

        jSplitPane1.setLeftComponent(msgBoxPane);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMarkAsReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsReadActionPerformed
        // TODO add your handling code here:
        try {
            Vector<MessageNote> readNotes = new Vector<MessageNote>();
            for (MessageNote n : this.currMsgBox) {
                if (n.isSelected()) {
                    n.setRead(true);
                    n.setBackground();
                    readNotes.add(n);
                }
            }
            markAsReadToDB(readNotes);
            this.repaint();

        } catch (Exception e) {
        }


}//GEN-LAST:event_btnMarkAsReadActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        Vector<MessageNote> deleted = new Vector<MessageNote>();
        for (MessageNote n : this.currMsgBox) {
            if (n.isSelected()) {
                deleted.add(n);
            }
        }
        this.removeFromDb(deleted);
        for (MessageNote n : deleted) {
            this.currMsgBox.remove(n);
            this.jPanel4.remove(n);
        }
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
        String folderName = this.indToFolder.get(this.jList1.getSelectedIndex());
        Vector<MessageNote> temp = this.folderToMsg.get(folderName);
        for (MessageNote n : deleted) {
            temp.remove(n);
        }
}//GEN-LAST:event_btnDeleteActionPerformed

    private void companyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyComboBoxActionPerformed
        // TODO add your handling code here:
        try {
            if (this.jPanel4.getComponentCount() == 1
                    && this.jPanel4.getComponent(0) instanceof GenericCreateMessageScreen) {
                if (this.companyComboBox.getSelectedIndex() != 0) {
                    this.companyComboBox.setSelectedIndex(0);
                }
            } else {
                this.displayBasedOnIndex(this.jList1.getSelectedIndex());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_companyComboBoxActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        for (MessageNote n : this.currMsgBox) {
            n.setSelected(this.jCheckBox1.isSelected());
        }
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void msgBoxChange(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_msgBoxChange

        for (MessageNote n : this.currMsgBox) {
            n.setSelected(false);
        }
        this.currMsgBox.clear();
        try {
            this.jCheckBox1.setSelected(false);
            this.displayBasedOnIndex(this.jList1.getSelectedIndex());

        } catch (Exception e) {
            this.jCheckBox1.setSelected(false);
        }
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
}//GEN-LAST:event_msgBoxChange

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        //call initial loaddata
        reLoad();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnComposeMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComposeMailActionPerformed
        // TODO add your handling code here:
       /*
        for (int i = 0; i < this.myCompanies.size(); i++) {
        Vector<Branch> currBranches = this.myCompanies.get(i).getBranches();
        System.out.println("Company ID: \t" + this.myCompanies.get(i).getId());
        System.out.println("Company DB: \t" + this.myCompanies.get(i).getDB());
        System.out.println("Company Name: \t" + this.myCompanies.get(i).getName());
        System.out.println("------------------------------------------------------");
        this.myCompanies.get(i).getDB();
        for (int j = 0; j < currBranches.size(); j++) {
        System.out.println("Branch ID: \t " + currBranches.get(j).getId());
        System.out.println("Branch Name: \t" + currBranches.get(j).getName());
        }
        System.out.println("========================================================");

        }*/
        this.companyComboBox.setSelectedIndex(0);
        GenericCreateMessageScreen composeScrn = new GenericCreateMessageScreen(this);
        this.jList1.getSelectionModel().clearSelection();
        this.jPanel4.removeAll();
        composeScrn.setSize(this.jPanel4.getMaximumSize());
        composeScrn.setVisible(true);
        this.jPanel4.add(composeScrn);
        composeScrn.revalidate();
        composeScrn.repaint();
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
        this.revalidate();
        this.repaint();

    }//GEN-LAST:event_btnComposeMailActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MsgPanel;
    private javax.swing.JButton btnComposeMail;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnMarkAsRead;
    private javax.swing.JButton btnRefresh;
    private java.awt.Button button1;
    private java.awt.Button button4;
    private javax.swing.JComboBox companyComboBox;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane msgBoxPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getName() {
        return "Message Board";
    }

    private void removeFromDb(Vector<MessageNote> msgs) {
        //to
        RunQueriesEx readQueries = new RunQueriesEx();
        Vector<String> myCompaniesId = new Vector<String>();
        Vector<String> myCompDB = new Vector<String>();
        for (Company c : myCompanies) {
            myCompaniesId.add(c.getId());
            myCompDB.add(c.getDB());
        }
        for (MessageNote n : msgs) {
            readQueries.add(new DeleteMessageQuery(n.getCompanyId(), myCompaniesId, myCompDB));
        }
        this.getMyConn().executeQueryEx(readQueries);
    }

    private MessageNote getMsgFromNoteId(String id) {
        for (MessageNote n : this.currMsgBox) {
            if (n.getMyNoteId().compareToIgnoreCase(id) == 0) {
                return n;
            }
        }
        return null;
    }

    public void deleteNote(String myNoteId) {
        MessageNote tobeDeleted = this.getMsgFromNoteId(myNoteId);
        this.jPanel4.remove(tobeDeleted);
        this.currMsgBox.remove(tobeDeleted);
        Vector<MessageNote> msgs = new Vector<MessageNote>();
        msgs.add(tobeDeleted);
        String folderName = this.indToFolder.get(this.jList1.getSelectedIndex());
        Vector<MessageNote> temp = this.folderToMsg.get(folderName);
        for (MessageNote n : msgs) {
            temp.remove(n);
        }
        this.removeFromDb(msgs);
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
    }

    private void displayBasedOnIndex(int selectedIndex) {
        //to do display messages appropiate to the index
        this.jPanel4.removeAll();
        this.currMsgBox.clear();
        String folderName = this.indToFolder.get(selectedIndex);
        Vector<MessageNote> msgToShow = this.folderToMsg.get(folderName);
        this.addMessages(msgToShow);
        this.jPanel4.revalidate();
        this.jPanel4.repaint();
    }

    private void markAsReadToDB(Vector<MessageNote> readNotes) {
        //to do mark in db as read
        RunQueriesEx readQueries = new RunQueriesEx();
        Vector<String> myCompaniesId = new Vector<String>();
        Vector<String> myCompaniesDB = new Vector<String>();
        for (Company c : this.myCompanies) {
            myCompaniesId.add(c.getId());
            myCompaniesDB.add(c.getDB());
        }
        for (MessageNote n : readNotes) {
            String noteCompanyId = n.getCompanyId();
            readQueries.add(new MarkAsReadQuery(noteCompanyId, myCompaniesId, myCompaniesDB));
        }
        this.getMyConn().executeQueryEx(readQueries);
    }

    /**
     * @return the myUser
     */
    public User getMyUser() {
        return myUser;
    }

    private void procIncomingMessageSet(Record_Set rs, Company comp) {

        if (rs.length() != 0) {
            do {
                try {
                    String msgId = rs.getString("message_id");
                    String senderId = rs.getString("sender_id");
                    String subject = rs.getString("subject");
                    String sendDate = rs.getString("send_date");
                    String sendTime = rs.getString("send_time");
                    String reciever = rs.getString("reciever_id");
                    String msg = rs.getString("message");
                    //String fldIdDb = rs.getString("folder");

                    String read = rs.getString("read");
                    int folderIdfromDb = rs.getInt("folder");
                    /*try {
                    folderIdfromDb = Integer.parseInt(rs.getString(fldIdDb));
                    } catch (Exception e) {
                    folderIdfromDb = 0;
                    System.out.println("folder integer not an integer " +e);
                    }*/
                    boolean markAsRead;
                    if (read.compareToIgnoreCase("t") == 0) {
                        markAsRead = true;
                    } else {
                        markAsRead = false;
                    }
                    String date = sendDate + " " + sendTime.substring(0, 5);
                    Contact c = this.getContactFromUserId(senderId, comp);
                    Contact c2 = this.getContactFromUserId(reciever, comp);
                    String name = c.getFirstName() + " " + c.getLastName();
                    String name2 = c2.getFirstName() + " " + c2.getLastName();
                    MessageNote n = new MessageNote(msgId, name, this.myUser.getUserId(), date, msg, subject, this, markAsRead, comp.getId(), name2);
                    int ourSelection = this.dbToInd.get(folderIdfromDb);
                    String folder = this.indToFolder.get(ourSelection);
                    Vector<MessageNote> bucket = this.folderToMsg.get(folder);
                    if (bucket == null) {
                        bucket = new Vector<MessageNote>();
                        this.folderToMsg.put(folder, bucket);
                    }
                    bucket.add(n);
                } catch (Exception e) {
                    System.out.println("error in pulling this record");
                }
            } while (rs.moveNext());
        }
    }

    private Contact getContactFromUserId(String userId, Company co) {
        Vector<Contact> contacts = this.compIdToContacts.get(co.getId());
        for (Contact c : contacts) {
            if (c.getUserId().compareToIgnoreCase(userId) == 0) {
                return c;
            }
        }
        //if unfound returns null
        return null;
    }

    private void fillAddressBook(Record_Set rs) {
        ArrayList<Vector<Contact>> contactBuckets = new ArrayList<Vector<Contact>>();
        for (int i = 0; i < this.myCompanies.size(); i++) {
            Vector<Contact> temp = new Vector<Contact>();
            contactBuckets.add(temp);
            this.compIdToContacts.put(this.myCompanies.get(i).getId(), temp);

        }
        do {
            String userId = rs.getString("user_id");
            String companyId = rs.getString("company_id");
            String firstName = rs.getString("user_first_name");
            String lastName = rs.getString("user_last_name");
            Contact newContact = new Contact(userId, companyId, firstName, lastName);
            this.allContacts.add(newContact);
            this.compIdToContacts.get(companyId).add(newContact);
        } while (rs.moveNext());

    }

    void setReadOnDB(MessageNote eMsg) {
        Vector<MessageNote> temp = new Vector<MessageNote>();
        temp.add(eMsg);
        this.markAsReadToDB(temp);
    }

    private Company getCompfromID(String companyId) {
        for (Company c : this.myCompanies) {
            if (c.getId().compareToIgnoreCase(companyId) == 0) {
                return c;
            }
        }
        return null;
    }

    private void procSendBox(Record_Set rs, Company comp) {

        if (rs.length() != 0) {
            do {
                String msgId = rs.getString("message_id");
                String senderId = rs.getString("sender_id");
                String subject = rs.getString("subject");
                String sendDate = rs.getString("send_date");
                String sendTime = rs.getString("send_time");
                String reciever = rs.getString("reciever_id");
                String msg = rs.getString("message");
                String read = rs.getString("read");
                boolean markAsRead;
                if (read.compareToIgnoreCase("t") == 0) {
                    markAsRead = true;
                } else {
                    markAsRead = false;
                }
                String date = sendDate + " " + sendTime.substring(0, 5);
                Contact c = this.getContactFromUserId(senderId, comp);
                Contact c2 = this.getContactFromUserId(reciever, comp);
                String name = c.getFirstName() + " " + c.getLastName();
                String name2 = c2.getFirstName() + " " + c2.getLastName();
                MessageNote n = new MessageNote(msgId, name, this.myUser.getUserId(), date, msg, subject, this, comp.getId(), name2);

                String folder = "SENT";
                Vector<MessageNote> bucket = this.folderToMsg.get(folder);
                if (bucket == null) {
                    bucket = new Vector<MessageNote>();
                    this.folderToMsg.put(folder, bucket);
                }
                bucket.add(n);

            } while (rs.moveNext());
        }
    }

    /**
     * @return the myConn
     */
    public Connection getMyConn() {
        return myConn;
    }

    public void reLoad() {
        this.currMsgBox.clear();
        this.getCompanyComboBox().setSelectedIndex(0);
        this.jList1.removeAll();
        this.loadData();
        this.revalidate();
        this.repaint();
    }

    public void replyNote(MessageNote note) {
        //To DO
        String compID = note.getCompanyId();
        Vector<Contact> contacts = this.compIdToContacts.get(compID);
        Contact c = null;
        for (Contact con : contacts) {
            if (note.getName().compareToIgnoreCase(con.getFirstName() + " " + con.getLastName()) == 0) {
                c = con;
            }
        }
        System.out.println("Contact found : " + c.getFirstName() + " " + c.getLastName());
        GenericCreateMessageScreen composeScrn = new GenericCreateMessageScreen(this, c, note);
        this.jList1.getSelectionModel().clearSelection();
        this.jPanel4.removeAll();
        composeScrn.setSize(this.jPanel4.getMaximumSize());
        composeScrn.setVisible(true);
        this.jPanel4.add(composeScrn);
        this.jPanel4.revalidate();
        this.revalidate();
        this.repaint();
    }

    /**
     * @return the companyComboBox
     */
    public javax.swing.JComboBox getCompanyComboBox() {
        return companyComboBox;
    }
}
