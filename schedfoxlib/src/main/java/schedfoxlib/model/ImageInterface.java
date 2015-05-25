/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public interface ImageInterface {
    public byte[] getDocumentData();
    public byte[] getThumbnailData();
    public byte[] getDocumentData(String companyId);
    public void setDocumentData(byte[] imageData);
    public void setThumbnailData(byte[] imageData);
    public String getMimeType();
    public void setMimeType(String mimeType);
    public BigDecimal getLatitude();
    public void setLatitude(BigDecimal lat);
    public void setLongitude(BigDecimal lat);
    public BigDecimal getLongitude();
}
