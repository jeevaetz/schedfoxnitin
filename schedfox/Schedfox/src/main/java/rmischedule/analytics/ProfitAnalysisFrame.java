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
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.analytics.get_client_pay_rates_query;

/**
 *
 * @author user
 */
public class ProfitAnalysisFrame extends javax.swing.JInternalFrame {

    private String companyId;
    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;
    private BranchComboModel branchModel;
    private SimpleDateFormat normalDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /** Creates new form HistoricalHoursFrame */
    public ProfitAnalysisFrame(String companyId) {
        initComponents();

        this.companyId = companyId;
        this.setTitle("Bill Rate vs Pay Rate");

        branchModel = new BranchComboModel();
        branchCombo.setModel(branchModel);
        branchCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        Calendar endDate = null;
        Calendar begDate = null;
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        begDate = StaticDateTimeFunctions.getBegOfWeek(Integer.parseInt(companyId));
        myFormat.format(begDate.getTime());
        endDate = StaticDateTimeFunctions.getEndOfWeek(Integer.parseInt(companyId));
        myFormat.format(endDate.getTime());
        begDate.add(Calendar.MONTH, -1);
        begDate = StaticDateTimeFunctions.getBegOfWeek(begDate, Integer.parseInt(companyId));
        myFormat.format(begDate.getTime());
        endDate.add(Calendar.WEEK_OF_YEAR, 0);
        myFormat.format(endDate.getTime());

        Company company = Main_Window.parentOfApplication.getCompanyById(this.companyId);
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



        this.refreshData();
    }

    /* Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset(Record_Set rst) {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd");

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        String currentClient = rst.getString("client_name");
        while (rst.getString("client_name").equals(currentClient)) {
            dataSet.addValue((rst.getBigDecimal("tot_ratio").doubleValue() * 100),
                    "Total Ratio", myFormat.format(rst.getDate("my_date")));
            rst.moveNext();
        }

        return dataSet;
    }

    private void refreshData() {
        Connection myConn = new Connection();
        myConn.myCompany = companyId;

        String id = ((Branch) branchModel.getSelectedItem()).getBranchId() + "";

        Date sDate = begCal.getCalendar().getTime();
        Date eDate = endCal.getCalendar().getTime();

        get_client_pay_rates_query hours = new get_client_pay_rates_query();
        hours.setPreparedStatement(new Object[]{sDate, eDate, sDate, eDate, Integer.parseInt(id), Integer.parseInt(id), Integer.parseInt(id)});

        Record_Set rst = myConn.executeQuery(hours);
        graphPanel.removeAll();

        while (!rst.getEOF()) {
            final JFreeChart chart = ChartFactory.createBarChart(
                    rst.getString("client_name"),
                    "Week of",
                    "Bill Rate vs Pay Rate (%)",
                    createDataset(rst),
                    PlotOrientation.VERTICAL,
                    false,
                    true,
                    false);


            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setMaximumSize(new Dimension(300, 150));
            chartPanel.setPreferredSize(new Dimension(300, 150));
            final CategoryPlot plot = (CategoryPlot) chart.getPlot();
            final int lowerBound = 0;
            

            chartPanel.addComponentListener(new ComponentListener() {

                public void componentResized(ComponentEvent e) {
                    Dimension mysize = chartPanel.getSize();

                    //Object chart = chartPanel.getChart().getCategoryPlot().getR

                    Point2D start = new Point2D.Float(0, (int)0);
                    Point2D end = new Point2D.Float(0, (int)mysize.getHeight() + 55);

                    float val = 1 - (float)((Main_Window.getAmountForFairProfit()) / (100 - lowerBound));
                    float val2 = 1 - (float)((Main_Window.getAmountForGoodProfit()) / (100 - lowerBound));
                    float[] dist = {0.0f, val - .01f, val + .01f, val2 - .01f, val2 + .01f, 1.0f};
                    Color[] colors = {new Color(255, 255, 255), new Color(255, 153, 102), new Color(255, 255, 153), new Color(255, 255, 153), new Color(204, 255, 153), new Color(255, 255, 255)};
                    LinearGradientPaint p =
                            new LinearGradientPaint(start, end, dist, colors);
                    plot.setBackgroundPaint(p);
                }

                public void componentMoved(ComponentEvent e) {

                }

                public void componentShown(ComponentEvent e) {

                }

                public void componentHidden(ComponentEvent e) {

                }

            });

            

            

            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            renderer.setBarPainter(new GradientBarPainter() {

                @Override
                public void paintBar(Graphics2D gd, BarRenderer br, int i, int i1, RectangularShape rs, RectangleEdge re) {
                    DefaultCategoryDataset dataSet = (DefaultCategoryDataset) br.getPlot().getDataset(0);
                    Double currentData = (Double) dataSet.getValue(i, i1);
                    if (currentData.compareTo(Main_Window.getAmountForGoodProfit()) < 0) {
                        GradientPaint gp1 = new GradientPaint(
                                0.0f, 0.0f, Color.GREEN,
                                0.0f, 0.0f, new Color(0, 64, 0));
                        br.setBaseFillPaint(gp1);
                        br.setSeriesPaint(0, gp1);
                    } else if (currentData.compareTo(Main_Window.getAmountForFairProfit()) < 0) {
                        GradientPaint gp1 = new GradientPaint(
                                0.0f, 0.0f, Color.YELLOW,
                                0.0f, 0.0f, new Color(0, 64, 0));
                        br.setSeriesPaint(0, gp1);
                    } else {
                        GradientPaint gp1 = new GradientPaint(
                                0.0f, 0.0f, Color.RED,
                                0.0f, 0.0f, new Color(0, 64, 0));
                        br.setBasePaint(gp1);
                        br.setSeriesPaint(0, gp1);
                    }
                    super.paintBar(gd, br, i, i1, rs, re);
                }

                @Override
                public void paintBarShadow(Graphics2D gd, BarRenderer br, int i, int i1, RectangularShape rs, RectangleEdge re, boolean bln) {
                    
                }
            });

            CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
            //domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            ValueAxis rangeAxis = (ValueAxis) plot.getRangeAxis();
            rangeAxis.setAutoRange(false);
            rangeAxis.setLowerBound(lowerBound);
            rangeAxis.setUpperBound(100);
            

            graphPanel.add(chartPanel);
        }
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(60);
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
            refreshData();
        }
    }

    private class HistoricalToolTips implements XYToolTipGenerator {

        public String generateToolTip(XYDataset xyd, int i, int i1) {
            TimeSeriesCollection dataCollection = (TimeSeriesCollection) xyd;
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
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
            .addGap(0, 319, Short.MAX_VALUE)
        );

        controlPanel.add(jPanel1);

        getContentPane().add(controlPanel);

        graphPanel.setLayout(new java.awt.GridLayout(0, 2));
        jScrollPane1.setViewportView(graphPanel);

        getContentPane().add(jScrollPane1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-692)/2, (screenSize.height-486)/2, 692, 486);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox branchCombo;
    private javax.swing.JPanel branchPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel endPanel;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables
}
