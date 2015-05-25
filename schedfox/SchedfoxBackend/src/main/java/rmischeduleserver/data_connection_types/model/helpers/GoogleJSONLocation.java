/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model.helpers;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public class GoogleJSONLocation {
    private BigDecimal lat;
    private BigDecimal lng;

    /**
     * @return the lat
     */
    public BigDecimal getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public BigDecimal getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }
}
