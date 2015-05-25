/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.forms;
//import com.sun.star.beans.PropertyValue;
//import com.sun.star.comp.helper.BootstrapException;
//import com.sun.star.connection.NoConnectException;
//import com.sun.star.frame.XComponentLoader;
//import com.sun.star.frame.XStorable;
//import com.sun.star.io.IOException;
//import com.sun.star.lang.IllegalArgumentException;
//import com.sun.star.lang.XMultiComponentFactory;
//import com.sun.star.uno.UnoRuntime;
//import com.sun.star.uno.XComponentContext;
//import com.gnostice.pdfone.*;
//import java.util.List;
//import ooo.connector.BootstrapSocketConnector;
//import ooo.connector.server.OOoServer;
import java.net.*;
import rmischedule.messaging.sms.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.awt.print.*;
//import com.sun.pdfview.*;
import java.lang.Exception;
import java.awt.print.PrinterException;

/**
 *
 * @author hal
 */
public class FormsClient {
    private static final String OOO_EXEC_FOLDER = "/usr/bin";

    private static final String TEMPLATE_FOLDER = "/home/hal/Schedfox Docs/";
    private static final String TEXT_DOCUMENT_NAME = "DrugAndAlchohol";

    private static final String FILE_URL_PREFIX = "file:///";
    private static final String TEXT_DOCUMENT_EXTENSION = ".odt";
    private static final String PDF_DOCUMENT_EXTENSION = ".pdf";
    private URL url;
    private URLConnection urlCon;

    public static void main(String[] args){

// Must be used with the “RestrictPermissions” property

    FormsClient fc=new FormsClient();
//        fc.getConnection("Aetna.odt");
//        try{
//            fc.convertWithConnector("",TEMPLATE_FOLDER);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    fc.printForms("DrugAndAlchohol");

    }
    public void getConnection(String doc){
        char docBuf[]=new char[64000];
        ByteBuffer docBuf1;
        CharBuffer docBuf2=CharBuffer.allocate(64000);
        Buffer buf;
//        byte byteArray[]=new byte[64000];
        char charArray[]=new char[64000];

        int len=0;

        try{
           url=new URL("http://schedfoximage.schedfox.com:8080/ContractServer/loadContract?contract=aetna.odt&name=sam");
//            url=new URL("file:/home/hal/Schedfox Docs/DrugAndAlchohol.odt");
            urlCon=url.openConnection();
            InputStreamReader in1 = new InputStreamReader(urlCon.getInputStream());
//            FileOutputStream out=new FileOutputStream("/home/hal/test.pdf");
            //            FileInputStream fs=new FileInputStream("/home/hal/Schedfox Docs/DrugAndAlchohol.odt");

            len=in1.read(charArray);
            in1.close();

            byte byteArray[]=new byte[len];
            for(int i=0;i<len;i++)
                byteArray[i]=(byte)charArray[i];

//            out.write(byteArray);
//            out.close();
//            byte byteArray[]=new byte[fs.available()];
//            fs.read(byteArray);
            docBuf1=ByteBuffer.wrap(byteArray);

//            PDFFile pdf=new PDFFile(docBuf1);
//            PDFPrintPage page=new PDFPrintPage(pdf);
            PrinterJob pjob=PrinterJob.getPrinterJob();
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            pjob.setJobName("job");
            Book book = new Book();
//            book.append(page, pf, pdf.getNumPages());
            pjob.setPageable(book);

            Paper paper = new Paper();
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
            pf.setPaper(paper);
            pjob.print();

            in1.close();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();
        }catch(PrinterException pe){
            pe.printStackTrace();
        }

    }
    public void getConnection1(){

        try{
//            url=new URL("http","//192.168.1.35",80,"/rmischedule/Schedfox Docs/DrugAndAlchohol.odt");
            urlCon=url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
    
    }
    public void printForms(String textDocumentName){


        String loadUrl=FILE_URL_PREFIX+TEMPLATE_FOLDER+textDocumentName+TEXT_DOCUMENT_EXTENSION;
        String storeUrl;

//        try {
//            storeUrl=FILE_URL_PREFIX+TEMPLATE_FOLDER+textDocumentName+"SC"+PDF_DOCUMENT_EXTENSION;
//            convertWithStaticConnector(loadUrl, storeUrl);
//
////            storeUrl=FILE_URL_PREFIX+TEMPLATE_FOLDER+textDocumentName+"C"+PDF_DOCUMENT_EXTENSION;
////            convertWithConnector(loadUrl, storeUrl);
//        }
//        catch (NoConnectException e) {
//            System.out.println("OOo is not responding");
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            System.exit(0);
//        }
    }

//    private static void convertWithStaticConnector(String loadUrl, String storeUrl) throws Exception, IllegalArgumentException, IOException, BootstrapException {

        // Connect to OOo
//        XComponentContext remoteContext = BootstrapSocketConnector.bootstrap(OOO_EXEC_FOLDER);
//
//    rene olsted    // Convert text document to PDF
//        convert(loadUrl, storeUrl, remoteContext);
//    }

//    private static void convertWithConnector(String loadUrl, String storeUrl) throws Exception, IllegalArgumentException, IOException, BootstrapException {
//
//        loadUrl="http://schedfoximage.schedfox.com:8080/ContractServer/loadContract?contract=aetna.odt&name=sam";
//    //         Create OOo server with additional -nofirststartwizard option
//        List oooOptions = OOoServer.getDefaultOOoOptions();
//        oooOptions.add("-nofirststartwizard");
//        OOoServer oooServer = new OOoServer(OOO_EXEC_FOLDER, oooOptions);
//
//        // Connect to OOo
//        BootstrapSocketConnector bootstrapSocketConnector = new BootstrapSocketConnector(oooServer);
//        XComponentContext remoteContext = bootstrapSocketConnector.connect();
//
//        // Convert text document to PDF
//        convert(loadUrl, storeUrl, remoteContext);
//
//        // Disconnect and terminate OOo server
//       bootstrapSocketConnector.disconnect();
//}

//    protected static void convert(String loadUrl, String storeUrl, XComponentContext remoteContext) throws IllegalArgumentException, IOException, Exception {
//
//        XComponentLoader xcomponentloader = getComponentLoader(remoteContext);
//
//        Object objectDocumentToStore = xcomponentloader.loadComponentFromURL(loadUrl, "_blank", 0, new PropertyValue[0]);
//        PropertyValue[] conversionProperties = new PropertyValue[1];
//        conversionProperties[0] = new PropertyValue();
//        conversionProperties[0].Name = "FilterName";
//        conversionProperties[0].Value = "writer_pdf_Export";
//        XStorable xstorable = (XStorable) UnoRuntime.queryInterface(XStorable.class,objectDocumentToStore);
//        xstorable.storeToURL(storeUrl, conversionProperties);
//    }
//
//    private static XComponentLoader getComponentLoader(XComponentContext remoteContext) throws Exception {
//
//        XMultiComponentFactory remoteServiceManager = remoteContext.getServiceManager();
//        Object desktop = remoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", remoteContext);
//        XComponentLoader xcomponentloader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class,desktop);
//
//        return xcomponentloader;
//    }

}
