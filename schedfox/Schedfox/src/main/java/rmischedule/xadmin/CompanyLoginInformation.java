/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CompanyInformation.java
 *
 * Created on Aug 4, 2010, 1:56:44 PM
 */
package rmischedule.xadmin;

import java.awt.Image;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rmischedule.components.ImagePreview;
import rmischedule.components.PictureParentInterface;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.data_connection.Connection;
import rmischedule.employee.login.EmployeeLogin;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.ImageLoader;
import schedfoxlib.model.CompanyInformationObj;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.save_company_url_information_query;

/**
 *
 * @author user
 */
public class CompanyLoginInformation extends GenericEditSubForm implements PictureParentInterface {

    public static String EMP_VIEW_BANNED = "View Banned Tab";
    public static String EMP_VIEW_BRANCH = "View Branch Tab";
    public static String EMP_VIEW_CERTIFICATIONS = "View Certification Tab";
    public static String EMP_EXPORT = "View Export Tab";
    public static String EMP_LOGIN = "View Login Tab";
    public static String EMP_STATE = "View State Certification Tab";
    public static String EMP_TRAINED = "View Trained Tab";
    public static String EMP_DYNAMIC_FIELDS = "View Dynamic Field Tab";
    public static String CALL_QUEUE_DM_ROTATION = "Days for DM Rotation";
    public static String CALL_QUEUE_CORP_ROTATION = "Days for Corp Rotation";
    public static String WEEKS_IN_PAST_TO_LOAD_FOR_EMP = "Weeks to view in past";
    public static String DAY_OF_WEEK_WHERE_NEXT_WEEK_VISIBLE = "Day where next week is available";
    public static String TIME_DAY_OF_WEEK_WHERE_NEXT_WEEK_VISIBLE = "Hour of day next week is available";

    private xCompanyEdit myParent;
    private rmischedule.components.PicturePanel clientHeader;
    private JFileChooser fchooser;
    private int numberOfRecordsets = 0;

    /** Creates new form CompanyInformation */
    public CompanyLoginInformation(xCompanyEdit myParent) {
        this.myParent = myParent;
        initComponents();

        clientHeader = new rmischedule.components.PicturePanel(this);
        imagePanel.add(clientHeader);

        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select an image for this company header");
        this.fchooser.setAcceptAllFileFilterUsed(false);
        this.fchooser.setMultiSelectionEnabled(false);

        //Add the preview pane.
        this.fchooser.setAccessory(new ImagePreview(this.fchooser));

        this.fchooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Image (JPEG, JPG)";
            }
        });
    }

    /**
     * Returns the string header of the tab.
     * @return String
     */
    public String getMyTabTitle() {
        return "Employee Login";
    }

    public String validateInfo() {
        String retVal = "";
        String url = urlTxt.getText().trim();
        Pattern myPattern = Pattern.compile("[a-zA-Z0-9]{0,49}");
        Matcher myMatcher = myPattern.matcher(url);
        if (!myMatcher.matches()) {
            if (url.length() > 50) {
                retVal = "The text for the url is too long, please use a shorter value1";
            } else {
                retVal = "You are using invalid text, please do not use anything other than letters or numbers";
            }
        }
        return retVal;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        imagePanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        urlTxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        clientOptionsPanel = new javax.swing.JTabbedPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Logins"));
        jPanel2.setMaximumSize(new java.awt.Dimension(32783, 160));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        imagePanel.setMaximumSize(new java.awt.Dimension(6300, 100));
        imagePanel.setMinimumSize(new java.awt.Dimension(16, 100));
        imagePanel.setPreferredSize(new java.awt.Dimension(16, 100));
        imagePanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel2.add(imagePanel);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(498, 30));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("You may access your employee site at:   ");
        jPanel1.add(jLabel1);

        jLabel2.setText("www.schedfox.com/");
        jPanel1.add(jLabel2);

        urlTxt.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jPanel1.add(urlTxt);

        jPanel4.add(jPanel1);

        jPanel2.add(jPanel4);

        add(jPanel2);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));
        jPanel3.add(clientOptionsPanel);

        add(jPanel3);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane clientOptionsPanel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField urlTxt;
    // End of variables declaration//GEN-END:variables

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        get_company_query companyInfo = new get_company_query();
        RunQueriesEx myQuery = new RunQueriesEx();
        try {
            companyInfo.update(Integer.parseInt(myParent.getMyIdForSave()));
        } catch (Exception e) {
            companyInfo.update(0);
        }
        myQuery.add(companyInfo);
        return myQuery;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        save_company_url_information_query myQuery = new save_company_url_information_query();
        String errors = this.validateInfo();
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, errors, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String url = urlTxt.getText().trim();
                myQuery.update(Integer.parseInt(myParent.getMyIdForSave()), url);
            } catch (Exception e) {
                myQuery.update(0, "");
            }
        }
        return myQuery;
    }

    @Override
    public void loadData(Record_Set rs) {
        if (!rs.getString("company_url").equals("0")) {
            this.urlTxt.setText(rs.getString("company_url"));
        }
        this.clientHeader.setImage(null);
        int company_id = Integer.parseInt(myParent.getMyIdForSave());
        get_company_query compQuery = new get_company_query();
        compQuery.update(company_id);
        Connection myConn = new Connection();
        Record_Set compRecord = myConn.executeQuery(compQuery);
        Company comp = null;
        for (int c = 0; c < compRecord.length(); c++) {
            comp = new Company(compRecord);
        }
        try {
            this.clientHeader.setImage(ImageLoader.getImage("login_header.jpg", comp.getDB(), "general").getImage());
        } catch (Exception npe) {
        }

        Hashtable<String, Vector<CompanyInformationObj>> hashOfOptions =
                Main_Window.parentOfApplication.getCompanyViewOptions(myParent.getMyIdForSave());
        Iterator<String> keys = hashOfOptions.keySet().iterator();
        clientOptionsPanel.removeAll();
        while (keys.hasNext()) {
            String key = keys.next();
            CompanyViewOptions myOptions = new CompanyViewOptions(hashOfOptions.get(key));
            clientOptionsPanel.addTab(key, myOptions);
        }
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
    }

    @Override
    public boolean userHasAccess() {
        return true;
    }

    public void editImage(rmischedule.components.PicturePanel panel) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                int company_id = Integer.parseInt(myParent.getMyIdForSave());
                Image image = ImageIO.read(fchooser.getSelectedFile());
                //Image scaledImage = image.getScaledInstance(EmployeeLogin.headerWidth, EmployeeLogin.headerHeight, Image.SCALE_SMOOTH);

                try {
                    ImageIcon newIcon = new ImageIcon(image);
                    get_company_query compQuery = new get_company_query();
                    compQuery.update(company_id);
                    Connection myConn = new Connection();
                    Record_Set compRecord = myConn.executeQuery(compQuery);
                    Company comp = null;
                    for (int c = 0; c <
                            compRecord.length(); c++) {
                        comp = new Company(compRecord);
                    }

                    if (comp == null) {
                        throw new Exception("Problem locating information for company!");
                    }

                    ImageLoader.saveImage("login_header.jpg", comp.getDB(), "general", newIcon);

                    this.clientHeader.setImage(newIcon.getImage());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "This image type is not allowed please use a different image, or " +
                        "use an image editing software to save as a standard JPEG image.",
                        "Error loading image!", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void addImage(rmischedule.components.PicturePanel panel) {
    }

    public void deleteImage(rmischedule.components.PicturePanel panel) {
    }

    public void downloadImage(rmischedule.components.PicturePanel panel) {
        
    }
}
