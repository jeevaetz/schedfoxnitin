/*
 * PEmployee.java
 *
 * Created on October 18, 2004, 7:43 AM
 */

package rmischeduleserver.util.xprint;

/**
 *
 * @author  ira
 * Container class for Employee information, makes it easier to change how employee
 * information is stored/retrieved
 */
public class PEmployee {
    
    private String Id, Name, Phone, Phone2, Address, Address2, City,State, Zip, Del;
    
    public PEmployee() {
        Id = new String();
        Name = new String();
        Phone = new String();
        Phone2 = new String();
        Address = new String();
        Address2 = new String();
        City = new String();
        State = new String();
        Zip = new String();
        Del = new String();
    }
    
    /** Creates a new instance of PEmployee */
    public void setData(String id, String name, String phone, String phone2, String address, String address2, String city, String state, String zip, String del) {
            Id = id;
            Name = name;
            Phone = phone;
            Phone2 = phone2;
            Address = address;
            Address2 = address2;
            City = city;
            State = state;
            Zip = zip;
            Del = del;
    }
    
    public String getId() {
        return Id;
    }
    
    public String getName() {
        return Name;
    }
    
    public String getPhone() {
        return Phone;
    }
    
    public String getPhone2() {
        return Phone2;
    }
    
    public String getAddress() {
        return Address;
    }
    
    public String getAddress2() {
        return Address2;
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
    
    public String getDel() {
        return Del;
    }
    
    public String toString() {
        return ("Id: " + Id + " Name: " + Name + " Phone: " + Phone + " Phone2 " + Phone2 + " Address: " + Address + " Address2: " + Address2 + " City: " + City + " State: " + State + " Zip: " + Zip + " Del: " + Del);

    }
}
