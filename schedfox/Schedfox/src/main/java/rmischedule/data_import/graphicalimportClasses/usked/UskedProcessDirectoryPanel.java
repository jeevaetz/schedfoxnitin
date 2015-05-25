/*
 * UskedProcessDirectoryPanel.java
 *
 * Created on March 1, 2006, 11:55 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.graphicalimportClasses.usked;
import java.util.*;
import java.io.*;
import rmischedule.components.data.*;
import rmischedule.data_import.dataImportClasses.usked.*;
import rmischedule.data_import.graphicalimportClasses.*;
import rmischedule.data_import.dataImportClasses.oneshotupdates.*;
/**
 *
 * @author Ira Juneau
 */
public class UskedProcessDirectoryPanel extends GenericProcessDirectoryPanel {
    
    private ArrayList<String> myListOfValidUskedDirectories;
    private ArrayList<FindNextUskedCompany> myCompanies;
    
    /** Creates a new instance of UskedProcessDirectoryPanel */
    public UskedProcessDirectoryPanel() {
        myListOfValidUskedDirectories = new ArrayList();
        myCompanies = new ArrayList();
    }
    
    private void processDir(String mainUskedDir){
        java.io.File dir = new java.io.File(mainUskedDir);
        if(dir.exists() && dir.isDirectory()){
            String[] list = dir.list();
            for(int i=0;i<list.length;i++){
                java.io.File tf = new java.io.File(mainUskedDir + list[i]);
                if(tf.isDirectory()){
                    java.io.File tf2 = new java.io.File(
                        mainUskedDir + list[i] + "/setmast.dbf"
                    );
                    if(tf2.exists()){
                        myListOfValidUskedDirectories.add(mainUskedDir + list[i]);
                    }                    
                }
            }
        }
    }
    
    public void showMe() {
        processDir(parent.getDirectory());
        addComponentsForUsked();
    }
    
    public void doOnNext() {
        ArrayList<uskedDataSource> mySelectedComps = new ArrayList();
        for (int i = 0; i < myCompanies.size(); i++) {
            if (myCompanies.get(i).isSelected()) {
                import_birthdates_class myimportbdate = new import_birthdates_class();
                myimportbdate.updateBirthdatesForCompany(myCompanies.get(i).getSelectedCompany().getId(), myCompanies.get(i).getSelectedBranch().getBranchId() + "", myCompanies.get(i).getDirectory());
                //mySelectedComps.add(myCompanies.get(i).getDataSource());
            }
        }
//        System.out.println("");
    }
    
    private void addComponentsForUsked() {
        for (int i = 0; i < myListOfValidUskedDirectories.size(); i++) {
            FindNextUskedCompany myNewPanel = new FindNextUskedCompany(myListOfValidUskedDirectories.get(i));
            myCompanies.add(myNewPanel);
            controlPanel.add(myNewPanel);
        }
    }
}
