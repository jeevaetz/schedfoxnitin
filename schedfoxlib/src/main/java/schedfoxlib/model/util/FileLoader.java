/**
 * Filename: FileLoader.java Author: Jeffrey Davis Date Created: 09/23/2010
 * Modifications: Purpose of File: file contains a static class designed to
 * handle calls to and from the FileServer; modeled off ImageLoader
 */
//  package declaration
package schedfoxlib.model.util;

//  import declarations
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Class Name: FileLoader Purpose of Class: a static class designed to handle
 * calls to and from the FileServer; modeled off ImageLoader
 */
public class FileLoader {

    private static String getLocationOfImageServer() {
        return "http://schedfoximage.schedfox.com:8080/ImageServer/";
    }

    private static String[] setPrefixSufix(String fullPath) {
        String[] returnArray = new String[2];

        //  split path
        StringTokenizer st = new StringTokenizer(fullPath, "/");
        ArrayList<String> path = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            path.add(st.nextToken());
        }
        //  get filename
        String fileName = path.get(path.size() - 1);

        //  split up file
        Pattern p = Pattern.compile("\\.");
        String[] splitFileName = p.split(fileName);

        //  assign prefix sufix
        StringBuffer fullPrefix = new StringBuffer(0);
        for (int idx = 0; idx < splitFileName.length - 1; idx++) {
            fullPrefix.append(splitFileName[idx]);
        }
        returnArray[0] = fullPrefix.toString().trim();
        returnArray[1] = "." + splitFileName[splitFileName.length - 1].trim();

