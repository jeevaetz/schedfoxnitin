/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

/**
 *
 * @author user
 */
public interface AddressInterface {
    public String getAddress1();
    public String getAddress2();
    public String getCity();
    public String getState();
    public String getZip();

    public boolean isValid();
}
