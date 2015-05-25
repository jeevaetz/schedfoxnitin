/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileFormData implements Serializable, Comparable {

    private Integer mobileFormDataId;
    private Integer mobileFormsId;
    private String dataLabel;
    private Integer dateType;
    private Boolean active;
    private Integer ordering;
    private String defaultValue;
    private Boolean showInSummary;

    public MobileFormData() {
        active = true;
    }

    public MobileFormData(Record_Set rst) {
        try {
            mobileFormDataId = rst.getInt("mobile_form_data_id");
        } catch (Exception exe) {
        }
        try {
            mobileFormsId = rst.getInt("mobile_forms_id");
        } catch (Exception exe) {
        }
        try {
            dataLabel = rst.getString("data_label");
        } catch (Exception exe) {
        }
        try {
            dateType = rst.getInt("date_type");
        } catch (Exception exe) {
        }
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {
        }
        try {
            ordering = rst.getInt("ordering");
        } catch (Exception exe) {
        }
        try {
            defaultValue = rst.getString("default_value");
        } catch (Exception exe) {
        }
        try {
            showInSummary = rst.getBoolean("show_in_summary");
        } catch (Exception exe) {
        }
    }

    /**
     * @return the mobileFormDataId
     */
    public Integer getMobileFormDataId() {
        return mobileFormDataId;
    }

    /**
     * @param mobileFormDataId the mobileFormDataId to set
     */
    public void setMobileFormDataId(Integer mobileFormDataId) {
        this.mobileFormDataId = mobileFormDataId;
    }

    /**
     * @return the mobileFormsId
     */
    public Integer getMobileFormsId() {
        return mobileFormsId;
    }

    /**
     * @param mobileFormsId the mobileFormsId to set
     */
    public void setMobileFormsId(Integer mobileFormsId) {
        this.mobileFormsId = mobileFormsId;
    }

    /**
     * @return the dataLabel
     */
    public String getDataLabel() {
        return dataLabel;
    }

    /**
     * @param dataLabel the dataLabel to set
     */
    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    /**
     * @return the dateType
     */
    public Integer getDateType() {
        return dateType;
    }

    /**
     * @param dateType the dateType to set
     */
    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the ordering
     */
    public Integer getOrdering() {
        return ordering;
    }

    /**
     * @param ordering the ordering to set
     */
    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MobileFormData) {
            try {
                MobileFormData data = (MobileFormData) o;
                int comp = this.getOrdering().compareTo(data.getOrdering());
                if (comp == 0) {
                    comp = this.getDataLabel().compareTo(data.getDataLabel());
                }
                return comp;
            } catch (Exception exe) {
            }
        }
        return 1;
    }

    public void setSelectionData(ArrayList<String> input) {
        StringBuilder newData = new StringBuilder();
        for (int s = 0; s < input.size(); s++) {
            if (s > 0) {
                newData.append("|");
            }
            newData.append(input.get(s));
        }
        this.setDefaultValue(newData.toString());
    }

    public ArrayList<String> getSelectionData() {
        if (this.getDateType().equals(6)) {
            try {
                String[] data = this.getDefaultValue().split("\\|");
                ArrayList<String> retVal = new ArrayList<String>();
                for (int d = 0; d < data.length; d++) {
                    retVal.add(data[d]);
                }
                return retVal;
            } catch (Exception exe) {
            }
        }
        return new ArrayList<String>();
    }

    /**
     * @return the showInSummary
     */
    public Boolean getShowInSummary() {
        return showInSummary;
    }

    /**
     * @param showInSummary the showInSummary to set
     */
    public void setShowInSummary(Boolean showInSummary) {
        this.showInSummary = showInSummary;
    }

    public String getReadableLabel() {
        try {
            Matcher m = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(getDataLabel());
            StringBuffer stringbf = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(stringbf, m.group(1).toUpperCase() + m.group(2).toLowerCase());
            }
            return stringbf.toString().replaceAll("_", " ");
        } catch (Exception exe) {
            return this.getDataLabel();
        }
    }
}
