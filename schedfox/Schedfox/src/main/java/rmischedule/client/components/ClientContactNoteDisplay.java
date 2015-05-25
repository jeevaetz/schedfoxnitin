/*
 * ClientContactNoteDisplay.java
 *
 * Created on August 9, 2006, 3:23 PM
 */

package rmischedule.client.components;

import rmischeduleserver.util.StaticDateTimeFunctions;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import rmischedule.components.*;

/**
 *
 * @author  shawn
 */
public class ClientContactNoteDisplay extends javax.swing.JPanel {
    
    private String noteId;
    public static final Color mouseOverColor = new Color(175, 175, 255);
    public static final Color selectedColor = new Color(0, 235, 30);
    
    /** Creates new form ClientContactNoteDisplay */
    public ClientContactNoteDisplay(String noteId, String userName, String timestamp, String text, String subject) {
        initComponents();
        this.noteTextArea.setText(text.replaceAll("\r", "\n"));
        this.noteId = noteId;
        this.userDataLabel.setText(userName);
        this.subjectDataLabel.setText(subject);
        try {
            StringTokenizer st = new StringTokenizer(timestamp, " ");
            this.dateDataLabel.setText(StaticDateTimeFunctions.convertDatabaseDateToReadable(st.nextToken()));
        } catch (Exception e) {}
        this.noteScrollPane.getVerticalScrollBar().setUnitIncrement(3);
        this.noteScrollPane.getVerticalScrollBar().setBlockIncrement(12);
    }
    
    public String getId()      { return this.noteId; }
    public String getNote()    { return this.noteTextArea.getText(); }
    public String getSubject() { return this.subjectDataLabel.getText(); }
    
    public void expand() {
        int newHeight = this.noteTextArea.getPreferredScrollableViewportSize().height + 4;
        newHeight = newHeight > 96 ? 96 : newHeight;
        this.noteScrollPane.setMaximumSize(new Dimension(32767, newHeight));
        this.setMinimumSize(new Dimension(0, newHeight + 21));
        this.setPreferredSize(new Dimension(0, newHeight + 21));
        this.setBorder(BorderFactory.createLineBorder(selectedColor, 1));
        this.revalidate();
        this.noteScrollPane.getVerticalScrollBar().setValue(0);
    }
    
    public void contract() {
        this.noteScrollPane.setMaximumSize(new Dimension(32767, 0));
        this.setMinimumSize(new Dimension(0, 21));
        this.setPreferredSize(new Dimension(0, 21));
        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.revalidate();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        headerPanel = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        userDataLabel = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        subjectDataLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        dateDataLabel = new javax.swing.JLabel();
        noteScrollPane = new javax.swing.JScrollPane();
        noteTextArea = new javax.swing.JTextArea();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setMinimumSize(new java.awt.Dimension(100, 21));
        setPreferredSize(new java.awt.Dimension(100, 21));
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.X_AXIS));

        headerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headerPanel.setMaximumSize(new java.awt.Dimension(32924, 19));
        headerPanel.setMinimumSize(new java.awt.Dimension(10, 19));
        headerPanel.setPreferredSize(new java.awt.Dimension(100, 19));
        userLabel.setText("User:  ");
        headerPanel.add(userLabel);

        userDataLabel.setForeground(new java.awt.Color(100, 100, 200));
        userDataLabel.setText("User Name Here");
        userDataLabel.setMaximumSize(new java.awt.Dimension(125, 14));
        userDataLabel.setMinimumSize(new java.awt.Dimension(125, 14));
        userDataLabel.setPreferredSize(new java.awt.Dimension(125, 14));
        headerPanel.add(userDataLabel);

        subjectLabel.setText("Subject:  ");
        headerPanel.add(subjectLabel);

        subjectDataLabel.setForeground(new java.awt.Color(100, 100, 200));
        subjectDataLabel.setText("blah");
        subjectDataLabel.setMaximumSize(new java.awt.Dimension(32767, 14));
        headerPanel.add(subjectDataLabel);

        dateLabel.setText("Date:  ");
        headerPanel.add(dateLabel);

        dateDataLabel.setForeground(new java.awt.Color(100, 100, 200));
        dateDataLabel.setText("00/00/0000");
        dateDataLabel.setMaximumSize(new java.awt.Dimension(70, 14));
        dateDataLabel.setMinimumSize(new java.awt.Dimension(70, 14));
        dateDataLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        headerPanel.add(dateDataLabel);

        add(headerPanel);

        noteScrollPane.setMaximumSize(new java.awt.Dimension(32767, 0));
        noteScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));
        noteScrollPane.setPreferredSize(new java.awt.Dimension(0, 100));
        noteTextArea.setEditable(false);
        noteTextArea.setLineWrap(true);
        noteTextArea.setWrapStyleWord(true);
        noteScrollPane.setViewportView(noteTextArea);

        add(noteScrollPane);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateDataLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JScrollPane noteScrollPane;
    private javax.swing.JTextArea noteTextArea;
    private javax.swing.JLabel subjectDataLabel;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JLabel userDataLabel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
    
}
