/**
 *  FileName:  NewUser_Alert_SubForm.java
 *  Created by:  Jeffrey N. Davis
 *  Date Created:  08/03/2010
 *  Purpose of File:  File contains a class designed to be a subform for
 *      viewing and editting information related recieving New User Alerts.
 *      This screen is accessible as a subForm of NewUser_Alert_Screen.
 *  Modification Information:
 */
//  package declarations
package rmischedule.admin.newuser_alert;

//  import declarations
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *  Class Name:  NewUser_Alert_SubForm
 *  Purpose of Class:   a class designed to be a subform for viewing and
 *      editting information related recieving New User Alerts.  This screen is
 *      accessible as a subForm of NewUser_Alert_Screen.
 */
public class NewUser_Alert_SubForm extends javax.swing.JPanel {
    //  private variable declarations

    //  private method implementations
    /**
     *  Method Name:  formatCellNumber
     *  Purpose of Method:  clears out any non digits for save
     *  Arguments:  a string containing the unformatted cell
     *  Returns:  a properly formatted cell
     *  Preconditions:  cell number unformatted
     *  Postconditions:  cell number formatted, returned
     */
    private String formatCellNumber(String cell)
    {
        //  declaration of strings to return/manipulate
        StringBuffer returnString = new StringBuffer(0);
        StringBuffer formatString = new StringBuffer(0);

        //  traverse cell, appending only digits
        formatString.append(cell);
        for(int i = 0;i < formatString.length();i ++)
        {
            char testChar = formatString.charAt(i);
            if(Character.isDigit(testChar))
                returnString.append(testChar);
        }

        //  return properly formatted string
        return returnString.toString();
    }

    //  public method implementations
    /**
     *  Method Name:  NewUser_Alert_SubForm
     *  Purpose of Method:  creates an instance of this object, sets initial
     *      variables
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set
     */
    public NewUser_Alert_SubForm() {
        //  initialize java swing components
        initComponents();
        this.addDocListener(jTextFieldSSN, this.jSSNDocLabel, Pattern.compile("\\d{9}"));
        this.addDocListener(this.jTextFieldPrimaryEmail, this.jPrimaryEmailDocLabel,
                Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{3,})$"));
        this.addDocListener(this.jTextFieldAlternateEmail, this.jAlternateEmailDocLabel,
                Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{3,})$"));
        this.addDocListener(this.jTextNumberTextField, this.jTextNumberDocLabel,
                Pattern.compile("^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"));
    }

    /**
     *  Method Name:  loadUserData
     *  Purpose of Method:  method loads the data contained within argument
     *      into the fields
     *  Arguments:  a NewUser_Alert_Data
     *  Returns:  void
     *  Preconditions:  data known by parent, not displayed in current subform
     *  Postconditions:  data loaded into this subform
     */
    public void loadUserData(NewUser_Alert_Data data)
    {
        //  load data
        StringBuffer ssn = new StringBuffer(0);
        ssn.append(data.getSSN());
        this.jTextFieldSSN.setText(ssn.toString());
        this.jTextFieldFirstName.setText(data.getFirstName());
        this.jTextFieldLastName.setText(data.getLastName());
        this.jCompanyTextField.setText(data.getCompany());
        this.jTextFieldPrimaryEmail.setText(data.getPrimaryEmail());
        if (data.isUseAlternateEmail())
        {
            this.jTextFieldAlternateEmail.setEnabled(true);
            this.jCheckBoxAltEmail.setSelected(true);
            this.jCheckBoxBothEmail.setEnabled(true);
            this.jTextFieldAlternateEmail.setText(data.getAlternateEmail());
        }
        if (data.isUseBothEmail())
        {
            this.jCheckBoxBothEmail.setSelected(true);
        }
        if (data.isIsSendText())
        {
            this.jTextNumberTextField.setEnabled(true);
            this.jCheckBoxRecieveText.setSelected(true);
            this.jTextNumberTextField.setText(data.getTextNumber());
        }
    }

