/**
 * Filename: EmployeePictures.java Author: Jeffrey Davis Date Created:
 * 08/23/2010 Modifications: Purpose of File: file contains a class designed to
 * sit within the employee edit window for picture manipulation
 */
//  package declaration
package rmischedule.employee.components;

//  import declarations
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rmischedule.components.ImagePreview;
import rmischedule.components.PicturePanel;
import rmischedule.utility.PicturePanelUtilities;
import rmischedule.components.PictureParentInterface;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.employee.xEmployeeEdit;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.ImageLoader;
import rmischedule.security.User;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_noncertification_query;

/**
 * Class Name: EmployeePictures Purpose of Class: a class designed to sit within
 * the employee edit window for picture manipulation
 */
public class EmployeePictures extends GenericEditSubForm implements PictureParentInterface {
    //  private variable declarations

    private ArrayList<PicturePanel> picturePanelArray;
    private String empId;
    private String companySchema;
    private xEmployeeEdit myParent;
    private JFileChooser fchooser;

    /**
     * Method Name: refreshPreviews Purpose of Method: refreshes the preview
     * panel whenever a new image is added Arguments: none Returns: void
     * Preconditions: previewPanl needs to be refreshed Postconditions:
     * previewPanel refreshed
     */
    private void refreshPreviews() {
        //  ensure panel is clear
        this.jPreviewPanel.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        for (int idx = 0; idx < picturePanelArray.size(); idx++) {
            c.gridx = idx % 4;
            c.gridy = idx / 4;
            c.weightx = .25;
            c.weighty = .25;
            this.jPreviewPanel.add(picturePanelArray.get(idx), c);

        }

        //  validate repaint panel
        this.jPreviewPanel.validate();
        this.jPreviewPanel.repaint();

        //  reset labels
        this.setDataLabels();

        togglePreview(true);
    }

    /**
     * Toggle imaging preview or the loading bar.
     *
     * @param showPreview
     */
    private void togglePreview(boolean showPreview) {
        try {
            CardLayout cardLayout = (CardLayout) jBasePanel.getLayout();
            if (showPreview) {
                cardLayout.show(jBasePanel, "imagePreview");
            } else {
                cardLayout.show(jBasePanel, "progressPanel");
            }
            jBasePanel.revalidate();
            jBasePanel.repaint();
        } catch (Exception e) {
        }
    }

