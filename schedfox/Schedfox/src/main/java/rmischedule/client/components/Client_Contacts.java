/*
 * Client_Contacts.java
 *
 * Created on August 3, 2006, 11:07 AM
 */
package rmischedule.client.components;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import rmischedule.client.xClientEdit;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.ClientContact;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.client_contact_query;
import rmischeduleserver.mysqlconnectivity.queries.client.delete_client_contact_note_query;
import rmischeduleserver.mysqlconnectivity.queries.client.delete_client_contact_query;
import rmischeduleserver.mysqlconnectivity.queries.client.get_client_contact_notes_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_contact_notes_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_contact_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_contact_id_query;

/**
 *
 * @author shawn
 */
public class Client_Contacts extends GenericEditSubForm {

    private MouseHandler mouseHandler = new MouseHandler();
    private ClientContactNoteDisplay selectedNote = null;
    private Vector<ClientContact> contactVector = new Vector<ClientContact>();
    private ContactTableModel contactModel = new ContactTableModel();

    /**
     * Creates new form Client_Contacts
     */
    public Client_Contacts(xClientEdit parent) {
        this.myparent = parent;
        initComponents();
        this.noteScrollPane.getVerticalScrollBar().setUnitIncrement(4);
        this.noteScrollPane.getVerticalScrollBar().setBlockIncrement(16);
        
    }

