/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import schedfoxlib.controller.FileControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.Company;
import schedfoxlib.model.FileModel;
import schedfoxlib.model.util.FileLoader;

/**
 *
 * @author user
 */
public class FileController implements FileControllerInterface {

    private String companyId;

    public static void main(String[] args) {
        try {
            FileController.getInstance("1852").getPostInstructionsForClient(3452);
        } catch (Exception exe) {}
    }
    
    private FileController(String companyId) {
        this.companyId = companyId;
    }

    public static FileController getInstance(String companyId) {
        return new FileController(companyId);
    }

    public ArrayList<String> getPostInstructionsForClient(Integer clientId) throws RetrieveDataException {
        try {
            Company comp = new CompanyController(companyId).getCompanyById(Integer.parseInt(companyId));
            return FileLoader.getFileNames(comp.getCompDB(), clientId.toString(), "location_additional_files");
        } catch (Exception exe) {
            return new ArrayList<String>();
        }
    }

    public FileModel getFile(String fileName) throws RetrieveDataException {
        try {
            File file = FileLoader.getFileFromServer(fileName);
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            FileInputStream iStream = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int numRead = 0;
            while ((numRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numRead);
            }
            FileModel fileModel = new FileModel();
            fileModel.setFileContents(oStream.toByteArray());
            return fileModel;
        } catch (Exception exe) {
            return new FileModel();
        }
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getPostInstructionsForClient(ArrayList<Integer> clientIds) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
