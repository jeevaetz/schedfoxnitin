/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditRateIncreaseTemplates.java
 *
 * Created on Jul 28, 2011, 3:35:07 PM
 */
package rmischedule.client.rate_increases;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.swing.JRViewer;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ClientRateTemplatesController;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.ClientRateTemplates;

/**
 *
 * @author user
 */
public class EditRateIncreaseTemplates extends javax.swing.JDialog {

    private String companyId;
    private ArrayList<ClientContract> clientContract;
    private FileInputStream fInput;
    private RateTemplateComboModel rateComboModel;
    private static String CLIENT_NAME = "Client Name";
    private static String PROJECTED_INCREASE = "Rate of Increase %";
    private static String CURRENT_DATE = "Current Date";
    private static String CLIENT_CONTACT = "Billing Contact";
    private static String CLIENT_ADDRESS = "Client Address";
    private static String CLIENT_CITY = "Client City";
    private static String CLIENT_STATE = "Client State";
    private static String CLIENT_ZIP = "Client Zip";
    private static String GLOBAL_INCREASE = "Global_Increase";

    private static String SIGNATURE_FILE = "Signature_Image";

    /**
     * Creates new form EditRateIncreaseTemplates
     */
    public EditRateIncreaseTemplates(java.awt.Frame parent, boolean modal, String companyId, ArrayList<ClientContract> clientContract) {
        super(parent, modal);
        rateComboModel = new RateTemplateComboModel();
        initComponents();

        this.companyId = companyId;
        this.clientContract = clientContract;

        refreshTemplates();

        tabbedPane.addChangeListener(new ChangeListener() {
            private boolean hasRun = false;

            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1 && !hasRun) {
                    hasRun = true;
                    generateReportPreview();
                } else {
                    hasRun = false;
                }
            }
        });

        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_NAME + "}"));
        //dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{DM Name}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CURRENT_DATE + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + PROJECTED_INCREASE + "}"));
        //dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{Rate of Increase $}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_CONTACT + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_ADDRESS + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_CITY + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_STATE + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + CLIENT_ZIP + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + GLOBAL_INCREASE + "}"));
        dragDropPane.add(new RateFieldPanel(myLayeredPane, content, "{" + SIGNATURE_FILE + "}"));

        dragDropPane.add(new JPanel());
    }

    private void refreshTemplates() {
        try {
            ClientRateTemplatesController rateController = ClientRateTemplatesController.getInstance(companyId);
            ArrayList<ClientRateTemplates> templates = rateController.getActiveTemplates();

            rateComboModel.clear();
            for (int t = 0; t < templates.size(); t++) {
                rateComboModel.add(templates.get(t));
            }
            rateComboModel.setSelectedIndex(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateReportPreview() {
        Connection myConn = new Connection();
        myConn.myCompany = this.companyId;

        try {
            previewPanel.removeAll();
            JRProperties.setProperty(JRViewer.VIEWER_RENDER_BUFFER_MAX_SIZE, "1000000000000000000");
            JasperPrint report = null;

            String globalValue = "";
            String letterText = letterEditor.getText();
            if (letterText.contains(GLOBAL_INCREASE)) {
                globalValue = (String) JOptionPane.showInputDialog(
                        Main_Window.parentOfApplication, "Enter the global rate increase:",
                        "Global Rate Increase", JOptionPane.PLAIN_MESSAGE);
            }
            for (int c = 0; c < clientContract.size(); c++) {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/increase_letter.jasper");
                InputStream backgroundStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/champ.jpg");
                InputStream signatureStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/signature.png");

                Client currClient = clientContract.get(c).getClient();
                letterText = letterEditor.getText();

                letterText = letterText.replaceAll("\t", "        ");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                letterText = letterText.replaceAll("\\{Current Date\\}", dateFormat.format(new Date()));
                letterText = letterText.replaceAll("\\{" + CLIENT_NAME + "\\}", clientContract.get(c).getClient().getClientName());
                letterText = letterText.replaceAll("\\{" + PROJECTED_INCREASE + "\\}", clientContract.get(c).getProjectedIncrease() + "%");
                letterText = letterText.replaceAll("\\{" + PROJECTED_INCREASE + "\\}", clientContract.get(c).getProjectedIncrease() + "%");

                String clientAddress = "";
                String clientCity = "";
                String clientState = "";
                String clientZip = "";
                boolean hasBillingContact = false;

                ArrayList<ClientContact> contacts = clientContract.get(c).getClient().getContacts(companyId);
                String contact = "To Whom it May Concern";
                for (int co = 0; co < contacts.size(); co++) {
                    if (contacts.get(co).getClientContactType() == 5) {
                        contact = contacts.get(co).getFullName();
                        clientAddress = contacts.get(co).getClientContactAddress();
                        if (contacts.get(co).getClientContactAddress2() != null && contacts.get(co).getClientContactAddress2().trim().length() > 0) {
                            clientAddress += "\r\n" + contacts.get(co).getClientContactAddress2();
                        }
                        clientCity = contacts.get(co).getClientContactCity();
                        clientState = contacts.get(co).getClientContactState();
                        clientZip = contacts.get(co).getClientContactZip();
                        hasBillingContact = true;
                    }
                }
                for (int co = 0; co < contacts.size(); co++) {
                    if (contacts.get(co).getClientContactIsPrimary() == 1) {
                        if (!hasBillingContact) {
                            contact = contacts.get(co).getFullName();
                        }
                        if (clientAddress == null || clientAddress.length() < 2) {
                            clientAddress = contacts.get(co).getClientContactAddress();
                            if (contacts.get(co).getClientContactAddress2() != null && contacts.get(co).getClientContactAddress2().trim().length() > 0) {
                                clientAddress += "\r\n" + contacts.get(co).getClientContactAddress2();
                            }
                            clientCity = contacts.get(co).getClientContactCity();
                            clientState = contacts.get(co).getClientContactState();
                            clientZip = contacts.get(co).getClientContactZip();
                        }
                    }
                }
                try {
                    if (contact.equals("To Whom it May Concern") && contacts.size() > 0) {
                        contact = contacts.get(0).getFullName();
                    }
                    if (clientAddress == null || clientAddress.length() < 2) {
                        clientAddress = currClient.getAddress1() + " " + currClient.getAddress2();
                        if (currClient.getAddress2() != null && currClient.getAddress2().trim().length() > 0) {
                            clientAddress += "\r\n" + currClient.getAddress2();
                        }
                        clientCity = currClient.getCity();
                        clientState = currClient.getState();
                        clientZip = currClient.getZip();
                    }
                } catch (Exception exe) {
                }
                letterText = letterText.replaceAll("\\{" + CLIENT_CONTACT + "\\}", contact);
                letterText = letterText.replaceAll("\\{" + CLIENT_ADDRESS + "\\}", clientAddress);
                letterText = letterText.replaceAll("\\{" + CLIENT_CITY + "\\}", clientCity);
                letterText = letterText.replaceAll("\\{" + CLIENT_STATE + "\\}", clientState);
                letterText = letterText.replaceAll("\\{" + CLIENT_ZIP + "\\}", clientZip);
                letterText = letterText.replaceAll("\\{" + GLOBAL_INCREASE + "\\}", globalValue);

                HashMap parameters = new HashMap();
                parameters.put("background", backgroundStream);
                parameters.put("letter_body", letterText);
                parameters.put("image_read", fInput);
                parameters.put("sigFile", signatureStream);
                if (report == null) {
                    report = JasperFillManager.fillReport(reportStream, parameters);
                } else {
                    JasperPrint newReport = JasperFillManager.fillReport(reportStream, parameters);
                    for (int p = 0; p < newReport.getPages().size(); p++) {
                        report.addPage((JRPrintPage) newReport.getPages().get(p));
                    }
                }
            }
            JRViewer myView = new JRViewer(report);
            previewPanel.add(myView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        controlPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        templateSelectionCombo = new javax.swing.JComboBox();
        saveBtn = new javax.swing.JButton();
        contentPane = new javax.swing.JPanel();
        myLayeredPane = new javax.swing.JLayeredPane();
        content = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        letterEditor = new RateEditor();
        dragDropPane = new javax.swing.JPanel();
        previewPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        controlPane.setMaximumSize(new java.awt.Dimension(2000, 45));
        controlPane.setMinimumSize(new java.awt.Dimension(200, 45));
        controlPane.setPreferredSize(new java.awt.Dimension(500, 45));
        controlPane.setLayout(new javax.swing.BoxLayout(controlPane, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Template");
        jLabel1.setMaximumSize(new java.awt.Dimension(60, 16));
        controlPane.add(jLabel1);

        templateSelectionCombo.setModel(rateComboModel);
        templateSelectionCombo.setMaximumSize(new java.awt.Dimension(32767, 28));
        templateSelectionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateSelectionComboActionPerformed(evt);
            }
        });
        controlPane.add(templateSelectionCombo);

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        controlPane.add(saveBtn);

        jPanel3.add(controlPane);

        contentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        contentPane.setLayout(new java.awt.GridLayout(1, 0));

        content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.X_AXIS));

        scrollPane.setViewportView(letterEditor);

        content.add(scrollPane);

        dragDropPane.setMaximumSize(new java.awt.Dimension(120, 900000));
        dragDropPane.setMinimumSize(new java.awt.Dimension(120, 10));
        dragDropPane.setPreferredSize(new java.awt.Dimension(120, 100));
        dragDropPane.setLayout(new javax.swing.BoxLayout(dragDropPane, javax.swing.BoxLayout.Y_AXIS));
        content.add(dragDropPane);

        content.setBounds(0, 0, 228, 400);
        myLayeredPane.add(content, javax.swing.JLayeredPane.DEFAULT_LAYER);

        contentPane.add(myLayeredPane);

        jPanel3.add(contentPane);

        tabbedPane.addTab("Template", jPanel3);

        previewPanel.setLayout(new javax.swing.BoxLayout(previewPanel, javax.swing.BoxLayout.X_AXIS));
        tabbedPane.addTab("Preview", previewPanel);

        getContentPane().add(tabbedPane);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-719)/2, (screenSize.height-690)/2, 719, 690);
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        ClientRateTemplates clientTemplates = new ClientRateTemplates();
        clientTemplates.setActive(true);

        if (templateSelectionCombo.getSelectedItem() == null || rateComboModel.getSelectedTemplate().getTemplateName().length() == 0) {
            String value = JOptionPane.showInputDialog(Main_Window.parentOfApplication, "Enter a template name.",
                    "Input", JOptionPane.OK_OPTION);
            clientTemplates.setTemplateName(value);
        } else {
            clientTemplates = rateComboModel.getSelectedTemplate();
        }
        clientTemplates.setTemplateText(letterEditor.getText());

        try {
            ClientRateTemplatesController controller = ClientRateTemplatesController.getInstance(companyId);
            controller.saveTemplate(clientTemplates);

            refreshTemplates();
            letterEditor.setText("");

            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Template Saved!",
                    "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        content.setSize(myLayeredPane.getSize());
        content.setPreferredSize(myLayeredPane.getPreferredSize());
        content.revalidate();
    }//GEN-LAST:event_formComponentResized

    private void templateSelectionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateSelectionComboActionPerformed
        ClientRateTemplates clientRateTemp = rateComboModel.getSelectedTemplate();
        letterEditor.setText(clientRateTemp.getTemplateText());
    }//GEN-LAST:event_templateSelectionComboActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    private javax.swing.JPanel contentPane;
    private javax.swing.JPanel controlPane;
    private javax.swing.JPanel dragDropPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JEditorPane letterEditor;
    private javax.swing.JLayeredPane myLayeredPane;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JComboBox templateSelectionCombo;
    // End of variables declaration//GEN-END:variables
}
