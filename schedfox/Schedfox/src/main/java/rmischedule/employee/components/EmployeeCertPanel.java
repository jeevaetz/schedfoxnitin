/*
 * EmployeeCertPanel.java
 *
 * Created on October 12, 2005, 1:50 PM
 */
package rmischedule.employee.components;

import rmischeduleserver.util.StaticDateTimeFunctions;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import rmischedule.components.jcalendar.*;
import rmischedule.components.*;
import rmischedule.components.graphicalcomponents.DragAndDropLabel;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import java.util.*;
import schedfoxlib.model.EmployeeCertification;

/**
 *
 * @author  Ira Juneau
 */
public class EmployeeCertPanel extends DragAndDropLabel {

    private EmployeeCertPanel thisObj;
    private EmployeeHasCert empHasCert;
    private Employee_Certifications myParent;
    private JCalendarComboBox startCombo;
    private JCalendarComboBox endCombo;
    private Color defaultColor;
    private boolean isInitializing;
    private String interval;
    private EmployeeCertification cert;

    /** Creates new form EmployeeCertPanel */
    public EmployeeCertPanel(Employee_Certifications myPar, EmployeeCertification cert) {
        super(myPar.myLayeredPane, myPar.ControlPanel);
        initComponents();
        isInitializing = true;
        thisObj = this;
        myParent = myPar;
        this.cert = cert;

        this.CertNameLabel.setText(cert.getName());
        this.CertDescLabel.setText(cert.getDescription());
        this.CertNameLabel1.setText(cert.getName());

        try {
            startCombo = new JCalendarComboBox(cert.getCalendarAcquired());
        } catch (Exception e) {
            startCombo = new JCalendarComboBox(Calendar.getInstance());

        }
        try {
            if (cert.getCalendarExpired().get(Calendar.YEAR) < 1990) {
                throw new Exception("");
            }
            endCombo = new JCalendarComboBox(cert.getCalendarExpired());
        } catch (Exception e) {
            endCombo = new JCalendarComboBox(Calendar.getInstance());
        }
        startCombo.addChangeListener(new myChangeListener());
        endCombo.addChangeListener(new myChangeListener());
        CertGotOn.add(startCombo);
        CertBadOn.add(endCombo);


        if (cert.getCertification_default_renewal_time().equals("00:00:00")) {
            CertGotOn.setVisible(false);
            CertBadOn.setVisible(false);
            startCombo.setVisible(false);
            endCombo.setVisible(false);
        }
        for (int i = 0; i < getComponentCount(); i++) {
            getComponent(i).addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    getThisObj().mouseClicked(e);
                    if (e.getClickCount() > 1) {
                        getEmpHasCert().setCertification();
                    }
                }

                public void mouseEntered(MouseEvent e) {
                    getThisObj().mouseEntered(e);
                    setBackground(new Color(199, 216, 255));
                }

                public void mouseExited(MouseEvent e) {
                    getThisObj().mouseExited(e);
                    setBackground(getDefaultColor());
                }

                public void mousePressed(MouseEvent e) {
                    getThisObj().mousePressed(e);
                }

                public void mouseReleased(MouseEvent e) {
                    getThisObj().mouseReleased(e);
                }
            });
            getComponent(i).addMouseMotionListener(new MouseMotionAdapter() {

                public void mouseDragged(MouseEvent e) {
                    getThisObj().mouseDragged(e);
                }
            });
        }

        empHasCert = new EmployeeHasCert();
        empHasCert.setCertification(cert.isHasCert());
        isInitializing = false;
    }

    public void setCertified(boolean iscert) {
        getEmpHasCert().setCertification(iscert);
    }

    /**
     * @return the thisObj
     */
    public EmployeeCertPanel getThisObj() {
        return thisObj;
    }

    /**
     * @param thisObj the thisObj to set
     */
    public void setThisObj(EmployeeCertPanel thisObj) {
        this.thisObj = thisObj;
    }

    /**
     * @return the empHasCert
     */
    public EmployeeHasCert getEmpHasCert() {
        return empHasCert;
    }

    /**
     * @return the myParent
     */
    public Employee_Certifications getMyParent() {
        return myParent;
    }

    /**
     * @param myParent the myParent to set
     */
    public void setMyParent(Employee_Certifications myParent) {
        this.myParent = myParent;
    }

    /**
     * @return the certid
     */
    public int getCertid() {
        return cert.getCert_id();
    }

    /**
     * @return the renewTime
     */
    public String getRenewTime() {
        return cert.getCertification_default_renewal_time();
    }

    /**
     * @return the startCombo
     */
    public JCalendarComboBox getStartCombo() {
        return startCombo;
    }

    /**
     * @param startCombo the startCombo to set
     */
    public void setStartCombo(JCalendarComboBox startCombo) {
        this.startCombo = startCombo;
    }

    /**
     * @return the endCombo
     */
    public JCalendarComboBox getEndCombo() {
        return endCombo;
    }

    /**
     * @param endCombo the endCombo to set
     */
    public void setEndCombo(JCalendarComboBox endCombo) {
        this.endCombo = endCombo;
    }

    /**
     * @return the defaultColor
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * @param defaultColor the defaultColor to set
     */
    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    /**
     * @return the isInitializing
     */
    public boolean isIsInitializing() {
        return isInitializing;
    }

    /**
     * @param isInitializing the isInitializing to set
     */
    public void setIsInitializing(boolean isInitializing) {
        this.isInitializing = isInitializing;
    }

    /**
     * @return the start
     */
    public Calendar getStart() {
        return cert.getCalendarAcquired();
    }

    /**
     * @return the end
     */
    public Calendar getEnd() {
        return cert.getCalendarExpired();
    }

    /**
     * @return the CertBadOn
     */
    public javax.swing.JPanel getCertBadOn() {
        return CertBadOn;
    }

    /**
     * @param CertBadOn the CertBadOn to set
     */
    public void setCertBadOn(javax.swing.JPanel CertBadOn) {
        this.CertBadOn = CertBadOn;
    }

    /**
     * @return the CertDescLabel
     */
    public javax.swing.JLabel getCertDescLabel() {
        return CertDescLabel;
    }

    /**
     * @param CertDescLabel the CertDescLabel to set
     */
    public void setCertDescLabel(javax.swing.JLabel CertDescLabel) {
        this.CertDescLabel = CertDescLabel;
    }

    /**
     * @return the CertGotOn
     */
    public javax.swing.JPanel getCertGotOn() {
        return CertGotOn;
    }

    /**
     * @param CertGotOn the CertGotOn to set
     */
    public void setCertGotOn(javax.swing.JPanel CertGotOn) {
        this.CertGotOn = CertGotOn;
    }

    /**
     * @return the CertNameLabel
     */
    public javax.swing.JLabel getCertNameLabel() {
        return CertNameLabel;
    }

    /**
     * @param CertNameLabel the CertNameLabel to set
     */
    public void setCertNameLabel(javax.swing.JLabel CertNameLabel) {
        this.CertNameLabel = CertNameLabel;
    }

    /**
     * @return the CertNameLabel1
     */
    public javax.swing.JLabel getCertNameLabel1() {
        return CertNameLabel1;
    }

    /**
     * @param CertNameLabel1 the CertNameLabel1 to set
     */
    public void setCertNameLabel1(javax.swing.JLabel CertNameLabel1) {
        this.CertNameLabel1 = CertNameLabel1;
    }

    /**
     * @return the CertificationList
     */
    public javax.swing.JPanel getCertificationList() {
        return CertificationList;
    }

    /**
     * @param CertificationList the CertificationList to set
     */
    public void setCertificationList(javax.swing.JPanel CertificationList) {
        this.CertificationList = CertificationList;
    }

    /**
     * @return the HasCertification
     */
    public javax.swing.JPanel getHasCertification() {
        return HasCertification;
    }

    /**
     * @param HasCertification the HasCertification to set
     */
    public void setHasCertification(javax.swing.JPanel HasCertification) {
        this.HasCertification = HasCertification;
    }

    /**
     * @return the jLabel1
     */
    public javax.swing.JLabel getJLabel1() {
        return jLabel1;
    }

    /**
     * @param jLabel1 the jLabel1 to set
     */
    public void setJLabel1(javax.swing.JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    /**
     * @return the jLabel2
     */
    public javax.swing.JLabel getJLabel2() {
        return jLabel2;
    }

    /**
     * @param jLabel2 the jLabel2 to set
     */
    public void setJLabel2(javax.swing.JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    /**
     * @return the interval
     */
    public String getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }

    private class myChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if (getCertGotOn().isVisible()) {
                try {
                    employee_certification_update mySaveQuery = new employee_certification_update();
                    String startD = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getStartCombo().getCalendar());
                    String endD = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getEndCombo().getCalendar());
                    mySaveQuery.update(getMyParent().myParent.getMyIdForSave(), getCertid() + "", true, startD, endD);
                    employee_certification_update mySaveQuery2 = new employee_certification_update();
                    mySaveQuery2.update(getMyParent().myParent.getMyIdForSave(), getCertid() + "", false, startD, endD);
                    getMyParent().myParent.getConnection().executeUpdate(mySaveQuery2);
                    getMyParent().myParent.getConnection().executeUpdate(mySaveQuery);
                } catch (Exception ex) {
                }
            }
        }
    }

    private class EmployeeHasCert {

        private boolean hasCert;
        private CardLayout myLayout;

        public EmployeeHasCert() {
            myLayout = (CardLayout) getLayout();
            setCertification(hasCert);
        }

        public void setCertification() {
            setCertification(!hasCert);
        }

        public void setCertification(boolean hasCertification) {
            hasCert = hasCertification;
            if (hasCert) {
                myLayout.show(getThisObj(), "hascert");

                try {
                    getStartCombo().setCalendar(getStart());
                } catch (Exception e) {
                    getStartCombo().setCalendar(Calendar.getInstance());
                }
                try {
                    if (cert.getCalendarExpired().get(Calendar.YEAR) < 1990) {
                        throw new Exception("");
                    }
                    getEndCombo().setCalendar(getEnd());
                } catch (Exception e) {
                    getEndCombo().setCalendar(Calendar.getInstance());
                }

                getMyParent().moveCertToEmployeeHas(getThisObj());
                employee_certification_update mySaveQuery = new employee_certification_update();
                String startD = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getStartCombo().getCalendar());
                String endD = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getEndCombo().getCalendar());

                mySaveQuery.update(getMyParent().myParent.getMyIdForSave(), getCertid() + "", true, startD, endD);
                try {
                    if (!isIsInitializing()) {
                        getMyParent().myParent.getConnection().executeUpdate(mySaveQuery);
                    }
                } catch (Exception e) {
                }
            } else {
                myLayout.show(getThisObj(), "nocert");
                getMyParent().moveCertToCertification(getThisObj());
                employee_certification_update mySaveQuery = new employee_certification_update();
                String start = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getStartCombo().getCalendar());
                String end = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getEndCombo().getCalendar());
                mySaveQuery.update(getMyParent().myParent.getMyIdForSave(), getCertid() + "", false, start, end);
                try {
                    if (!isIsInitializing()) {
                        getMyParent().myParent.getConnection().executeUpdate(mySaveQuery);
                    }
                } catch (Exception e) {
                }
            }
            //if the interval is set to no 0 renewable interval
            if (interval != null && interval.compareTo("00:00:00") == 0) {
                CertGotOn.setVisible(false);
                CertBadOn.setVisible(false);
                startCombo.setVisible(false);
                endCombo.setVisible(false);
            }

            setBackground(getDefaultColor());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        CertificationList = new javax.swing.JPanel();
        CertNameLabel = new javax.swing.JLabel();
        CertDescLabel = new javax.swing.JLabel();
        HasCertification = new javax.swing.JPanel();
        CertNameLabel1 = new javax.swing.JLabel();
        CertGotOn = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CertBadOn = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(32767, 20));
        setMinimumSize(new java.awt.Dimension(10, 20));
        setPreferredSize(new java.awt.Dimension(10, 20));
        CertificationList.setLayout(new javax.swing.BoxLayout(CertificationList, javax.swing.BoxLayout.X_AXIS));

        CertificationList.setMaximumSize(new java.awt.Dimension(32767, 24));
        CertificationList.setMinimumSize(new java.awt.Dimension(10, 24));
        CertificationList.setOpaque(false);
        CertificationList.setPreferredSize(new java.awt.Dimension(10, 24));
        CertNameLabel.setMaximumSize(new java.awt.Dimension(120, 14));
        CertNameLabel.setMinimumSize(new java.awt.Dimension(120, 14));
        CertNameLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        CertificationList.add(CertNameLabel);

        CertDescLabel.setMaximumSize(new java.awt.Dimension(3400, 14));
        CertificationList.add(CertDescLabel);

        add(CertificationList, "nocert");

        HasCertification.setLayout(new javax.swing.BoxLayout(HasCertification, javax.swing.BoxLayout.X_AXIS));

        HasCertification.setOpaque(false);
        CertNameLabel1.setMaximumSize(new java.awt.Dimension(120, 14));
        CertNameLabel1.setMinimumSize(new java.awt.Dimension(120, 14));
        CertNameLabel1.setPreferredSize(new java.awt.Dimension(120, 14));
        HasCertification.add(CertNameLabel1);

        CertGotOn.setLayout(new javax.swing.BoxLayout(CertGotOn, javax.swing.BoxLayout.X_AXIS));

        CertGotOn.setOpaque(false);
        jLabel1.setText("Date Received Certification");
        CertGotOn.add(jLabel1);

        HasCertification.add(CertGotOn);

        CertBadOn.setLayout(new javax.swing.BoxLayout(CertBadOn, javax.swing.BoxLayout.X_AXIS));

        CertBadOn.setOpaque(false);
        jLabel2.setText("Renewal Date");
        CertBadOn.add(jLabel2);

        HasCertification.add(CertBadOn);

        add(HasCertification, "hascert");

    }
    // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CertBadOn;
    private javax.swing.JLabel CertDescLabel;
    private javax.swing.JPanel CertGotOn;
    private javax.swing.JLabel CertNameLabel;
    private javax.swing.JLabel CertNameLabel1;
    private javax.swing.JPanel CertificationList;
    private javax.swing.JPanel HasCertification;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
