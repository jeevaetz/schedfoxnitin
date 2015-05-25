/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

import java.util.ArrayList;

/**
 *
 * @author ijuneau
 */
public class DataMappingClass {
    private ArrayList<String> mappingValues;
    private String uskedId;
    private TableMapClass tableMapping;

    private boolean shouldDisplayInManualExport = true;

    public DataMappingClass(TableMapClass tableMap, String... mappings) {
        mappingValues = new ArrayList<String>();
        for (int m = 0; m < mappings.length; m++) {
            mappingValues.add(mappings[m]);
        }
        this.tableMapping = tableMap;
        this.uskedId = "";
    }

    public void setValue(String value) {
        tableMapping.addValue(value);
    }

    public ArrayList<String> getValues() {
        return tableMapping.getValues();
    }

    public String getValueAt(int row) {
        return tableMapping.getValueAt(row);
    }

    public ArrayList<String> getMappingValues() {
        return this.mappingValues;
    }

    /**
     * Returns only the first value from our mappings, useful for the template
     * generation.
     * @return String
     */
    public String getFirstValue() {
        String retVal = "";
        for (int i = 0; i < mappingValues.size(); i++) {
            if (i == 0) {
                retVal = mappingValues.get(i);
            }
        }
        return retVal;
    }

    public TableMapClass getTableMapping() {
        return this.tableMapping;
    }

    @Override
    public boolean equals(Object o) {
        boolean retVal = false;
        if (o instanceof String) {
            String compVal = (String)o;
            for (int m = 0; m < mappingValues.size(); m++) {
                if (compVal.equalsIgnoreCase(mappingValues.get(m))) {
                    retVal = true;
                }
            }
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.mappingValues != null ? this.mappingValues.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        return this.getFirstValue();
    }

    /**
     * @return the uskedId
     */
    public String getUskedId() {
        return uskedId;
    }

    /**
     * @param uskedId the uskedId to set
     */
    public void setUskedId(String uskedId) {
        this.uskedId = uskedId;
    }

    /**
     * @return the shouldDisplayInManualExport
     */
    public boolean isShouldDisplayInManualExport() {
        return shouldDisplayInManualExport;
    }

    /**
     * @param shouldDisplayInManualExport the shouldDisplayInManualExport to set
     */
    public void setShouldDisplayInManualExport(boolean shouldDisplayInManualExport) {
        this.shouldDisplayInManualExport = shouldDisplayInManualExport;
    }
}
