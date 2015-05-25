/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.OfficerDailyReport;

/**
 *
 * @author user
 */
public class OfficerDailyReportTableModel extends AbstractTableModel {

    private ArrayList<OfficerDailyReport> officerDailyReports;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private String company;
    
    public OfficerDailyReportTableModel() {
        officerDailyReports = new ArrayList<OfficerDailyReport>();
    }
    
    public void clear() {
        officerDailyReports = new ArrayList<OfficerDailyReport>();
        super.fireTableDataChanged();
    }
    
    public void addOfficerReport(OfficerDailyReport report) {
        this.officerDailyReports.add(report);
        super.fireTableDataChanged();
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public int getRowCount() {
        return officerDailyReports.size();
    }

    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Date";
        } else if (columnIndex == 1) {
            return "Officer";
        }
        return "Text";
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            OfficerDailyReport officerReport = officerDailyReports.get(rowIndex);
            if (columnIndex == 1) {
                return dateFormat.format(officerReport.getLoggedIn());
            } else if (columnIndex == 2) {
                return officerReport.getOfficer(this.company).getFullName();
            } else {
                return "";
            }
        } catch (Exception exe) {
            return "";
        }
    }
    
}
