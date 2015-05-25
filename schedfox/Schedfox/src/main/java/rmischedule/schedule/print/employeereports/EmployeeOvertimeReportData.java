/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.print.employeereports;

import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author jason.allen
 */
public class EmployeeOvertimeReportData implements JRDataSource {

    private Vector emp;
    private int currentLoc = 0;
    private boolean start = true;
    private String title;
    private Double summary_hours = 0.0;
    private Double summary_overtime = 0.0;
    
    private class repEmployee {
        private String name;
        private int time;
        private int ot;
        
        public repEmployee(String n, int t, int ot) {
            this.name = n;
            this.time = t;
            this.ot = ot;
        }
        
        private String getName() {
            return this.name;
        }
        
        private Double getTime() {
            Double t = (this.time / 60.0);            
            return t;
        }
        
        private Double getOvertime() {
            Double t = (this.ot / 60.0);            
            return t;
        }
        
        private Double getOvertimePct() {            
            Double p = (double)(this.getOvertime() / this.getTime());
            return (p * 100);
        }
    }
    
    public EmployeeOvertimeReportData(String title, Record_Set rs) {
        this.title = title;
        this.emp = new Vector();
        do{
            String sEmpName = rs.getString("ename");            
            int iTime = rs.getInt("total_time");    
            int iOt = rs.getInt("overtime");
            this.summary_hours += iTime;            
            if(iOt > 0) {
                this.summary_overtime += iOt;
                this.emp.add(new repEmployee(sEmpName, iTime, iOt));            
            }
        }while(rs.moveNext());        
    }
    
    public boolean next() throws JRException {
        if(start) {
            start = false;
            return (this.emp.size() > 0);
        }
        
        this.currentLoc++;

        if(this.currentLoc >= this.emp.size()){
            return false;
        }
        
        return true;
    }

    public Object getFieldValue(JRField field) throws JRException {
        repEmployee re = (repEmployee)this.emp.get(currentLoc);
        
        if(field.getName().equals("ename")) {
            return re.getName();
        }
        
        if(field.getName().equals("total_time")){
            return re.getTime();
        }
        
        if(field.getName().equals("overtime")) {
            return re.getOvertime();
        }
        
        if(field.getName().equals("overtime_pct")) {
            return re.getOvertimePct();
        }
        
        if(field.getName().equals("title")) {
            return this.title;
        }
        
        if(field.getName().equals("summary_hours")){
            return (this.summary_hours / 60.0);
        }
        
        if(field.getName().equals("summary_overtime")) {
            return (this.summary_overtime / 60.0);
        }
        
        if(field.getName().equals("summary_overtime_pct")){
            Double t = (this.summary_hours / 60.0);
            Double o = (this.summary_overtime / 60.0);
            return (o / t) * 100;
        }
        
        throw new UnsupportedOperationException("Not supported yet." + field.getName());
    }

}
