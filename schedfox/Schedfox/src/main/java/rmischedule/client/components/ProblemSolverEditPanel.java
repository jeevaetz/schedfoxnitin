/*
 * ProblemSolverEditPanel.java
 *
 * Created on August 14, 2006, 1:40 PM
 */
package rmischedule.client.components;

import com.inet.jortho.SpellChecker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.*;
import rmischedule.templates.models.TemplateModel;
import rmischedule.main.Main_Window;
import schedfoxlib.model.PhoneContact;
import schedfoxlib.model.ProblemsolverTemplate;

/**
 *
 * @author shawn
 */
public class ProblemSolverEditPanel extends javax.swing.JPanel {

    private boolean selected = false;
    public static final Color mouseOverColor = new Color(175, 175, 255);
    public static final Color selectedColor = new Color(0, 235, 30);
    private static final Border mouseOverBorder = new LineBorder(mouseOverColor, 1);
    private static final Border selectedBorder = new LineBorder(selectedColor, 1);
    private static final Border emptyBorder = new EmptyBorder(1, 1, 1, 1);
    private JPopupMenu copyPasteMenu;
    private ArrayList<ProblemsolverTemplate> templatesList;
    private final int type;
    private final int companyId;
    private final EditProblemSolverDialog myParent;
    private TemplateModel templateModel;

    private CardLayout headerLayout;
    private ProblemContactTypePanel contacts;

    /**
     * Creates new form ProblemSolverEditPanel
     */
    public ProblemSolverEditPanel(String description, String text, int type,
            ArrayList<ProblemsolverTemplate> templates,
            int companyId, EditProblemSolverDialog myParent,
            ProblemContactTypePanel contacts) {
        initComponents();
        this.contacts = contacts;

        this.descriptionLabel.setText(description);
        this.textArea.setText(text);
        this.contract();
        headerLayout = (CardLayout) expandPanel.getLayout();

        //  fill templates list, set drop-down items
        if (templates.isEmpty()) {
            this.jTemplateNameComboBox.setVisible(false);
        } else {
            this.jTemplateNameComboBox.setVisible(true);
        }

        this.templatesList = templates;
        this.templateModel = new TemplateModel(this.templatesList);
        this.jTemplateNameComboBox.setModel(templateModel);
        if (contacts != null) {
            this.problemContacts.add(contacts);
            this.contacts.togglePanel();
        }
        if (!this.templatesList.isEmpty()) {
            for (int idx = 0; idx < this.templatesList.size(); idx++) {
                String listText = this.templatesList.get(idx).getProblemsolverValue();
                if (listText.equalsIgnoreCase(text)) {
                    this.jTemplateNameComboBox.setSelectedIndex(idx);
                }
            }
        }

        this.type = type;
        this.companyId = companyId;
        this.myParent = myParent;
        this.addMouseListener(new MouseListener() {

            public void mouseExited(MouseEvent evt) {
                if (!selected) {
                    setBorder(emptyBorder);
                }
            }

            public void mouseEntered(MouseEvent evt) {
                if (!selected) {
                    setBorder(mouseOverBorder);
                }
            }

            public void mouseReleased(MouseEvent evt) {
            }

            public void mousePressed(MouseEvent evt) {
            }

            public void mouseClicked(MouseEvent evt) {
                if (selected) {
                    contract();
                    selected = false;
                    headerLayout.show(expandPanel, "default");
                } else {
                    expand();
                    selected = true;
                    textArea.requestFocusInWindow();
                    headerLayout.show(expandPanel, "control");
                }
            }
        });
        try {
            SpellChecker.register(textArea);
            SpellChecker.enablePopup(textArea, true);
            SpellChecker.getOptions().setIgnoreAllCapsWords(true);
        } catch (Exception e) {
        }
        this.addPopupMenu();
    }

    public ArrayList<PhoneContact> getPhoneContacts() {
        if (this.contacts != null) {
            return this.contacts.getSelectedContacts();
        }
        return new ArrayList<PhoneContact>();
    }

