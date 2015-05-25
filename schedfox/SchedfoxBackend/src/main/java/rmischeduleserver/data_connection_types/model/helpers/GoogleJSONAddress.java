/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class GoogleJSONAddress {
    //private List<GoogleJSONAddressComponents> address_components;
    //private String formatted_address;
    private GoogleJSONGeometry geometry;
    

//    /**
//     * @return the address_components
//     */
//    public List<GoogleJSONAddressComponents> getAddress_components() {
//        return address_components;
//    }
//
//    /**
//     * @param address_components the address_components to set
//     */
//    public void setAddress_components(List<GoogleJSONAddressComponents> address_components) {
//        this.address_components = address_components;
//    }
//
//    /**
//     * @return the formatted_address
//     */
//    public String getFormatted_address() {
//        return formatted_address;
//    }
//
//    /**
//     * @param formatted_address the formatted_address to set
//     */
//    public void setFormatted_address(String formatted_address) {
//        this.formatted_address = formatted_address;
//    }

    /**
     * @return the geometry
     */
    public GoogleJSONGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(GoogleJSONGeometry geometry) {
        this.geometry = geometry;
    }
}
