/*
 * xClient_Notes.java
 *
 * Created on September 8, 2005, 12:24 PM
 */

package rmischedule.client.components;

import java.util.*;

import javax.swing.*;
import rmischedule.main.*;
import rmischedule.security.*;
import schedfoxlib.model.util.Record_Set;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.employee.components.xEmployee_Notes;
import rmischedule.misc.SelectNoteTypesDialog;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.mysqlconnectivity.queries.util.*;

import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.client.client_note_list_query;
import schedfoxlib.model.ClientNotes;
import schedfoxlib.model.NoteInterface;
/**
 *
 * @author  Ira Juneau
 */
public class xClient_Notes extends GenericEditSubForm implements ClientNoteInterface {

    protected int updateCount;
    protected GenericEditForm myparent;
    protected Vector<Integer> noteId;
    protected ArrayList<GraphicalNoteClass> myNotes;
    
    /** Creates new form xClient_Notes */
    public xClient_Notes(GenericEditForm myParent) {
        initComponents();
        myNotes = new ArrayList<GraphicalNoteClass>();
        myparent = myParent;
        updateCount = 0;
        
        printNotesBtn.setIcon(Main_Window.Printer_Icon);
    }

    public void doOnClear() {
        NoteTxt.setText("");
        NoteList.removeAll();
        updateCount = 0;
        myNotes.clear();
    }

    public JPanel getMyForm() {
        return this;
    }

