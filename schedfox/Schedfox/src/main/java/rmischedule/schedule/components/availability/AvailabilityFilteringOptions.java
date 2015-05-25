/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AvailabilityFilteringOptions.java
 *
 * Created on Jul 14, 2010, 10:10:06 AM
 */
package rmischedule.schedule.components.availability;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_Employee_Availability;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.CertificationClass;

/**
 *
 * 
 */
public class AvailabilityFilteringOptions extends javax.swing.JPanel {

    public static int SIZE_TO_START_AT = 0;
    public static int SIZE_TO_GROW_TO = 270;
    private Schedule_View_Panel svp;
    private Schedule_Employee_Availability sea;
    private boolean isShowing;
    private boolean isLocked;
    private Vector<CertificationClass> certs;
    private Vector<EmployeeType> types;
    private Vector<IndividualAvailabilityFilter> certFilter;
    private Vector<IndividualAvailabilityFilter> typeFilter;
    private Vector<IndividualAvailabilityFilter> generalFilter;
    private boolean showTrained;
    private int minimumSize = 0;

    /** Creates new form AvailabilityFilteringOptions */
    public AvailabilityFilteringOptions(Schedule_View_Panel svp, Schedule_Employee_Availability sea) {
        initComponents();

        this.svp = svp;
        this.sea = sea;

        certFilter = new Vector<IndividualAvailabilityFilter>();
        typeFilter = new Vector<IndividualAvailabilityFilter>();
        generalFilter = new Vector<IndividualAvailabilityFilter>();

        certs = svp.getAvailableCertificationsForSchedule();
        types = svp.getAllEmployeeTypes();
        certificationFilterPanel.setVisible(false);
        employeeTypeFilterPanel.setVisible(false);
        this.showFilteringOptionsForGeneral();
        if (certs.size() > 0) {
            this.showFilteringOptionsForCerts();
            certificationFilterPanel.setVisible(true);
        }
        if (types.size() > 0) {
            this.showFilteringOptionsForTypes();
            employeeTypeFilterPanel.setVisible(true);
        }
        isLocked = false;
        isShowing = false;

        this.setPreferredSize(new Dimension(2000, minimumSize));
        this.setMaximumSize(new Dimension(2000, minimumSize));
        this.setMinimumSize(new Dimension(10, minimumSize));
    }

    private boolean isSelectedByName(String name, Vector<IndividualAvailabilityFilter> filter) {
        boolean retVal = false;
        for (int i = 0; i < filter.size(); i++) {
            if (filter.get(i).isSelected() && filter.get(i).getObject() != null) {
                if (filter.get(i).getObject() instanceof String && filter.get(i).getObject().equals(name)) {
                    retVal = ((Boolean) filter.get(i).isSelected());
                }
            }
        }
        return retVal;
    }

    /**
     * @return the showTrained
     */
    public boolean isShowTrainedOnly() {
        return isSelectedByName("Trained", generalFilter);
    }

    public boolean isShowAvailableOnly() {
        return isSelectedByName("Available", generalFilter);
    }

    /**
     * @param showTrained the showTrained to set
     */
    public void setShowTrained(boolean showTrained) {
        IndividualAvailabilityFilter trained = null;
        for (int i = 0; i < this.generalFilter.size(); i++) {
            if (generalFilter.get(i).getObject() != null && generalFilter.get(i).getObject() instanceof String) {
                if (generalFilter.get(i).getObject().equals("Trained")) {
                    trained = generalFilter.get(i);
                }
            }
        }
        if (trained != null) {
            if (trained.isSelected() != showTrained) {
                this.toggleOne(generalFilter, trained);
                sea.acb.refreshEmpList();
            }
        }

    }

    public void setSelectedCertifications(Vector<CertificationClass> certs) {
        if (certs.size() > 0) {
            for (int i = 0; i < this.certFilter.size(); i++) {
                if (certFilter.get(i).getObject() != null && certFilter.get(i).getObject() instanceof CertificationClass) {
                    CertificationClass currCert = (CertificationClass) certFilter.get(i).getObject();
                    boolean found = false;
                    for (int c = 0; c < certs.size(); c++) {
                        if (currCert.getId().equals(certs.get(c).getId())) {
                            found = true;
                            if (!certFilter.get(i).isSelected()) {
                                this.toggleOne(certFilter, certFilter.get(i));
                            }
                        }
                    }
                    if (!found && certFilter.get(i).isSelected()) {
                        this.toggleOne(certFilter, certFilter.get(i));
                    }
                }
            }
        } else {
            this.toggleAll(certFilter);
        }
        sea.acb.refreshEmpList();
    }

