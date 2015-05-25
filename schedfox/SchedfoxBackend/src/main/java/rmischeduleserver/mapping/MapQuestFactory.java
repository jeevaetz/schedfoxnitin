/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mapping;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import schedfoxlib.model.AddressInterface;
import schedfoxlib.model.MappingLocation;
import rmischeduleserver.mapping.gson.LatLong;
import rmischeduleserver.mapping.gson.Location;

/** 
 *
 * @author user
 */
public class MapQuestFactory {

    private static final String key = "Fmjtd|luu22907nd,70%3Do5-5rylg";
    private static final String url = "http://www.mapquestapi.com";

    public static BufferedImage displayMap(double lat, double lon, int x, int y, int zoom) throws Exception {
        URL urlAddress = getMapURL(lat, lon, x, y, zoom);
        URLConnection urlConn = urlAddress.openConnection();
        urlConn.setDoInput(true);
        InputStream iStream = urlConn.getInputStream();
        BufferedImage image = ImageIO.read(iStream);
        iStream.close();
        return image;
    }

    public static URL getMapURL(double lat, double lon, int x, int y, int zoom) throws Exception {
        StringBuilder parameters = new StringBuilder();
        parameters.append("?key=").append(key);
        parameters.append("&size=").append(x + "," + y);
        parameters.append("&zoom=").append(zoom + "");
        parameters.append("&pois=").append("mcenter," + lat + "," + lon + "|");
        parameters.append("&center=").append(lat + "").append(",").append(lon + "");

        String address = url + "/staticmap/v3/getmap"
                + parameters.toString();
        return new URL(address);
    }

    public static URL getMapURL(DirectionResults direction,
            int x, int y, int zoom) throws Exception {
        LinkedList<Location> locations = direction.getRoute().getLocations();
        Location origination = locations.getFirst();
        Location destination = locations.getLast();
        

        StringBuilder parameters = new StringBuilder();
        parameters.append("?key=").append(key);
        parameters.append("&shape=").append(direction.getRoute().getShape().getShapePoints());
        parameters.append("&shapeformat=").append("cmp6");
        parameters.append("&size=").append("400,400");
        parameters.append("&type=").append("map");
        parameters.append("&type=").append("map");
        parameters.append("&zoom=").append("" + zoom);
        parameters.append("&scenter=").append(origination.getLatLong().getLat()).append(",").append(origination.getLatLong().getLng());
        parameters.append("&ecenter=").append(destination.getLatLong().getLat()).append(",").append(destination.getLatLong().getLng());

        String address = url + "/staticmap/v3/getmap"
                + parameters.toString();
        return new URL(address);
    }

    public static BufferedImage displayMap(DirectionResults direction,
            int x, int y, int zoom) throws Exception {

        URL urlAddress = getMapURL(direction, x, y, zoom);

        URLConnection urlConn = urlAddress.openConnection();
        urlConn.setDoInput(true);
        InputStream iStream = urlConn.getInputStream();
        BufferedImage image = ImageIO.read(iStream);
        iStream.close();
        return image;
    }

