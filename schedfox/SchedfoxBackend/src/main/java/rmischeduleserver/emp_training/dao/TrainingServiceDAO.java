/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.emp_training.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.emp_training.Catalog;
import rmischeduleserver.emp_training.CatalogListing;
import rmischeduleserver.emp_training.TrainingCourse;
import rmischeduleserver.emp_training.TrainingError;
import rmischeduleserver.emp_training.TrainingInfo;
import rmischeduleserver.emp_training.exception.TrainingException;

/**
 *
 * @author user
 */
public class TrainingServiceDAO {

    public TrainingInfo createTraining(String fName, String lName, String email) throws TrainingException {
        TrainingInfo retVal = new TrainingInfo();

        String userName = fName.toLowerCase().substring(0, 1) + lName.toLowerCase();
        if (userName.length() < 5) {
            userName += "x";
        }
        String password = userName;
        int i = 1;
        retVal = getTrainingCourses(fName, lName, userName, password, email);
        while (retVal.getTrainingError().getCreateAccountError() != 0) {
            String genUserName = fName.toLowerCase().substring(0, 1) + lName.toLowerCase() + i;
            String genPassword = userName;
            retVal = getTrainingCourses(fName, lName, genUserName, genPassword, email);
            i++;
            if (i > 20) {
                throw new TrainingException(TrainingException.ErrorType.BAD_PASSWORD,
                        "Could not fetch training info! Password may be incorrect.");
            }
        }
        return retVal;
    }