    public Vector<EmployeeType> getUnSelectedEmployeeTypes() {
        Vector<EmployeeType> retVal = new Vector<EmployeeType>();
        IndividualAvailabilityFilter allFilter = null;
        for (int i = 0; i < this.typeFilter.size(); i++) {
            if (!typeFilter.get(i).isSelected() && typeFilter.get(i).getObject() != null) {
                if (typeFilter.get(i).getObject() instanceof EmployeeType) {
                    retVal.add((EmployeeType) typeFilter.get(i).getObject());
                }
            }
            if (typeFilter.get(i).isAllFilter()) {
                allFilter = typeFilter.get(i);
            }
        }
        if (allFilter != null) {
            if (allFilter.isSelected()) {
                retVal.clear();
            } else {
                EmployeeType empType = new EmployeeType();
                empType.setEmployeeTypeId(-1);
                retVal.add(empType);
            }
        }
        return retVal;
    }

    public Vector<CertificationClass> getUnSelectedCertifications() {
        Vector<CertificationClass> retVal = new Vector<CertificationClass>();
        IndividualAvailabilityFilter allFilter = null;
        try {
            for (int i = 0; i < this.certFilter.size(); i++) {
                if (!certFilter.get(i).isSelected() && certFilter.get(i).getObject() != null) {
                    retVal.add((CertificationClass) certFilter.get(i).getObject());
                }
                if (certFilter.get(i).isAllFilter()) {
                    allFilter = certFilter.get(i);
                }
            }
            if (allFilter != null & allFilter.isSelected()) {
                retVal.clear();
            } else if (allFilter != null & !allFilter.isSelected()) {
                retVal.add(new CertificationClass("-1", "", "", "", "", "", false));
            }
        } catch (Exception e) {
            System.out.println("No certifications have been setup, unable to filter by them!");
        }
        return retVal;
    }