    public static DirectionResults displayDirections(AddressInterface location1,
            AddressInterface location2, int xOffSet, int yOffset, int zoom) throws Exception {

        StringBuilder parameters = new StringBuilder();
        parameters.append("?key=").append(key);
        parameters.append("&callback=").append("renderAdvancedNarrative");
        parameters.append("&avoidTimedConditions=").append("false");
        parameters.append("&routeType=").append("shortest");
        parameters.append("&narrativeType=").append("html");
        parameters.append("&enhancedNarrative=").append("true");
        parameters.append("&shapeFormat=").append("cmp6");
        parameters.append("&generalize=").append("0");
        parameters.append("&locale=").append("en_US");
        parameters.append("&unit=").append("m");
        if (location1.isValid()) {
            try {
                String locationStr = location1.getAddress1()
                        + " " + location1.getCity() + ", " + location1.getState()
                        + " " + location1.getZip();
                parameters.append("&from=").append(URLEncoder.encode(locationStr, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Problem encoding address for MapQuest");
            }
        }
        if (location2.isValid()) {
            try {
                String locationStr = location2.getAddress1()
                        + " " + location2.getCity() + ", " + location2.getState()
                        + " " + location2.getZip();
                parameters.append("&to=").append(URLEncoder.encode(locationStr, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Problem encoding address for MapQuest");
            }
        }

        String address = url + "/directions/v1/route"
                + parameters.toString();

        URL urlAddress = new URL(address);
        URLConnection urlConn = urlAddress.openConnection();
        urlConn.setDoInput(true);
        InputStream iStream = urlConn.getInputStream();

        String responseStr = "";
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int numRead = 0;
        while ((numRead = iStream.read(buffer)) > -1) {
            oStream.write(buffer, 0, numRead);
        }
        responseStr = new String(oStream.toByteArray());
        responseStr = responseStr.replace("renderAdvancedNarrative(", "").replace(");", "");

        Gson myGsonParser = new Gson();
        try {
            return myGsonParser.fromJson(responseStr, DirectionResults.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Geocodes an address from the Map Quest service
     * @param addresses
     * @throws Exception
     */
    public static ArrayList<MappingLocation> geocodeAddresses(ArrayList<AddressInterface> addresses) throws Exception {
        ArrayList<MappingLocation> retVal = new ArrayList<MappingLocation>();
        StringBuilder parameters = new StringBuilder();
        parameters.append("?key=").append(key);
        parameters.append("&callback=").append("renderGeocode");
        //parameters.append("&callback=").append(URLEncoder.encode("renderGeocode", "UTF-8"));
        for (int a = 0; a < addresses.size(); a++) {
            AddressInterface currAddress = addresses.get(a);
            if (currAddress.isValid()) {
                try {
                    String locationStr = currAddress.getAddress1()
                            + " " + currAddress.getCity() + ", " + currAddress.getState()
                            + " " + currAddress.getZip();
                    parameters.append("&location=").append(URLEncoder.encode(locationStr, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Problem encoding address for MapQuest");
                }
            }
        }

        String address = url + "/geocoding/v1/address"
                + parameters.toString();
        URL urlAddress = new URL(address);
        URLConnection urlConn = urlAddress.openConnection();
        urlConn.setDoInput(true);
        InputStream iStream = urlConn.getInputStream();

        String responseStr = "";
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int numRead = 0;
        while ((numRead = iStream.read(buffer)) > -1) {
            oStream.write(buffer, 0, numRead);
        }
        responseStr = new String(oStream.toByteArray());
        responseStr = responseStr.replace("renderGeocode(", "").replace(");", "");
        Gson myGsonParser = new Gson();
        try {
            GeoCodingResults geoResults =
                    myGsonParser.fromJson(responseStr, GeoCodingResults.class);
            LinkedList<GeoCodingResults.ResultObject> results = geoResults.getResults();
            LinkedList<Location> locations = results.get(0).getLocations();
            for (int a = 0; a < addresses.size(); a++) {
                LatLong latLong = locations.get(0).getLatLong();
                if (addresses.get(a).isValid()) {
                    MappingLocation location = new MappingLocation();
                    location.setLat(latLong.getLat());
                    location.setLon(latLong.getLng());
                    retVal.add(location);
                } else {
                    retVal.add(null);
                }
            }
        } catch (Exception e) {
            //Display no image.
        }
        return retVal;
    }

    public static void main(String args[]) {
        MapQuestFactory factory = new MapQuestFactory();
        ArrayList<AddressInterface> addresses = new ArrayList<AddressInterface>();
        TestAddress source = new TestAddress("1001 Lake Carolyn Pkwy", "", "Irving", "TX", "75039");
        TestAddress dest = new TestAddress("2601 W. 7th St", "", "fort Worth", "TX", "76107");
        try {
            factory.displayDirections(source, dest, 0, 0, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Gson myGsonParser = new Gson();
//        String jsonTest = "{\"results\":[{\"locations\":[";
//        jsonTest += "{\"latLng\":";
//	jsonTest += "{\"lng\":-99.346703,\"lat\":31.19688},";
//        jsonTest += "\"adminArea4\":\"\",\"adminArea5Type\":\"City\",\"adminArea4Type\":\"County\",\"adminArea5\":\"\",\"street\":\"\",";
//        jsonTest += "\"adminArea1\":\"US\",\"adminArea3\":\"TX\",\"type\":\"s\",\"displayLatLng\":{\"lng\":-99.346703,\"lat\":31.19688}";
//        jsonTest += "}]}]}";
//
//        GeoCodingResults geoResults =
//                myGsonParser.fromJson(jsonTest, GeoCodingResults.class);
//        System.out.println("Here");

    }

    public static String getKeyForRequests() {
        return key;
    }
}