    /**
     *  Method Name:  reset
     *  Purpose of Method:  resets all fields in this form
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  form needs to be reste
     *  Postconditions:  form reset
     */
    public void reset() {
        this.jTextFieldSSN.setText(null);
        this.jTextFieldFirstName.setText(null);
        this.jTextFieldLastName.setText(null);
        this.jCompanyTextField.setText(null);
        this.jTextFieldPrimaryEmail.setText(null);
        this.jTextFieldAlternateEmail.setText(null);
        this.jTextFieldAlternateEmail.setEnabled(false);
        this.jCheckBoxAltEmail.setSelected(false);
        this.jCheckBoxBothEmail.setSelected(false);
        this.jTextNumberTextField.setText(null);
        this.jTextNumberTextField.setEnabled(false);
        this.jCheckBoxRecieveText.setSelected(false);
    }

    /**
     *  Method Name:  getFormData
     *  Purpose of Method:  loads the data from the form into a data object
     *      for use by parent
     *  Arguments:  none
     *  Returns:  an instance of NewUser_Alert_Data containing the data from
     *      the form
     *  Preconditions:  parent requires data from form
     *  Postconditions:  data returned
     */
    public NewUser_Alert_Data getFormData() {
        //  declaration of data object to return
        NewUser_Alert_Data returnData = new NewUser_Alert_Data();

        //  check for faulty ssn
        try
        {
            Integer ssn = new Integer(this.jTextFieldSSN.getText());
            returnData.setSSN(ssn);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        returnData.setFirstName(this.jTextFieldFirstName.getText());
        returnData.setLastName(this.jTextFieldLastName.getText());
        returnData.setCompany(this.jCompanyTextField.getText());
        returnData.setPrimaryEmail(this.jTextFieldPrimaryEmail.getText());
        if (this.jCheckBoxAltEmail.isSelected()) {
            returnData.setAlternateEmail(this.jTextFieldAlternateEmail.getText());
            returnData.setUseAlternateEmail(true);
        } else {
            returnData.setAlternateEmail(null);
            returnData.setUseAlternateEmail(false);
        }
        if (this.jCheckBoxRecieveText.isSelected()) {
            returnData.setTextNumber(formatCellNumber(this.jTextNumberTextField.getText()));
            returnData.setIsSendText(true);
        } else {
            returnData.setTextNumber(null);
            returnData.setIsSendText(false);
        }
        if (this.jCheckBoxAltEmail.isSelected()) {
            returnData.setUseBothEmail(true);
        } else {
            returnData.setUseBothEmail(false);
        }

        //  return data object
        return returnData;
    }

   /**
     *  Method Name:  isDataSafe
     *  Purpose of Method:  checks data from subForm to ensure it is safe
     *      to save
     *  Arguments:  none
     *  Returns:  a boolean describing if the data is saved
     *  Preconditions:  safety check on data not run
     *  Postconditions:  data checked
     */
    public boolean isDataSafe()
    {
        //  declaration of flag, get data from subform
        boolean flag = true;

        //  perform safety checks
        if(!this.jSSNDocLabel.getText().matches("Valid"))
        {
            JOptionPane.showMessageDialog(null, "You must enter a valid SSN to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        if(this.jTextFieldFirstName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(null, "You must enter a first name to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        if(this.jTextFieldLastName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(null, "You must enter a last name to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        if(this.jCompanyTextField.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(null, "You must enter a company to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        if(!this.jPrimaryEmailDocLabel.getText().matches("Valid"))
        {
            JOptionPane.showMessageDialog(null, "You must enter a valid Primary Email to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        if(this.jCheckBoxAltEmail.isSelected())
        {
            if(!this.jAlternateEmailDocLabel.getText().matches("Valid"))
            {
                JOptionPane.showMessageDialog(null, "You must enter a valid Alternate Email, or unselect the Use Alt Email box to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
                flag = false;
            }
        }
        if(this.jCheckBoxRecieveText.isSelected())
        {
            if(!this.jTextNumberDocLabel.getText().matches("Valid"))
            {
                JOptionPane.showMessageDialog(null, "You must enter a Text #, or unselect the Receive Text box to save.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
                flag = false;
            }
        }
        
        //  return flag
        return flag;
    }

    /**
     *  Method Name:  getMyForm
     *  Purpose of Method:  returns this class as a JPanel
     *  Arguments:  none
     *  Returns:  this class as a JPanel
     *  Precondition:  another piece of code requires this class
     *  Postcondition:  this class returned
     */
    public JPanel getMyForm() {
        return this;
    }

    //  java swing code
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelPanel = new javax.swing.JPanel();
        jHeadingLabel = new javax.swing.JLabel();
        jUserInformationPanel = new javax.swing.JPanel();
        jLabelSSN = new javax.swing.JLabel();
        jTextFieldSSN = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldLastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldPrimaryEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldAlternateEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBoxRecieveText = new javax.swing.JCheckBox();
        jCheckBoxBothEmail = new javax.swing.JCheckBox();
        jCheckBoxAltEmail = new javax.swing.JCheckBox();
        jCompanyLabel = new javax.swing.JLabel();
        jCompanyTextField = new javax.swing.JTextField();
        jSSNDocLabel = new javax.swing.JLabel();
        jFirstNameDocLabel = new javax.swing.JLabel();
        jLastNameDocLabel = new javax.swing.JLabel();
        jAlternateEmailDocLabel = new javax.swing.JLabel();
        jCompanyDocLabel = new javax.swing.JLabel();
        jPrimaryEmailDocLabel = new javax.swing.JLabel();
        jTextNumberDocLabel = new javax.swing.JLabel();
        jTextNumberTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        jLabelPanel.setLayout(new java.awt.GridBagLayout());

        jHeadingLabel.setText("User Alert Information");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jLabelPanel.add(jHeadingLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(jLabelPanel, gridBagConstraints);

        jUserInformationPanel.setLayout(new java.awt.GridBagLayout());

        jLabelSSN.setText("SSN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabelSSN, gridBagConstraints);

        jTextFieldSSN.setMinimumSize(new java.awt.Dimension(6, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jUserInformationPanel.add(jTextFieldSSN, gridBagConstraints);

        jLabel1.setText("First Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jTextFieldFirstName, gridBagConstraints);

        jLabel2.setText("Last Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jTextFieldLastName, gridBagConstraints);

        jLabel3.setText("Primary Email:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jTextFieldPrimaryEmail, gridBagConstraints);

        jLabel4.setText("Alt Email:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabel4, gridBagConstraints);

        jTextFieldAlternateEmail.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jTextFieldAlternateEmail, gridBagConstraints);

        jLabel5.setText("Text #:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jLabel5, gridBagConstraints);

        jCheckBoxRecieveText.setText("Receive Text?");
        jCheckBoxRecieveText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRecieveTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jCheckBoxRecieveText, gridBagConstraints);

        jCheckBoxBothEmail.setText("Send to both email?");
        jCheckBoxBothEmail.setEnabled(false);
        jCheckBoxBothEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBothEmailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        jUserInformationPanel.add(jCheckBoxBothEmail, gridBagConstraints);

        jCheckBoxAltEmail.setText("Use Alt Email?");
        jCheckBoxAltEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAltEmailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jCheckBoxAltEmail, gridBagConstraints);

        jCompanyLabel.setText("Company:  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jUserInformationPanel.add(jCompanyLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jCompanyTextField, gridBagConstraints);
        jUserInformationPanel.add(jSSNDocLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jUserInformationPanel.add(jFirstNameDocLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jUserInformationPanel.add(jLastNameDocLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        jUserInformationPanel.add(jAlternateEmailDocLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jUserInformationPanel.add(jCompanyDocLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        jUserInformationPanel.add(jPrimaryEmailDocLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        jUserInformationPanel.add(jTextNumberDocLabel, gridBagConstraints);

        jTextNumberTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jUserInformationPanel.add(jTextNumberTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        add(jUserInformationPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxBothEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBothEmailActionPerformed
    }//GEN-LAST:event_jCheckBoxBothEmailActionPerformed

    private void jCheckBoxAltEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAltEmailActionPerformed
        if (this.jCheckBoxAltEmail.isSelected())
        {
            this.jTextFieldAlternateEmail.setEnabled(true);
            this.jCheckBoxBothEmail.setEnabled(true);
        } 
        else
        {
            this.jTextFieldAlternateEmail.setText("");
            this.jTextFieldAlternateEmail.setEnabled(false);
            this.jCheckBoxBothEmail.setSelected(false);
            this.jCheckBoxBothEmail.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxAltEmailActionPerformed

    private void jCheckBoxRecieveTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRecieveTextActionPerformed
        if (this.jCheckBoxRecieveText.isSelected())
        {
            this.jTextNumberTextField.setEnabled(true);
        } 
        else
        {
            this.jTextNumberTextField.setText("");
            this.jTextNumberTextField.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxRecieveTextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jAlternateEmailDocLabel;
    private javax.swing.JCheckBox jCheckBoxAltEmail;
    private javax.swing.JCheckBox jCheckBoxBothEmail;
    private javax.swing.JCheckBox jCheckBoxRecieveText;
    private javax.swing.JLabel jCompanyDocLabel;
    private javax.swing.JLabel jCompanyLabel;
    private javax.swing.JTextField jCompanyTextField;
    private javax.swing.JLabel jFirstNameDocLabel;
    private javax.swing.JLabel jHeadingLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jLabelPanel;
    private javax.swing.JLabel jLabelSSN;
    private javax.swing.JLabel jLastNameDocLabel;
    private javax.swing.JLabel jPrimaryEmailDocLabel;
    private javax.swing.JLabel jSSNDocLabel;
    private javax.swing.JTextField jTextFieldAlternateEmail;
    private javax.swing.JTextField jTextFieldFirstName;
    private javax.swing.JTextField jTextFieldLastName;
    private javax.swing.JTextField jTextFieldPrimaryEmail;
    private javax.swing.JTextField jTextFieldSSN;
    private javax.swing.JLabel jTextNumberDocLabel;
    private javax.swing.JTextField jTextNumberTextField;
    private javax.swing.JPanel jUserInformationPanel;
    // End of variables declaration//GEN-END:variables

    // VU's document listener bullshit
    private void addDocListener(javax.swing.JTextField tf, javax.swing.JLabel lbl, Pattern pattern) {
        tf.getDocument().addDocumentListener(new DocumentChecker(pattern, lbl));
    }

    class DocumentChecker implements DocumentListener {

        private Pattern pattern;
        private Matcher matcher;
        private javax.swing.JLabel errorLabel;

        public DocumentChecker(Pattern pattern, javax.swing.JLabel errorLabel) {
            this.pattern = pattern;
            this.errorLabel = errorLabel;
        }

        public void insertUpdate(DocumentEvent e) {
            this.checkInput(e);
        }

        public void removeUpdate(DocumentEvent e) {
            this.checkInput(e);
        }

        public void changedUpdate(DocumentEvent e) {
            this.checkInput(e);
        }

        private void checkInput(DocumentEvent e) {
            try {
                Document doc = (Document) e.getDocument();
                if (doc.getLength() > 0) {
                    String input = doc.getText(0, doc.getLength());
                    this.matcher = this.pattern.matcher(input);
                    if (this.matcher.matches())
                    {
                        this.errorLabel.setText("Valid");
                        //this.errorLabel.se
                    } 
                    else
                    {

                        this.errorLabel.setText("Currently Invalid Format");
                    }
                }else{
                    this.errorLabel.setText("");
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }
};
