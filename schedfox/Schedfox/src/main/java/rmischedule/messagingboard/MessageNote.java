/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessageNote.java
 *
 * Created on Jun 24, 2010, 10:24:49 AM
 */
package rmischedule.messagingboard;

import com.creamtec.ajaxswing.AjaxSwingConstants;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import com.creamtec.ajaxswing.core.ClientAgent;
import rmischedule.main.*;
import rmischedule.client.components.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.SwingUtilities;
import com.creamtec.ajaxswing.AjaxSwingConstants;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import java.awt.Color;
import java.awt.Dimension;
import java.util.StringTokenizer;
import rmischedule.main.Main_Window;

/**
 *
 * @author vnguyen
 */
public class MessageNote extends javax.swing.JPanel {

    private String myNoteId;
    private String myUserId;
    private boolean read;
    private boolean selected = false;
    private String companyId;
    private String subject;
    private String sentDate;
    private String msg;
    private String name;
    private final GenericMessageBoard myParent;
    private static Color myReadColor = Color.lightGray;
    private static Color myUnReadColor = Color.green;
    private static Color onMouseOver = new Color(168, 188, 231);
    private Color mySelectedColor = Color.YELLOW;

    public MessageNote(String noteId, String fromUser, String userId, String date, String note, String noteType,
            GenericMessageBoard parent, boolean read, String companyId, String toUser) {
        this.myNoteId = noteId;
        this.myParent = parent;
        this.read = read;
        this.sentDate = date;
        this.msg = note;
        this.subject = noteType;
        this.companyId = companyId;
        this.name = fromUser;
        initNotes(noteId, fromUser, userId, date, note, noteType, toUser);


    }

    MessageNote(String msgId, String fromUser, String userId, String date, String msg, String subject, GenericMessageBoard parent, String companyId, String toUser) {
        this.myNoteId = msgId;
        this.myParent = parent;
        this.read = true;
        this.sentDate = date;
        this.msg = msg;
        this.subject = subject;
        this.companyId = companyId;
        initNotes(msgId, fromUser, userId, date, msg, this.subject, toUser);
    }

    private void initNotes(String msgId, String userName, String userId, String date, String note, String noteType, String toUser) {

        try {
            initComponents();
            this.toLabel.setText(toUser + "\t");

            IconLabel.setIcon(Main_Window.Arrow_Up_Icon);
            myUserId = userId;
            toggleSize();
            EditSaveNotes.setVisible(false);
            Reply.setIcon(Main_Window.Edit_Notes_Icon);
            SaveNote.setIcon(Main_Window.Close_Notes_Icon);
            Reply.setBackground(new Color(199, 216, 255, 90));

            HeaderPanel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
            IconLabel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
            HeaderPanel.addMouseListener(new testMouseListener());
            IconLabel.addMouseListener(new testMouseListener());

            Reply.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    // EditSaveNotes.setVisible(true);
                    Reply.setBackground(new Color(199, 216, 255));
                }

                public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    //EditSaveNotes.setVisible(false);
                    Reply.setBackground(new Color(199, 216, 255, 90));
                }