    private void addPopupMenu() {
        copyPasteMenu = new JPopupMenu();

        JMenuItem copyMenu = new JMenuItem("Copy");
        copyMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textArea.copy();
            }
        });
        JMenuItem cutMenu = new JMenuItem("Cut");
        cutMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textArea.cut();
            }
        });
        JMenuItem pasteMenu = new JMenuItem("Paste");
        pasteMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textArea.paste();
            }
        });

        copyPasteMenu.add(copyMenu);
        copyPasteMenu.add(cutMenu);
        copyPasteMenu.add(pasteMenu);

        textArea.add(copyPasteMenu);
    }

    public void expand() {
        int newHeight = 95;
        if (this.contacts != null) {
            newHeight = newHeight + 120;
        }
        this.textAreaScrollPane.setMaximumSize(new Dimension(32767, newHeight));
        this.setMaximumSize(new Dimension(100000, newHeight + 24));
        this.setMinimumSize(new Dimension(0, newHeight + 24));
        this.setPreferredSize(new Dimension(0, newHeight + 24));
        this.setBorder(selectedBorder);
        this.revalidate();
        this.textAreaScrollPane.getVerticalScrollBar().setValue(0);
    }

    public void contract() {
        this.textAreaScrollPane.setMaximumSize(new Dimension(32767, 0));
        this.setupTextIcon();
        this.setMaximumSize(new Dimension(100000, 24));
        this.setMinimumSize(new Dimension(0, 24));
        this.setPreferredSize(new Dimension(0, 24));
        this.setBorder(emptyBorder);
        this.revalidate();
    }

    public void setupTextIcon() {
        if (this.toString().length() > 0) {
            this.descriptionLabel.setIcon(Main_Window.Note16x16);
        } else {
            this.descriptionLabel.setIcon(null);
        }
    }

    /**
     * Allows access to <code>EditProblemSolverDialog</code> for reload by
     * <code>EditTemplateDiag</code>
     *
     * @return myParent the instance of <code>EditProblemSolverDialog</code>
     * that created this object
     */
    protected EditProblemSolverDialog getMyParent() {
        return this.myParent;
    }

    @Override
    public String toString() {
        return this.textArea.getText().trim();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify thi always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        expandPanel = new javax.swing.JPanel();
        emptyPanel = new javax.swing.JPanel();
        templatePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTemplateNameComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jEditTemplatesButton = new javax.swing.JButton();
        textAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        problemContacts = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        headerPanel.setMinimumSize(new java.awt.Dimension(10, 24));
        headerPanel.setPreferredSize(new java.awt.Dimension(100, 24));
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.LINE_AXIS));

        descriptionLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        descriptionLabel.setForeground(new java.awt.Color(100, 100, 200));
        descriptionLabel.setText("Description");
        descriptionLabel.setMaximumSize(new java.awt.Dimension(300, 800));
        descriptionLabel.setMinimumSize(new java.awt.Dimension(300, 19));
        descriptionLabel.setPreferredSize(new java.awt.Dimension(300, 19));
        headerPanel.add(descriptionLabel);

        expandPanel.setLayout(new java.awt.CardLayout());
        expandPanel.add(emptyPanel, "default");

        templatePanel.setMinimumSize(new java.awt.Dimension(10, 28));
        templatePanel.setPreferredSize(new java.awt.Dimension(100, 28));
        templatePanel.setLayout(new javax.swing.BoxLayout(templatePanel, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("Use Template");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 10));
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 18));
        templatePanel.add(jLabel1);

        jTemplateNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTemplateNameComboBoxActionPerformed(evt);
            }
        });
        templatePanel.add(jTemplateNameComboBox);

        jPanel1.setMaximumSize(new java.awt.Dimension(140, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(140, 28));
        jPanel1.setPreferredSize(new java.awt.Dimension(140, 100));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jEditTemplatesButton.setText("Edit Templates");
        jEditTemplatesButton.setMaximumSize(new java.awt.Dimension(70, 28));
        jEditTemplatesButton.setMinimumSize(new java.awt.Dimension(70, 28));
        jEditTemplatesButton.setPreferredSize(new java.awt.Dimension(70, 28));
        jEditTemplatesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditTemplatesButtonActionPerformed(evt);
            }
        });
        jPanel1.add(jEditTemplatesButton);

        templatePanel.add(jPanel1);

        expandPanel.add(templatePanel, "control");

        headerPanel.add(expandPanel);

        add(headerPanel);

        textAreaScrollPane.setMaximumSize(new java.awt.Dimension(32767, 0));
        textAreaScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));
        textAreaScrollPane.setPreferredSize(new java.awt.Dimension(162, 85));

        textArea.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMaximumSize(new java.awt.Dimension(2147483647, 1000000000));
        textArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textAreaMouseClicked(evt);
            }
        });
        textArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textAreaKeyTyped(evt);
            }
        });
        textAreaScrollPane.setViewportView(textArea);

        add(textAreaScrollPane);

        problemContacts.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4));
        problemContacts.setMinimumSize(new java.awt.Dimension(10, 0));
        problemContacts.setPreferredSize(new java.awt.Dimension(100, 0));
        problemContacts.setLayout(new java.awt.GridLayout(1, 0));
        add(problemContacts);
    }// </editor-fold>//GEN-END:initComponents

    private void textAreaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textAreaKeyTyped
        this.setupTextIcon();
    }//GEN-LAST:event_textAreaKeyTyped

    private void textAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textAreaMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            this.copyPasteMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_textAreaMouseClicked

    private void jEditTemplatesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTemplatesButtonActionPerformed
        String boxText = this.textArea.getText();

        EditTemplateDiag diag = new EditTemplateDiag(Main_Window.parentOfApplication, true,
                this.templatesList, this, this.type, this.companyId, boxText);
        diag.pack();
        diag.setVisible(true);
    }//GEN-LAST:event_jEditTemplatesButtonActionPerformed

    private void jTemplateNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTemplateNameComboBoxActionPerformed
        try {
            this.textArea.setText("");
            this.textArea.append(this.templatesList.get(this.jTemplateNameComboBox.getSelectedIndex()).getProblemsolverValue());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jTemplateNameComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JPanel expandPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton jEditTemplatesButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jTemplateNameComboBox;
    private javax.swing.JPanel problemContacts;
    private javax.swing.JPanel templatePanel;
    private javax.swing.JTextArea textArea;
    private javax.swing.JScrollPane textAreaScrollPane;
    // End of variables declaration//GEN-END:variables
}
