/*
 * Login_Frame.java
 *
 * Created on July 26, 2004, 9:22 AM
 */
package rmischedule.login;

import schedfoxlib.model.util.Record_Set;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rmischedule.main.*;
import rmischedule.data_connection.Connection;
import rmischeduleserver.control.UserController;
//import rmischedule.data_connection.Connection1;
import rmischeduleserver.mysqlconnectivity.queries.login.*;
import schedfoxlib.controller.exceptions.NoUserException;
import schedfoxlib.model.User;

/**
 *
 * @author jason.allen
 */
public class Login_Frame extends JInternalFrame {

    private Main_Window parent;
    private JTextField efUser;
    private JPasswordField efPwd;
    private Container content;
    public Login_Form lf;
    //layers
    public JLayeredPane myLayeredPane;
    public JPanel controlContainer;
    public JPanel glassLayer;
    public Begin_Logon bl;
    private String[] loginFromCommand;

    public Login_Frame(Main_Window Parent, String[] login) {
        parent = Parent;
        loginFromCommand = login;
        bl = new Begin_Logon(parent, this);

        content = getContentPane();
        content.setLayout(new BorderLayout());

        lf = new Login_Form(this);

        efUser = lf.efUser;
        efPwd = lf.efPassword;

        setTitle(Main_Window.compBranding.getLoginWindowText());

        /*
         * create our layered panes for drag and drop
         */
        myLayeredPane = new JLayeredPane();
        myLayeredPane.setLayout(null);

        controlContainer = new JPanel();
        controlContainer.setLayout(new BorderLayout());

        JLabel tmp = new JLabel();
        tmp.setIcon(Main_Window.Load_Image);


        controlContainer.add(tmp, BorderLayout.CENTER);

        glassLayer = new JPanel();
        glassLayer.setLayout(null);
        glassLayer.setOpaque(false);

        glassLayer.add(lf);
        lf.setBounds(365, 5, 255, 140);

        myLayeredPane.add(controlContainer, JLayeredPane.DEFAULT_LAYER);
        myLayeredPane.add(glassLayer, JLayeredPane.DRAG_LAYER);

        content.add(myLayeredPane, BorderLayout.CENTER);

        //setSize(631,174);
        setSize(640, 180);
        setLocation(((parent.getWidth() / 2) - 350), ((parent.getHeight() / 2) - 130));
        if (loginFromCommand.length > 0) {
            if (loginFromCommand[0].equals("demo")) {
                goLogin();
                return;
            }
//            else {
//                ChangePasswordDialog myChangeDialog = new ChangePasswordDialog(Main_Window.parentOfApplication, true, loginFromCommand[0], loginFromCommand[1]);
//                myChangeDialog.setVisible(true);
//            }
        }

        /*
         * set size of the layers
         */
        controlContainer.setSize(631, 154);
        glassLayer.setSize(631, 154);

        lf.pbLogin.addActionListener(new Login_Button_Action());
        lf.pbLogin.setBackground(new java.awt.Color(138, 50, 26, 200));

        efUser.setBackground(new java.awt.Color(255, 255, 255));
        efPwd.setBackground(new java.awt.Color(255, 255, 255));
        
        try {
            this.setSelected(true);
        } catch (Exception ex) {
        }
        this.efUser.requestFocusInWindow();
    }

    public static void goLogin(final String userName, final String password, Connection connection, final String[] loginFromCommand, final Login_Frame frame) {
        if (userName.length() <= 0 && loginFromCommand[0].length() <= 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Please input in a user name to login", "Login error!", JOptionPane.ERROR_MESSAGE);
        } else {
            Thread tr = new Thread() {

                @Override
                public void run() {
                    String realUserName = "";
                    String realPassword = "";
                    if (loginFromCommand.length > 0 && loginFromCommand[0].equals("demo")) {
                        realUserName = "demo";
                        realPassword = "demo";
                    } else {
                        realUserName = userName;
                        realPassword = password.trim();
                    }

                    User user = null;
                    boolean validLogin = false;
                    Begin_Logon innerBl = new Begin_Logon(Main_Window.parentOfApplication, frame);
                    try {
                        UserController userController = new UserController("");
                        user = userController.getUserByLogin(realUserName, realPassword);
                        Main_Window.parentOfApplication.setManagementId(user.getUserManagementId() + "");
                        if (frame != null) {
                            frame.setVisible(false);
                        }
                        innerBl.lw.setVisible(true);
                        innerBl.start();
                        if (frame != null) {
                            frame.dispose();
                        }
                        Main_Window.parentOfApplication.setUser(user.getUserId().toString(), "");
                        innerBl.stop = true;
                        validLogin = true;
                    } catch (NoUserException nue) {
                        validLogin = employeeLogin(realUserName, realPassword);
                        if (validLogin) {
                            if (frame != null) {
                                frame.setVisible(false);
                            }
                            innerBl.lw.setVisible(true);
                            innerBl.start();
                            if (frame != null) {
                                frame.dispose();
                            }
                            innerBl.stop = true;
                        }
                    } catch (Exception e) {
                    }
                    if (!validLogin) {
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Invalid Login!", "Invalid Login!", JOptionPane.ERROR_MESSAGE);
                    }

                }
            };
            tr.start();
        }
    }

    private static boolean employeeLogin(String userName, String passWord) {
        boolean retVal = false;
        String[] values = userName.split("-");
        Main_Window.parentOfApplication.setManagementId("-1");
        if (values.length > 1) {
            Connection myConn = new Connection();
            fallback_to_employee_login_query myEmployeeLoginQuery = new fallback_to_employee_login_query();
            myEmployeeLoginQuery.update(values[0]);

            Record_Set rs = myConn.executeQuery(myEmployeeLoginQuery);

            try {
                String companyDb = rs.getString("company_db");
                if (companyDb != null && companyDb.length() > 0) {
                    login_as_employee_query loginQuery = new login_as_employee_query();
                    loginQuery.update(companyDb, values[1], passWord);
                    Record_Set empLogin = myConn.executeQuery(loginQuery);
                    if (empLogin.getString("employee_id").length() > 0) {
                        Main_Window.parentOfApplication.setUser(empLogin.getString("employee_id"), companyDb);
                        retVal = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid employee login for: " + userName);
            }
        }


        return retVal;
    }

    public void goLogin() {
        Login_Frame.goLogin(efUser.getText(), new String(efPwd.getPassword()), new Connection(), loginFromCommand, this);


    }

    class Cancel_Button_Action implements ActionListener {

        Main_Window parent;

        public Cancel_Button_Action(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            parent.dispose();
            System.exit(1);
        }
    }

    class Login_Button_Action implements ActionListener {

        public Login_Button_Action() {
        }

        public void actionPerformed(ActionEvent e) {
            goLogin();

        }
    }
}
