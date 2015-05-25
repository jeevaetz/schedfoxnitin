/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.data_connection_types;

import schedfoxlib.model.util.Record_Set;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Hashtable;
import java.util.zip.Deflater;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class PostgresRecord_Set extends Record_Set {

    public PostgresRecord_Set() {
        super();
    }

    public PostgresRecord_Set(ResultSet rst) {
        super(rst);
    }
    
    /**
     * Should be called immediately on each resultset so that we can generate and
     * compress the data...and free up all open connections!
     */
//    public void generateAndCompress() {
//        ResultSetMetaData myData = null;
//        int sizeOfResultSet = 0;
//        try {
//            myData = myResultSet.getMetaData();
//            columnSize = myData.getColumnCount();
//            myResultSet.last();
//            sizeOfResultSet = myResultSet.getRow();
//            myResultSet.first();
//        } catch (Exception noResultSet) {
//            columnSize = 0;
//        }
//        try {
//            columnHeaders = new String[columnSize];
//            myRecordset= new Hashtable(columnSize);
//            for (int i = 0; i < columnSize; i++) {
//                try {
//                    columnHeaders[i] = (myData.getColumnName(i + 1)).toLowerCase();
//                    myRecordset.put(columnHeaders[i], new Integer(i));
//                } catch (Exception e) {
//                    System.out.println("Error reading column headers.");
//                }
//            }
//            int initialSize = 20 * sizeOfResultSet * columnSize * 4;
//            byte[] myCurrentDataInBytes = null;
//            byte[] myUncompressedArray = new byte[initialSize];
//            int columnPos = 0;
//            int posInUnconArray = 0;
//            for (int i = 0; i < sizeOfResultSet * columnSize; i++) {
//                try {
//                    myCurrentDataInBytes = myResultSet.getBytes(columnPos + 1);
//                } catch (Exception e) {
//
//                } finally {
//                    if (myCurrentDataInBytes == null) {
//                        myCurrentDataInBytes = " ".getBytes();
//                    } else if (myCurrentDataInBytes.length == 0) {
//                        myCurrentDataInBytes = " ".getBytes();
//                    }
//                }
//                if (posInUnconArray + myCurrentDataInBytes.length + myTermByteSize > myUncompressedArray.length) {
//                    byte[] newArray = new byte[(posInUnconArray + myCurrentDataInBytes.length + myTermByteSize) * 2];
//                    System.arraycopy(myUncompressedArray, 0, newArray, 0, posInUnconArray);
//                    myUncompressedArray = newArray;
//                }
//                for (int writePos = 0; writePos < myCurrentDataInBytes.length; writePos++) {
//                    myUncompressedArray[posInUnconArray] = myCurrentDataInBytes[writePos];
//                    posInUnconArray++;
//                }
//                for (int writePos = 0; writePos < myTerminationByte.length; writePos++) {
//                    myUncompressedArray[posInUnconArray] = myTerminationByte[writePos];
//                    posInUnconArray++;
//                    uncompressedDataLength = posInUnconArray;
//                }
//                columnPos++;
//                if (columnPos >= columnSize) {
//                    try {
//                        myResultSet.next();
//                    } catch (Exception e) {}
//                    columnPos = 0;
//                }
//            }
//            Deflater myCompresser = new Deflater();
//            //myCompresser.setStrategy(Deflater.FILTERED);
//            myCompresser.setInput(myUncompressedArray);
//            myCompresser.finish();
//
//            byte[] tempComp = new byte[myUncompressedArray.length];
//            compressedDataLength = myCompresser.deflate(tempComp);
//
//
//            myCompressedData = new byte[compressedDataLength];
//            System.arraycopy(tempComp, 0, myCompressedData, 0, compressedDataLength);
//            //System.out.println("Size in bytes before: " + uncompressedDataLength + " after: " + compressedDataLength + " array size " + myCompressedData.length);
//        } catch (Exception exe) {
//            exe.printStackTrace();
//        } finally{
//            try {
//                myResultSet.close();
//            } catch (Exception e) {}
//        }
//    }
}
