/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.data_export.ultra32;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author jason.allen
 */
public class ExportReportData implements JRDataSource {

    private RecData[] data = new RecData[7];
    private int currentLocation = 0;
    private boolean start = true;
    private String title;
    private double total;
    
    private class RecData {
        private String date;
        private int hrs;
        
        public RecData(String date, int hrs) {
            this.date = date;
            this.hrs = hrs;
        }
        
        public String getDate() {
            return this.date;
        }
        
        public Double getHrs() {
            return ((double)this.hrs / 60.0);
        }
    }
    
    public ExportReportData(String title, String[] headers, Record_Set rs){
        this.title = title;
        int total = 0;
        for (int i = 0; i < rs.length(); i++ ){
            data[i] = new RecData(headers[i], rs.getInt(0));
            total += rs.getInt(0);
            rs.moveNext();
        }
        this.total = (double)total / 60.0;
    }
    
    public boolean next() throws JRException {
        if(this.start) {
            this.start = false;
            return true;
        }
        
        this.currentLocation++;
        if(this.currentLocation == 7) {
            return false;
        }
        
        return true;
    }

    public Object getFieldValue(JRField field) throws JRException {
        try {
            if(field.getName().equals("title")) {
                return this.title;
            }

            if(field.getName().equals("date")) {
                return this.data[this.currentLocation].getDate();
            }
        
        
            if(field.getName().equals("hours")) {
                try {
                    return this.data[this.currentLocation].getHrs();
                } catch (Exception exe) {
                    return 0;
                }
            }

            if(field.getName().equals("total")) {
                return this.total;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        throw new UnsupportedOperationException("Not supported yet." + field.getName());
    }

}
