/*
 * dbf_reader.java
 *
 * Created on December 3, 2004, 10:06 AM
 */

package rmischedule.components.data;

import java.util.*;
import java.io.*;
import java.text.*;
import rmischedule.components.StringIntHashtable;

/**
 *
 * @author  jason.allen
 *
 *  This class was created to work with dbf files that are create from a great 
 *  deal of sources exa FoxPro Clipper dBase ect.  This peticular class should
 *  handle every aspect of the file read process and parsing the data to its 
 *  correct status.  This class should be self explanitory it its simplicity.
 *
 */
public class dbf_reader {
    
    static final int HEADER_LENGTH          = 32;
    static final int COLUMN_HEADER_LENGTH   = 32;
    
    /**
     *  Probably wandering why I'm using java's HashMap and not the custom
     *  StringIntHashTable.  Well  the reason is that I would like this to
     *  be used fairly transparent to anything else.  Mainly just plug and
     *  chug.
     */
    private header      dbf_header;
    private HashMap     dbf_columns;
    private subHeader[] dbf_a_columns;
    private record[]    dbf_record;
    private subHeader sb;
    
    public StringIntHashtable    dbf_index;
    
    private String file_name;
    
    private int currentPos = 0;

    private boolean bof;
    private boolean eof;
    
    /** Creates a new instance of dbf_reader */
    public dbf_reader(String fileName) {
        file_name = fileName;
        
        
        FileInputStream dbfInputStream;        
        File dbfFile = new File(file_name);        
        try{
           dbfInputStream = new FileInputStream(dbfFile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return;
        }
        
        
        /** Reading will begin */
        byte[] b = new byte[HEADER_LENGTH];
        try{
            dbfInputStream.read(b);
            dbf_header = new header(b);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
        /**
         *  Prepare our arrays for the data
         */
        
        dbf_columns = new HashMap();        
        
        //this loop does break
        int i = 0;
        while(true){
            b = new byte[COLUMN_HEADER_LENGTH];            
            try{
                dbfInputStream.read(b);
                if(b[0] == 0x0D){
                    break;
                }             
                subHeader sh = new subHeader(b, i);
                dbf_columns.put(sh.getFieldName(), sh);
                i++;
            }catch(IOException e){
                e.printStackTrace();
                return;
            }            
        }
        
        dbf_a_columns = new subHeader[i];
        Object sh[] = dbf_columns.values().toArray();
        for(i=0;i<dbf_a_columns.length;i++){
            dbf_a_columns[((subHeader)sh[i]).getPosition()] = ((subHeader)sh[i]);
        }
        
        b = new byte[264 - HEADER_LENGTH];
        try{
            dbfInputStream.read(b);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }            
        dbf_record = new record[dbf_header.getNoOfRecords()];
        for(i=0;i<dbf_record.length;i++){
            b = new byte[dbf_header.getLengthOfDataRecord()];
            try{
                if(dbfInputStream.read(b) > 0){
                    dbf_record[i] = new record(b);
                }else{
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
                return;
            }      
        }
    }

    public String getString(String fieldName){
        sb = (subHeader)dbf_columns.get(fieldName);
        if(sb == null){
            return "";
        }
        return dbf_record[currentPos].raw_data[sb.getPosition()];
    }
    
    public String getString(String fieldName, String index){
        sb = (subHeader)dbf_columns.get(fieldName);
        if(sb == null){
            return "";
        }
        
        if(dbf_index == null){
            return getString(fieldName);
        }
        
        if(dbf_index.get(index) == -1){
            return "";
        }
        return dbf_record[dbf_index.get(index)].raw_data[sb.getPosition()];
    }
    
    public boolean isRecDel(){
        return dbf_record[currentPos].is_deleted;
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
        if(currentPos == dbf_header.getNoOfRecords()){
            currentPos--;
            eof = true;
            return false;
        }
        return true;
    }
    
    public void moveFirst() {
        currentPos = 0;
        eof = false;
    }
    
    public boolean getEOF(){
        return eof;
    }

    public boolean getBOF(){
        return bof;
    }
    
    public int length(){
        return dbf_record.length;
    }
    
    public String[] getColumnNames(){
        String[] str = new String[dbf_a_columns.length];
        for(int i=0;i<str.length;i++){
            str[i] = dbf_a_columns[i].getFieldName();
        }
        return str;
    }
    
    public Object[] getRecordData(){
        return dbf_record[currentPos].raw_data;
    }
    
    public dbf_recordset filter(String fieldName, String filter){
        sb = (subHeader)dbf_columns.get(fieldName);
        int col = sb.getPosition();
        int length = dbf_record.length;
        
        Vector tmpVector = new Vector();
        for(int i=0;i<length;i++){
            if(dbf_record[i].raw_data[col].equals(filter)){
                tmpVector.add(dbf_record[i]);
            }            
        }    
        
        dbf_recordset rs = new dbf_recordset(dbf_columns, dbf_a_columns);
        record[] tmpR = new record[tmpVector.size()];
        tmpVector.toArray(tmpR);
        rs.setRecords(tmpR);
        return rs;
    }
    
    public String getValueFromFilter(
        String fieldName, String filterFieldName, String filter
    ){
        sb = (subHeader)dbf_columns.get(fieldName);
        int col = sb.getPosition();
        
        sb = (subHeader)dbf_columns.get(filterFieldName);
        int fcol = sb.getPosition();
        
        int length = dbf_record.length;
        
        Vector tmpVector = new Vector();
        for(int i=0;i<length;i++){
            if(dbf_record[i].raw_data[col].equals(filter)){
                return dbf_record[i].raw_data[fcol];
            }            
        }    
        
        return null;        
    }
    
    /**
     *  public void setIndex(string fieldName)
     *  
     *  The Field Name MUST be unique!
     *
     */
    public void setIndex(String fieldName){
        dbf_index = new StringIntHashtable(dbf_record.length * 2);

        subHeader sb = (subHeader)dbf_columns.get(fieldName);

        int col = sb.getPosition();
        int length = dbf_record.length;
        
        for(int i=0;i<length;i++){
            dbf_index.add(dbf_record[i].raw_data[col], i);
        }    
    }
    
    /** The header class allows us to keep track of the header information  */
    class header{
        int file_type;
        int table_flags;
       
        String last_updated; 
        
        int no_of_records;
        int position_of_first_data_record;
        int length_of_data_record;
        
        /**
         *  Our header is split up as follows.
         * --------------------------------------------------------------------
         *   Byte Offset    Description
         * --------------------------------------------------------------------
         *      00              Type of file
         *      01-03           Last Updated (YYMMDD)
         *      04-07           Number of records in file
         *      08-09           Position of first data record
         *      10-11           Length of one data record (inc del flag)
         *      12-27           Reserved
         *      28              Table Flags
         *      29              Code Page Mark
         *      30-31           Reserved contains 0x00           
         *
         */        
        
        /** Static Offsets */
        final static int OFF_FILE_TYPE              =  0;
        final static int OFF_LAST_UPDATED           =  1;
        final static int OFF_NUMBER_OF_RECORDS      =  4;
        final static int OFF_POS_OF_FIRST_REC       =  8;
        final static int OFF_LENGTH_OF_DATA_RECORD  = 10;
        final static int OFF_TABLE_FLAGS            = 28;
        final static int OFF_CODE_PAGE_MARK         = 29;

        /** Static Lengths */
        final static int LEN_FILE_TYPE              = 1;
        final static int LEN_LAST_UPDATED           = 3;
        final static int LEN_NUMBER_OF_RECORDS      = 4;
        final static int LEN_POS_OF_FIRST_REC       = 2;
        final static int LEN_LENGTH_OF_DATA_RECORD  = 2;
        final static int LEN_TABLE_FLAGS            = 1;
        final static int LEN_CODE_PAGE_MARK         = 1;

        public header(byte[] b){            
            parseFileType(b[OFF_FILE_TYPE]);            
            
            parseLastUpdated(
                getSeg(b, OFF_LAST_UPDATED, LEN_LAST_UPDATED)
            );
            
            parseNoOfRecords(
                getSeg(b, OFF_NUMBER_OF_RECORDS, LEN_NUMBER_OF_RECORDS)
            );
            
            parsePosOfFirstRecord(
                getSeg(b, OFF_POS_OF_FIRST_REC, LEN_POS_OF_FIRST_REC)
            );
            
            parseLengthOfDataRecord(
                getSeg(b, OFF_LENGTH_OF_DATA_RECORD, LEN_LENGTH_OF_DATA_RECORD)
            );
        }        

        private void parseFileType(byte b){
            file_type = (int)b;
        }
        
        private void parseLastUpdated(byte[] b){
            last_updated = "20" +
                           String.valueOf(b[0]) + "-" +
                           String.valueOf(b[1]) + "-" +
                           String.valueOf(b[2]);
        }
        
        private void parseNoOfRecords(byte[] b){            
            no_of_records  = 0;
            no_of_records += ((int)b[3] & 0xFF) << 24;
            no_of_records += ((int)b[2] & 0xFF) << 16;
            no_of_records += ((int)b[1] & 0xFF) << 8;
            no_of_records += ((int)b[0] & 0xFF) << 0;
        }
        
        private void parsePosOfFirstRecord(byte[] b){
           position_of_first_data_record  = 0;
           position_of_first_data_record += ((int)b[1] & 0xFF) << 8;
           position_of_first_data_record += ((int)b[0] & 0xFF) << 0;
        }

        private void parseLengthOfDataRecord(byte[] b){
            length_of_data_record  = 0;
            length_of_data_record += ((int)b[1] & 0xFF) << 8;
            length_of_data_record += ((int)b[0] & 0xFF) << 0;
        }
        
        /** get stuff */    
        public int getNoOfRecords()       {return no_of_records;}
        public int getLengthOfDataRecord(){return length_of_data_record;}
       
    }

    /** subHeaders allows for you column names and type */
    public class subHeader{
        String field_name;
        char field_type;
        int displacement_of_field;
        int length_of_field;
        int no_of_decimal_places;
        int field_flags;
        
        int myPosition;
        
        /**
         *  Our Sub-header is split up as follows.
         * --------------------------------------------------------------------
         *   Byte Offset    Description
         * --------------------------------------------------------------------
         *      00-10           Field name (Maximum 10 char)
         *      11              Field Type
         *      12-15           Displacement of Field In Reacord
         *      16              Length of field
         *      17              number of decimal places
         *      18              Field Flags
         *      19-32           Reserved
         *
         *
         */

        /** Static Offsets */
        static final int OFF_FIELD_NAME                 =  0;
        static final int OFF_FIELD_TYPE                 = 11;
        static final int OFF_DISPLACEMENT_OF_FIELD      = 12;
        static final int OFF_LENGTH_OF_FIELD            = 16;
        static final int OFF_NUMBER_OF_DECIMAL_PLACES   = 17;
        static final int OFF_FIELD_FLAGS                = 18;
        
        /** Static Lengths */
        static final int LEN_FIELD_NAME                 = 10;
        static final int LEN_FIELD_TYPE                 =  1;
        static final int LEN_DISPLACEMENT_OF_FIELD      =  4;
        static final int LEN_LENGTH_OF_FIELD            =  1;
        static final int LEN_NUMBER_OF_DECIMAL_PLACES   =  1;
        static final int LEN_FIELD_FLAGS                =  1;
        
        /** Static Field Types 
         *    NOTE:  These are not all of the types!
         */
        
        static final char FT_CHAR       = 'C';
        static final char FT_NUMERIC    = 'N';
        static final char FT_DATE       = 'D';
        static final char FT_DATE_TIME  = 'T';
        
        public subHeader(byte[] b, int position){
            myPosition = position;
            
            parseFieldName(
                getSeg(b, OFF_FIELD_NAME, LEN_FIELD_NAME)                
            );
            
            parseFieldType(
                getSeg(b, OFF_FIELD_TYPE, LEN_FIELD_TYPE)                
            );
            
            parseDisplacementOfField(
                getSeg(b, OFF_DISPLACEMENT_OF_FIELD, LEN_DISPLACEMENT_OF_FIELD)                            
            );
            
            parseLengthOfField(
                getSeg(b, OFF_LENGTH_OF_FIELD, LEN_LENGTH_OF_FIELD)
            );
            
            parseNumberOfDecimalPlaces(
                getSeg(b, OFF_NUMBER_OF_DECIMAL_PLACES, LEN_NUMBER_OF_DECIMAL_PLACES)
            );
            
            parseFieldFlags(
                getSeg(b, OFF_FIELD_FLAGS, LEN_FIELD_FLAGS)
            );
        }
        
        private void parseFieldName(byte[] b){
            field_name = "";
            for(int i = 0;i<10;i++){
                if(b[i] != 0x00){
                    field_name += (char)b[i];
                }else{
                    break;
                }
            }
        }
        
        private void parseFieldType(byte[] b){
            field_type = (char)b[0];
        }
        
        private void parseDisplacementOfField(byte[] b){
            //not really usefull, location in memory I think
        }
        
        private void parseLengthOfField(byte[] b){
            length_of_field = b[0];
        }
        
        private void parseNumberOfDecimalPlaces(byte[] b){
            no_of_decimal_places = b[0];
        }
        
        private void parseFieldFlags(byte[] b){
            //not usefull at the moment
        }
        
        /** our get functions */
        public String getFieldName()    {return field_name;}
        public char   getFieldType()    {return field_type;}
        public int    getPosition()     {return myPosition;}
        public int    getLengthOfField(){return length_of_field;}
        
        /** type functions */
        public boolean isDateTime() {return(field_type == FT_DATE_TIME);}
    }

    /** this is our record class that holds our basic information */
    public class record{
        public boolean is_deleted;
        public String[] raw_data;
        
        public record(byte[] b){
            raw_data = new String[dbf_a_columns.length];
            int offset = 1;
            int len = 0;
            char currChar;
            
            is_deleted = (b[0] == 0x2A)  ? true : false;
            
            for(int i=0;i<dbf_a_columns.length;i++){                
                len = dbf_a_columns[i].getLengthOfField();
                byte btmp[] = getSeg(b, offset, len);
                String stmp = "";
                
                /**
                 * The DateTime ('T') is 4bit julian and 4bit millisec time.
                 *
                 * We shall return "YYYY-MM-DD HH:MM:SS"
                 *
                 */
                if(dbf_a_columns[i].isDateTime()){
                    int itmp = 0;
                    for(int a=0;a<4;a++){
                        itmp += ((int)btmp[a] & 0xFF) << (a*8);
                    }
                    stmp = (itmp==0) ? "1000-10-10" : convertJulian(itmp);

                    itmp = 0;
                    for(int a=5;a<8;a++){
                        itmp += ((int)btmp[a] & 0xFF) << (a*8);
                    }
                    stmp += " ";
                    stmp += (itmp==0) ? "00:00:00" : convertMSTime(itmp);
                }else{
                    for(int a=0;a<len;a++){
                        currChar = (char)btmp[a]; 
                        if (currChar != '\'') { //Take out any ' that will cause problems w/ sql!
                            stmp += currChar;
                        }
                    }
                }
                
                raw_data[i] = stmp;
                offset += len;
            }
        }
        
    }
    
    private String convertJulian(int jd){
        long l, n, i, j;
         int d, m, y;
        
        l = jd + 68569;
        n = ( 4 * l ) / 146097;
        l = l - (( (146097 * n) + 3 ) / 4);
        i = ( 4000 * ( l + 1 ) ) / 1461001;
        l = l - (( 1461 * i ) / 4) + 31;
        j = ( 80 * l ) / 2447;
        d = (int)(l - (( 2447 * j ) / 80));
        l = j / 11;
        m = (int)(j + 2 - ( 12 * l ));
        y = (int)((100 * ( n - 49 )) + i + l);
        
        String date = "";
        date += (y < 10) ? "0"+String.valueOf(y) : String.valueOf(y); 
        date += "-";
        date += (m < 10) ? "0"+String.valueOf(m) : String.valueOf(m); 
        date += "-";
        date += (d < 10) ? "0"+String.valueOf(d) : String.valueOf(d); 

        return(date);
    }
    
    private String convertMSTime(int tm){
        int s, h, m;
        String time;
        
        h = (tm/1000/60/60);
        tm -= h * 1000 * 60 * 60;
        m = (tm/1000/60) % 60;
        if (m == 59) {
            h+=1;
            m=0;
        }
        tm -= m * 1000 * 60;
        s = (tm/1000)    % 60;
        

        time  = (h < 10) ? "0"+String.valueOf(h) : String.valueOf(h);
        time += ":";
        time += (m < 10) ? "0"+String.valueOf(m) : String.valueOf(m);
        time += ":";
        time += (s < 10) ? "0"+String.valueOf(s) : String.valueOf(s);
            
        
        return(time);                 
    }
    
    /** getSeg is used to extract the needed information form the byte[] */
    private byte[] getSeg(byte[] b, int offset, int length){
        byte[] tmp = new byte[length];
        System.arraycopy(b, offset, tmp, 0, length);
        return tmp;
    }
}
