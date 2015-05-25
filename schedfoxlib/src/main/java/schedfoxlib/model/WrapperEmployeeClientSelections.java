/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.ArrayList;

/**
 *
 * @author ira
 */
public class WrapperEmployeeClientSelections {
    private ArrayList<Integer> selectedClientIds;
    private ArrayList<Integer> selectedEmployeeIds;
    
    public WrapperEmployeeClientSelections() {
        this.selectedClientIds = new ArrayList<Integer>();
        this.selectedEmployeeIds = new ArrayList<Integer>();
    }

    /**
     * @return the selectedClientIds
     */
    public ArrayList<Integer> getSelectedClientIds() {
        return selectedClientIds;
    }

    /**
     * @param selectedClientIds the selectedClientIds to set
     */
    public void setSelectedClientIds(ArrayList<Integer> selectedClientIds) {
        this.selectedClientIds = selectedClientIds;
    }

    /**
     * @return the selectedEmployeeIds
     */
    public ArrayList<Integer> getSelectedEmployeeIds() {
        return selectedEmployeeIds;
    }

    /**
     * @param selectedEmployeeIds the selectedEmployeeIds to set
     */
    public void setSelectedEmployeeIds(ArrayList<Integer> selectedEmployeeIds) {
        this.selectedEmployeeIds = selectedEmployeeIds;
    }
}
