//  package declaration
package rmischedule.templates.controllers;

//  import declarations
import java.awt.Cursor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import rmischedule.data_connection.Connection;
import rmischedule.templates.models.InitializeTemplateSystem;
import rmischedule.templates.models.TemplateComboBoxModel;
import schedfoxlib.model.TemplateData;
import rmischedule.templates.view.TemplateDiagForm;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.templates.delete_template_query;
import rmischeduleserver.mysqlconnectivity.queries.templates.retrieve_template_definition_data_query;
import rmischeduleserver.mysqlconnectivity.queries.templates.retrieve_templates_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.templates.save_template_query;

/**
 * This object is a singleton controller for the template system.
 *
 * @author Jeffrey N. Davis
 * @since 03/07/2011
 */
public class TemplateSystemController {
    /*  private variable declarations   */

    private InitializeTemplateSystem initObject;
    private TemplateDiagForm diagForm;
    private Map<Integer, TemplateData> templates;
    private TemplateComboBoxModel templateModel;
    private boolean isCompanyWide;
    private boolean usesPlacesholders;
    private String templateDescription;
    /*  Singleton Code  */
    private static final TemplateSystemController INSTANCE = new TemplateSystemController();

    /**
     * Default Construction of this object
     */
    private TemplateSystemController() {
        this.initObject = null;
        this.diagForm = null;
        templates = new LinkedHashMap<Integer, TemplateData>();
        this.templateModel = new TemplateComboBoxModel(templates);
        this.isCompanyWide = false;
        this.usesPlacesholders = false;
        this.templateDescription = "";
    }

    /**
     * Returns the one true TemplateSystemController
     *
     * @returns INSTANCE
     */
    public static TemplateSystemController getInstance() {
        return INSTANCE;
    }

    /*  private method implementations  */
    /**
     * Resets all instance members for use by a new View
     *
     * @param incomingInit an instance of {@code InitializeTemplateSystem}
     * @param incomingForm an instance of {@code TemplateDiagForm}; the view
     */
    private void resetInstance(InitializeTemplateSystem incomingInit, TemplateDiagForm incomingForm) {
        /*  reset all instance variable */
        this.initObject = null;
        this.diagForm = null;
        this.templates.clear();
        this.templateModel = null;
        this.isCompanyWide = false;
        this.usesPlacesholders = false;
        this.templateDescription = null;

        this.initObject = incomingInit;
        this.diagForm = incomingForm;
    }