                public void mouseClicked(MouseEvent e) {
                    //calls a popwindow
                    replyNoteAction();
                }
            });
            SaveNote.setBackground(new Color(199, 216, 255, 90));
            SaveNote.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    EditSaveNotes.setVisible(true);
                    SaveNote.setBackground(new Color(199, 216, 255));
                }

                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    EditSaveNotes.setVisible(false);
                    SaveNote.setBackground(new Color(199, 216, 255, 90));
                }

                public void mouseClicked(MouseEvent e) {
                    myParent.deleteNote(getMyNoteId());
                }
            });
            this.FromLabel.setText(userName);
            try {
                DateLabel.setText(date);
            } catch (Exception e) {
            }
            this.Subjectlabel.setText(noteType);
            NoteTextBox.setText(note.replaceAll("\r", "\n"));
            myNoteId = msgId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.HeaderPanel.setBackground(this.getDefaultHeaderColor());
        this.revalidate();
        this.repaint();
    }

    private void replyNoteAction() {
      this.myParent.replyNote(this);
    }

    public void toggleSize() {
        if (IconLabel.getIcon() != Main_Window.Arrow_Up_Icon) {
            IconLabel.setIcon(Main_Window.Arrow_Up_Icon);
            int newHeight = this.NoteTextBox.getText().length() / 6;
            /*if (newHeight > 110) {
            newHeight = 110;
            }*/
            if (newHeight < 66) {
                newHeight = 66;
            }
            MainNotePanel.setPreferredSize(new Dimension(100, newHeight));
            MainNotePanel.setMaximumSize(new Dimension(10000, newHeight));
            MainNotePanel.setMinimumSize(new Dimension(0, 66));
            myLayeredPane.setPreferredSize(new Dimension(100, newHeight + 20));
            myLayeredPane.setMaximumSize(new Dimension(10000, newHeight + 20));
            myLayeredPane.setMinimumSize(new Dimension(0, 86));
            setPreferredSize(new Dimension(100, newHeight + 20));
            setMaximumSize(new Dimension(10000, newHeight + 20));
            setMinimumSize(new Dimension(100, 86));
            this.EditSaveNotes.setVisible(true);
        } else {
            IconLabel.setIcon(Main_Window.Arrow_Down_Icon);
            MainNotePanel.setPreferredSize(new Dimension(0, 0));
            MainNotePanel.setMaximumSize(new Dimension(0, 0));
            MainNotePanel.setMinimumSize(new Dimension(0, 0));
            myLayeredPane.setPreferredSize(new Dimension(100, 20));
            myLayeredPane.setMaximumSize(new Dimension(10000, 20));
            myLayeredPane.setMinimumSize(new Dimension(100, 20));
            setMinimumSize(new Dimension(100, 20));
            setMaximumSize(new Dimension(10000, 20));
            setPreferredSize(new Dimension(100, 20));
            this.EditSaveNotes.setVisible(false);
        }

        revalidate();
        repaint();
//        if (AjaxSwingManager.isAjaxSwingRunning()) {
//            SwingUtilities.invokeLater(new Runnable() {
//
//                    public void run() {
//                        repaint();
//                        ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(myParent, true);
//                    }
//                });
//
//        }
    }

    /**
     * @return the myNoteId
     */
    public String getMyNoteId() {
        return myNoteId;
    }

    public boolean isSelected() {
        return this.selectedCheckBox.isSelected();
    }

    public void setSelected(boolean checked) {
        this.selected = checked;
        this.selectedCheckBox.setSelected(checked);
        //color change
        if (!this.selected) {
            HeaderPanel.setBackground(this.getDefaultHeaderColor());
        } else {
            HeaderPanel.setBackground(this.mySelectedColor);
        }
    }

    /**
     * @return the defaultHeaderColor
     */
    public Color getDefaultHeaderColor() {
        if (this.selected) {
            return mySelectedColor;
        } else if (this.isRead()) {
            return myReadColor;
        } else {
            return myUnReadColor;
        }
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        if (!this.read && read) {
            this.myParent.setReadOnDB(this);
        }
        this.read = read;

    }

    public void setBackground() {
        this.HeaderPanel.setBackground(this.getDefaultHeaderColor());
    }

    /**
     * @return the sentDate
     */
    public String getSentDate() {
        return sentDate;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    private class testMouseListener implements MouseListener {

        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() > 1) {
                setSelected(true);
                HeaderPanel.setBackground(getDefaultHeaderColor());
                toggleSize();
            }
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
            HeaderPanel.setBackground(MessageNote.onMouseOver);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            HeaderPanel.setBackground(getDefaultHeaderColor());
        }

        public void mousePressed(MouseEvent e) {
            setSelected(!selected);
        }

        public void mouseReleased(MouseEvent e) {
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

        myLayeredPane = new javax.swing.JLayeredPane();
        MainPanel = new javax.swing.JPanel();
        HeaderPanel = new javax.swing.JPanel();
        selectedCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        toLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        FromLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DateLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Subjectlabel = new javax.swing.JLabel();
        IconLabel = new javax.swing.JLabel();
        MainNotePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        NoteTextBox = new javax.swing.JTextArea();
        ControlPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        EditSaveNotes = new javax.swing.JPanel();
        Reply = new javax.swing.JLabel();
        SaveNote = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(700, 300));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeLayeredPane(evt);
            }
        });
        setLayout(new java.awt.GridLayout(1, 0));

        MainPanel.setLayout(new javax.swing.BoxLayout(MainPanel, javax.swing.BoxLayout.Y_AXIS));

        HeaderPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        HeaderPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        HeaderPanel.setMinimumSize(new java.awt.Dimension(10, 20));
        HeaderPanel.setPreferredSize(new java.awt.Dimension(10, 20));
        HeaderPanel.setLayout(new javax.swing.BoxLayout(HeaderPanel, javax.swing.BoxLayout.LINE_AXIS));

        selectedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedCheckBoxActionPerformed(evt);
            }
        });
        HeaderPanel.add(selectedCheckBox);

        jLabel2.setText("To:   ");
        HeaderPanel.add(jLabel2);

        toLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        toLabel.setMaximumSize(new java.awt.Dimension(110, 14));
        toLabel.setMinimumSize(new java.awt.Dimension(110, 14));
        HeaderPanel.add(toLabel);

        jLabel1.setText("From");
        jLabel1.setMaximumSize(new java.awt.Dimension(45, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(45, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(45, 14));
        HeaderPanel.add(jLabel1);

        FromLabel.setForeground(new java.awt.Color(147, 64, 64));
        FromLabel.setMaximumSize(new java.awt.Dimension(110, 14));
        FromLabel.setMinimumSize(new java.awt.Dimension(110, 14));
        FromLabel.setPreferredSize(new java.awt.Dimension(110, 14));
        HeaderPanel.add(FromLabel);

        jLabel3.setText("Date");
        jLabel3.setMaximumSize(new java.awt.Dimension(40, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(40, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(40, 14));
        HeaderPanel.add(jLabel3);

        DateLabel.setForeground(new java.awt.Color(147, 64, 64));
        DateLabel.setMaximumSize(new java.awt.Dimension(140, 14));
        DateLabel.setMinimumSize(new java.awt.Dimension(140, 14));
        DateLabel.setPreferredSize(new java.awt.Dimension(140, 14));
        HeaderPanel.add(DateLabel);

        jLabel5.setText("Subject");
        jLabel5.setMaximumSize(new java.awt.Dimension(60, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(60, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 14));
        HeaderPanel.add(jLabel5);

        Subjectlabel.setForeground(new java.awt.Color(147, 64, 64));
        Subjectlabel.setMaximumSize(new java.awt.Dimension(1500, 14));
        Subjectlabel.setMinimumSize(new java.awt.Dimension(150, 14));
        Subjectlabel.setPreferredSize(new java.awt.Dimension(150, 14));
        HeaderPanel.add(Subjectlabel);

        IconLabel.setMaximumSize(new java.awt.Dimension(18, 18));
        IconLabel.setMinimumSize(new java.awt.Dimension(18, 18));
        IconLabel.setPreferredSize(new java.awt.Dimension(18, 18));
        HeaderPanel.add(IconLabel);

        MainPanel.add(HeaderPanel);

        MainNotePanel.setMaximumSize(new java.awt.Dimension(32767, 200));
        MainNotePanel.setPreferredSize(new java.awt.Dimension(4, 0));
        MainNotePanel.setLayout(new javax.swing.BoxLayout(MainNotePanel, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(24, 0));

        NoteTextBox.setEditable(false);
        NoteTextBox.setFont(new java.awt.Font("Courier", 1, 14));
        NoteTextBox.setLineWrap(true);
        NoteTextBox.setRows(3);
        NoteTextBox.setDisabledTextColor(new java.awt.Color(111, 126, 158));
        NoteTextBox.setEnabled(false);
        NoteTextBox.setMinimumSize(new java.awt.Dimension(0, 0));
        NoteTextBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NoteBoxHideControl(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                NoteTextBoxShowControl(evt);
            }
        });
        jScrollPane1.setViewportView(NoteTextBox);

        MainNotePanel.add(jScrollPane1);

        MainPanel.add(MainNotePanel);

        MainPanel.setBounds(0, 0, 570, 70);
        myLayeredPane.add(MainPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        ControlPanel.setOpaque(false);
        ControlPanel.setLayout(new javax.swing.BoxLayout(ControlPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setOpaque(false);
        ControlPanel.add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 20));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setOpaque(false);
        jPanel3.add(jPanel4);

        EditSaveNotes.setOpaque(false);
        EditSaveNotes.setLayout(new javax.swing.BoxLayout(EditSaveNotes, javax.swing.BoxLayout.LINE_AXIS));

        Reply.setBackground(new java.awt.Color(199, 216, 255));
        Reply.setText("REPLY");
        Reply.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Reply.setMaximumSize(new java.awt.Dimension(85, 20));
        Reply.setMinimumSize(new java.awt.Dimension(85, 20));
        Reply.setOpaque(true);
        Reply.setPreferredSize(new java.awt.Dimension(85, 20));
        Reply.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        EditSaveNotes.add(Reply);

        SaveNote.setBackground(new java.awt.Color(199, 216, 255));
        SaveNote.setText("DELETE");
        SaveNote.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        SaveNote.setMaximumSize(new java.awt.Dimension(85, 20));
        SaveNote.setMinimumSize(new java.awt.Dimension(85, 20));
        SaveNote.setOpaque(true);
        SaveNote.setPreferredSize(new java.awt.Dimension(85, 20));
        SaveNote.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        EditSaveNotes.add(SaveNote);

        jPanel3.add(EditSaveNotes);

        jPanel5.setMaximumSize(new java.awt.Dimension(25, 32767));
        jPanel5.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanel3.add(jPanel5);

        ControlPanel.add(jPanel3);

        ControlPanel.setBounds(0, 0, 570, 70);
        myLayeredPane.add(ControlPanel, javax.swing.JLayeredPane.MODAL_LAYER);

        add(myLayeredPane);
    }// </editor-fold>//GEN-END:initComponents

    private void NoteBoxHideControl(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NoteBoxHideControl
        //EditSaveNotes.setVisible(false);
	}//GEN-LAST:event_NoteBoxHideControl

    private void NoteTextBoxShowControl(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NoteTextBoxShowControl
        //EditSaveNotes.setVisible(true);
	}//GEN-LAST:event_NoteTextBoxShowControl

    private void resizeLayeredPane(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_resizeLayeredPane
        MainPanel.setBounds(myLayeredPane.getBounds());
        ControlPanel.setBounds(myLayeredPane.getBounds());
        MainPanel.revalidate();
        ControlPanel.revalidate();
        MainNotePanel.revalidate();
        this.revalidate();        // TODO add your handling code here:
    }//GEN-LAST:event_resizeLayeredPane

    private void selectedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedCheckBoxActionPerformed
        // TODO add your handling code here:
        this.setSelected(this.selectedCheckBox.isSelected());
    }//GEN-LAST:event_selectedCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JPanel EditSaveNotes;
    private javax.swing.JLabel FromLabel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JLabel IconLabel;
    private javax.swing.JPanel MainNotePanel;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JTextArea NoteTextBox;
    private javax.swing.JLabel Reply;
    private javax.swing.JLabel SaveNote;
    private javax.swing.JLabel Subjectlabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane myLayeredPane;
    private javax.swing.JCheckBox selectedCheckBox;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables
}
