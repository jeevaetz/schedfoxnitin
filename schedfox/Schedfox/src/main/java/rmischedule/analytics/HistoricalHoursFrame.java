/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HistoricalHoursFrame.java
 *
 * Created on Apr 5, 2011, 2:06:21 PM
 */
package rmischedule.analytics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.analytics.get_historical_hours_query;

/**
 *
 * @author user
 */
public class HistoricalHoursFrame extends javax.swing.JInternalFrame {

    private String companyId;
    private Company company;
    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;
    private BranchComboModel branchModel;

    private ChartPanel chartPanel;
    private TimeSeriesCollection dataset;
    private TimeSeries s1;
    private TimeSeries s2;
    private TimeSeries s3;

    private SimpleDateFormat normalDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /** Creates new form HistoricalHoursFrame */
    public HistoricalHoursFrame(String companyId) {
        initComponents();

        this.companyId = companyId;
        company = Main_Window.parentOfApplication.getCompanyById(this.companyId);
        
        branchModel = new BranchComboModel();
        branchCombo.setModel(branchModel);
        branchCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData(company);
            }
        });
        Calendar endDate = null;
        Calendar begDate = null;
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        begDate = StaticDateTimeFunctions.getBegOfWeek(Integer.parseInt(companyId));
        myFormat.format(begDate.getTime());
        endDate = StaticDateTimeFunctions.getEndOfWeek(Integer.parseInt(companyId));
        myFormat.format(endDate.getTime());
        begDate.add(Calendar.YEAR, -1);
        begDate = StaticDateTimeFunctions.getBegOfWeek(begDate, Integer.parseInt(companyId));
        myFormat.format(begDate.getTime());
        endDate.add(Calendar.WEEK_OF_YEAR, 0);
        myFormat.format(endDate.getTime());

        Vector<Branch> branches = company.getBranches();
        Branch allBranches = new Branch(-1, "All Branches");
        branchModel.addBranch(allBranches);
        for (int b = 0; b < branches.size(); b++) {
            branchModel.addBranch(branches.get(b));
        }

        begCal = new JCalendarComboBox(begDate);
        endCal = new JCalendarComboBox(endDate);
        begCal.addChangeListener(new DateChangeListener());
        endCal.addChangeListener(new DateChangeListener());

        startPanel.add(begCal);
        endPanel.add(endCal);



        this.refreshData(company);
    }

    /* Creates a sample dataset.
     *
     * @return The dataset.
     */
    private TimeSeriesCollection createDataset() {
        Connection myConn = new Connection();
        myConn.myCompany = companyId;

        String id = ((Branch) branchModel.getSelectedItem()).getBranchId() + "";

        Date sDate = begCal.getCalendar().getTime();
        Date eDate = endCal.getCalendar().getTime();

        get_historical_hours_query hours = new get_historical_hours_query();
        hours.setPreparedStatement(new Object[]{sDate, eDate, Integer.parseInt(id)});

        Record_Set rst = myConn.executeQuery(hours);

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd");

        s1 = new TimeSeries("Total Hours Worked", Day.class);
        s2 = new TimeSeries("Non Billable Hours Worked", Day.class);
        s3 = new TimeSeries("Billable Hours Worked", Day.class);
        
        for (int r = 0; r < rst.length(); r++) {
            s1.add(new Day(rst.getDate("date")), rst.getBigDecimal("tot").doubleValue());
            s2.add(new Day(rst.getDate("date")), rst.getBigDecimal("nonbillablehours").doubleValue());
            s3.add(new Day(rst.getDate("date")), rst.getBigDecimal("billablehours").doubleValue());
            
            rst.moveNext();
        }

        dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        if (nonBillableChk.isSelected()) {
            dataset.addSeries(s2);
        }
        if (billableChk.isSelected()) {
            dataset.addSeries(s3);
        }
        return dataset;
    }

    private void refreshData(Company company) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                company.getName() + " Hours Worked (Weekly)",
                "Date",
                "Hours Worked",
                createDataset(),
                true,
                true,
                false);
        graphPanel.removeAll();

        chartPanel = new ChartPanel(chart);
        XYPlot plot = (XYPlot) chart.getPlot();

        chartPanel.repaint();

        plot.getRenderer().setSeriesToolTipGenerator(0, new HistoricalToolTips());
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        // customise the range axis...
        DateAxis rangeAxis = (DateAxis) plot.getDomainAxis();

        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setFillPaint(Color.white);

        rangeAxis.setStandardTickUnits(rangeAxis.createStandardDateTickUnits());
        graphPanel.add(chartPanel);
        this.repaint();
        this.revalidate();
    }

    private class BranchComboModel implements ComboBoxModel {

        private Vector<Branch> branches;
        private int selectedItem = 0;

        public BranchComboModel() {
            branches = new Vector<Branch>();
        }

        public void addBranch(Branch branch) {
            this.branches.add(branch);
        }

        public void setSelectedItem(Object anItem) {
            if (anItem instanceof Branch) {
                for (int b = 0; b < branches.size(); b++) {
                    if (branches.get(b).getBranchId().equals(((Branch) anItem).getBranchId())) {
                        selectedItem = b;
                    }
                }
            }
        }

        public Object getSelectedItem() {
            try {
                return branches.get(selectedItem);
            } catch (Exception e) {
                return "";
            }
        }

        public int getSize() {
            return branches.size();
        }

        public Object getElementAt(int index) {
            return branches.get(index);
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }

    private class DateChangeListener implements ChangeListener {

        public DateChangeListener() {
        }

        public void stateChanged(ChangeEvent e) {
            refreshData(company);
        }
    }


    private class HistoricalToolTips implements XYToolTipGenerator {

        public String generateToolTip(XYDataset xyd, int i, int i1) {
            TimeSeriesCollection dataCollection = (TimeSeriesCollection)xyd;
            TimeSeries activeSeries = dataCollection.getSeries(i);
            TimeSeriesDataItem item = activeSeries.getDataItem(i1);
            StringBuilder tooltip = new StringBuilder();
            tooltip.append("Hours Worked: " + item.getValue().intValue());
            tooltip.append(" (" + normalDateFormat.format(item.getPeriod().getStart()) + ")");
            return tooltip.toString();
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

        controlPanel = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        endPanel = new javax.swing.JPanel();
        branchPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        branchCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        nonBillableChk = new javax.swing.JCheckBox();
        billableChk = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        graphPanel = new javax.swing.JPanel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        controlPanel.setMaximumSize(new java.awt.Dimension(150, 32767));
        controlPanel.setMinimumSize(new java.awt.Dimension(150, 0));
        controlPanel.setPreferredSize(new java.awt.Dimension(150, 455));
        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.Y_AXIS));

        startPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        startPanel.setPreferredSize(new java.awt.Dimension(100, 35));
        startPanel.setLayout(new java.awt.GridLayout(1, 0));
        controlPanel.add(startPanel);

        endPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        endPanel.setPreferredSize(new java.awt.Dimension(100, 35));
        endPanel.setLayout(new java.awt.GridLayout(1, 0));
        controlPanel.add(endPanel);

        branchPanel.setMaximumSize(new java.awt.Dimension(32767, 45));
        branchPanel.setPreferredSize(new java.awt.Dimension(100, 45));
        branchPanel.setLayout(new java.awt.GridLayout(2, 0));

        jLabel1.setText("Branch");
        branchPanel.add(jLabel1);

        branchCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        branchPanel.add(branchCombo);

        controlPanel.add(branchPanel);

        nonBillableChk.setText("Show Non Billable");
        nonBillableChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonBillableChkActionPerformed(evt);
            }
        });

        billableChk.setText("Show Billable Hours");
        billableChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billableChkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nonBillableChk)
                    .addComponent(billableChk))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(nonBillableChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(billableChk)
                .addContainerGap())
        );

        controlPanel.add(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        controlPanel.add(jPanel1);

        getContentPane().add(controlPanel);

        graphPanel.setLayout(new java.awt.GridLayout(1, 0));
        getContentPane().add(graphPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-765)/2, (screenSize.height-486)/2, 765, 486);
    }// </editor-fold>//GEN-END:initComponents

    private void billableChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billableChkActionPerformed
        try {
            if (billableChk.isSelected()) {
                dataset.addSeries(s3);
            } else {
                dataset.removeSeries(s3);
            }
            chartPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_billableChkActionPerformed

    private void nonBillableChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonBillableChkActionPerformed
        try {
            if (nonBillableChk.isSelected()) {
                dataset.addSeries(s2);
            } else {
                dataset.removeSeries(s2);
            }
            chartPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_nonBillableChkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox billableChk;
    private javax.swing.JComboBox branchCombo;
    private javax.swing.JPanel branchPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel endPanel;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox nonBillableChk;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables
}
