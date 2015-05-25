/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.AddressControllerInterface;
import com.google.gson.Gson;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.model.DistanceInfo;
import rmischeduleserver.control.model.DistanceObject;
import schedfoxlib.model.exception.NoMappingResultsException;
import rmischeduleserver.mysqlconnectivity.queries.address.check_if_address_distance_exists_query;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Address;
import schedfoxlib.model.Client;
import schedfoxlib.model.helpers.GoogleJSONResult;
import rmischeduleserver.mysqlconnectivity.queries.address.check_if_address_exists_query;
import rmischeduleserver.mysqlconnectivity.queries.address.get_addresses_for_employee_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.address.get_distance_calcs_for_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.address.get_lat_long_query;
import rmischeduleserver.mysqlconnectivity.queries.address.save_address_geocode_query;
import rmischeduleserver.mysqlconnectivity.queries.address.save_address_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.AddressGeocodeDistance;
import schedfoxlib.model.CalcedLocationDistance;

/**
 *
 * @author user
 */
public class AddressController implements AddressControllerInterface {

    private String companyId;
    private String geocodeURL = "http://maps.googleapis.com/maps/api/geocode/json?";
    private String distanceURL = "https://maps.googleapis.com/maps/api/distancematrix/json?mode=auto&sensor=false";
    private String mapURL = "http://maps.googleapis.com/maps/api/staticmap?";

    private AddressController(String companyId) {
        this.companyId = companyId;
    }

    public static AddressController getInstance(String companyId) {
        return new AddressController(companyId);
    }

