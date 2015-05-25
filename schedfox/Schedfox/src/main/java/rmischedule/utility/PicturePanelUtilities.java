//  package declaration
package rmischedule.utility;

//  import declarations
import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import schedfoxlib.model.util.ImageLoader;

/**
 * An object to currently handle saving and opening an image containted in
 * <code>PicturePanel</code> <p>This class is currently designed to both open
 * and save an image contained within an instance of
 * <code>PicturePanel</code>. <b>This is considered one operation by the class,
 * and is currently inseperable</b>. <p><i>CURRENT OPERATIONS SUPPORTED:
 * </i><b>1.)
 * <code>Operation.DOWNLOAD</code> (save image, open image)</b> <p><b>NOTE:
 * </b>This class runs in its own thread. <p><b>NOTE: </b>Class is designed to
 * allow the easy extension of additional operations
 *
 * @author Jeffrey N. Davis
 * @see rmischedule.components.PictureParentInterface
 * @see rmischedule.components.PicturePanel
 * @since 01/27/2011
 */
public final class PicturePanelUtilities extends Thread {
    //  private variable declarations

    private final Image image;
    private final Operation type;

    /*
     * list of allowed image extensions
     */
    private static final String GIF = "gif";
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String PNG = "png";

    /*
     * the preferred file extension for images
     */
    private static final String PREFERRED_IMAGE_EXTENSION = "jpg";

    /**
     * A list of allowed operations.
     */
    public static enum Operation {

        DOWNLOAD
    };

    //  private method implementations
    /**
     * Checks to ensure the requested operation is valid.
     *
     * @return isValid a boolean describing if the requested operation is valid
     */
    private final boolean isOperationValid() {
        boolean isOperationValid = false;

        if (this.type == Operation.DOWNLOAD) {
            isOperationValid = true;
        }

        return isOperationValid;
    }

    /**
     * Checks to see if a requested save file has a valid image extension
     *
     * @param fileName a string representing the file name to be checked
     * @return isValidExtension a boolean describing if the extension is valid
     */
    private final boolean hasProperImageExtension(String fileName) {
        boolean isValidExtension = false;

        //  get extension
        String extension = null;
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            extension = fileName.substring(index + 1).toLowerCase();
        }

        //  run comparisons
        if (extension != null) {
            if (extension.equalsIgnoreCase(GIF)
                    || extension.equalsIgnoreCase(JPG)
                    || extension.equalsIgnoreCase(JPEG)
                    || extension.equalsIgnoreCase(PNG)) {
                isValidExtension = true;
            }
        }

