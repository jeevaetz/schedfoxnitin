/*
 * Record_Set.java
 *
 * Created on January 19, 2005, 8:09 AM
 */
package schedfoxlib.model.util;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/**
 *
 * @author  Quietus
 */
public class Record_Set implements Serializable {
    
    protected HashMap<String, Integer>  myRecordset;
    protected ArrayList<byte[]>         myDataSet;
    protected String[]                    columnHeaders;
    protected transient ResultSet         myResultSet;
    public String                       lu;
    
    public String branch;
    public String company;
    
    protected int currentPos;
    protected int columnSize;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Record_Set() {
        lu = new String();
        branch = new String();
        company = new String();
        branch = new String();
        company = new String();
    }
    
    public Record_Set(ResultSet rs) {
        myResultSet = rs;
        branch = new String();
        company = new String();
        lu = new String();

    }

    public void decompressData() {

    }
    
    /**
     * Should be called immediately on each resultset so that we can generate and
     * compress the data...and free up all open connections!
     */
    public void generateAndCompress() {
        ResultSetMetaData myData = null;
        try {
            myData = myResultSet.getMetaData();
            columnSize = myData.getColumnCount();
            myDataSet = new ArrayList<byte[]>();
        } catch (Exception noResultSet) {
            columnSize = 0;
        }
        try {
            columnHeaders = new String[columnSize];
            myRecordset= new HashMap(columnSize);
            for (int i = 0; i < columnSize; i++) {
                try {
                    columnHeaders[i] = (myData.getColumnName(i + 1)).toLowerCase();
                    myRecordset.put(columnHeaders[i], new Integer(i));
                } catch (Exception e) {
                    System.out.println("Error reading column headers.");
                }
            }
            if (myResultSet != null) {
                while (myResultSet.next()) {
                    for (int i = 0; i < columnSize; i++) {
                        try {
                            myDataSet.add(myResultSet.getBytes(i + 1));
                        } catch (Exception e) {
                            myDataSet.add((myResultSet.getInt(i + 1) + "").getBytes());
                        } finally {

                        }
                    }
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally{
            try {
                myResultSet.close();
            } catch (Exception e) {}
        }
    }
    
    /**
     * Method is to force the Record_Set object to populate its data without 
     * serialization, should be used whenever Record_Set is used without sending
     * over a port....
     */
    public void forceGenerate() {

    }
    
    public int getColumnSize() {
        return columnSize;
    }
    
    public void moveToFront() {
        currentPos = 0;
    }
    
    /**
     * REturns true if the column is defined in the resultset
     * @param id The column Name
     * @return boolean
     */
    public boolean hasColumn(String id) {
        return (myRecordset.get(id) != null);
    }

    public int getInt(String id){
        Integer i = myRecordset.get(id);
        if (i == null) {
            return -1;
        }
        return getInt(i);
    }
    
    public byte[] getByteArray(String id){
        Integer i = myRecordset.get(id);
        if (i == null) {
            return new byte[0];
        }
        return getByteArray(i);
    }

    public BigDecimal getBigDecimal(String id) {
        Integer i = myRecordset.get(id);
        if (i == null) {
            return new BigDecimal(-1);
        }
        return getBigDecimal(i);
    }

    public BigDecimal getBigDecimal(int i) {
        if(i>-1){
            BigDecimal c;
            try{
                c = new BigDecimal(Float.parseFloat(new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8")));
            } catch (Exception e){
                c = null;
            }
            return c;
        }
        return new BigDecimal(-1);
    }

    public float getFloat(String id) {
        Integer i = myRecordset.get(id);
        if (i == null) {
            return getFloat(-1);
        }
        return getFloat(i);
    }

    public float getFloat(int i) {
        if(i>-1){
            float c;
            try{
                c = Float.parseFloat(new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8"));
            } catch (Exception e){
                c = (float)-1;
            }
            return c;
        }
        return (float)-1;
    }

    public byte[] getByteArray(int i) {
        if(i>-1){            
            byte[] c;
            try{
                c = myDataSet.get((currentPos * columnSize) + i);
            }catch(NumberFormatException e){
                c = new byte[0];
            }
            return c;
        }
        return new byte[0];
    }
    
    public int getInt(int i) {
        if(i>-1){            
            int c;
            try{
                c = Integer.parseInt(new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8"));
            } catch (Exception e){
                c = 0;
            }
            return c;
        }
        return -1;
    }
    
    public int getPos(String id) {
        return myRecordset.get(id).intValue();
    }
    
    public boolean getBoolean(int i) {
        if(i>-1){        
            try {
                String booleanString = new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8");
                if(booleanString.toLowerCase().equals("t") ||
                        booleanString.toLowerCase().equals("1") ||
                        booleanString.toLowerCase().equals("true")) {
                    return true;
                }
            } catch (Exception e) {}
        }
        return false; 
    }

    public Date getDate(int i) {
        if (i > -1) {
            try {
                String columnData = new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8");
                if (myDataSet.get((currentPos * columnSize) + i) == null || columnData.equals("0")) {
                    return null;
                }
                return dateFormat.parse(columnData);
            } catch (Exception e) {

            }
        }
        return null;
    }

    public Date getTimestamp(int i) {
        if (i > -1) {
            try {
                if (myDataSet.get((currentPos * columnSize) + i) == null || myDataSet.get((currentPos * columnSize) + i).equals("0")) {
                    return null;
                }
                return timeFormat.parse(new String(myDataSet.get((currentPos * columnSize) + i), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Date getTimestamp(String id) {
        return getTimestamp(myRecordset.get(id));
    }
    
    public Date getDate(String id) {
        return getDate(myRecordset.get(id));
    }

    public boolean getBoolean(String id){
        int i = myRecordset.get(id);
        return getBoolean(i);
    }
    
    @Override
    public void finalize() {
        myRecordset = null;
        myDataSet = null;
    }
    
    public String getString(String id){
        Integer currData = myRecordset.get(id);
        if (currData == null) {
            return "";
        }
        return getString(currData);
    }
    
    public String getString(int currData) {
        if(currData>-1){
            try {
                return new String(myDataSet.get((currentPos * columnSize) + currData), "UTF-8");
            } catch (Exception e) {}
        }
        return "";
    }
    
    public boolean movePrev(){
        if(currentPos != 0){
            currentPos--;
            return true;
        }
        return false;        
    }
    
    public boolean moveNext(){
        currentPos++;
        if(currentPos >= length()){
            return false;
        }
        return true;
    }
    
    /**
     * Again making a toString method so that debugger can now display all informatin contained
     * in this class on mouseover, wonderful...
     */
    public String toString() {
        StringBuilder myReturn = new StringBuilder();
        myReturn.append("Data size: " + myDataSet.size() + " ColumnSize: " + columnSize + " Row Size: " + length() + " Current Pos " + currentPos + " \n");
  
        int looptill = 40;
        if (looptill > length()) {
            looptill = length();
            myReturn.append("Displaying all " + looptill + " rows...\n");
        } else {
            myReturn.append("Displaying first " + looptill + " rows...\n");
        }
        
        myReturn.append("COLUMNS: \n");
        
        for (int x = 0; x < myRecordset.size(); x++) {
            myReturn.append("   " + columnHeaders[x]);
        }
        
        myReturn.append("\n DATA \n");
        
        for (int i = 0; i < looptill; i++) {
            for (int x = 0; x < myRecordset.size(); x++) {
                try {
                    myReturn.append("  " + new String(myDataSet.get(i * columnSize + x), "UTF-8"));
                } catch (Exception e) {}
            }
            myReturn.append("\n");
        }
        
        if (myReturn.length() <= 0) {
            myReturn.append("No Data in RecordSet");
        }
        return myReturn.toString();
 
 }

    public boolean getEOF(){
        if (currentPos >= length()) {
            return true;
        }
        return false;
    }

    public boolean getBOF(){
        return (length() == 0);
    }
    
    public int length() {
        try {
            return myDataSet.size() / columnSize;
        } catch (Exception e) {
            return 0;
        }
    }
    
}