    public static void main(String args[]) {
        AddressController addressController = getInstance("2");

        ClientController clientController = ClientController.getInstance("2");
        try {
            Client client1 = clientController.getClientById(4067);
            Client client2 = clientController.getClientById(3641);

            addressController.fetchLatLong(client1.getAddressObj());
            addressController.fetchLatLong(client2.getAddressObj());

            addressController.generateDistanceBetweenTwoPoints(client1.getAddressObj(), client2.getAddressObj());
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public String displayClientMap(Client client) {
        if (this.checkIfAddressLatLongExists(client.getAddressObj())) {
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            ArrayList<Address> empAddresses = new ArrayList<Address>();

            get_addresses_for_employee_branch_query query = new get_addresses_for_employee_branch_query();
            query.setCompany(companyId);
            query.setPreparedStatement(new Object[]{client.getBranchId()});

            try {
                Record_Set rst = conn.executeQuery(query, "");
                for (int c = 0; c < rst.length(); c++) {
                    empAddresses.add(new Address(rst));
                    rst.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this.displayAddressWithOutlyingAddresses(client.getAddressObj(), empAddresses);
        }
        return "";
    }

    private String displayAddressWithOutlyingAddresses(Address centerAddress, ArrayList<Address> outlyingAddresses) {
        String params = "center=" + centerAddress.getLatitude() + "," + centerAddress.getLongitude();
        params += "&zoom=10&size=400x400&sensor=false";
        params += "&markers=color:blue%7Clabel:E|"; //%7
        for (int i = 0; i < outlyingAddresses.size(); i++) {
            Address currAddress = outlyingAddresses.get(i);
            params += currAddress.getLatitude() + "," + currAddress.getLongitude() + "|";
        }
        return mapURL + params;
    }

    @Override
    public boolean checkIfAddressLatLongExists(Address address) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean retVal = false;

        check_if_address_exists_query query = new check_if_address_exists_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});
        query.update(address);

        try {
            Record_Set rst = conn.executeQuery(query, "");
            if (rst.length() > 0) {
                retVal = true;
                address.setLatitude(rst.getFloat("latitude"));
                address.setLongitude(rst.getFloat("longitude"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<Address> fetchLatLongsFromDatabase(ArrayList<Address> addresses) {
        for (int a = 0; a < addresses.size(); a++) {
            addresses.set(a, fetchLatLongFromDatabase(addresses.get(a)));
        }
        return addresses;
    }

    @Override
    public Address fetchLatLongFromDatabase(Address address) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_lat_long_query query = new get_lat_long_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getZip()});
        try {
            Record_Set rst = conn.executeQuery(query, "");
            if (rst.length() > 0) {
                address.setAddressGeocodeId(rst.getInt("address_geocode_id"));
                address.setLatitude(rst.getFloat("latitude"));
                address.setLongitude(rst.getFloat("longitude"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

    public HashMap<Integer, ArrayList<CalcedLocationDistance>> getAllDistanceInfoForBranch(int branchId) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        HashMap<Integer, ArrayList<CalcedLocationDistance>> retVal = new HashMap<Integer, ArrayList<CalcedLocationDistance>>();
        get_distance_calcs_for_branch_query query = new get_distance_calcs_for_branch_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{branchId});
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                CalcedLocationDistance locationDistance = new CalcedLocationDistance();
                locationDistance.setClientId(rst.getInt("client_id"));
                locationDistance.setEmployeeId(rst.getInt("employee_id"));
                locationDistance.setTravelDistance(rst.getInt("travel_distance"));
                locationDistance.setTravelDuration(rst.getInt("travel_duration"));

                if (retVal.get(rst.getInt("employee_id")) == null) {
                    retVal.put(rst.getInt("employee_id"), new ArrayList<CalcedLocationDistance>());
                }
                ArrayList<CalcedLocationDistance> currData = retVal.get(rst.getInt("employee_id"));
                currData.add(locationDistance);

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Address fetchLatLongFromGoogle(Address address) throws RetrieveDataException, NoMappingResultsException {
        StringBuilder response = new StringBuilder();
        try {
            String addressParam = "address=" + URLEncoder.encode(address.getAddress1() + " ", "UTF-8") + URLEncoder.encode(address.getAddress2() + " ", "UTF-8")
                    + URLEncoder.encode(address.getCity() + ", ", "UTF-8") + URLEncoder.encode(address.getState() + " ", "UTF-8")
                    + URLEncoder.encode(address.getZip(), "UTF-8")
                    + "&sensor=false";
            URL myUrl = new URL(geocodeURL + addressParam);
            URLConnection conn = myUrl.openConnection();
            InputStream iStream = conn.getInputStream();
            byte[] buffer = new byte[2048];
            int byteread = 0;

            while ((byteread = iStream.read(buffer)) > -1) {
                response.append(new String(buffer, 0, byteread));
            }
            iStream.close();

            GoogleJSONResult data = new Gson().fromJson(response.toString(), GoogleJSONResult.class);

            address.setLatitude(data.getResults().get(0).getGeometry().getLocation().getLat().floatValue());
            address.setLongitude(data.getResults().get(0).getGeometry().getLocation().getLng().floatValue());
            return address;
        } catch (Exception e) {
            if (response.indexOf("ZERO_RESULTS") > -1) {
                throw new NoMappingResultsException();
            } else {
                throw new RetrieveDataException();
            }
        }
    }

    public void generateDistanceBetweenTwoPoints(Address address1, Address address2) throws RetrieveDataException {
        try {
            if (!checkIfDistanceExists(address1, address2)) {
                String addressParam = "&origins=" + address1.getLatitude() + "," + address1.getLongitude()
                        + "&destinations=" + address2.getLatitude() + "," + address2.getLongitude();
                URL myUrl = new URL(distanceURL + addressParam);
                URLConnection conn = myUrl.openConnection();
                InputStream iStream = conn.getInputStream();
                byte[] buffer = new byte[2048];
                int byteread = 0;
                StringBuilder response = new StringBuilder();

                while ((byteread = iStream.read(buffer)) > -1) {
                    response.append(new String(buffer, 0, byteread));
                }
                iStream.close();

                DistanceObject data = new Gson().fromJson(response.toString(), DistanceObject.class);
                if (data.getStatus().equals("OVER_QUERY_LIMIT")) {
                    throw new RetrieveDataException();
                }
                for (int d = 0; d < data.getRows().size(); d++) {
                    for (int o = 0; o < data.getRows().get(d).getElements().size(); o++) {
                        DistanceInfo distInfo = data.getRows().get(d).getElements().get(o);

                        AddressGeocodeDistance addressDistance = new AddressGeocodeDistance();
                        addressDistance.setAddressGeocode1(address1.getAddressGeocodeId());
                        addressDistance.setAddressGeocode2(address2.getAddressGeocodeId());
                        try {
                            addressDistance.setTravelDistance((int) distInfo.getDistance().getValue());
                        } catch (Exception exe) {
                            addressDistance.setTravelDistance(-1);
                        }
                        try {
                            addressDistance.setTravelDuration((int) distInfo.getDuration().getValue());
                        } catch (Exception exe) {
                            addressDistance.setTravelDuration(-1);
                        }
                        this.persistDistanceBetween(addressDistance);

                    }
                }
                System.out.println(data);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public Address fetchLatLong(Address address) throws RetrieveDataException, NoMappingResultsException {
        address = fetchLatLongFromDatabase(address);
        if (address.getAddressGeocodeId() == null) {
            address = fetchLatLongFromGoogle(address);
        }
        return address;
    }

    public void persistDistanceBetween(AddressGeocodeDistance addressDistance) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_address_geocode_query query = new save_address_geocode_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});
        query.update(addressDistance);

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfDistanceExists(Address address1, Address address2) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean retVal = false;

        check_if_address_distance_exists_query addressExistsQuery = new check_if_address_distance_exists_query();
        addressExistsQuery.setCompany(companyId);
        addressExistsQuery.setPreparedStatement(new Object[]{address1.getAddressGeocodeId(), address2.getAddressGeocodeId()});

        try {
            Record_Set rst = conn.executeQuery(addressExistsQuery, "");
            if (rst.length() > 0) {
                retVal = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public void persistAddressLatLong(Address address) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_address_query query = new save_address_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});
        query.update(address);

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