    /**
     * Method Name: loadPreviews Purpose of Method: hits the image server for
     * images, loads them into internal data structure Arguments: none Returns:
     * void Preconditions: images need to be retrieved from image server
     * Postconditions: images retrieved
     */
    private void loadPreviews() {
        final EmployeePictures thisObj = this;
        Runnable fetchImage = new Runnable() {
            public void run() {
                empId = myParent.getMyIdForSave();
                final String currentCompanyId = myparent.getConnection().myCompany;

                Runnable displayImages = new Runnable() {
                    public void run() {
                        Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);

                        ArrayList<String> imageLocations =
                                ImageLoader.getFileNames(empId, comp.getDB(), ".jpg", "myImages", "additional_images", "_");

                        ArrayList<ImageIcon> previewArray =
                                ImageLoader.getImagePreviews(imageLocations, comp.getDB(), ".jpg", empId, "additional_images");

                        //  ensure picture panel is empty
                        picturePanelArray.clear();
                        //  load picturePanelArray
                        for (int idx = 0; idx < previewArray.size(); idx++) {
                            PicturePanel image = new PicturePanel(thisObj);
                            image.setFileName(imageLocations.get(idx));
                            image.setMyHeight(150);
                            image.setMyWidth(150);
                            image.setPreferredSize(new Dimension(150, 150));
                            image.setMinimumSize(new Dimension(150, 150));
                            image.setImage(previewArray.get(idx).getImage());
                            picturePanelArray.add(image);
                        }

                        Runnable refreshImage = new Runnable() {
                            public void run() {
                                refreshPreviews();
                            }
                        };
                        try {
                            SwingUtilities.invokeAndWait(refreshImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                try {
                    new Thread(displayImages).start();
                } catch (Exception e) {
                    togglePreview(true);
                }
            }
        };
        togglePreview(false);
        new Thread(fetchImage).start();

    }

    /**
     * Method Name: setDataLabels Purpose of Method: determines information to
     * be displayed via labels, then displays it Arguments: none Returns: void
     * Preconditions: data to be displayed unknown and not displayed
     * Postconditions: data known and displayed
     */
    private void setDataLabels() {
        this.jNumFilesLabel.setText("Employee total pictures:  " + picturePanelArray.size());
        this.jInformationPanel.validate();
        this.jInformationPanel.repaint();
    }

    /**
     * Creates new form EmployeePictures
     */
    public EmployeePictures(xEmployeeEdit main) {
        //  initialze class variables
        picturePanelArray = new ArrayList<PicturePanel>();
        myParent = main;

        initComponents();

        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select an image for this employee");
        this.fchooser.setAcceptAllFileFilterUsed(false);
        this.fchooser.setMultiSelectionEnabled(false);

        //Add the preview pane.
        this.fchooser.setAccessory(new ImagePreview(this.fchooser));

        this.fchooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Image (JPEG, JPG)";
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jControlPanel = new javax.swing.JPanel();
        jButtonPanel = new javax.swing.JPanel();
        jAddImageButton = new javax.swing.JButton();
        jInformationPanel = new javax.swing.JPanel();
        jNumFilesLabel = new javax.swing.JLabel();
        jBasePanel = new javax.swing.JPanel();
        jPreviewScrollPane = new javax.swing.JScrollPane();
        jPreviewPanel = new javax.swing.JPanel();
        progressPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jControlPanel.setLayout(new java.awt.GridLayout(0, 2));

        jButtonPanel.setLayout(new java.awt.GridBagLayout());

        jAddImageButton.setText("Add Image");
        jAddImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddImageButtonActionPerformed(evt);
            }
        });
        jButtonPanel.add(jAddImageButton, new java.awt.GridBagConstraints());

        jControlPanel.add(jButtonPanel);

        jInformationPanel.setLayout(new java.awt.GridBagLayout());
        jInformationPanel.add(jNumFilesLabel, new java.awt.GridBagConstraints());

        jControlPanel.add(jInformationPanel);

        add(jControlPanel, java.awt.BorderLayout.PAGE_START);

        jBasePanel.setLayout(new java.awt.CardLayout());

        jPreviewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPreviewPanel.setLayout(new java.awt.GridBagLayout());
        jPreviewScrollPane.setViewportView(jPreviewPanel);

        jBasePanel.add(jPreviewScrollPane, "imagePreview");

        progressPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 20));
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(3000, 300));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Loading Images...Please Wait.");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1, java.awt.BorderLayout.SOUTH);

        progressPanel.add(jPanel1);

        jProgressBar1.setIndeterminate(true);
        progressPanel.add(jProgressBar1);

        jPanel2.setMaximumSize(new java.awt.Dimension(3000, 300));
        progressPanel.add(jPanel2);

        jBasePanel.add(progressPanel, "progressPanel");

        add(jBasePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jAddImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddImageButtonActionPerformed
        PicturePanel pp = null;
        this.editImage(pp);
    }//GEN-LAST:event_jAddImageButtonActionPerformed

    //  abstract method implemenations
    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        /**
         * dummy query for testing
         */
        employee_state_noncertification_query query = new employee_state_noncertification_query();

        return query;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        /**
         * dummy query for testing
         */
        employee_state_noncertification_query query = new employee_state_noncertification_query();

        return query;
    }

    @Override
    public void loadData(Record_Set rs) {
        //  load images from server
        this.loadPreviews();


    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Pictures";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
        //  clear data structure
        this.picturePanelArray.clear();

        //  clear panel
        this.jPreviewPanel.removeAll();
    }

    @Override
    public boolean userHasAccess() {
//        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
        return true;
    }

    public void editImage(PicturePanel panel) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Runnable displayImages = new Runnable() {
                public void run() {
                    String currentCompanyId = myparent.getConnection().myCompany;
                    Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);

                    try {
                        Runnable togglePreview = new Runnable() {
                            public void run() {
                                togglePreview(false);
                                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                                setCursor(hourglassCursor);
                            }
                        };
                        SwingUtilities.invokeAndWait(togglePreview);

                        //  save image
                        BufferedImage image = ImageIO.read(fchooser.getSelectedFile());
                        ImageIcon newIcon = new ImageIcon(image);
                        ImageLoader.saveAdditionalImages(comp.getDB(), "additional_images",
                                "myImages", ".jpg", "_", empId, newIcon);

                        Thread.sleep(800);

                        Runnable togglePreview2 = new Runnable() {
                            public void run() {
                                loadPreviews();
                                refreshPreviews();
                            }
                        };
                        SwingUtilities.invokeAndWait(togglePreview2);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "There was an error saving your image",
                                "ImageServer Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        //  reset cursor
                        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                        setCursor(normalCursor);
                    }
                }
            };
            new Thread(displayImages).start();
        }
    }

    public void addImage(PicturePanel panel) {
    }

    public void deleteImage(PicturePanel panel) {
        int response = JOptionPane.showConfirmDialog(this, "Do you wish to delete this image?",
                "Delete Image?", JOptionPane.YES_NO_OPTION);
        if (response == 0) {
            boolean isRemoveSuccesful = false;
            try {
                //  set cursor
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                //  get company schema
                String currentCompanyId = myparent.getConnection().myCompany;
                Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);

                //  call remove image
                User user = Main_Window.parentOfApplication.getUser();
                isRemoveSuccesful = ImageLoader.removeImage(panel.getFileName(), comp.getDB(),
                        "remove_additional_employee_images", empId, user.getUserId(), "_".trim());

                // reload image panel
                this.loadPreviews();
                this.refreshPreviews();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                //  reset cursor
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);

                //  inform user if remove was succesful
                if (isRemoveSuccesful) {
                    JOptionPane.showMessageDialog(this, "Image successfully removed.",
                            "Remove Image", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error:  Image could not be removed.  Please contact SchedFox administrators.",
                            "Remove Image", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void downloadImage(PicturePanel panel) {
        PicturePanelUtilities download = new PicturePanelUtilities.PicturePanelBuilder()
                .image(panel.getImage())
                .operation(PicturePanelUtilities.Operation.DOWNLOAD)
                .build();
        download.start();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAddImageButton;
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JPanel jButtonPanel;
    private javax.swing.JPanel jControlPanel;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jNumFilesLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPreviewPanel;
    private javax.swing.JScrollPane jPreviewScrollPane;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPanel progressPanel;
    // End of variables declaration//GEN-END:variables
};
