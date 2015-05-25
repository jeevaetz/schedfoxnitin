/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mapping;

import schedfoxlib.model.AddressInterface;

/**
 *
 * @author user
 */
public class TestAddress implements AddressInterface {

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    public TestAddress(String address1, String address2, String city, String state, String zip) {
        this.address1 = address1;
        this.address2 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;

    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public boolean isValid() {
        return true;
    }



}