    public void showFilteringOptionsForGeneral() {
        IndividualAvailabilityFilter allFilter =
                new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, "Don't filter by training", true);
        allFilter.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                toggleAll(generalFilter);
            }
        });
        allFilter.setSelected(false);
        generalFilter.add(allFilter);

        final IndividualAvailabilityFilter availabilityFilter =
                new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, "Available Employees", false);
        availabilityFilter.setObject("Available");
        availabilityFilter.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                toggleOne(generalFilter, availabilityFilter);
                sea.acb.refreshEmpList();
            }
        });
        availabilityFilter.setSelected(true);
        generalFilter.add(availabilityFilter);

        final IndividualAvailabilityFilter trainedFilter =
                new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, "Trained (If Applicable)", false);
        trainedFilter.setObject("Trained");
        trainedFilter.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                toggleOne(generalFilter, trainedFilter);
                sea.acb.refreshEmpList();
            }
        });
        trainedFilter.setSelected(false);
        generalFilter.add(trainedFilter);

        for (int t = 0; t < generalFilter.size(); t++) {
            generalPanel.add(generalFilter.get(t));
        }
    }

    public void showFilteringOptionsForTypes() {
        IndividualAvailabilityFilter allFilter =
                new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, "Don't filter by type", true);
        allFilter.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                toggleAll(typeFilter);
            }
        });
        typeFilter.add(allFilter);

        for (int i = 0; i < this.types.size(); i++) {
            final IndividualAvailabilityFilter typeFilterLoc =
                    new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, types.get(i).getEmployeeType(), false);
            typeFilterLoc.setObject(this.types.get(i));
            typeFilterLoc.setSelected(false);
            typeFilterLoc.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {
                    toggleOne(typeFilter, typeFilterLoc);
                    sea.acb.refreshEmpList();
                }
            });
            typeFilter.add(typeFilterLoc);
        }

        for (int t = 0; t < typeFilter.size(); t++) {
            typePanel.add(typeFilter.get(t));
        }
    }

    public void showFilteringOptionsForCerts() {
        IndividualAvailabilityFilter allFilter =
                new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, "Don't filter by Cert", true);
        allFilter.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                toggleAll(certFilter);
            }
        });
        certFilter.add(allFilter);
        for (int i = 0; i < this.certs.size(); i++) {
            final IndividualAvailabilityFilter certFilterLoc =
                    new IndividualAvailabilityFilter(Main_Window.Green_16_Icon, Main_Window.Yellow_16_Icon, Main_Window.Red_16_Icon, certs.get(i).getName(), false);
            certFilterLoc.setSelected(false);
            certFilterLoc.setObject(this.certs.get(i));
            certFilterLoc.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {
                    toggleOne(certFilter, certFilterLoc);
                    sea.acb.refreshEmpList();
                }
            });
            certFilter.add(certFilterLoc);
        }

        for (int c = 0; c < certFilter.size(); c++) {
            certPanel.add(certFilter.get(c));
        }
    }

    public void toggleOne(Vector<IndividualAvailabilityFilter> filters, IndividualAvailabilityFilter filter) {
        boolean selected = !filter.isSelected();
        filter.setSelected(selected);
        boolean allTheSame = true;
        IndividualAvailabilityFilter mainFilter = null;
        for (int f = 0; f < filters.size(); f++) {
            if (!filters.get(f).isAllFilter()) {
                if (filters.get(f).isSelected() != selected) {
                    allTheSame = false;
                }
            } else {
                mainFilter = filters.get(f);
            }
        }
        if (mainFilter != null) {
            if (!allTheSame) {
                mainFilter.setSelected(false);
            } else {
                mainFilter.setSelected(!selected);
            }
        }
    }

    public void toggleAll(Vector<IndividualAvailabilityFilter> filters) {
        IndividualAvailabilityFilter allFilter = null;
        for (int f = 0; f < filters.size(); f++) {
            if (filters.get(f).isAllFilter()) {
                allFilter = filters.get(f);
            }
        }
        if (allFilter != null && !allFilter.isSelected()) {
            allFilter.setSelected(!allFilter.isSelected());
            boolean isSelected = allFilter.isSelected();
            if (isSelected) {
                for (int f = 0; f < filters.size(); f++) {
                    if (!filters.get(f).isAllFilter()) {
                        filters.get(f).setSelected(!isSelected);
                    }
                }
            }
        }
        sea.acb.refreshEmpList();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Method to toggle this display.
     * @param shouldShow
     */
    public void toggleDisplay() {
        if (!isLocked) {
            isShowing = !isShowing;
            if (isShowing) {
                Dimension myDimension = this.getPreferredSize();
                myDimension.height += SIZE_TO_GROW_TO;
                this.setPreferredSize(new Dimension(2000, myDimension.height));
                this.setMinimumSize(new Dimension(10, myDimension.height));
                this.setMaximumSize(new Dimension(2000, myDimension.height));
                this.setSize(new Dimension(2000, myDimension.height));
                sea.repaint();
                sea.revalidate();
            } else {
                Dimension myDimension = this.getPreferredSize();
                myDimension.height -= SIZE_TO_GROW_TO;
                this.setPreferredSize(new Dimension(2000, myDimension.height));
                this.setMinimumSize(new Dimension(10, myDimension.height));
                this.setMaximumSize(new Dimension(2000, myDimension.height));
                this.setSize(new Dimension(2000, myDimension.height));
                sea.repaint();
                sea.revalidate();
            }
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

        scrollPanel = new javax.swing.JScrollPane();
        rootPane = new javax.swing.JPanel();
        generalFilterPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        employeeTypeFilterPanel = new javax.swing.JPanel();
        typePanel = new javax.swing.JPanel();
        certificationFilterPanel = new javax.swing.JPanel();
        certPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 0));

        scrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        rootPane.setLayout(new javax.swing.BoxLayout(rootPane, javax.swing.BoxLayout.Y_AXIS));

        generalFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trained Filtering"));
        generalFilterPanel.setLayout(new java.awt.GridLayout());

        generalPanel.setLayout(new javax.swing.BoxLayout(generalPanel, javax.swing.BoxLayout.Y_AXIS));
        generalFilterPanel.add(generalPanel);

        rootPane.add(generalFilterPanel);

        employeeTypeFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Type"));
        employeeTypeFilterPanel.setLayout(new java.awt.GridLayout(1, 0));

        typePanel.setLayout(new javax.swing.BoxLayout(typePanel, javax.swing.BoxLayout.Y_AXIS));
        employeeTypeFilterPanel.add(typePanel);

        rootPane.add(employeeTypeFilterPanel);

        certificationFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Certifications"));
        certificationFilterPanel.setLayout(new java.awt.GridLayout(1, 0));

        certPanel.setLayout(new javax.swing.BoxLayout(certPanel, javax.swing.BoxLayout.Y_AXIS));
        certificationFilterPanel.add(certPanel);

        rootPane.add(certificationFilterPanel);

        scrollPanel.setViewportView(rootPane);

        add(scrollPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel certPanel;
    private javax.swing.JPanel certificationFilterPanel;
    private javax.swing.JPanel employeeTypeFilterPanel;
    private javax.swing.JPanel generalFilterPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel rootPane;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables
}