    public String getMyTabTitle() {
        return "Notes";
    }
    
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        client_note_list_query cnlq = new client_note_list_query();
        note_type_list_query myNoteType = new note_type_list_query();
        RunQueriesEx myCompleteQuery = new RunQueriesEx();
        cnlq.update(myparent.getMyIdForSave());
        myCompleteQuery.update(myNoteType, cnlq);
        return myCompleteQuery;
    }

    @Override
    public void reloadData() {
        RunQueriesEx query = (RunQueriesEx)getQuery(true);
        ArrayList<Record_Set> data = this.myparent.getConnection().executeQueryEx(query);
        this.updateCount = 0;
        for (int d = 0; d < data.size(); d++) {
            this.loadData(data.get(d));
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }
    
    public void save() {
        
    }
    
    public Integer getNoteIdByName(String name) {
        for (int i = 0; i < noteTypeLb.getItemCount(); i++) {
            if (name.equals((String)noteTypeLb.getItemAt(i))) {
                return noteId.get(i);
            }
        }
        return 0;
    }
    
    @Override
    public void deleteNote(NoteInterface clientNote) {
        try {
            if (JOptionPane.showConfirmDialog(myparent, "Do you want to delete this note permanently?", "Delete Note?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
                clientController.deleteClientNote((ClientNotes)clientNote);
                reloadData();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public void saveNote(NoteInterface clientNote) {
        try {
            ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
            clientController.saveClientNote((ClientNotes)clientNote);
            reloadData();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    
    protected NoteInterface loadNote(Record_Set rs) {
        return new ClientNotes(rs);
    }
    
    @Override
    public void loadData(Record_Set rs) {
        if (updateCount == 1) {
            NoteList.removeAll();
            myNotes = new ArrayList<GraphicalNoteClass>();
            for (int i = 0; i < rs.length(); i++) {
                GraphicalNoteClass myNewNote = new GraphicalNoteClass(loadNote(rs), this, myparent.getConnection().myCompany);
                myNotes.add(myNewNote);
                NoteList.add(myNewNote);
                rs.moveNext();
            }
        } else {
            noteTypeLb.removeAllItems();
            noteId = new Vector();
            for (int i = 0; i < rs.length(); i++) {
                noteTypeLb.addItem(rs.getString("name"));
                noteId.add(rs.getInt("id"));
                rs.moveNext();
            }
            try {
                noteTypeLb.setSelectedIndex(0);
            } catch (Exception e) {}
        }
        updateCount++;
        NoteList.invalidate();
        
    }
    
    public boolean needsMoreRecordSets() {
        if (updateCount == 1) {
            return true;
        } else {
            updateCount = 0;
        }
        return false;
    }
    
    public boolean userHasAccess() {
        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.CLIENT_INFORMATION);
    }
    
    public void saveNote() {
        if(myparent.getSelectedObject() == null) {
            return;
        }
        
        try {
            ClientNotes clientNote = new ClientNotes();
            clientNote.setClientId(Integer.parseInt(myparent.getMyIdForSave()));
            clientNote.setClientNotesDateTime(new Date(GenericController.getInstance("2").getCurrentTimeMillis()));
            clientNote.setClientNotesNotes(NoteTxt.getText().trim());
            clientNote.setNoteTypeId(getNoteIdByName((String)noteTypeLb.getSelectedItem()));
            clientNote.setReadOnCheckin(notifyEmp.isSelected());
            clientNote.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
            clientController.saveClientNote(clientNote);
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        this.reloadData();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        noteListContainer = new javax.swing.JPanel();
        HeaderPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        printNotesBtn = new javax.swing.JButton();
        NoteListScroll = new javax.swing.JScrollPane();
        NoteList = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        noteTypeLb = new javax.swing.JComboBox();
        notifyEmp = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        NoteTxt = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        saveNoteBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        noteListContainer.setMaximumSize(new java.awt.Dimension(50000, 250));
        noteListContainer.setMinimumSize(new java.awt.Dimension(0, 200));
        noteListContainer.setPreferredSize(new java.awt.Dimension(0, 200));
        noteListContainer.setLayout(new javax.swing.BoxLayout(noteListContainer, javax.swing.BoxLayout.Y_AXIS));

        HeaderPanel.setMaximumSize(new java.awt.Dimension(10000, 32));
        HeaderPanel.setMinimumSize(new java.awt.Dimension(0, 32));
        HeaderPanel.setPreferredSize(new java.awt.Dimension(0, 32));
        HeaderPanel.setLayout(new javax.swing.BoxLayout(HeaderPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(6, 28, 111));
        jLabel1.setText(" Click on a note to display contents or click on the printer to print out notes.");
        jLabel1.setMaximumSize(new java.awt.Dimension(50000, 15));
        HeaderPanel.add(jLabel1);

        printNotesBtn.setMaximumSize(new java.awt.Dimension(32, 32));
        printNotesBtn.setMinimumSize(new java.awt.Dimension(32, 32));
        printNotesBtn.setPreferredSize(new java.awt.Dimension(32, 32));
        printNotesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printNotesBtnActionPerformed(evt);
            }
        });
        HeaderPanel.add(printNotesBtn);

        noteListContainer.add(HeaderPanel);

        NoteList.setLayout(new javax.swing.BoxLayout(NoteList, javax.swing.BoxLayout.Y_AXIS));
        NoteListScroll.setViewportView(NoteList);

        noteListContainer.add(NoteListScroll);

        add(noteListContainer);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Add A New Note"));
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 140));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 140));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 140));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 24));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Note Type  ");
        jPanel1.add(jLabel2);

        noteTypeLb.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel1.add(noteTypeLb);

        notifyEmp.setText("Notify employee on next phone checkin");
        jPanel1.add(notifyEmp);

        jPanel2.add(jPanel1);

        jScrollPane1.setViewportView(NoteTxt);

        jPanel2.add(jScrollPane1);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 25));

        saveNoteBtn.setText("Save Note");
        saveNoteBtn.setMaximumSize(new java.awt.Dimension(140, 20));
        saveNoteBtn.setMinimumSize(new java.awt.Dimension(140, 20));
        saveNoteBtn.setPreferredSize(new java.awt.Dimension(140, 20));
        saveNoteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNoteBtnActionPerformed(evt);
            }
        });
        jPanel3.add(saveNoteBtn);

        jPanel2.add(jPanel3);

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents

    private void saveNoteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNoteBtnActionPerformed
        if (NoteTxt.getText().trim().length() > 0) {
            saveNote();
            NoteTxt.setText("");
        }
    }//GEN-LAST:event_saveNoteBtnActionPerformed

    private void printNotesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printNotesBtnActionPerformed
        boolean isEmployeeWindow = this instanceof xEmployee_Notes;
        
        SelectNoteTypesDialog selectNoteTypes = new SelectNoteTypesDialog(Main_Window.parentOfApplication, true, 
                myparent.getConnection().myCompany, Integer.parseInt(myparent.getMyIdForSave()), isEmployeeWindow);
        selectNoteTypes.setVisible(true);
    }//GEN-LAST:event_printNotesBtnActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HeaderPanel;
    protected javax.swing.JPanel NoteList;
    private javax.swing.JScrollPane NoteListScroll;
    protected javax.swing.JTextArea NoteTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel noteListContainer;
    protected javax.swing.JComboBox noteTypeLb;
    private javax.swing.JCheckBox notifyEmp;
    private javax.swing.JButton printNotesBtn;
    private javax.swing.JButton saveNoteBtn;
    // End of variables declaration//GEN-END:variables

    
}