    /**
     * Hits the database at {@code control_db.template_definitions} to determine
     * what the system must initialize to
     */
    private void determineTemplate() {
        /*  initialize connection, record set   */
        Connection myConnection = new Connection();
        Record_Set rs = null;

        /*  construct query */
        retrieve_template_definition_data_query query = new retrieve_template_definition_data_query(this.initObject.getTemplateType());
        myConnection.prepQuery(query);

        /*  execute query   */
        try {
            rs = myConnection.executeQuery(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*  parse record set    */
        if (rs.length() > 0) {
            do {
                this.isCompanyWide = rs.getBoolean("is_company_wide");
                this.usesPlacesholders = rs.getBoolean("uses_placeholders");
                this.templateDescription = rs.getString("template_location_description");
            } while (rs.moveNext());
        } else {
            throw new RuntimeException("Template definition not found!");
        }
    }

    /**
     * Creates new objects for a new templates, and an imported template if
     * applicable
     */
    private void createStandardTemplateObjects() {
        TemplateData newTemplate = new TemplateData();
        newTemplate.setCompanyId(Integer.parseInt(this.initObject.getCompanyId()));
        newTemplate.setBranchId(Integer.parseInt(this.initObject.getBranchId()));
        newTemplate.setTemplateType(this.initObject.getTemplateType());
        newTemplate.setTemplateValue("Enter New Template Name");
        this.templates.put(newTemplate.hashCode(), newTemplate);

        if (this.initObject.getIncomingText().length() > 0) {
            TemplateData importedTemplate = new TemplateData();
            importedTemplate.setCompanyId(Integer.parseInt(this.initObject.getCompanyId()));
            importedTemplate.setBranchId(Integer.parseInt(this.initObject.getBranchId()));
            importedTemplate.setTemplateType(this.initObject.getTemplateType());
            importedTemplate.setTemplateValue("Enter Imported Template Name");
            importedTemplate.setTemplateValue(this.initObject.getIncomingText());
            this.templates.put(importedTemplate.hashCode(), importedTemplate);
        }
    }

    /**
     * Hits the database to load all associated templates into
     * {@code this.templates}
     */
    private void loadTemplates() {
        /*  initialize connection, record set   */
        Connection myConnection = new Connection();
        //  check to see if template is of administrator type
        if (!this.isAdministratorTemplate()) {
            myConnection.setCompany(this.initObject.getCompanyId());
            myConnection.setBranch(this.initObject.getBranchId());
        }
        Record_Set rs = null;

        /*  construct query */
        retrieve_templates_by_id_query retrieveQuery = new retrieve_templates_by_id_query(this.initObject.getTemplateType());
        myConnection.prepQuery(retrieveQuery);

        /*  execute query   */
        try {
            rs = myConnection.executeQuery(retrieveQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*  parse record set    */
        if (rs.length() > 0) {
            do {
                TemplateData data = new TemplateData(rs);
                templates.put(data.hashCode(), data);

            } while (rs.moveNext());
        }
    }

    /**
     * Initializes View <p>This method initializes the view by performing
     */
    private void initializeView() {
        this.diagForm.getJTemplateListingComboBox().setModel(templateModel);
        int initialIndex = (this.initObject.getIncomingText().length() > 0) ? 1 : 0;
        this.diagForm.getJTemplateListingComboBox().setSelectedIndex(initialIndex);
        this.diagForm.getJPlaceholdersButton().setVisible(this.usesPlacesholders);
        this.diagForm.setTitle(this.templateDescription);

        this.comboBoxAction();
    }

    /**
     * Ensures that template is ready to save
     *
     * @return isReadyForSave
     */
    private boolean isReadyForSave() {
        if (this.diagForm.getJTemplateNameTextField().getText().length() == 0) {
            JOptionPane.showMessageDialog(this.diagForm, "You must enter a template name before saving.",
                    "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (this.diagForm.getJTemplateNameTextField().getText().equalsIgnoreCase("Enter New Template Name")) {
            JOptionPane.showMessageDialog(this.diagForm, "You must enter a new template name before saving.",
                    "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (this.diagForm.getJTemplateNameTextField().getText().equalsIgnoreCase("Enter Imported Template Name")) {
            JOptionPane.showMessageDialog(this.diagForm, "You must enter a new template name before saving.",
                    "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (this.diagForm.getJTemplateTextArea().getText().length() == 0) {
            JOptionPane.showMessageDialog(this.diagForm, "You must enter a template before saving.",
                    "Error Saving Template", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        /*  check for altered template, first by name, then by value    */
        TemplateData elementToSave = (TemplateData) this.diagForm.getJTemplateListingComboBox().getSelectedItem();
        if (elementToSave.getTemplateName() != null) {
            if ((elementToSave.getTemplateName().equalsIgnoreCase(this.diagForm.getJTemplateNameTextField().getText()))
                    && elementToSave.getTemplateValue().equalsIgnoreCase(this.diagForm.getJTemplateTextArea().getText())) {
                JOptionPane.showMessageDialog(this.diagForm, "You cannot save an unaltered template.",
                        "Error Saving Template", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * Ensures a template is ready for deletion
     *
     * @return isReady
     */
    private boolean isReadyForDeletion() {
        TemplateData selectedElement = (TemplateData) this.diagForm.getJTemplateListingComboBox().getSelectedItem();
        int selectedIndex = this.diagForm.getJTemplateListingComboBox().getSelectedIndex();

        /*  check for new template  */
        if (selectedIndex == 0) {
            JOptionPane.showMessageDialog(this.diagForm, "Cannot delete a new template.",
                    "Delete Template?", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /*  check for imported template */
        if (selectedIndex == 1 && selectedElement.getTemplateName().equalsIgnoreCase("Enter Imported Template Name")) {
            JOptionPane.showMessageDialog(this.diagForm, "Cannot delete an imported template.",
                    "Delete Template?", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /*  check for altered template  */
        String textToCompare = this.diagForm.getJTemplateTextArea().getText();
        boolean hasMatch = false;
        Collection<TemplateData> collection = this.templates.values();
        for (TemplateData element : collection) {
            if (textToCompare.equalsIgnoreCase(element.getTemplateValue())) {
                hasMatch = true;
            }
        }
        if (!hasMatch) {
            JOptionPane.showMessageDialog(this.diagForm, "Cannot delete an altered template",
                    "Delete Template?", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Determines if the template to save is new or an update
     *
     * @return isNew true if template is new, false if not
     */
    private boolean isTemplateNew() {
        int selectedIndex = this.diagForm.getJTemplateListingComboBox().getSelectedIndex();

        /*  check to see if template if of New Template */
        if (selectedIndex == 0) {
            return true;
        }

        /*  check to see template is of imported template   */
        if (selectedIndex == 1 && this.initObject.getIncomingText().length() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Determines if the template controller is used by an Administrator System
     *
     * @return isAdministrator
     */
    private boolean isAdministratorTemplate() {
        if (this.initObject.getCompanyId().equalsIgnoreCase("-1")
                && this.initObject.getBranchId().equalsIgnoreCase("-1")) {
            return true;
        }

        return false;
    }

    /*  public controller methods   */
    /**
     * Initialize this controller to the specific instance of
     * {@code TemplateDiagForm} that called it
     *
     * @param InitializeTemplateSystem
     * @param TemplateDiag
     */
    public void init(InitializeTemplateSystem incomingInit, TemplateDiagForm incomingForm) {
        this.resetInstance(incomingInit, incomingForm);
        this.determineTemplate();
        this.createStandardTemplateObjects();
        this.loadTemplates();
        this.templateModel = new TemplateComboBoxModel(this.templates);
        this.initializeView();
    }

    /**
     * Handles New Template Action
     */
    public void newTemplateAction() {
        this.diagForm.getJTemplateListingComboBox().setSelectedIndex(0);
        this.diagForm.getJTemplateListingComboBox().revalidate();
        this.diagForm.getJTemplateListingComboBox().repaint();
        this.comboBoxAction();
    }

    /**
     * Handles Save Template Action
     */
    public void saveTemplateAction() {
        if (this.isReadyForSave()) {
            boolean isNew = this.isTemplateNew();
            boolean doSave = true;
            int response = 0;
            save_template_query query = null;
            TemplateData elementToSave = (TemplateData) this.diagForm
                    .getJTemplateListingComboBox().getSelectedItem();

            /*  construct query based on isNew  */
            if (isNew) {
                query = new save_template_query.Builder()
                        .template_type(this.initObject.getTemplateType())
                        .company_id(Integer.parseInt(this.initObject.getCompanyId()))
                        .branch_id(Integer.parseInt(this.initObject.getBranchId()))
                        .template_value(this.diagForm.getJTemplateTextArea().getText())
                        .template_name(this.diagForm.getJTemplateNameTextField().getText())
                        .Build();
            } else {
                query = new save_template_query.Builder()
                        .isNew(false)
                        .template_value(this.diagForm.getJTemplateTextArea().getText())
                        .template_name(this.diagForm.getJTemplateNameTextField().getText())
                        .template_id(elementToSave.getTemplateId())
                        .company_id(Integer.parseInt(this.initObject.getCompanyId()))
                        .branch_id(Integer.parseInt(this.initObject.getBranchId()))
                        .template_type(elementToSave.getTemplateType())
                        .Build();
            }

            /*  ensure users wishes to update   */
            if (!isNew) {
                response = JOptionPane.showConfirmDialog(this.diagForm, "Do you wish to update:  " + this.diagForm.getJTemplateNameTextField().getText() + "?",
                        "Update Template?", JOptionPane.YES_NO_CANCEL_OPTION);
                doSave = (response == 0) ? true : false;
            }

            if (doSave) {
                /* set cursor to wait  */
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                this.diagForm.setCursor(hourglassCursor);

                /*  setup connect   */
                Connection myConnection = new Connection();
                if (!this.isAdministratorTemplate()) {
                    myConnection.myCompany = this.initObject.getCompanyId();
                    myConnection.myBranch = this.initObject.getBranchId();
                }
                myConnection.prepQuery(query);

                /*  execute, report results */
                try {
                    myConnection.executeQuery(query);
                    JOptionPane.showMessageDialog(this.diagForm, "Template Save Successful.",
                            "Save Template", JOptionPane.INFORMATION_MESSAGE);
                    this.initObject.getParentTemplateController().setHasNewSave(true);
                    this.initObject.getParentTemplateController().setNewTemplateName(this.diagForm.getJTemplateNameTextField().getText());
                    this.diagForm.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this.diagForm, "An error occurred during template save.  Please contact SchedFox.",
                            "Save Template", JOptionPane.ERROR_MESSAGE);
                } finally {
                    /*  reset cursor    */
                    Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    this.diagForm.setCursor(normalCursor);
                }   //  end finally block
            }   //  end doSave if block
        }   //  end isReadyForSave if block
    }   //  end method block

    /**
     * Handles Delete Template Action
     */
    public void deleteTemplateAction() {
        if (this.isReadyForDeletion()) {
            /*  initialize connection, record set   */
            Connection myConnection = new Connection();
            if (!this.isAdministratorTemplate()) {
                myConnection.setCompany(this.initObject.getCompanyId());
                myConnection.setBranch(this.initObject.getBranchId());
            }
            Record_Set rs = null;

            /*  construct query */
            TemplateData selectedElement = (TemplateData) this.diagForm.getJTemplateListingComboBox().getSelectedItem();
            delete_template_query retrieveQuery = new delete_template_query(selectedElement.getTemplateId());
            myConnection.prepQuery(retrieveQuery);

            /*  ensure user wishes to delete template   */
            int response = JOptionPane.showConfirmDialog(this.diagForm, "Do you wish to delete:  " + this.diagForm.getJTemplateNameTextField().getText() + "?",
                    "Delete Template?", JOptionPane.YES_NO_CANCEL_OPTION);
            boolean doDelete = (response == 0) ? true : false;

            /*  execute query   */
            if (doDelete) {
                /* set cursor to wait  */
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                this.diagForm.setCursor(hourglassCursor);

                try {
                    rs = myConnection.executeQuery(retrieveQuery);
                    JOptionPane.showMessageDialog(this.diagForm, "Template Deletion Successful.",
                            "Delete Template", JOptionPane.INFORMATION_MESSAGE);
                    this.init(this.initObject, this.diagForm);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this.diagForm, "An error occurred during template deletion.  Please contact SchedFox.",
                            "Delete Template", JOptionPane.ERROR_MESSAGE);
                } finally {
                    /*  reset cursor    */
                    Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    this.diagForm.setCursor(normalCursor);
                }   //  end finally
            }   //  end doDelete if
        }   //  end if
    }   //  end method

    /**
     * Handles Placeholder Action
     */
    public void placeholderAction() {
        JOptionPane.showMessageDialog(this.diagForm, "Placeholders are still in development.",
                "In Development", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles ComboBox Action
     *
     * @param selectedIndex
     */
    public void comboBoxAction() {
        TemplateData selectedElement = (TemplateData) this.diagForm.getJTemplateListingComboBox().getSelectedItem();
        this.diagForm.getJTemplateNameTextField().setText(selectedElement.getTemplateName());
        this.diagForm.getJTemplateTextArea().setText(selectedElement.getTemplateValue());
    }
};
