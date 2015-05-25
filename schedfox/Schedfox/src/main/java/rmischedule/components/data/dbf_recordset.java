/*
 * dbf_recordset.java
 *
 * Created on December 30, 2004, 7:36 AM
 */

package rmischedule.components.data;

import java.util.*;

/**
 *
 * @author  jason.allen
 */
public class dbf_recordset {
    private HashMap                dbf_columns;
    private dbf_reader.record[]    dbf_record;
    private dbf_reader.subHeader[] dbf_a_columns;    
    private int currentPos;
    /** Creates a new instance of dbf_recordset */
    public dbf_recordset(HashMap dbfColumns, dbf_reader.subHeader[] sub_header){
        dbf_columns = dbfColumns;
        dbf_a_columns = sub_header;
        currentPos = 0;
    }
    
    public void setRecords(dbf_reader.record[] records){
        dbf_record = records;
    }
    
    public String getString(String fieldName){
        dbf_reader.subHeader sb = (dbf_reader.subHeader)dbf_columns.get(fieldName);
        if(sb == null){
            return "unkown record";
        }
        return dbf_record[currentPos].raw_data[sb.getPosition()];
    }
    
    public boolean isRecDel(){
        return dbf_record[currentPos].is_deleted;
    }

    public dbf_recordset filter(String fieldName, String filter){
        dbf_reader.subHeader sb = (dbf_reader.subHeader)dbf_columns.get(fieldName);
        int col = sb.getPosition();
        int length = dbf_record.length;
        
        Vector tmpVector = new Vector();
        for(int i=0;i<length;i++){
            if(dbf_record[i].raw_data[col].equals(filter)){
                tmpVector.add(dbf_record[i]);
            }            
        }    
        
        dbf_recordset rs = new dbf_recordset(dbf_columns, dbf_a_columns);
        dbf_reader.record[] tmpR = new dbf_reader.record[tmpVector.size()];
        tmpVector.toArray(tmpR);
        rs.setRecords(tmpR);
        return rs;
    }
    
}
