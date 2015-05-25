/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileFormDataFillout implements Serializable, ImageInterface {
    private Integer mobileFormDataFilloutId;
    private Integer mobileFormFilloutId;
    private Integer mobileFormDataId;
    private String mobileData;
    private byte[] mobileDataBytes;
    
    public MobileFormDataFillout() {
        
    }
    
    public MobileFormDataFillout(Record_Set rst) {
        try {
            mobileFormDataFilloutId = rst.getInt("mobile_form_data_fillout_id");
        } catch (Exception exe) {}
        try {
            mobileFormFilloutId = rst.getInt("mobile_form_fillout_id");
        } catch (Exception exe) {}
        try {
            mobileFormDataId = rst.getInt("mobile_form_data_id");
        } catch (Exception exe) {}
        try {
            mobileData = rst.getString("mobile_data");
        } catch (Exception exe) {}
        try {
            mobileDataBytes = rst.getByteArray("mobile_data_bytes");
        } catch (Exception exe) {}
    }

    /**
     * @return the mobileFormDataFilloutId
     */
    public Integer getMobileFormDataFilloutId() {
        return mobileFormDataFilloutId;
    }

    /**
     * @param mobileFormDataFilloutId the mobileFormDataFilloutId to set
     */
    public void setMobileFormDataFilloutId(Integer mobileFormDataFilloutId) {
        this.mobileFormDataFilloutId = mobileFormDataFilloutId;
    }

    /**
     * @return the mobileFormFilloutId
     */
    public Integer getMobileFormFilloutId() {
        return mobileFormFilloutId;
    }

    /**
     * @param mobileFormFilloutId the mobileFormFilloutId to set
     */
    public void setMobileFormFilloutId(Integer mobileFormFilloutId) {
        this.mobileFormFilloutId = mobileFormFilloutId;
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
     * @return the mobileData
     */
    public String getMobileData() {
        return mobileData;
    }

    /**
     * @param mobileData the mobileData to set
     */
    public void setMobileData(String mobileData) {
        this.mobileData = mobileData;
    }

    /**
     * @return the mobileDataBytes
     */
    public byte[] getMobileDataBytes() {
        return mobileDataBytes;
    }

    /**
     * @param mobileDataBytes the mobileDataBytes to set
     */
    public void setMobileDataBytes(byte[] mobileDataBytes) {
        this.mobileDataBytes = mobileDataBytes;
    }

    @Override
    public byte[] getDocumentData() {
        return this.mobileDataBytes;
    }

    @Override
    public byte[] getThumbnailData() {
        return this.mobileDataBytes;
    }

    @Override
    public byte[] getDocumentData(String companyId) {
        return this.mobileDataBytes;
    }

    @Override
    public void setDocumentData(byte[] imageData) {
        this.mobileDataBytes = imageData;
    }

    @Override
    public void setThumbnailData(byte[] imageData) {
        this.mobileDataBytes = imageData;
    }

    @Override
    public String getMimeType() {
        return "image/jpeg";
    }

    @Override
    public void setMimeType(String mimeType) {
        
    }

    @Override
    public BigDecimal getLatitude() {
        return new BigDecimal(0);
    }

    @Override
    public void setLatitude(BigDecimal lat) {
        
    }

    @Override
    public void setLongitude(BigDecimal lat) {
        
    }

    @Override
    public BigDecimal getLongitude() {
        return new BigDecimal(0);
    }
}
