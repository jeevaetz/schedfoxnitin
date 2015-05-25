/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.new_user;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author Ira
 */
public class create_new_schema_for_company extends GeneralQueryFormat {

    private String schema;

    public create_new_schema_for_company() {
        myReturnString = new String();
    }

    public boolean isTableCreationQuery() {
        return true;
    }
    
    public String toString() {
        return "";
//        URL schemaURL = getClass().getResource("/GenerateSchema.sql");
//        File schemaFile = null;
//        FileInputStream schemaInputStream = null;
//        String schemaSQL = "";
//        try {
//            try {
//                schemaFile = new File(schemaURL.toURI());
//            } catch (Exception e) {
//                schemaFile = new File(schemaURL.getPath());
//            }
//            byte[] fileContents = new byte[(int) schemaFile.length()];
//            schemaInputStream = new FileInputStream(schemaFile);
//            schemaInputStream.read(fileContents);
//            schemaSQL = new String(fileContents);
//            schemaSQL = schemaSQL.replaceAll("generated_db", this.schema);
//            schemaSQL = schemaSQL.replaceAll("SET client_encoding = 'UTF8';", "");
//        } catch (Exception exe) {
//            exe.printStackTrace();
//        } finally {
//            try {
//                schemaInputStream.close();
//            } catch (Exception e) {
//            }
//        }
//        return schemaSQL;
    }

    public boolean hasAccess() {
        return true;
    }

    public void update(String schemaToCreate) {
        this.schema = schemaToCreate;
    }
}