    public GeneralQueryFormat getQuery(boolean isSelected) {
        return new client_contact_query(myparent.getMyIdForSave());
    }

    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }

    public void loadData(Record_Set rs) {
        contactVector = new Vector(rs.length());

        for (int i = 0; i < rs.length(); i++) {
            ClientContact contact = new ClientContact(rs);
            contactVector.add(contact);
            rs.moveNext();
        }

        Collections.sort(contactVector);
        this.addButton.setEnabled(true);
        contactModel.fireTableDataChanged();
    }

    public javax.swing.JPanel getMyForm() {
        return this;
    }

    public java.lang.String getMyTabTitle() {
        return "Contacts";
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public void doOnClear() {
        this.clearDataLabels();
        this.clearNotes();
        contactVector.clear();
        this.addButton.setEnabled(false);
        this.addNoteButton.setEnabled(false);
        contactModel.fireTableDataChanged();
    }

    public boolean userHasAccess() {
        return true;
    }

    private void clearDataLabels() {
        this.titleDataLabel.setText("");
        this.nameDataLabel.setText("");
        this.phoneDataLabel.setText("");
        this.cellDataLabel.setText("");
        this.faxDataLabel.setText("");
        this.addressDataLabel1.setText("");
        this.addressDataLabel2.setText("");
        this.cityStateZipLabel.setText("");
        this.emailDataLabel.setText("");
    }

    public void reloadData() {
        Integer lastSelected = 0;
        if (this.contactTable.getSelectedRow() != -1) {
            lastSelected = contactVector.get(contactTable.getSelectedRow()).getClientContactId();
        }
        client_contact_query reloadQuery = new client_contact_query(myparent.getMyIdForSave());
        this.myparent.getConnection().prepQuery(reloadQuery);
        this.clearData();
        this.loadData(this.myparent.getConnection().executeQuery(reloadQuery));
        for (int i = 0; i < contactVector.size(); i++) {
            if ((contactVector.get(i)).getClientContactId().equals(lastSelected)) {
                this.contactTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void saveContact(ClientContact c) {
        boolean update = true;
        if (c.getClientContactId() == null || c.getClientContactId() == 0) {
            update = false;
            get_client_contact_id_query getContactQuery = new get_client_contact_id_query();
            this.myparent.getConnection().prepQuery(getContactQuery);
            getContactQuery.setPreparedStatement(new Object[]{});
            Record_Set rst = myparent.getConnection().executeQuery(getContactQuery);
            c.setClientContactId(rst.getInt(0));
        }
        save_client_contact_query saveQuery = new save_client_contact_query(c, update);

        this.myparent.getConnection().prepQuery(saveQuery);
        this.myparent.getConnection().executeQuery(saveQuery);

    }

    private void setSelectedNote(ClientContactNoteDisplay note) {
        if (this.selectedNote != null) {
            this.selectedNote.contract();
            this.scrollToNote(this.selectedNote);
        }
        if (note != null) {
            note.expand();
            this.scrollToNote(note);
        }
        this.selectedNote = note;
        this.editNoteButton.setEnabled(note != null);
        this.deleteNoteButton.setEnabled(note != null);
    }

    private void scrollToNote(ClientContactNoteDisplay note) {
        if (!this.noteScrollPane.getViewport().isValid()) {
            this.noteScrollPane.validate();
        }
        this.noteScrollPane.getViewport().setViewPosition(note.getLocation());
    }

    private void clearNotes() {
        this.setSelectedNote(null);
        this.mouseHandler = new MouseHandler();
        this.scrollingNotePanel.removeAll();
        this.scrollingNotePanel.revalidate();
        this.noteScrollPane.repaint();
    }

    private void addNote(ClientContactNoteDisplay newNote) {
        newNote.addMouseListener(this.mouseHandler);
        this.scrollingNotePanel.add(newNote);
    }

    private void saveNote(String noteId, String text, String subject) {
        try {
            Integer contactID = contactVector.get(contactTable.getSelectedRow()).getClientContactId();
            save_client_contact_notes_query noteQuery =
                    new save_client_contact_notes_query(noteId, Main_Window.parentOfApplication.getUser().getUserId(),
                    contactID.toString(), text, subject);

            this.myparent.getConnection().prepQuery(noteQuery);
            this.myparent.getConnection().executeUpdate(noteQuery);
            this.clearNotes();
            this.loadNotesForSelectedContact();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNotesForSelectedContact() {
        try {
            ClientContact contact = (ClientContact) contactVector.get(contactTable.getSelectedRow());
            get_client_contact_notes_query noteQuery = new get_client_contact_notes_query(contact.getClientContactId().toString());
            this.myparent.getConnection().prepQuery(noteQuery);
            Record_Set rs = this.myparent.getConnection().executeQuery(noteQuery);

            rs.decompressData();
            for (int i = 0; i < rs.length(); i++) {
                this.addNote(new ClientContactNoteDisplay(rs.getString("note_id"), rs.getString("user_name"),
                        rs.getString("timestamp"), rs.getString("note_text"),
                        rs.getString("note_subject")));
                rs.moveNext();
            }

            this.addNoteButton.setEnabled(true);
            this.noteScrollPane.getVerticalScrollBar().setValue(0);
            this.scrollingNotePanel.revalidate();
            this.noteScrollPane.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        detailTabbedPane = new javax.swing.JTabbedPane();
        detailPanel = new javax.swing.JPanel();
        leftDetailPanel = new javax.swing.JPanel();
        leftLabelPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        cellLabel = new javax.swing.JLabel();
        faxLabel = new javax.swing.JLabel();
        leftDataPanel = new javax.swing.JPanel();
        titleDataLabel = new javax.swing.JLabel();
        nameDataLabel = new javax.swing.JLabel();
        phoneDataLabel = new javax.swing.JLabel();
        cellDataLabel = new javax.swing.JLabel();
        faxDataLabel = new javax.swing.JLabel();
        rightDetailPanel = new javax.swing.JPanel();
        rightLabelPanel = new javax.swing.JPanel();
        addressLabel = new javax.swing.JLabel();
        spacerLabel1 = new javax.swing.JLabel();
        spacerLabel2 = new javax.swing.JLabel();
        spacerLabel3 = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        rightDataPanel = new javax.swing.JPanel();
        addressDataLabel1 = new javax.swing.JLabel();
        addressDataLabel2 = new javax.swing.JLabel();
        cityStateZipLabel = new javax.swing.JLabel();
        spacerLabel4 = new javax.swing.JLabel();
        emailDataLabel = new javax.swing.JLabel();
        notePanel = new javax.swing.JPanel();
        noteButtonPanel = new javax.swing.JPanel();
        spacerPanel2 = new javax.swing.JPanel();
        deleteNoteButton = new javax.swing.JButton();
        editNoteButton = new javax.swing.JButton();
        addNoteButton = new javax.swing.JButton();
        noteScrollPane = new javax.swing.JScrollPane();
        scrollingNotePanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        spacerPanel = new javax.swing.JPanel();
        noteLabel = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        contactTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        controlPanel.setMinimumSize(new java.awt.Dimension(175, 175));
        controlPanel.setPreferredSize(new java.awt.Dimension(100, 175));
        controlPanel.setLayout(new java.awt.BorderLayout());

        detailTabbedPane.setPreferredSize(new java.awt.Dimension(263, 100));

        detailPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(185, 185, 255), new java.awt.Color(100, 125, 175)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        detailPanel.setMaximumSize(new java.awt.Dimension(32767, 100));
        detailPanel.setMinimumSize(new java.awt.Dimension(550, 100));
        detailPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        detailPanel.setRequestFocusEnabled(false);
        detailPanel.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        leftDetailPanel.setLayout(new javax.swing.BoxLayout(leftDetailPanel, javax.swing.BoxLayout.LINE_AXIS));

        leftLabelPanel.setMaximumSize(new java.awt.Dimension(60, 32767));
        leftLabelPanel.setMinimumSize(new java.awt.Dimension(60, 100));
        leftLabelPanel.setPreferredSize(new java.awt.Dimension(60, 100));
        leftLabelPanel.setLayout(new javax.swing.BoxLayout(leftLabelPanel, javax.swing.BoxLayout.Y_AXIS));

        titleLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        titleLabel.setText("Title:");
        titleLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftLabelPanel.add(titleLabel);

        nameLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        nameLabel.setText("Name:");
        nameLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftLabelPanel.add(nameLabel);

        phoneLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        phoneLabel.setText("Phone:");
        phoneLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftLabelPanel.add(phoneLabel);

        cellLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cellLabel.setText("Cell:");
        cellLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftLabelPanel.add(cellLabel);

        faxLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        faxLabel.setText("Fax:");
        faxLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftLabelPanel.add(faxLabel);

        leftDetailPanel.add(leftLabelPanel);

        leftDataPanel.setMaximumSize(new java.awt.Dimension(400, 32767));
        leftDataPanel.setMinimumSize(new java.awt.Dimension(100, 80));
        leftDataPanel.setLayout(new javax.swing.BoxLayout(leftDataPanel, javax.swing.BoxLayout.Y_AXIS));

        titleDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        titleDataLabel.setText("TITLE HERE");
        titleDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftDataPanel.add(titleDataLabel);

        nameDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        nameDataLabel.setText("NAME HERE");
        nameDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftDataPanel.add(nameDataLabel);

        phoneDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        phoneDataLabel.setText("PHONE HERE");
        phoneDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftDataPanel.add(phoneDataLabel);

        cellDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cellDataLabel.setText("CELL HERE");
        cellDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftDataPanel.add(cellDataLabel);

        faxDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        faxDataLabel.setText("FAX HERE");
        faxDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        leftDataPanel.add(faxDataLabel);

        leftDetailPanel.add(leftDataPanel);

        detailPanel.add(leftDetailPanel);

        rightDetailPanel.setMaximumSize(new java.awt.Dimension(460, 100));
        rightDetailPanel.setLayout(new javax.swing.BoxLayout(rightDetailPanel, javax.swing.BoxLayout.LINE_AXIS));

        rightLabelPanel.setMaximumSize(new java.awt.Dimension(60, 32767));
        rightLabelPanel.setMinimumSize(new java.awt.Dimension(60, 100));
        rightLabelPanel.setPreferredSize(new java.awt.Dimension(60, 100));
        rightLabelPanel.setLayout(new javax.swing.BoxLayout(rightLabelPanel, javax.swing.BoxLayout.Y_AXIS));

        addressLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        addressLabel.setText("Address:");
        addressLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        rightLabelPanel.add(addressLabel);

        spacerLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        spacerLabel1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        spacerLabel1.setMinimumSize(new java.awt.Dimension(0, 16));
        spacerLabel1.setPreferredSize(new java.awt.Dimension(0, 16));
        rightLabelPanel.add(spacerLabel1);

        spacerLabel2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        spacerLabel2.setMaximumSize(new java.awt.Dimension(32767, 32767));
        spacerLabel2.setMinimumSize(new java.awt.Dimension(0, 16));
        spacerLabel2.setPreferredSize(new java.awt.Dimension(0, 16));
        rightLabelPanel.add(spacerLabel2);

        spacerLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        spacerLabel3.setMaximumSize(new java.awt.Dimension(32767, 32767));
        spacerLabel3.setMinimumSize(new java.awt.Dimension(0, 16));
        spacerLabel3.setPreferredSize(new java.awt.Dimension(0, 16));
        rightLabelPanel.add(spacerLabel3);

        emailLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        emailLabel.setText("E-Mail:");
        emailLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        rightLabelPanel.add(emailLabel);

        rightDetailPanel.add(rightLabelPanel);

        rightDataPanel.setMaximumSize(new java.awt.Dimension(400, 32767));
        rightDataPanel.setMinimumSize(new java.awt.Dimension(400, 100));
        rightDataPanel.setPreferredSize(new java.awt.Dimension(400, 100));
        rightDataPanel.setLayout(new javax.swing.BoxLayout(rightDataPanel, javax.swing.BoxLayout.Y_AXIS));

        addressDataLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addressDataLabel1.setText("ADDRESS1 HERE");
        addressDataLabel1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        rightDataPanel.add(addressDataLabel1);

        addressDataLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addressDataLabel2.setText("ADDRESS2 HERE");
        addressDataLabel2.setMaximumSize(new java.awt.Dimension(32767, 32767));
        rightDataPanel.add(addressDataLabel2);

        cityStateZipLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cityStateZipLabel.setText("CITY/STATE/ZIP HERE");
        cityStateZipLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        rightDataPanel.add(cityStateZipLabel);

        spacerLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        spacerLabel4.setMaximumSize(new java.awt.Dimension(32767, 32767));
        spacerLabel4.setMinimumSize(new java.awt.Dimension(0, 16));
        spacerLabel4.setPreferredSize(new java.awt.Dimension(0, 16));
        rightDataPanel.add(spacerLabel4);

        emailDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        emailDataLabel.setForeground(new java.awt.Color(0, 51, 255));
        emailDataLabel.setText("EMAIL HERE");
        emailDataLabel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        emailDataLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailDataLabelMouseClicked(evt);
            }
        });
        rightDataPanel.add(emailDataLabel);

        rightDetailPanel.add(rightDataPanel);

        detailPanel.add(rightDetailPanel);

        detailTabbedPane.addTab("Contact Information", detailPanel);

        notePanel.setMaximumSize(new java.awt.Dimension(32767, 100));
        notePanel.setMinimumSize(new java.awt.Dimension(100, 100));
        notePanel.setPreferredSize(new java.awt.Dimension(258, 100));
        notePanel.setLayout(new java.awt.BorderLayout());

        noteButtonPanel.setLayout(new javax.swing.BoxLayout(noteButtonPanel, javax.swing.BoxLayout.Y_AXIS));
        noteButtonPanel.add(spacerPanel2);

        deleteNoteButton.setText("Delete Note");
        deleteNoteButton.setEnabled(false);
        deleteNoteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNoteButtonActionPerformed(evt);
            }
        });
        noteButtonPanel.add(deleteNoteButton);

        editNoteButton.setText("Edit Note");
        editNoteButton.setEnabled(false);
        editNoteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNoteButtonActionPerformed(evt);
            }
        });
        noteButtonPanel.add(editNoteButton);

        addNoteButton.setText("New Note");
        addNoteButton.setEnabled(false);
        addNoteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNoteButtonActionPerformed(evt);
            }
        });
        noteButtonPanel.add(addNoteButton);

        notePanel.add(noteButtonPanel, java.awt.BorderLayout.EAST);

        noteScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scrollingNotePanel.setBackground(new java.awt.Color(255, 255, 255));
        scrollingNotePanel.setLayout(new javax.swing.BoxLayout(scrollingNotePanel, javax.swing.BoxLayout.Y_AXIS));
        noteScrollPane.setViewportView(scrollingNotePanel);

        notePanel.add(noteScrollPane, java.awt.BorderLayout.CENTER);

        detailTabbedPane.addTab("Notes", notePanel);

        controlPanel.add(detailTabbedPane, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel, javax.swing.BoxLayout.LINE_AXIS));

        spacerPanel.setMaximumSize(new java.awt.Dimension(32767, 23));
        spacerPanel.setMinimumSize(new java.awt.Dimension(10, 23));
        spacerPanel.setPreferredSize(new java.awt.Dimension(100, 23));
        spacerPanel.setLayout(new javax.swing.BoxLayout(spacerPanel, javax.swing.BoxLayout.LINE_AXIS));

        noteLabel.setText("     * indicates a primary contact");
        spacerPanel.add(noteLabel);

        buttonPanel.add(spacerPanel);

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(deleteButton);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(editButton);

        addButton.setText("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addButton);

        controlPanel.add(buttonPanel, java.awt.BorderLayout.NORTH);

        add(controlPanel, java.awt.BorderLayout.SOUTH);

        contactTable.setModel(contactModel);
        contactTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(contactTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addNoteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNoteButtonActionPerformed
        EditContactNoteDialog ed = new EditContactNoteDialog(Main_Window.parentOfApplication, true);
        ed.setVisible(true);

        if (ed.getSavedNote() != null) {
            this.saveNote("0", ed.getSavedNote(), ed.getSavedSubject());
        }

        ed.dispose();
    }//GEN-LAST:event_addNoteButtonActionPerformed

    private void editNoteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNoteButtonActionPerformed
        EditContactNoteDialog ed = new EditContactNoteDialog(Main_Window.parentOfApplication, true,
                this.selectedNote.getNote(), this.selectedNote.getSubject());
        ed.setVisible(true);

        if (ed.getSavedNote() != null) {
            this.saveNote(this.selectedNote.getId(), ed.getSavedNote(), ed.getSavedSubject());
        }

        ed.dispose();
    }//GEN-LAST:event_editNoteButtonActionPerformed

    private void deleteNoteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNoteButtonActionPerformed
        delete_client_contact_note_query noteQuery = new delete_client_contact_note_query(this.selectedNote.getId());
        this.myparent.getConnection().prepQuery(noteQuery);
        this.myparent.getConnection().executeUpdate(noteQuery);
        this.clearNotes();
        this.loadNotesForSelectedContact();
    }//GEN-LAST:event_deleteNoteButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this.myparent, "Are you sure you want to delete this contact?", "Delete Contact?",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION || this.contactTable.getSelectedRow() == -1) {
            return;
        }

        ClientContact contact = contactVector.get(contactTable.getSelectedRow());
        delete_client_contact_query deleteQuery = new delete_client_contact_query(contact.getClientContactId().toString());

        this.myparent.getConnection().prepQuery(deleteQuery);
        this.myparent.getConnection().executeUpdate(deleteQuery);
        this.reloadData();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (this.contactTable.getSelectedRow() == -1) {
            return;
        }

        EditContactDialog editDialog = new EditContactDialog(Main_Window.parentOfApplication, true, contactVector.get(contactTable.getSelectedRow()), this.myparent);

        editDialog.setVisible(true);
        ClientContact c = editDialog.getSavedContact();
        if (c != null) {
            this.saveContact(c);
            this.reloadData();
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        this.contactTable.clearSelection();
        EditContactDialog editDialog = new EditContactDialog(Main_Window.parentOfApplication, true, this.myparent);

        editDialog.setVisible(true);
        ClientContact c = editDialog.getSavedContact();
        if (c != null) {
            this.saveContact(c);
            this.reloadData();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void contactTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactTableMouseClicked
        this.showData();
        JTable target = (JTable)evt.getSource();
        int row = target.getSelectedRow();
        int column = target.getSelectedColumn();
        if (column == 4) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("mailto:" + emailDataLabel.getText()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_contactTableMouseClicked

    private void emailDataLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailDataLabelMouseClicked
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("mailto:" + emailDataLabel.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_emailDataLabelMouseClicked

    private void showData() {

        if (this.contactTable.getSelectedRow() == -1) {
            this.clearDataLabels();
            this.editButton.setEnabled(false);
            this.deleteButton.setEnabled(false);
            return;
        }
        this.clearNotes();

        this.editButton.setEnabled(true);
        this.deleteButton.setEnabled(true);
        ClientContact contact = contactVector.get(contactTable.getSelectedRow());

        this.titleDataLabel.setText(contact.getClientContactTitle());
        this.nameDataLabel.setText(contact.getFullName());
        this.phoneDataLabel.setText(contact.getClientContactPhone());
        this.cellDataLabel.setText(contact.getClientContactCell());
        this.faxDataLabel.setText(contact.getClientContactFax());
        this.addressDataLabel1.setText(contact.getClientContactAddress());

        if (contact.getClientContactAddress2().trim().length() == 0) {
            this.cityStateZipLabel.setText(contact.getClientContactAddress2());
            this.addressDataLabel2.setText((contact.getClientContactCity().trim().length() == 0 ? "" : contact.getClientContactCity() + ", ") + contact.getClientContactState() + " " + contact.getClientContactZip());
        } else {
            this.addressDataLabel2.setText(contact.getClientContactAddress2());
            this.cityStateZipLabel.setText((contact.getClientContactCity().trim().length() == 0 ? "" : contact.getClientContactCity() + ", ") + contact.getClientContactState() + " " + contact.getClientContactZip());
        }
        this.emailDataLabel.setText(contact.getClientContactEmail());

        this.loadNotesForSelectedContact();
    }

    
    private class ContactTableModel extends AbstractTableModel {

        private Connection myConn;

        public ContactTableModel() {
        }

        public int getRowCount() {
            return contactVector.size();
        }

        public int getColumnCount() {
            return 5;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 4) {
                return true;
            }
            return false;
        }
        
        private void initConnection() {
            if (myConn == null) {
                this.myConn = myparent.getConnection();
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Name";
            } else if (columnIndex == 1) {
                return "Type";
            } else if (columnIndex == 2) {
                return "Phone";
            } else if (columnIndex == 3) {
                return "Cell";
            } else {
                return "Email";
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            try {
                ClientContact currContact = contactVector.get(rowIndex);
                if (columnIndex == 0) {
                    StringBuilder nameValue = new StringBuilder();
                    if (currContact.getClientContactIsPrimary() == 1) {
                        nameValue.append("* ");
                    }
                    nameValue.append(currContact.getClientContactFirstName()).append(" ")
                            .append(currContact.getClientContactLastName());
                    return nameValue.toString();
                } else if (columnIndex == 1) {
                    this.initConnection();
                    return currContact.getClientContactTypeObj(myConn.myCompany).getContactType();
                } else if (columnIndex == 2) {
                    return currContact.getClientContactPhone();
                } else if (columnIndex == 3) {
                    return currContact.getClientContactCell();
                } else if (columnIndex == 4) {
                    return currContact.getClientContactEmail();
                } else {
                    return "";
                }
            } catch (Exception e) {
                return "";
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton addNoteButton;
    private javax.swing.JLabel addressDataLabel1;
    private javax.swing.JLabel addressDataLabel2;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel cellDataLabel;
    private javax.swing.JLabel cellLabel;
    private javax.swing.JLabel cityStateZipLabel;
    private javax.swing.JTable contactTable;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteNoteButton;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JTabbedPane detailTabbedPane;
    private javax.swing.JButton editButton;
    private javax.swing.JButton editNoteButton;
    private javax.swing.JLabel emailDataLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel faxDataLabel;
    private javax.swing.JLabel faxLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftDataPanel;
    private javax.swing.JPanel leftDetailPanel;
    private javax.swing.JPanel leftLabelPanel;
    private javax.swing.JLabel nameDataLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel noteButtonPanel;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JPanel notePanel;
    private javax.swing.JScrollPane noteScrollPane;
    private javax.swing.JLabel phoneDataLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JPanel rightDataPanel;
    private javax.swing.JPanel rightDetailPanel;
    private javax.swing.JPanel rightLabelPanel;
    private javax.swing.JPanel scrollingNotePanel;
    private javax.swing.JLabel spacerLabel1;
    private javax.swing.JLabel spacerLabel2;
    private javax.swing.JLabel spacerLabel3;
    private javax.swing.JLabel spacerLabel4;
    private javax.swing.JPanel spacerPanel;
    private javax.swing.JPanel spacerPanel2;
    private javax.swing.JLabel titleDataLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private class MouseHandler implements MouseListener {

        public void mouseEntered(MouseEvent evt) {
            if (evt.getSource() instanceof ClientContactNoteDisplay) {
                ClientContactNoteDisplay note = (ClientContactNoteDisplay) evt.getSource();

                if (note != selectedNote) {
                    note.setBorder(BorderFactory.createLineBorder(note.mouseOverColor, 1));
                }
            }
        }

        public void mouseExited(MouseEvent evt) {
            if (evt.getSource() instanceof ClientContactNoteDisplay) {
                ClientContactNoteDisplay note = (ClientContactNoteDisplay) evt.getSource();

                if (note != selectedNote) {
                    note.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                }
            }
        }

        public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() instanceof ClientContactNoteDisplay) {
                ClientContactNoteDisplay note = (ClientContactNoteDisplay) evt.getSource();

                if (note != selectedNote) {
                    setSelectedNote(note);
                } else {
                    setSelectedNote(null);
                }
            }
        }

        public void mousePressed(MouseEvent evt) {
        }

        public void mouseReleased(MouseEvent evt) {
        }
    }
}