        return returnArray;
    }

    public static ArrayList<String> getFileNames(String companySchema, String key,
            String fileType) {
        //  declaration of list to return
        ArrayList<String> fileNames = new ArrayList<String>();

        //  set params
        String params = "?company=" + companySchema + "&key=" + key + "&type=" + fileType;

        //  set URL
        URL fileServerRetrieval = null;
        try {
            fileServerRetrieval = new URL(getLocationOfImageServer() + "GetFilesAssociatedServlet" + params);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) fileServerRetrieval.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try {
            urlConn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        //  conncet
        try {
            urlConn.connect();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  read input from server
        try {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while ((numBytesRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            while (myToken.hasMoreElements()) {
                fileNames.add(myToken.nextToken());
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  return fileName
        return fileNames;
    }

    public static String getFileURL(String fileName, String companySchema, String typeOfImage) {
        String params = "?file=" + fileName + "&company=" + companySchema + "&type=" + typeOfImage + "&key=";
        return getLocationOfImageServer() + "RetrieveFileServlet" + params;
    }

    public static File getOneFileByName(String companySchema, String fileName,
            String fileType) throws IOException {
        File retVal = null;

        URL imageServerRetrieval = new URL(getFileURL(fileName, companySchema, fileType));
        HttpURLConnection urlConn = (HttpURLConnection) imageServerRetrieval.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("GET");
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        urlConn.connect();
        BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
        retVal = File.createTempFile("tempFile", ".sched");
        FileOutputStream oStream = new FileOutputStream(retVal);
        byte[] buffer = new byte[2048];
        int numRead = 0;
        while ((numRead = (iStream.read(buffer))) > -1) {
            oStream.write(buffer, 0, numRead);
        }

        return retVal;
    }

    public static String getFileUrl(String fileName) {
        String params = null;
        try {
            params = "?fullFilePath=" + URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return getLocationOfImageServer() + "RetrieveAdditionalFilesServlet" + params;

    }

    public static File getFileFromServer(String fileName) {
        File retVal = null;

        String[] prefixSufix = FileLoader.setPrefixSufix(fileName);
        String prefix = prefixSufix[0];
        String sufix = prefixSufix[1];


        //  declaration of file to retrieve, set params
        File retrievedFile = null;


        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try {
            URL fileServerRetrieval = new URL(getFileUrl(fileName));
            urlConn = (HttpURLConnection) fileServerRetrieval.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setDoInput(true);
//        urlConn.setDoOutput(true);
        try {
            urlConn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
//        urlConn.setRequestProperty("Connection", "Keep-Alive");

        //  conncet
        try {
            urlConn.connect();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  read input from server
        InputStream iStream = null;
        try {
            iStream = urlConn.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            retrievedFile = File.createTempFile(prefix, sufix);
            retrievedFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(retrievedFile.getAbsolutePath());
            byte[] buffer = new byte[2048];
            int data = -1;
            //Yes this looks silly but I guess there is a java error http://stackoverflow.com/questions/10333257/java-io-ioexception-premature-eof-while-downloading-pdf-over-https
            data = iStream.read();
            while (data > -1) {
                fos.write(data);
                try {
                    data = iStream.read();
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
            fos.flush();
            fos.close();
            iStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        retVal = retrievedFile;
        return retVal;
    }

    public static ArrayList<File> getFiles(ArrayList<String> fileLocations) {
        ArrayList<File> fileArray = new ArrayList<File>();

        //  iterate through fileLocations, loading files into fileArray
        for (int idx = 0; idx < fileLocations.size(); idx++) {

            String[] prefixSufix = FileLoader.setPrefixSufix(fileLocations.get(idx));
            String prefix = prefixSufix[0];
            String sufix = prefixSufix[1];


            //  declaration of file to retrieve, set params
            File retrievedFile = null;


            //  set HTTP connection
            HttpURLConnection urlConn = null;
            try {
                URL fileServerRetrieval = new URL(getFileUrl(fileLocations.get(idx)));
                urlConn = (HttpURLConnection) fileServerRetrieval.openConnection();
            } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            try {
                urlConn.setRequestMethod("GET");
            } catch (ProtocolException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            urlConn.setRequestProperty("Connection", "Keep-Alive");

            //  conncet
            try {
                urlConn.connect();
            } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

            //  read input from server
            BufferedInputStream iStream = null;
            try {
                iStream = new BufferedInputStream(urlConn.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                retrievedFile = File.createTempFile(prefix, sufix);
                retrievedFile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(retrievedFile.getAbsolutePath());
                byte[] buffer = new byte[2048];
                int readAmt = -1;
                while ((readAmt = iStream.read(buffer)) > -1) {
                    fos.write(buffer, 0, readAmt);
                }
                fos.flush();
                fos.close();
                iStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //  add to data structure
            if (retrievedFile != null) {
                fileArray.add(retrievedFile);
            }
        }

        return fileArray;
    }

    public static boolean saveAdditionalFile(String fileType, String companySchema,
            String key, String fileName, File fileToSave) {
        boolean isUploadSuccessfull = true;

        //  setup client/post
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getLocationOfImageServer() + "SaveAdditionalFilesServlet");

        //  set multipart
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        //  add parts
        try {
            reqEntity.addPart("company", new StringBody(companySchema));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            reqEntity.addPart("type", new StringBody(fileType));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            reqEntity.addPart("fullFile", new StringBody(fileName));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            reqEntity.addPart("key", new StringBody(key));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileBody bin = new FileBody(fileToSave);
        reqEntity.addPart("attachment_field", bin);

        //  set entity
        httppost.setEntity(reqEntity);

        //  execute
        Object response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  check result
        String result = response.toString();
        if (result.length() == 0 || result.substring(0, 6).matches("Error:")) {
            isUploadSuccessfull = false;
        }

        return isUploadSuccessfull;
    }

    //RemoveAdditionalFilesServlet?company=little_caesars_pizza0_db&type=remove_additional_employee_files&fileSeperator=_&key=297&user=2284&fullFile=Jeff_Davis_Stack_Dump_06_22.txt
    public static boolean removeAdditionalFile(String companySchema, String type,
            String fileSeperator, String key, String user_id, String fileName) {
        boolean isSuccessfull = true;
        String params = null;

        //  set params
        try {
            params = getLocationOfImageServer() + "RemoveAdditionalFilesServlet"
                    + "?company=" + URLEncoder.encode(companySchema, "UTF-8")
                    + "&type=" + URLEncoder.encode(type, "UTF-8")
                    + "&fileSeperator=" + URLEncoder.encode(fileSeperator, "UTF-8")
                    + "&key=" + URLEncoder.encode(key, "UTF-8")
                    + "&user=" + URLEncoder.encode(user_id, "UTF-8")
                    + "&fullFile=" + URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  create URL, set
        URL removeAdditionalFilesServer = null;
        try {
            removeAdditionalFilesServer = new URL(params);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            isSuccessfull = false;
        }

        //  create HTTP connection, set
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) removeAdditionalFilesServer.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            isSuccessfull = false;
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);

        //  connect
        try {
            urlConn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            isSuccessfull = false;
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        try {
            urlConn.connect();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            isSuccessfull = false;
        }

        //  read output from servlet
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int numBytesRead = 0;
        BufferedInputStream iStream = null;
        try {
            iStream = new BufferedInputStream(urlConn.getInputStream());
            while ((numBytesRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
//            if(temp.length() != 0 && temp.substring(0, 6).matches("Error:")) {
//                v = false;
//            }
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            isSuccessfull = false;
        }


//        // create URL, set
//        URL removeImageServer = null;
//        try {
//            removeImageServer = new URL(params);
//        }
//        catch(MalformedURLException ex) {
//            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
//            success = false;
//        }
//
//        //  create HTTP connection, set
//        HttpURLConnection urlConn = null;
//        try {
//            urlConn = (HttpURLConnection) removeImageServer.openConnection();
//        }
//        catch(IOException ex) {
//            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
//            success = false;
//        }
//        urlConn.setDoOutput(true);
//        urlConn.setDoInput(true);
//
//        //  connect
//        try {
//            urlConn.setRequestMethod("GET");
//        } catch (ProtocolException ex) {
//            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
//            success = false;
//        }
//        urlConn.setRequestProperty("Connection", "Keep-Alive");
//        try {
//            urlConn.connect();
//        } catch (IOException ex) {
//            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
//            success = false;
//        }
//        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int numBytesRead = 0;
//        BufferedInputStream iStream = null;
//        try {
//            iStream = new BufferedInputStream(urlConn.getInputStream());
//            while((numBytesRead = iStream.read(buffer)) > -1) {
//                oStream.write(buffer, 0, numBytesRead);
//            }
//
//            String temp = new String(oStream.toByteArray());
//            if(temp.length() != 0 && temp.substring(0, 6).matches("Error:")) {
//                success = false;
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
//            success = false;
//        }

        return isSuccessfull;
    }
};