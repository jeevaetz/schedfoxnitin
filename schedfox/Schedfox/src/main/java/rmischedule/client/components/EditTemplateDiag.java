//  package declaration
package rmischedule.client.components;

//  import declarations
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.ProblemsolverTemplate;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.save_problemsolver_template_query;
import com.inet.jortho.SpellChecker;
import rmischedule.templates.models.TemplateModel;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.delete_problemsolver_template_query;

/**
 *  A class designed to edit templates for <literal>Corporate Communicator</literal>
 *  @author Jeffrey Davis
 *  @see rmischedule.client.components.ProblemSolverEditPanel 
 *  @since 02/2011
 */
public final class EditTemplateDiag extends javax.swing.JDialog {
    //  private variable declarations
    private final TitledBorder titleBorder;
    private ArrayList<ProblemsolverTemplate> dataList;
    private ProblemSolverEditPanel myParent;
    private final int type;
    private final int companyId;
    private TemplateModel templateModel;

    /**
     *  Checks to ensure Template is Ready for save
     *  @return isReady a boolean describing if the form has been completed
     */
    private boolean isReadyForSave()
    {
        if ( this.jTemplateNameTextField.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this, "You must enter a template name before saving.",
                "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if ( this.jTemplateNameTextField.getText().equalsIgnoreCase("Enter Template Name"))
        {
            JOptionPane.showMessageDialog(this, "You must enter a new template name before saving.",
                "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }
            
        if ( this.jTemplateTextArea.getText().length() == 0 )
        {
            JOptionPane.showMessageDialog(this, "You must enter a template before saving.",
                "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }
            
        return true;
    }

    /**
     *  Checks to ensure template can be deleted
     *  @return isDeleteReady
     */
    private boolean isReadyForDeletion()
    {
        int selectedIndex = this.jTemplateListingComboBox.getSelectedIndex();

        /*  check for new template  */
        if ( selectedIndex == 0)
        {
            JOptionPane.showMessageDialog(this, "Cannot delete a new template.",
                "Delete Template?", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /*  check for Enter Template Name */
        try
        {
           ProblemsolverTemplate importedCheck = this.dataList.get(1);
           if ( importedCheck.getProblemsolverName().equalsIgnoreCase("Enter Template Name") )
           {
               JOptionPane.showMessageDialog(this, "Cannot delete an imported template.",
                "Delete Template?", JOptionPane.ERROR_MESSAGE);
               return false;
           }
        }
        catch ( NullPointerException ex)
        {
           //   do nothing, there will be cases where there is nothing at index == 1
        }
        /*  check for no matching template  */
        String textToCompare = this.jTemplateTextArea.getText();
        boolean hasMatch = false;
        for ( ProblemsolverTemplate element:  this.dataList )
        {
            if ( textToCompare.equalsIgnoreCase( element.getProblemsolverValue() ) )
                hasMatch = true;
        }
        if ( !hasMatch )
        {
            JOptionPane.showMessageDialog(this, "Cannot delete an altered template",
                "Delete Template?", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private boolean isTemplateNew()
    {
        //  check to see if template is of new template
        if ( this.jTemplateListingComboBox.getSelectedIndex() == 0 )
            return true;
        
        //  check to see if template is of {@code ImportedTemplate}
        String templateName = this.dataList.get(1).getProblemsolverName();
        if ( templateName.equalsIgnoreCase("Enter Template Name" ) )
            return true;

        return false;
    }

    private EditTemplateDiag()
    {
        throw new UnsupportedOperationException("Default Constructor not supported");
    }

    /** Creates new form EditTemplateDiag */
    public EditTemplateDiag(java.awt.Frame parent, boolean modal, ArrayList<ProblemsolverTemplate> argDataList,
        ProblemSolverEditPanel myParent, int type, int companyId, String originalText) {
        super(parent, modal);
        initComponents();
        
        //  set variables
        this.myParent = myParent;
        this.type = type;
        this.companyId = companyId;

        //  set icons
        this.jSaveButton.setIcon(Main_Window.Edit_Template_Diag_Save_16x16_px);
        this.jExitButton.setIcon(Main_Window.Edit_Template_Diag_Exit_16x16_px);
        this.jDeleteButton.setIcon(Main_Window.Edit_Template_Diag_Delete_16x16_px);
        
        //  set title
        titleBorder = new TitledBorder("Edit Templates");
        this.jBasePanel.setBorder(titleBorder);

        //  setup data structure
        this.dataList = new ArrayList<ProblemsolverTemplate>();
        ProblemsolverTemplate newTemplate = new ProblemsolverTemplate();
        newTemplate.setProblemsolverName("New Template");
        this.dataList.add(newTemplate);
        if ( originalText.length() > 1)
        {
            //  check to see if imported text is equal to previously saved template
            boolean isNewText = true;
            for ( ProblemsolverTemplate element:  argDataList)
            {
                if ( element.getProblemsolverValue().equalsIgnoreCase(originalText))
                    isNewText = false;
            }
            if ( isNewText )
            {
                ProblemsolverTemplate importedTemplate = new ProblemsolverTemplate();
                importedTemplate.setProblemsolverName("Enter Template Name");
                importedTemplate.setProblemsolverValue(originalText);
                this.dataList.add(importedTemplate);
            }
        }
        for ( ProblemsolverTemplate element:  argDataList)
            this.dataList.add(element);

        //  set model
        this.templateModel = new TemplateModel( this.dataList );
        this.jTemplateListingComboBox.setModel( templateModel );
        boolean textHasMatch = false;
        for ( int idx = 0; idx < this.dataList.size(); idx ++ )
        {
            String elementText = this.dataList.get(idx).getProblemsolverValue();
            if (elementText == null )
                elementText = "";
            if ( elementText.equalsIgnoreCase ( originalText ) )
            {
                this.jTemplateListingComboBox.setSelectedIndex(idx);
                textHasMatch = true;
            }
        }
        if ( !textHasMatch )
            this.jTemplateListingComboBox.setSelectedIndex(0);

        //  set text area
        this.jTemplateTextArea.setWrapStyleWord(true);
        this.jTemplateTextArea.setLineWrap(true);
        this.jTemplateTextArea.setColumns(25);

        //  set spellchecker
        try
        {
            SpellChecker.register( this.jTemplateNameTextField );
            SpellChecker.register( this.jTemplateTextArea );
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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
        java.awt.GridBagConstraints gridBagConstraints;

        jBasePanel = new javax.swing.JPanel();
        jControlPanel = new javax.swing.JPanel();
        jTemplateListingComboBox = new javax.swing.JComboBox();
        jNewTemplateButton = new javax.swing.JButton();
        jTemplateNameTextField = new javax.swing.JTextField();
        jTemplateNameLabel = new javax.swing.JLabel();
        jTemplateComboBoxLabel = new javax.swing.JLabel();
        jTextAreaPanel = new javax.swing.JPanel();
        jTemplateTextScrollPane = new javax.swing.JScrollPane();
        jTemplateTextArea = new javax.swing.JTextArea();
        jSaveDeleteExitPanel = new javax.swing.JPanel();
        jExitButton = new javax.swing.JButton();
        jSaveButton = new javax.swing.JButton();
        jDeleteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jBasePanel.setLayout(new javax.swing.BoxLayout(jBasePanel, javax.swing.BoxLayout.PAGE_AXIS));

        jControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edit Template Control Panel"));

        jTemplateListingComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jTemplateListingComboBoxActionPerformed(evt);
            }
        });

        jNewTemplateButton.setText("New");
        jNewTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewTemplateButtonActionPerformed(evt);
            }
        });

        jTemplateNameLabel.setText("Template Name:");

        jTemplateComboBoxLabel.setText("Templates:");

        javax.swing.GroupLayout jControlPanelLayout = new javax.swing.GroupLayout(jControlPanel);
        jControlPanel.setLayout(jControlPanelLayout);
        jControlPanelLayout.setHorizontalGroup(
            jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jControlPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTemplateComboBoxLabel)
                    .addComponent(jTemplateNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jControlPanelLayout.createSequentialGroup()
                        .addComponent(jTemplateListingComboBox, 0, 147, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jNewTemplateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTemplateNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addContainerGap())
        );
        jControlPanelLayout.setVerticalGroup(
            jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jControlPanelLayout.createSequentialGroup()
                .addGroup(jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTemplateListingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTemplateComboBoxLabel)
                    .addComponent(jNewTemplateButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTemplateNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTemplateNameLabel)))
        );

        jBasePanel.add(jControlPanel);

        jTextAreaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter Template:  "));
        jTextAreaPanel.setLayout(new java.awt.GridBagLayout());

        jTemplateTextArea.setColumns(20);
        jTemplateTextArea.setRows(5);
        jTemplateTextScrollPane.setViewportView(jTemplateTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 345;
        gridBagConstraints.ipady = 155;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jTextAreaPanel.add(jTemplateTextScrollPane, gridBagConstraints);

        jBasePanel.add(jTextAreaPanel);

        jExitButton.setText("Exit");
        jExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jExitButtonActionPerformed(evt);
            }
        });

        jSaveButton.setText("Save");
        jSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveButtonActionPerformed(evt);
            }
        });

        jDeleteButton.setText("Delete");
        jDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSaveDeleteExitPanelLayout = new javax.swing.GroupLayout(jSaveDeleteExitPanel);
        jSaveDeleteExitPanel.setLayout(jSaveDeleteExitPanelLayout);
        jSaveDeleteExitPanelLayout.setHorizontalGroup(
            jSaveDeleteExitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSaveDeleteExitPanelLayout.createSequentialGroup()
                .addContainerGap(197, Short.MAX_VALUE)
                .addComponent(jSaveButton)
                .addGap(12, 12, 12)
                .addComponent(jDeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jSaveDeleteExitPanelLayout.setVerticalGroup(
            jSaveDeleteExitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSaveDeleteExitPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSaveDeleteExitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jExitButton)
                    .addComponent(jSaveButton)
                    .addComponent(jDeleteButton))
                .addContainerGap())
        );

        jBasePanel.add(jSaveDeleteExitPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jBasePanel, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-451)/2, (screenSize.height-374)/2, 451, 374);
    }// </editor-fold>//GEN-END:initComponents

    private void jExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jExitButtonActionPerformed
        this.myParent.getMyParent().reload();
        this.dispose();
    }//GEN-LAST:event_jExitButtonActionPerformed

    private void jNewTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewTemplateButtonActionPerformed
        this.jTemplateListingComboBox.setSelectedIndex(0);
        this.jTemplateNameTextField.setText("");
        this.jTemplateNameTextField.setEditable(true);
        this.jTemplateNameTextField.setEnabled(true);
        this.jSaveButton.setEnabled( true );
    }//GEN-LAST:event_jNewTemplateButtonActionPerformed

    private void jSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveButtonActionPerformed
        if ( this.isReadyForSave() )
        {
            boolean isNew = this.isTemplateNew();
            int selectedIndex = this.jTemplateListingComboBox.getSelectedIndex();
           

            ProblemsolverTemplate templateToSave = new ProblemsolverTemplate();
            templateToSave.setProblemsolverName( this.jTemplateNameTextField.getText() );
            templateToSave.setProblemsolverValue( this.jTemplateTextArea.getText() );
            templateToSave.setProblemSolverType( this.type );
            if ( !isNew )
                templateToSave.setProblemsolverTemplateId( this.dataList.get( selectedIndex ).getProblemsolverTemplateId());

            boolean doSave = true;
            int response = 0;
            if( !isNew )
                response = JOptionPane.showConfirmDialog(this, "Do you wish to update:  " + this.jTemplateNameTextField.getText() + "?",
                    "Update Template?", JOptionPane.YES_NO_CANCEL_OPTION);
            if ( response != 0)
                doSave = false;
            if ( doSave )
            {
                //  set cursor to wait
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                save_problemsolver_template_query query = new save_problemsolver_template_query ( templateToSave, isNew);
                
                Connection myConnection = new Connection();
                myConnection.myCompany = this.companyId + "";
                myConnection.prepQuery(query);
                System.out.println("query:  " + query.toString());
                myConnection.executeQuery(query);

                this.myParent.getMyParent().reload();

                //  reset cursor
                 Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);

                JOptionPane.showMessageDialog(this, "Save successful.", "Save Template", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jSaveButtonActionPerformed

    private void jDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeleteButtonActionPerformed
        int selectedIndex = this.jTemplateListingComboBox.getSelectedIndex();
        

        if ( this.isReadyForDeletion() )
        {
            int reponse = JOptionPane.showConfirmDialog(this, "Do you wish to delete:  " + this.jTemplateNameTextField.getText() + "?",
                "Delete Template?", JOptionPane.YES_NO_CANCEL_OPTION);
            if ( reponse == 0)
            {
                //  set cursor to wait
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                // selectedIndex = this.jTemplateListingComboBox.getS
                ProblemsolverTemplate templateToDelete = (ProblemsolverTemplate) this.jTemplateListingComboBox.getSelectedItem();

                delete_problemsolver_template_query query = new delete_problemsolver_template_query ( templateToDelete );
                Connection myConnection = new Connection();
                myConnection.myCompany = this.companyId + "";
                myConnection.prepQuery(query);
                myConnection.executeQuery(query);

                this.myParent.getMyParent().reload();

                //  reset cursor
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);

                JOptionPane.showMessageDialog(this, "Deletion successful.", "Delete Template", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            
            }
        }
    }//GEN-LAST:event_jDeleteButtonActionPerformed

    private void jTemplateListingComboBoxActionPerformed(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jTemplateListingComboBoxActionPerformed

        int selectedIndex = this.jTemplateListingComboBox.getSelectedIndex();
        if ( this.dataList.size() >= selectedIndex)
        {
            if ( selectedIndex == 0 )
                this.jTemplateNameTextField.setText("");
            else
                this.jTemplateNameTextField.setText( this.dataList.get( selectedIndex ).getProblemsolverName() );
            this.jTemplateTextArea.setText( this.dataList.get( selectedIndex ).getProblemsolverValue() );
        }
    
    }//GEN-LAST:event_jTemplateListingComboBoxActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditTemplateDiag dialog = new EditTemplateDiag(new javax.swing.JFrame(), true, null, null, 0, 0, "");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JPanel jControlPanel;
    private javax.swing.JButton jDeleteButton;
    private javax.swing.JButton jExitButton;
    private javax.swing.JButton jNewTemplateButton;
    private javax.swing.JButton jSaveButton;
    private javax.swing.JPanel jSaveDeleteExitPanel;
    private javax.swing.JLabel jTemplateComboBoxLabel;
    private javax.swing.JComboBox jTemplateListingComboBox;
    private javax.swing.JLabel jTemplateNameLabel;
    private javax.swing.JTextField jTemplateNameTextField;
    private javax.swing.JTextArea jTemplateTextArea;
    private javax.swing.JScrollPane jTemplateTextScrollPane;
    private javax.swing.JPanel jTextAreaPanel;
    // End of variables declaration//GEN-END:variables

};