        return isValidExtension;
    }

    /**
     * Corrects the extension for an image save
     *
     * @param selectedFileName a string representing the uncorrected file name
     * @return correctedFileName a string representing the correct file name
     */
    private final String correctImageExtension(String selectedFileName) {
        StringBuilder correctedFileName = new StringBuilder();

        int index = selectedFileName.lastIndexOf('.');
        if (index > 0) {
            correctedFileName.append(selectedFileName.substring(0, index));
        } else {
            correctedFileName.append(selectedFileName);
        }
        correctedFileName.append(".");
        correctedFileName.append(PREFERRED_IMAGE_EXTENSION);

        return correctedFileName.toString();
    }

    /**
     * Gets and returns the extension of the file.
     *
     * @param absoluteFileName a string representing the absolute file name
     * @return exention a string representing the extension
     */
    private final String getImageExtension(String absoluteFileName) {
        String extension = null;

        int index = absoluteFileName.lastIndexOf('.');
        extension = absoluteFileName.substring(index + 1);

        return extension;
    }

    /**
     * Saves and opens the image contained within
     * <code>PicturePanel</code> that called this object. <p>This method opens a
     * JFileChooser window to save a file, then opens the image file via
     * <code>Desktop.getDesktop</code>. <p><b>NOTE:</b> This object will not
     * allow a file to be overwritten. If an existing file is detected, this
     * object will complain and terminate.
     */
    private final void download() {
        /*
         * setup, run JFileChooser
         */
        JFileChooser fileChooser = new JFileChooser();
        ImageFilter imageFilter = new ImageFilter();
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setMultiSelectionEnabled(false);
        int response = fileChooser.showSaveDialog(null);

        /*
         * if file selected, download and open
         */
        if (response == 0) {
            File selectedFile = fileChooser.getSelectedFile();

            //  do not allow overwrite, complain and do nothing else
            if (selectedFile.exists()) {
                JOptionPane.showMessageDialog(null,
                        "Error:  you cannot overwrite a pre-existing file.  "
                        + "Please enter a new file name.",
                        "Error Saving Image",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                //  check extension; if not valid, modify and rename file
                String selectedAbsoluteFileName = selectedFile.getAbsolutePath();
                String correctAbsoluteFileName = null;
                if (this.hasProperImageExtension(selectedAbsoluteFileName)) {
                    correctAbsoluteFileName = selectedAbsoluteFileName;
                } else {
                    correctAbsoluteFileName = this.correctImageExtension(selectedAbsoluteFileName);
                }

                //  create new file
                File savedFile = new File(correctAbsoluteFileName);
                boolean isCreated = false;
                try {
                    isCreated = savedFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    isCreated = false;
                    JOptionPane.showMessageDialog(null,
                            "There was an error downloading the image.  "
                            + "The new file could not be created.  Please contact SchedFox Administrators.",
                            "Error Saving File",
                            JOptionPane.ERROR_MESSAGE);
                }

                //  write out image
                if (isCreated) {
                    BufferedImage bufferedImage = ImageLoader.toBufferedImage(this.image);
                    boolean isSaved = false;
                    try {
                        ImageIO.write(bufferedImage,
                                this.getImageExtension(correctAbsoluteFileName),
                                savedFile);
                        isSaved = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        isSaved = false;
                        JOptionPane.showMessageDialog(null,
                                "There was an error downloading the image.  "
                                + "The image could not be written to the file.  Please contact SchedFox Administrators.",
                                "Error Saving File",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    //  display
                    if (isSaved) {
                        try {
                            Desktop.getDesktop().edit(savedFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null,
                                    "There was an error downloading the image.  "
                                    + "The image was saved but could not be opened.  Please contact SchedFox Administrators.",
                                    "Error Saving File",
                                    JOptionPane.ERROR_MESSAGE);
                        }   //  end try/catch
                    }   //  end isSaved if
                }   //  end isCreated if
            }   //  end selected file exists else
        }   //  end if response == 0
    }   //  end method

    /**
     * Creates a default instance of this class <p><b>This default constructor
     * is not supported.</b>
     *
     * @throws
     * <code>UnsupportedOperationException</code>
     */
    private PicturePanelUtilities() {
        throw new UnsupportedOperationException("Default Constructor call of PicturePanelUtilities not allowed.");
    }

    /**
     * Creates an useable instance of this class
     *
     * @param image an instance of the image to be saved and opened
     * @param type an enum describing which Utility to perform
     * @throws
     * <code>IllegalArgumentException</code> if a parameter is not valid
     */
    private PicturePanelUtilities(PicturePanelBuilder builder) {
        //  check params
        this.image = builder.image;
        if (this.image == null) {
            JOptionPane.showMessageDialog(null, "An error has occurred while downloading "
                    + "an image.  Please contact SchedFox Administrators.  Object:  DownloadPicturePanelImage",
                    "Unrecoverable Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException();
        }
        this.type = builder.type;
        if (!this.isOperationValid()) {
            JOptionPane.showMessageDialog(null, "An error has occurred while downloading "
                    + "an image.  Please contact SchedFox Administrators.  Object:  DownloadPicturePanelImage",
                    "Unrecoverable Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException();
        }
    }

    /**
     * Starts this object's thread.
     *
     * <p>This method calls internal object methods depending on the operation
     * type.
     */
    @Override
    public void start() {
        //  determine type of operation called
        if (this.type == PicturePanelUtilities.Operation.DOWNLOAD) {
            this.download();
        }
    }

    /**
     * A static Builder object for
     * <code>PicturePanelUtilities</code>
     *
     * <p>This class is a simple static Builder object, designed so that other
     * objects declaring an instance of
     * <code>PicturePanelUtilities</code> can <i>telescope</i> paramaters
     * depening on the <b><code>Operation.type</code></b>.
     *
     * @author Jeffrey Davis
     * @see rmischedule.utility.PicturePanelUtilities
     * @since 1/25/2011
     */
    public static class PicturePanelBuilder {
        //  required parameters

        private Image image;
        private Operation type;

        public PicturePanelBuilder() {
        }

        public PicturePanelBuilder image(Image argImage) {
            image = argImage;
            return this;
        }

        public PicturePanelBuilder operation(Operation argType) {
            type = argType;
            return this;
        }

        public PicturePanelUtilities build() {
            return new PicturePanelUtilities(this);
        }
    }

    /**
     * A class to allow only images to appear in JFileChooser
     *
     * <p>This class extends FileFilter to allow only images to appear in a
     * JFileChooser
     *
     * @author Jeffrey N. Davis
     * @since 01/27/2011
     */
    private static final class ImageFilter extends FileFilter {

        /**
         * Returns the file extension of the argument
         *
         * <p>This method takes in a file, performs string manipulations on the
         * file name to return the extension
         *
         * @param file an instance of
         * <code>File</code>
         * @return extension a string representing the file extension
         */
        private String getFileExtension(File file) {
            String extension = null;
            String fileName = file.getName();

            int index = fileName.lastIndexOf('.');
            if (index > 0 && index < fileName.length() - 1) {
                extension = fileName.substring(index + 1).toLowerCase();
            }

            return extension;
        }

        /**
         * Create a default instance of this class
         */
        public ImageFilter() {
        }

        /**
         * @param f an instance of file to check
         * @return accept a boolean describing whether this file is accepted by
         * the
         * <code>ImageFilter</code>
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = this.getFileExtension(f);
            if (extension != null) {
                if (extension.equals(GIF)
                        || extension.equals(JPG)
                        || extension.equals(JPEG)
                        || extension.equals(PNG)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "Images";
        }
    }
};