/*
 * PClient.java
 *
 * Created on October 18, 2004, 7:43 AM
 */

package rmischeduleserver.util.xprint;

/**
 *
 * @author  ira
 */
public class PClient {
    
    private String Id, Name, Address, City, State, Zip, Phone;
    
    /** Creates a new instance of PClient */
    public PClient( ) {
       
    }
    
    public void setData(String id, String name, String a, String C, String S, String Z, String P) {
        Id = id;
        Address = a;
        City = C;
        State = S;
        Zip = Z;
        Name = name;
        Phone = P;
    }
    
    public String getPhone() {
        return Phone;
    }
    
    public String getAddress() {
        return Address;
    }
    
    public String getCity() {
        return City;
    }
    
    public String getState() {
        return State;
    }
    
    public String getZip() {
        return Zip;
    }
    
    public String getId() {
        return Id;
    }
    
    public String getName() {
        return Name;
    }
    
    public String toString() {
        return ("Id: " + Id + " Name: " + Name);
    }
}
