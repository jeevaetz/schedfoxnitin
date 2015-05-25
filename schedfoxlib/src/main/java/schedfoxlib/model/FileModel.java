/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

/**
 *
 * @author user
 */
public class FileModel {
    private byte[] fileContents;
    
    public FileModel() {
        
    }

    /**
     * @return the fileContents
     */
    public byte[] getFileContents() {
        return fileContents;
    }

    /**
     * @param fileContents the fileContents to set
     */
    public void setFileContents(byte[] fileContents) {
        this.fileContents = fileContents;
    }
}
