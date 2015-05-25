/*
 * GraphicalNoteClass.java
 *
 * Created on October 3, 2005, 2:45 PM
 */
package rmischedule.components.graphicalcomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import rmischedule.main.Main_Window;

import com.creamtec.ajaxswing.AjaxSwingConstants;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.text.SimpleDateFormat;
import rmischedule.client.components.ClientNoteInterface;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.ClientNotes;
import schedfoxlib.model.NoteInterface;
import schedfoxlib.model.User;

/**
 *
 * @author Ira Juneau
 */
public class GraphicalNoteClass extends javax.swing.JPanel {

    private Integer myNoteId;
    private Integer myUserId;
    private Color defaultHeaderColor;
    private final ClientNoteInterface myParent;

    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    private NoteInterface note;

    public GraphicalNoteClass(NoteInterface clientNote, ClientNoteInterface parent, String companyId) {
        initComponents();

        IconLabel.setIcon(Main_Window.Arrow_Up_Icon);
        myUserId = clientNote.getUserId();
        myParent = parent;
        this.note = clientNote;
        toggleSize();
        EditSaveNotes.setVisible(false);
        EditNote.setIcon(Main_Window.Edit_Notes_Icon);
        SaveNote.setIcon(Main_Window.Close_Notes_Icon);
        EditNote.setBackground(new Color(199, 216, 255, 90));
        HeaderPanel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
        IconLabel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
        HeaderPanel.addMouseListener(new testMouseListener());
        IconLabel.addMouseListener(new testMouseListener());

        final NoteInterface localClientNote = clientNote;
        EditNote.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                EditSaveNotes.setVisible(true);
                EditNote.setBackground(new Color(199, 216, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                EditSaveNotes.setVisible(false);
                EditNote.setBackground(new Color(199, 216, 255, 90));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!NoteTextBox.isEnabled() || (AjaxSwingManager.isAjaxSwingRunning() && !NoteTextBox.isEditable())) {
                    if (AjaxSwingManager.isAjaxSwingRunning()) {
                        NoteTextBox.setEditable(true);
                        NoteTextBox.setForeground(Color.BLACK);
                    } else {
                        NoteTextBox.setEnabled(true);
                    }
                    EditNote.setText("SAVE NOTE");
                    EditNote.setIcon(Main_Window.Save_Notes_Icon);
                } else {
                    localClientNote.setNote(NoteTextBox.getText().trim());
                    myParent.saveNote(localClientNote);
                    if (AjaxSwingManager.isAjaxSwingRunning()) {
                        NoteTextBox.setEditable(false);
                        NoteTextBox.setForeground(new Color(185, 185, 185));
                    } else {
                        NoteTextBox.setEnabled(false);
                    }
                    EditNote.setText("EDIT NOTE");
                    EditNote.setIcon(Main_Window.Edit_Notes_Icon);
                }
            }
        });
        SaveNote.setBackground(new Color(199, 216, 255, 90));
        SaveNote.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                EditSaveNotes.setVisible(true);
                SaveNote.setBackground(new Color(199, 216, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                EditSaveNotes.setVisible(false);
                SaveNote.setBackground(new Color(199, 216, 255, 90));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                myParent.deleteNote(localClientNote);
            }
        });
        try {
            UserController userController = new UserController(companyId);
            User currUser = userController.getUserByIdFromHash(localClientNote.getUserId());
            UserLabel.setText(currUser.getUserFullName());
        } catch (Exception exe) {}
        try {
            DateLabel.setText(myFormat.format(clientNote.getDateEntered()));
        } catch (Exception e) {
        }
        try {
            GenericController genericController = GenericController.getInstance(companyId);
            NoteLabel.setText(genericController.getNoteTypeByIdFromCache(clientNote.getNoteTypeId()).getNoteTypeName());
        } catch (Exception exe) {}
        NoteTextBox.setText(clientNote.getNote().replaceAll("\r", "\n"));
        myNoteId = clientNote.getPrimaryKey();
        defaultHeaderColor = HeaderPanel.getBackground();
    }

    /**
     * Returns contents of UserLabel
     */
    public Integer getUser() {
        return myUserId;
    }

    /**
     * Returns note contents
     */
    public String getNote() {
        String myString = NoteTextBox.getText();
        myString = myString.replaceAll("'", "''");
        myString = myString.replaceAll("\n", "\\\\r");
        return myString;
    }

    /**
     * Returns date label contents
     */
    public String getDate() {
        return DateLabel.getText();
    }

    /**
     * Returns the Note Type
     */
    public String getNoteType() {
        return NoteLabel.getText();
    }

    /**
     * Returns note id...
     */
    public Integer getNoteId() {
        return myNoteId;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myLayeredPane = new javax.swing.JLayeredPane();
        MainPanel = new javax.swing.JPanel();
        HeaderPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        UserLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DateLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        NoteLabel = new javax.swing.JLabel();
        IconLabel = new javax.swing.JLabel();
        MainNotePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        NoteTextBox = new javax.swing.JTextArea();
        ControlPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        EditSaveNotes = new javax.swing.JPanel();
        EditNote = new javax.swing.JLabel();
        SaveNote = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(24, 200));
        setPreferredSize(new java.awt.Dimension(0, 0));
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

        jLabel1.setText("User");
        jLabel1.setMaximumSize(new java.awt.Dimension(45, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(45, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(45, 14));
        HeaderPanel.add(jLabel1);

        UserLabel.setForeground(new java.awt.Color(147, 64, 64));
        UserLabel.setMaximumSize(new java.awt.Dimension(110, 14));
        UserLabel.setMinimumSize(new java.awt.Dimension(110, 14));
        UserLabel.setPreferredSize(new java.awt.Dimension(110, 14));
        HeaderPanel.add(UserLabel);

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

        jLabel5.setText("Note Type");
        jLabel5.setMaximumSize(new java.awt.Dimension(60, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(60, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 14));
        HeaderPanel.add(jLabel5);

        NoteLabel.setForeground(new java.awt.Color(147, 64, 64));
        NoteLabel.setMaximumSize(new java.awt.Dimension(1500, 14));
        NoteLabel.setMinimumSize(new java.awt.Dimension(150, 14));
        NoteLabel.setPreferredSize(new java.awt.Dimension(150, 14));
        HeaderPanel.add(NoteLabel);

        IconLabel.setMaximumSize(new java.awt.Dimension(18, 18));
        IconLabel.setMinimumSize(new java.awt.Dimension(18, 18));
        IconLabel.setPreferredSize(new java.awt.Dimension(18, 18));
        HeaderPanel.add(IconLabel);

        MainPanel.add(HeaderPanel);

        MainNotePanel.setMaximumSize(new java.awt.Dimension(32767, 200));
        MainNotePanel.setMinimumSize(new java.awt.Dimension(24, 0));
        MainNotePanel.setPreferredSize(new java.awt.Dimension(4, 0));
        MainNotePanel.setLayout(new javax.swing.BoxLayout(MainNotePanel, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(24, 0));

        NoteTextBox.setFont(new java.awt.Font("Courier", 1, 14)); // NOI18N
        NoteTextBox.setLineWrap(true);
        NoteTextBox.setRows(3);
        NoteTextBox.setWrapStyleWord(true);
        NoteTextBox.setDisabledTextColor(new java.awt.Color(111, 126, 158));
        NoteTextBox.setEnabled(false);
        NoteTextBox.setMinimumSize(new java.awt.Dimension(0, 0));
        NoteTextBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HideControl(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ShowControl(evt);
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

        EditNote.setBackground(new java.awt.Color(199, 216, 255));
        EditNote.setText("EDIT NOTE");
        EditNote.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        EditNote.setMaximumSize(new java.awt.Dimension(85, 20));
        EditNote.setMinimumSize(new java.awt.Dimension(85, 20));
        EditNote.setOpaque(true);
        EditNote.setPreferredSize(new java.awt.Dimension(85, 20));
        EditNote.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        EditSaveNotes.add(EditNote);

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

    private void ShowControl(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowControl
        EditSaveNotes.setVisible(false);
    }//GEN-LAST:event_ShowControl

    private void HideControl(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HideControl
        EditSaveNotes.setOpaque(true);
        EditSaveNotes.setVisible(true);
    }//GEN-LAST:event_HideControl

    private void resizeLayeredPane(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_resizeLayeredPane
        MainPanel.setBounds(myLayeredPane.getBounds());
        ControlPanel.setBounds(myLayeredPane.getBounds());
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            jScrollPane1.setBounds(0, 0, 657, MainNotePanel.getPreferredSize().height);
            MainNotePanel.setBounds(0, 20, 657, MainNotePanel.getPreferredSize().height);
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(MainPanel, true);
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(ControlPanel, true);
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(MainNotePanel, true);
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(jScrollPane1, true);
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(NoteTextBox, true);
        }
        MainPanel.revalidate();
        ControlPanel.revalidate();
        MainNotePanel.revalidate();
        this.revalidate();
    }//GEN-LAST:event_resizeLayeredPane
    public void toggleSize() {
        if (IconLabel.getIcon() != Main_Window.Arrow_Up_Icon) {
            IconLabel.setIcon(Main_Window.Arrow_Up_Icon);
            int newHeight = this.NoteTextBox.getLineCount() * 22;
            if (newHeight > 110) {
                newHeight = 110;
            }
            if (newHeight < 66) {
                newHeight = 66;
            }
            MainNotePanel.setPreferredSize(new Dimension(100, newHeight));
            MainNotePanel.setMaximumSize(new Dimension(10000, newHeight));
            MainNotePanel.setMinimumSize(new Dimension(0, 66));
            setPreferredSize(new Dimension(100, newHeight + 20));
            setMaximumSize(new Dimension(10000, newHeight + 20));
            setMinimumSize(new Dimension(100, 86));
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                EditSaveNotes.setVisible(true);
            }
        } else {
            IconLabel.setIcon(Main_Window.Arrow_Down_Icon);
            MainNotePanel.setPreferredSize(new Dimension(0, 0));
            MainNotePanel.setMaximumSize(new Dimension(0, 0));
            MainNotePanel.setMinimumSize(new Dimension(0, 0));
            setMinimumSize(new Dimension(100, 20));
            setMaximumSize(new Dimension(10000, 20));
            setPreferredSize(new Dimension(100, 20));
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                EditSaveNotes.setVisible(false);
            }
        }
        revalidate();
        repaint();
    }

    /**
     * @return the note
     */
    public NoteInterface getClientNote() {
        return note;
    }

    /**
     * @param clientNote the note to set
     */
    public void setClientNote(ClientNotes clientNote) {
        this.note = clientNote;
    }

    private class testMouseListener implements MouseListener {

        public void mouseClicked(java.awt.event.MouseEvent evt) {
            toggleSize();
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
            HeaderPanel.setBackground(new Color(168, 188, 231));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            HeaderPanel.setBackground(defaultHeaderColor);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JLabel EditNote;
    private javax.swing.JPanel EditSaveNotes;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JLabel IconLabel;
    private javax.swing.JPanel MainNotePanel;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JLabel NoteLabel;
    private javax.swing.JTextArea NoteTextBox;
    private javax.swing.JLabel SaveNote;
    private javax.swing.JLabel UserLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane myLayeredPane;
    // End of variables declaration//GEN-END:variables
}