    /**
     *
     * @param usked_id
     * @return
     * @throws TrainingException
     */
    public CatalogListing getCatalogsForClient(String usked_id) throws TrainingException {
        CatalogListing retVal = new CatalogListing();

        String type = "ISO-8859-1";

        String clientCatalogUrl = IPLocationFile.getTRAINING_IP() + "/eLMS/SERVICES/client/client_catalog_listing.php?";
        try {
            clientCatalogUrl += "usked_id=" + URLEncoder.encode(usked_id, type);
        } catch (Exception e) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR, "Could not encode the URL properly, there may be a problem "
                    + "with the usked id provided.");
        }

        try {
            
            URL trainURL = new URL(clientCatalogUrl);
            URLConnection urlConn = trainURL.openConnection();

            urlConn.setDoInput(true);
            InputStream iStream = urlConn.getInputStream();
            byte[] buffer = new byte[2048];
            int numRead = 0;
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            while ((numRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numRead);
            }

            Digester digester = new Digester();
            digester.setValidating(false);
            digester.addObjectCreate("Courses", CatalogListing.class);
            digester.addObjectCreate("Courses/Catalog", Catalog.class);
            digester.addBeanPropertySetter("Courses/Catalog/catalog_id", "catalog_id");
            digester.addBeanPropertySetter("Courses/Catalog/catalog_name", "catalog_name");
            digester.addBeanPropertySetter("Courses/Catalog/catalog_test_url ", "catalog_test_url ");
            digester.addBeanPropertySetter("Courses/Catalog/catalog_manage_url ", "catalog_manage_url ");
            digester.addSetNext("Courses/Catalog", "addCatalog");

            retVal = (CatalogListing) digester.parse(new StringReader(new String(oStream.toByteArray())));

        } catch (MalformedURLException mue) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "The URL for the training program is not in the correct format!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "Could not read from the provided URL!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "Unknown error! Exception: " + e);
        }
        return retVal;
    }

    /**
     * Connects up to the training server and gets the current courses and
     * returns them in java objects.
     * @param fName First Name
     * @param lName Last Name
     * @param userName user Name
     * @param password Password
     * @param email Email
     * @return Vector<TrainingCourse>
     * @throws Exception
     */
    public TrainingInfo getTrainingCourses(String fName, String lName,
            String userName, String password, String email) throws TrainingException {
        TrainingInfo retVal = new TrainingInfo();

        String type = "ISO-8859-1";

        String trainingUrl = IPLocationFile.getTRAINING_IP() + "/eLMS/SERVICES/autologin.php?";
        try {
            trainingUrl += "fname=" + URLEncoder.encode(fName, type) + "&lname=" + URLEncoder.encode(lName, type);
            trainingUrl += "&uname=" + URLEncoder.encode(userName, type) + "&pw=" + URLEncoder.encode(password, type);
            trainingUrl += "&email=" + URLEncoder.encode(email, type);
        } catch (Exception e) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR, "Could not encode the URL properly, there may be a problem "
                    + "with the username provided.");
        }
        if (fName.trim().length() < 3) {
            throw new TrainingException(TrainingException.ErrorType.SHORT_USERNAME,
                    "First name is missing.");
        }
        if (lName.trim().length() < 3) {
            throw new TrainingException(TrainingException.ErrorType.SHORT_USERNAME,
                    "Last name is missing.");
        }
        if (email.trim().length() < 5) {
            throw new TrainingException(TrainingException.ErrorType.MISSING_EMAIL,
                    "Email is missing.");
        }
        try {
            URL trainURL = new URL(trainingUrl);
            URLConnection urlConn = trainURL.openConnection();

            urlConn.setDoInput(true);
            InputStream iStream = urlConn.getInputStream();
            byte[] buffer = new byte[2048];
            int numRead = 0;
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            while ((numRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numRead);
            }
            retVal = parseString(new String(oStream.toByteArray()));
        } catch (MalformedURLException mue) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "The URL for the training program is not in the correct format!");
        } catch (IOException ioe) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "Could not read from the provided URL!");
        } catch (Exception e) {
            throw new TrainingException(TrainingException.ErrorType.SERVER_ERROR,
                    "Unknown error!");
        }

        return retVal;
    }

    /**
     * Parses the string and breaks it down to the Vector<TrainingCourse>
     * @param xml String
     * @return Vector<TrainingCourse>
     */
    private TrainingInfo parseString(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            Document dom = db.parse(is);
            return parseDocument(dom);
        } catch (ParserConfigurationException pce) {
            throw new Exception("Problem setting up the XML parser. "
                    + "Exception: " + pce);
        } catch (SAXException se) {
            throw new Exception("Could not parse the XML. "
                    + "Exception: " + se);
        } catch (IOException ioe) {
            throw new Exception("Could not read in the output from the URL. "
                    + "Exception: " + ioe);
        }
    }

    /**
     * Returns the vector of training course information.
     * @param dom
     * @return
     */
    private Vector<TrainingCourse> getTrainingCourse(Document dom) {
        Vector<TrainingCourse> courseInfo = new Vector<TrainingCourse>();
        Element docEle = dom.getDocumentElement();

        //get a nodelist of elements
        NodeList coursesNodeList = docEle.getElementsByTagName("courses");
        if (coursesNodeList != null && coursesNodeList.getLength() > 0) {
            for (int i = 0; i < coursesNodeList.getLength(); i++) {
                Element coursesElement = (Element) coursesNodeList.item(i);
                NodeList courseList = coursesElement.getElementsByTagName("course");
                for (int c = 0; c < courseList.getLength(); c++) {
                    Element courseElement = (Element) courseList.item(c);
                    if (courseElement != null) {
                        TrainingCourse tc = getCourse(courseElement);
                        //add it to list
                        courseInfo.add(tc);
                    }
                }
            }
        }
        return courseInfo;
    }

    private TrainingError getTrainingError(Document dom) {
        TrainingError trainingError = new TrainingError();

        Element docEle = dom.getDocumentElement();
        NodeList errorsList = docEle.getElementsByTagName("errors");

        if (errorsList != null && errorsList.getLength() > 0) {
            for (int el = 0; el < errorsList.getLength(); el++) {
                Element errorElement = (Element) errorsList.item(el);
                NodeList loginError = errorElement.getElementsByTagName("loginError");
                NodeList accountError = errorElement.getElementsByTagName("createAccountError");
                if (loginError != null && loginError.getLength() > 0) {
                    for (int e = 0; e < loginError.getLength(); e++) {
                        Element loginErrorElement = (Element) loginError.item(e);
                        int error = 0;
                        try {
                            error = Integer.parseInt(loginErrorElement.getTextContent());
                        } catch (Exception exe) {
                        }
                        trainingError.setLoginError(error);
                    }
                }
                if (accountError != null && accountError.getLength() > 0) {
                    for (int e = 0; e < accountError.getLength(); e++) {
                        Element accountErrorElement = (Element) accountError.item(e);
                        int error = 0;
                        try {
                            error = Integer.parseInt(accountErrorElement.getTextContent());
                        } catch (Exception exe) {
                        }
                        trainingError.setCreateAccountError(error);
                    }
                }
            }
        }
        return trainingError;
    }

    /**
     * Parses the document and gets the XML, breaks it down to java objects
     * @param dom Document
     * @return Vector<TrainingCourse>
     */
    private TrainingInfo parseDocument(Document dom) {
        TrainingInfo trainingInfo = new TrainingInfo();
        trainingInfo.setTrainingCourses(this.getTrainingCourse(dom));
        trainingInfo.setTrainingError(this.getTrainingError(dom));
        return trainingInfo;
    }

    /**
     * Breaks down a singular XML entity into a training course.
     * @param el Element
     * @return TrainingCourse
     */
    private TrainingCourse getCourse(Element el) {
        TrainingCourse retVal = new TrainingCourse();
        retVal.setCourseName(el.getAttribute("name"));
        boolean testExists = false;
        try {
            testExists = Boolean.parseBoolean(el.getAttribute("testExists"));
        } catch (Exception e) {
        }
        retVal.setCourseExists(testExists);
        NodeList urlList = el.getElementsByTagName("url");
        for (int u = 0; u < urlList.getLength(); u++) {
            Element urlElement = (Element) urlList.item(u);
            retVal.setURL(urlElement.getFirstChild().getTextContent());
        }
        return retVal;
    }

    public static void main(String args[]) {
        String xml = "<data> ";
        xml += "<courses> ";
        xml += "<course name=\"Field Supervision\" testExists=\"true\">  ";
        xml += "    <url>http://192.168.1.143/eLMS/player.php?course_id=9</url>  ";
        xml += "</course>  ";
        xml += "</courses>  ";
        xml += "<errors>  ";
        xml += "    <loginError>2</loginError>  ";
        xml += "    <createAccountError>5</createAccountError>  ";
        xml += "</errors>  ";
        xml += "</data> ";

        TrainingServiceDAO testServiceDAO = new TrainingServiceDAO();
        try {
            //testServiceDAO.parseString(xml);
            testServiceDAO.createTraining("Ira", "Juneau", "irabjuneau@yahoo.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
