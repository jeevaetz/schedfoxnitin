/*
 * Client_Ids.java
 * Created cause Jim wanted a centralized area to see all id's associated w/ a
 * client.
 * Created on Jan 24, 2011, 1:23:32 PM
 */
package rmischedule.client.components;

import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.NameUtils;
import com.creamtec.ajaxswing.support.ComponentUtils;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rmischedule.components.ImagePreview;
import rmischedule.components.PicturePanel;
import rmischedule.components.PictureParentInterface;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;
import rmischedule.data_connection.Connection;
import rmischedule.employee.login.EmployeeLogin;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientExport;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.client_export_usked_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_login_information_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_save_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_worksite_list_query;
import rmischeduleserver.mysqlconnectivity.queries.client.get_client_training_ids_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_training_ids_query;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;
import schedfoxlib.model.util.ImageLoader;

/**
 *
 * @author user
 */
public class Client_Ids extends GenericEditSubForm implements PictureParentInterface {

    private PicturePanel clientImage;
    private JFileChooser fchooser;
    private ArrayList<Client_Worksite_Item> worksiteItems;

    /** Creates new form Client_Ids */
    public Client_Ids() {
        initComponents();

        worksiteItems = new ArrayList<Client_Worksite_Item>();
        this.clientImage = new rmischedule.components.PicturePanel(this);
        picturePanel.add(this.clientImage);

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

    public void setLoginName(String name) {
        if (!loginTxt.getText().trim().equals("")) {
            return;
        }

        StringBuilder inName = new StringBuilder(name);
        StringBuilder outName = new StringBuilder(inName.length());
        int x = 0;

        for (int i = 0; i < inName.length(); i++) {
            if (x > 24) {
                break;
            }
            if (inName.charAt(i) != ' ') {
                outName.append(inName.charAt(i));
                x++;
            }
        }
        loginTxt.setText(outName.substring(0, x));
        passwordTxt.setText(outName.substring(0, x));
    }

    private void loadLoginInformation() {
        client_login_information_query loginQuery = new client_login_information_query();
        try {
            loginQuery.update(Integer.parseInt(myparent.getMyIdForSave()));
            Connection myConn = myparent.getConnection();
            Record_Set rs = myConn.executeQuery(loginQuery);
            try {
                if (!rs.getString("url").equalsIgnoreCase("0")) {
                    urlTxt.setText(rs.getString("url"));
                }
            } catch (Exception e) {
                urlTxt.setText("");
            }
            try {
                if (!rs.getString("cusername").equalsIgnoreCase("0")) {
                    loginTxt.setText(rs.getString("cusername"));
                }
            } catch (Exception e) {
                loginTxt.setText("");
            }
            try {
                if (!rs.getString("cpassword").equalsIgnoreCase("0")) {
                    passwordTxt.setText(rs.getString("cpassword"));
                }
            } catch (Exception e) {
                passwordTxt.setText("");
            }
        } catch (Exception e) {
            System.out.println("Problem loading Client Login Information in ID tab.");
            e.printStackTrace();
            urlTxt.setText("");
            loginTxt.setText("");
            passwordTxt.setText("");
        }
    }

    /**
     * Loads the training information into the panel.
     */
    private void loadTrainingInformation() {
        get_client_training_ids_query idsQuery = new get_client_training_ids_query();
        idsQuery.setPreparedStatement(new Object[]{Integer.parseInt(myparent.getMyIdForSave())});
        Connection myConn = myparent.getConnection();
        Record_Set rst = myConn.executeQuery(idsQuery);
        corporationIDTxt.setText(rst.getString("corporation_id"));
        groupIDTxt.setText(rst.getString("group_id"));
        myIDTxt.setText(rst.getString("id"));

        if (((Client) myparent.getSelectedObject()).getClientWorksite() == 0) {
            corporationIDTxt.setVisible(true);
            corporationLabel.setVisible(true);
            corporationIDTxt.setEditable(true);
            groupIDTxt.setVisible(true);
            groupLabel.setVisible(true);
            groupIDTxt.setEditable(true);
            myIDTxt.setVisible(false);
            idLabel.setVisible(false);
        } else {
            corporationIDTxt.setEditable(false);
            groupIDTxt.setEditable(false);
            myIDTxt.setVisible(true);
            idLabel.setVisible(true);
        }
    }

    private void loadWorksiteInformation() {
        try {
            client_worksite_list_query myQuery = new client_worksite_list_query();
            myQuery.setPreparedStatement(
                    new Object[]{Integer.parseInt(myparent.getMyIdForSave())});
            Connection myConn = myparent.getConnection();
            Record_Set rs = myConn.executeQuery(myQuery);

            worksitePanel.removeAll();
            worksiteItems.clear();
            int i = 0;
            for (i = 0; i < rs.length(); i++) {
                Client_Worksite_Item wci = new Client_Worksite_Item(i, this);
                if (rs.getInt("client_worksite_order") > 0) {
                    wci.update(
                            i + 1, rs.getString("client_id"), rs.getString("client_name"),
                            rs.getString("rate_code_id"));
                } else {
                    wci.update(
                            i + 1, rs.getString("client_id"), rs.getString("client_name"),
                            rs.getString("rate_code_id"));
                }
                worksiteItems.add(wci);
                worksitePanel.add(wci);
                rs.moveNext();
            }
            Client_Worksite_Item wci = new Client_Worksite_Item(i + 1, this);
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                ComponentUtils.getHtmlAttributes(wci).put(
                        "onClick",
                        "document.getElementById('" + NameUtils.getComponentName(wci.getEfName()) + "').focus()");
            }
            worksiteItems.add(wci);
            worksitePanel.add(wci);
            worksitePanel.revalidate();
            worksiteParentPanel.repaint();
        } catch (Exception e) {
            System.out.println("Could not load worksites.");
        }
    }

