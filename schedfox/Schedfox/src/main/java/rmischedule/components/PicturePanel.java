/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PicturePanel.java
 *
 * Created on Jun 18, 2010, 1:34:26 PM
 */
package rmischedule.components;

import com.creamtec.ajaxswing.AjaxSwingConstants;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import rmischedule.main.Main_Window;

/**
 *
 * @author user
 */
public class PicturePanel extends javax.swing.JPanel {

    private PictureParentInterface myParent;
    private int myHeight = 150;
    private int myWidth = 130;
    private PicturePanel thisPP;
    private String fileName;
    private Image image;
    protected boolean allowEdit = true;
    protected boolean allowDelete = true;
    protected boolean allowDownload = true;

    private Full_Size_Image_Container myImageContainer;

    /** Creates new form PicturePanel */
    public PicturePanel(PictureParentInterface myParent) {
        initComponents();
        resizeComponents();
        this.thisPP = this;

        this.myParent = myParent;

        //  added by Jeffrey Davis on 09/2010 for "blow up images"
        setUpEnlargeMouseListener();

        EditPictureLabel.setIcon(Main_Window.Edit_Notes_Icon);
        EditPictureLabel.setVisible(false);
        DeletePictureLabel.setIcon(Main_Window.Delete24x24);
        DeletePictureLabel.setVisible(false);
        this.jDownloadLabel.setIcon(Main_Window.Download_Image_24x24_px);
        this.jDownloadLabel.setText("Download Image");
        this.jDownloadLabel.setVisible(false);

        this.setText();

        EditPictureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                showControl();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                editImage();
            }
        });

        DeletePictureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                showControl();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                deleteImage();
            }
        });

        jDownloadLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                showControl();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jDownloadLabel.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                downloadImage();
            }
        });

    }

    private void setUpEnlargeMouseListener() {
        this.jPanel2.addMouseListener(new EnlargeImageListener());
        this.ImagePanel.addMouseListener(new EnlargeImageListener());
    }

    private Full_Size_Image_Container getImageContainer() {
        if (this.myImageContainer == null) {
            this.myImageContainer = new Full_Size_Image_Container();
            this.myImageContainer.setVisible(false);
            Main_Window.parentOfApplication.desktop.add(this.myImageContainer);
        }
        return this.myImageContainer;
    }

    private void dispalyFullImage() {
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);

        myImageContainer = getImageContainer();
        myImageContainer.setImage(this.image);
        myImageContainer.setVisible(true);
        myImageContainer.setEnabled(true);
        myImageContainer.toFront();
        myImageContainer.show();
        
        try {
            myImageContainer.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(PicturePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  reset cursor
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);
    }

    /**
     * Overload in child class if you don't want to display the edit image link
     * @return
     */
    protected boolean allowEditImage() {
        return this.allowEdit;
    }

    /**
     * Overload in child class if you don't want to display delete image link
     * @return
     */
    protected boolean allowDeleteImage() {
        return this.allowDelete;
    }

    /**
     * Overload in child class if you don't want to display download image link
     * @return
     */
    protected boolean allowDownloadImage() {
        return this.allowDownload;
    }

    private void editImage() {
        myParent.editImage(this);
    }

    private void deleteImage() {
        myParent.deleteImage(this);
    }

    private void downloadImage() {
        myParent.downloadImage(this);
    }

    private int getMyHeight() {
        if (imageLabel.getHeight() <= 0) {
            return myHeight;
        } else {
            return imageLabel.getHeight();
        }

    }

    public void setImage(Image image) {
        if (image != null) {
            this.image = image;
            try {
                Image scaledImage = image.getScaledInstance(-1, getMyHeight(), Image.SCALE_SMOOTH);
                if (scaledImage.getWidth(null) > getMyWidth()) {
                    scaledImage = image.getScaledInstance(getMyWidth(), -1, Image.SCALE_SMOOTH);
                }
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imageLabel.setIcon(null);
        }
        this.setText();
    }

    public void setText() {
        if (imageLabel.getIcon() == null) {
            EditPictureLabel.setText("Add Picture");
        } else {
            EditPictureLabel.setText("Edit Picture");
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public Image getImage() {
        return this.image;
    }

    public void setFileName(String tempFileName) {
        try {
            this.fileName = tempFileName;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private void showControl() {
        if (imageLabel.getIcon() != null) {
            this.DeletePictureLabel.setVisible(true && this.allowDeleteImage());
            this.jDownloadLabel.setVisible(true && this.allowDownloadImage());
        }

        this.EditPictureLabel.setVisible(true && this.allowEditImage());
    }

    private void hideControl() {
        this.EditPictureLabel.setVisible(false);
        this.DeletePictureLabel.setVisible(false);
    }

    private class EnlargeImageListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            boolean shouldDisplayImage = false;
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                shouldDisplayImage = true;
            } else {
                if (e.getClickCount() > 1) {
                    shouldDisplayImage = true;
                }
            }
            if (shouldDisplayImage) {
                thisPP.dispalyFullImage();
            }
        }

        public void mousePressed(MouseEvent e) {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                thisPP.dispalyFullImage();
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        ImagePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        DeletePictureLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        EditPictureLabel = new javax.swing.JLabel();
        jDownloadPanel = new javax.swing.JPanel();
        jDownloadLabel = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setLayout(new java.awt.GridLayout(1, 0));

        jLayeredPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLayeredPane1ComponentResized(evt);
            }
        });

        ImagePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        ImagePanel.setLayout(new java.awt.GridLayout(1, 0));

        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imageLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                imageLabelMouseExited(evt);
            }
        });
        ImagePanel.add(imageLabel);

        ImagePanel.setBounds(0, 0, 180, 190);
        jLayeredPane1.add(ImagePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        ControlPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        ControlPanel.setOpaque(false);
        ControlPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
        );

        ControlPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(180, 25));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));
        jPanel1.add(DeletePictureLabel);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(20, 25));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);

        EditPictureLabel.setBackground(new java.awt.Color(204, 204, 255));
        EditPictureLabel.setText("Edit Picture");
        EditPictureLabel.setMaximumSize(new java.awt.Dimension(100, 20));
        EditPictureLabel.setMinimumSize(new java.awt.Dimension(100, 20));
        EditPictureLabel.setOpaque(true);
        EditPictureLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel1.add(EditPictureLabel);

        ControlPanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        jDownloadPanel.setLayout(new java.awt.GridBagLayout());
        jDownloadPanel.add(jDownloadLabel, new java.awt.GridBagConstraints());

        ControlPanel.add(jDownloadPanel, java.awt.BorderLayout.NORTH);

        ControlPanel.setBounds(0, 0, 180, 190);
        jLayeredPane1.add(ControlPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        add(jLayeredPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void resizeComponents() {
        ImagePanel.setBounds(jLayeredPane1.getBounds());
        ControlPanel.setBounds(jLayeredPane1.getBounds());
        this.revalidate();
        ControlPanel.revalidate();
        ImagePanel.revalidate();
        this.revalidate();
    }

    private void jLayeredPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLayeredPane1ComponentResized
        resizeComponents();
    }//GEN-LAST:event_jLayeredPane1ComponentResized

    private void imageLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseEntered
        this.showControl();
    }//GEN-LAST:event_imageLabelMouseEntered

    private void imageLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseExited
        this.hideControl();
    }//GEN-LAST:event_imageLabelMouseExited
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JLabel DeletePictureLabel;
    private javax.swing.JLabel EditPictureLabel;
    private javax.swing.JPanel ImagePanel;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel jDownloadLabel;
    private javax.swing.JPanel jDownloadPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables

    /**
     * @param myHeight the myHeight to set
     */
    public void setMyHeight(int myHeight) {
        this.myHeight = myHeight;
    }

    /**
     * @return the myWidth
     */
    public int getMyWidth() {
        if (imageLabel.getWidth() <= 0) {
            return myWidth;
        } else {
            return imageLabel.getWidth();
        }
    }

    /**
     * @param myWidth the myWidth to set
     */
    public void setMyWidth(int myWidth) {
        this.myWidth = myWidth;
    }

    /**
     * @param allowEdit the allowEdit to set
     */
    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    /**
     * @param allowDelete the allowDelete to set
     */
    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    /**
     * @param allowDownload the allowDownload to set
     */
    public void setAllowDownload(boolean allowDownload) {
        this.allowDownload = allowDownload;
    }
};