    /**
     * Loads usked information, display only in this tab.
     */
    private void loadUskedInformation() {
        try {
            ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
            ClientExport export = clientController.getClientExport(Integer.parseInt(myparent.getMyIdForSave()));

            UltraIDTextField.setText(export.getUskedWsId());
            UltraParentID.setText(export.getUskedCliId());
            if (UltraIDTextField.getText().length() == 0) {
                UltraIDTextField.setText(UltraParentID.getText());
                UltraParentID.setText("");
            }
        } catch (Exception e) {
            System.out.println("Problem loading Usked Information in ID tab.");
            e.printStackTrace();
            UltraIDTextField.setText("");
            UltraParentID.setText("");
        }
    }

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        return new GenericQuery("Select NOW();");
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        try {
            String uskedId = getUskedClientId();

            String groupId = groupIDTxt.getText().trim();
            if (groupId.trim().length() == 0 && uskedId != null) {
                groupId = uskedId;
            }

            String url = urlTxt.getText();
            if (url.trim().length() == 0 && uskedId != null) {
                url = uskedId.replaceAll(" ", "_").toLowerCase();
            }

            String login = loginTxt.getText();
            if (login.trim().length() == 0 && uskedId != null) {
                login = uskedId.replaceAll(" ", "_").toLowerCase();
            }

            String pass = passwordTxt.getText();
            if (pass.trim().length() == 0 && uskedId != null) {
                pass = uskedId.replaceAll(" ", "_").toLowerCase();
            }

            try {
                ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
                ArrayList<Client> clients = clientController.getClientsByLogin(login, pass, Integer.parseInt(myparent.getConnection().myBranch));
                if (clients.size() > 0) {
                    StringBuilder message = new StringBuilder();
                    message.append(clients.size()).append(" other clients in this branch use that same username and password. \r\n");
                    message.append("By using this password the following clients will be visible on the schedule when the client logs in: \r\n\r\n");

                    for (int c = 0; c < clients.size(); c++) {
                        message.append(clients.get(c).getClientName()).append("\r\n");
                    }

                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication, message.toString(),
                            "Login already taken!", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception exe) {
                
            }

            save_client_query saveLogin = new save_client_query();
            save_client_training_ids_query saveQuery = new save_client_training_ids_query();

            saveQuery.setPreparedStatement(new Object[]{
                        Integer.parseInt(myparent.getMyIdForSave()),
                        corporationIDTxt.getText().trim(),
                        groupId,
                        myIDTxt.getText().trim()
                    });
            myparent.getConnection().executeQuery(saveQuery);

            try {
                saveLogin.update(myparent.getMyIdForSave(), url, login, pass);
                myparent.getConnection().executeQuery(saveLogin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericQuery("Select NOW();");
    }

    public String getUskedClientId() {
        client_export_usked_query mySelectQuery = new client_export_usked_query();
        mySelectQuery.setPreparedStatement(new Object[]{Integer.parseInt(myparent.getMyIdForSave())});
        Connection myConn = myparent.getConnection();
        Record_Set rst = myConn.executeQuery(mySelectQuery);
        String value = rst.getString("usked_id");
        if (value == null || value.equals("0") || value.trim().length() == 0) {
            try {
                Client myClient = (Client)myparent.getSelectedObject();
                ClientController myController = ClientController.getInstance(myparent.getConnection().myCompany);
                value = myController.generateNewUskedId(myClient);
            } catch (Exception e) {
                value = null;
            }
        }
        return value;
    }

    @Override
    public void loadData(Record_Set rs) {
        this.loadTrainingInformation();
        this.loadUskedInformation();
        this.loadLoginInformation();
        this.showClientBanner();
        this.loadWorksiteInformation();
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "ID's";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
        corporationIDTxt.setText("");
        groupIDTxt.setText("");
        myIDTxt.setText("");
        loginTxt.setText("");
        passwordTxt.setText("");
        urlTxt.setText("");
        worksitePanel.removeAll();
    }

    @Override
    public boolean userHasAccess() {
        return true;
    }

    public void addWorksite(
            String wsid, String workSite, int order,
            String rate_code, String isDeleted) {
        String newClient = workSite;
        String id = myparent.getMyIdForSave();
        client_save_query mySaveQuery = new client_save_query();
        if (wsid.equals("0")) {
            wsid = "(SELECT CASE WHEN (SELECT (MAX(client_id) + 1) From client) IS NULL THEN 1 ELSE (SELECT (MAX(client_id) + 1) From client) END)";

        }
        mySaveQuery.update(
                wsid, newClient, "", "", "", "", "", "", "", "", "", "", "", isDeleted, "", "", "", "", "", "",
                myparent.getMyIdForSave(), "", "false", String.valueOf(order), rate_code);

        myparent.getConnection().prepQuery(mySaveQuery);

        try {
            myparent.getConnection().executeUpdate(mySaveQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myparent.getData();
        myparent.setSelected(id);
        try {
            ((GenericTabbedEditForm) myparent).setSelectedTab(this);
        } catch (Exception e) {
        }
    }

    public void editImage(PicturePanel panel) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Image image = ImageIO.read(fchooser.getSelectedFile());
                Image scaledImage = image.getScaledInstance(EmployeeLogin.headerWidth, EmployeeLogin.headerHeight, Image.SCALE_SMOOTH);

                try {
                    ImageIcon newIcon = new ImageIcon(scaledImage);
                    int client_id = Integer.parseInt(myparent.getMyIdForSave());

                    Company comp = Main_Window.parentOfApplication.getCompanyById(myparent.getConnection().myCompany);
                    ImageLoader.saveImage("client_header_" + client_id + ".jpg",
                            comp.getDB(), "general", newIcon);

                    this.clientImage.setImage(newIcon.getImage());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "This image type is not allowed please use a different image, or "
                        + "use an image editing software to save as a standard JPEG image.",
                        "Error loading image!", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * Treaded fetch images....
     */
    private void showClientBanner() {
        Runnable fetchClientImage = new Runnable() {

            public void run() {
                try {
                    Company comp = Main_Window.parentOfApplication.getCompanyById(myparent.getConnection().myCompany);
                    Client myClient = (Client) myparent.getSelectedObject();
                    final ImageIcon image = ImageLoader.getImage("client_header_"
                            + myClient.getClientId() + ".jpg",
                            comp.getDB(), "general");
                    Runnable setImage = new Runnable() {

                        public void run() {
                            if (image != null) {
                                clientImage.setImage(image.getImage());
                            } else {
                                clientImage.setImage(null);
                            }
                        }
                    };
                    SwingUtilities.invokeLater(setImage);
                } catch (Exception e) {
                }
            }
        };
        new Thread(fetchClientImage).start();
    }

    public void addImage(PicturePanel panel) {
    }

    public void deleteImage(PicturePanel panel) {
    }

    public void downloadImage(PicturePanel panel) {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainScrollPane = new javax.swing.JScrollPane();
        mainContentPane = new javax.swing.JPanel();
        proprofsPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        trainingIDPanel = new javax.swing.JPanel();
        corporationLabel = new javax.swing.JLabel();
        corporationIDTxt = new javax.swing.JTextField();
        groupLabel = new javax.swing.JLabel();
        groupIDTxt = new javax.swing.JTextField();
        idLabel = new javax.swing.JLabel();
        myIDTxt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        clientLoginPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        loginNamePanel = new javax.swing.JPanel();
        lbName = new javax.swing.JLabel();
        loginTxt = new javax.swing.JTextField();
        passwordPanel = new javax.swing.JPanel();
        lbName1 = new javax.swing.JLabel();
        passwordTxt = new javax.swing.JTextField();
        urlPanel = new javax.swing.JPanel();
        lbName2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        urlTxt = new javax.swing.JTextField();
        picturePanel = new javax.swing.JPanel();
        worksiteParentPanel = new javax.swing.JPanel();
        worksitePanel = new javax.swing.JPanel();
        uskedPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        UltraIDTextField = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        UltraParentID = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainContentPane.setMaximumSize(new java.awt.Dimension(200, 2520));
        mainContentPane.setMinimumSize(new java.awt.Dimension(100, 431));
        mainContentPane.setPreferredSize(new java.awt.Dimension(150, 600));
        mainContentPane.setLayout(new javax.swing.BoxLayout(mainContentPane, javax.swing.BoxLayout.Y_AXIS));

        proprofsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Proprofs Integration"));
        proprofsPanel.setMaximumSize(new java.awt.Dimension(20000, 150));
        proprofsPanel.setMinimumSize(new java.awt.Dimension(100, 130));
        proprofsPanel.setPreferredSize(new java.awt.Dimension(100, 135));
        proprofsPanel.setLayout(new javax.swing.BoxLayout(proprofsPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jLabel4.setFont(new java.awt.Font("sansserif", 0, 11)); // NOI18N
        jLabel4.setText("<html>Use these values to link to the pro prof system. Place the value in the beginning of the name of the test on proprof ie: Corporation ID = MyCorp then test name of [MyCorp] would go to all clients with a Corporation ID of MyCorp, this can be used to display the same test across multiple clients. </html>");
        jPanel8.add(jLabel4);

        proprofsPanel.add(jPanel8);

        trainingIDPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        trainingIDPanel.setMinimumSize(new java.awt.Dimension(10, 30));
        trainingIDPanel.setPreferredSize(new java.awt.Dimension(10, 30));
        trainingIDPanel.setLayout(new javax.swing.BoxLayout(trainingIDPanel, javax.swing.BoxLayout.LINE_AXIS));

        corporationLabel.setText("Corporation ID:");
        corporationLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 3));
        trainingIDPanel.add(corporationLabel);

        corporationIDTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        trainingIDPanel.add(corporationIDTxt);

        groupLabel.setText("Group ID:");
        groupLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 3));
        trainingIDPanel.add(groupLabel);

        groupIDTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        trainingIDPanel.add(groupIDTxt);

        idLabel.setText("Location ID:");
        idLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 3));
        trainingIDPanel.add(idLabel);

        myIDTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        trainingIDPanel.add(myIDTxt);

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 10));
        trainingIDPanel.add(jPanel4);

        proprofsPanel.add(trainingIDPanel);

        mainContentPane.add(proprofsPanel);

        clientLoginPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Login"));
        clientLoginPanel.setMaximumSize(new java.awt.Dimension(50028, 2000));
        clientLoginPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 11)); // NOI18N
        jLabel5.setText("<html>This is the information used for the client to view their schedule. The URL will display the Login Image set using the tab. The Client can login with the information below. PLEASE MAKE SURE NOT TO USE DUPLICATE LOGINS!</html>");
        jLabel5.setMaximumSize(new java.awt.Dimension(50000, 16));
        jPanel9.add(jLabel5);

        jPanel7.add(jPanel9);

        loginNamePanel.setMaximumSize(new java.awt.Dimension(50000, 30));
        loginNamePanel.setLayout(new javax.swing.BoxLayout(loginNamePanel, javax.swing.BoxLayout.LINE_AXIS));

        lbName.setText("Login Name:");
        lbName.setMaximumSize(new java.awt.Dimension(90, 14));
        lbName.setMinimumSize(new java.awt.Dimension(90, 14));
        lbName.setPreferredSize(new java.awt.Dimension(90, 14));
        loginNamePanel.add(lbName);

        loginTxt.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        loginNamePanel.add(loginTxt);

        jPanel7.add(loginNamePanel);

        passwordPanel.setMaximumSize(new java.awt.Dimension(50000, 30));
        passwordPanel.setLayout(new javax.swing.BoxLayout(passwordPanel, javax.swing.BoxLayout.LINE_AXIS));

        lbName1.setText("Password:");
        lbName1.setMaximumSize(new java.awt.Dimension(90, 14));
        lbName1.setMinimumSize(new java.awt.Dimension(90, 14));
        lbName1.setPreferredSize(new java.awt.Dimension(90, 14));
        passwordPanel.add(lbName1);

        passwordTxt.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        passwordPanel.add(passwordTxt);

        jPanel7.add(passwordPanel);

        urlPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        urlPanel.setLayout(new javax.swing.BoxLayout(urlPanel, javax.swing.BoxLayout.LINE_AXIS));

        lbName2.setText("URL:");
        lbName2.setMaximumSize(new java.awt.Dimension(90, 14));
        lbName2.setMinimumSize(new java.awt.Dimension(90, 14));
        lbName2.setPreferredSize(new java.awt.Dimension(90, 14));
        urlPanel.add(lbName2);

        jLabel3.setText("http://www.champsecurity.com/client/");
        urlPanel.add(jLabel3);

        urlTxt.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        urlPanel.add(urlTxt);

        jPanel7.add(urlPanel);

        jTabbedPane1.addTab("Login Info", jPanel7);

        picturePanel.setLayout(new java.awt.GridLayout(1, 0));
        jTabbedPane1.addTab("Login Image", picturePanel);

        clientLoginPanel.add(jTabbedPane1);

        mainContentPane.add(clientLoginPanel);

        worksiteParentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Worksites"));
        worksiteParentPanel.setMaximumSize(new java.awt.Dimension(50000, 300));
        worksiteParentPanel.setMinimumSize(new java.awt.Dimension(28, 200));
        worksiteParentPanel.setPreferredSize(new java.awt.Dimension(100, 240));
        worksiteParentPanel.setLayout(new java.awt.GridLayout(1, 0));

        worksitePanel.setLayout(new javax.swing.BoxLayout(worksitePanel, javax.swing.BoxLayout.Y_AXIS));
        worksiteParentPanel.add(worksitePanel);

        mainContentPane.add(worksiteParentPanel);

        uskedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Usked Integration"));
        uskedPanel.setMaximumSize(new java.awt.Dimension(10248, 70));
        uskedPanel.setMinimumSize(new java.awt.Dimension(100, 80));
        uskedPanel.setPreferredSize(new java.awt.Dimension(100, 85));
        uskedPanel.setLayout(new java.awt.GridLayout(2, 0));

        jPanel5.setMinimumSize(new java.awt.Dimension(126, 28));
        jPanel5.setPreferredSize(new java.awt.Dimension(220, 28));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        jLabel1.setText("Individual Account USKED");
        jLabel1.setMaximumSize(new java.awt.Dimension(220, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(220, 22));
        jLabel1.setPreferredSize(new java.awt.Dimension(220, 22));
        jPanel5.add(jLabel1);

        UltraIDTextField.setMaximumSize(new java.awt.Dimension(10000, 20));
        jPanel5.add(UltraIDTextField);

        uskedPanel.add(jPanel5);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        jLabel2.setText("Joint Corporate Account");
        jLabel2.setMaximumSize(new java.awt.Dimension(220, 22));
        jLabel2.setMinimumSize(new java.awt.Dimension(220, 22));
        jLabel2.setPreferredSize(new java.awt.Dimension(220, 22));
        jPanel6.add(jLabel2);

        UltraParentID.setMaximumSize(new java.awt.Dimension(10000, 20));
        jPanel6.add(UltraParentID);

        uskedPanel.add(jPanel6);

        mainContentPane.add(uskedPanel);

        mainScrollPane.setViewportView(mainContentPane);

        add(mainScrollPane);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel UltraIDTextField;
    private javax.swing.JLabel UltraParentID;
    private javax.swing.JPanel clientLoginPanel;
    private javax.swing.JTextField corporationIDTxt;
    private javax.swing.JLabel corporationLabel;
    private javax.swing.JTextField groupIDTxt;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbName1;
    private javax.swing.JLabel lbName2;
    private javax.swing.JPanel loginNamePanel;
    private javax.swing.JTextField loginTxt;
    private javax.swing.JPanel mainContentPane;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTextField myIDTxt;
    private javax.swing.JPanel passwordPanel;
    private javax.swing.JTextField passwordTxt;
    private javax.swing.JPanel picturePanel;
    private javax.swing.JPanel proprofsPanel;
    private javax.swing.JPanel trainingIDPanel;
    private javax.swing.JPanel urlPanel;
    private javax.swing.JTextField urlTxt;
    private javax.swing.JPanel uskedPanel;
    private javax.swing.JPanel worksitePanel;
    private javax.swing.JPanel worksiteParentPanel;
    // End of variables declaration//GEN-END:variables
}
