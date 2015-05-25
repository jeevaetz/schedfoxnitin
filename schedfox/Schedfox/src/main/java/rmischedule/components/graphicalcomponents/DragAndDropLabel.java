/*
 * DragAndDropLabel.java
 *
 * Created on February 28, 2005, 8:22 AM
 */
package rmischedule.components.graphicalcomponents;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.awt.Container;
import java.awt.Component;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 *
 * @author ira
 * Class that allows us to drag a Label around, needs to have a LayeredPane somewhere in its
 * ancestry...otherwise ur screwed...
 *
 * Ok here is the specifics the structure needs to be something like this
 *
 * ---   Main JPanel, or JInternalFrame, or JFrame
 * ---   Various components or none
 * ---   JLayeredPanel (this is needed to draw our ghosted image
 * ---   JPanel, content pane, somewhere on this content pane must be the DragAndDropContainer objects and our DragAndDropLabels
 * ---   AT LEAST ONE MORE JPANEL IS REQUIRED HERE BETWEEN THE CONTENT PANE AND OUR DRAGANDDROP OBJECTS
 * ---   Drag and Drop Objects
 *
 */
public class DragAndDropLabel extends JPanel {

    protected myMouseListener myMouseEventHandler;
    private dragPanel ghostedImage;
    private JLayeredPane parentPane;
    private int xOffset;
    private int yOffset;
    private int contxOffset;
    private int contyOffset;
    private boolean CalculatedOffsets;
    private Container containerPanel;
    private Object objectToPassContainer;
    private DragAndDropLabel thisObject;
    private Graphics myGraphics;
    private Image image;
    private BufferedImage bi;
    private boolean dontDragAndDrop;
    private Component parentComp;

    /** Creates a new instance of DragAndDropLabel
     *  Pass in the Layered Pane that is its parent and the root pane on the glass pane that contains
     *  The panels that you want to insert into...I know kinda confusing, also the Object that you want
     *  passed when the object is dropped into our class that extends DragAndDropContainer...
     */
    public DragAndDropLabel(JLayeredPane panelToDisplayOn, Container panelContainingDropContainers, Object objectToPassOnDrop) {
        parentPane = panelToDisplayOn;
        thisObject = this;
        containerPanel = panelContainingDropContainers;
        objectToPassContainer = objectToPassOnDrop;
        ghostedImage = new dragPanel();
        ghostedImage.setSize(this.getSize());
        myMouseEventHandler = new myMouseListener(this);
        this.addMouseListener(myMouseEventHandler);
        this.addMouseMotionListener(myMouseEventHandler);
        CalculatedOffsets = false;
        dontDragAndDrop = false;
    //this.setBackground(new Color(255,255,255,255));
    }

    public void dispose() {
        ghostedImage = null;
        objectToPassContainer = null;
        myMouseEventHandler = null;
        thisObject = null;
        parentPane = null;
    }

    public DragAndDropLabel(JLayeredPane panelToDisplayOn, Container panelContainingDropContainers) {
        this(panelToDisplayOn, panelContainingDropContainers, null);
    }

    public void setDragEnabled(boolean val) {
        dontDragAndDrop = !val;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * Constructor when no drag and drop is necessary...
     */
    public DragAndDropLabel() {
        dontDragAndDrop = true;
    }

    /**
     * Mouse Stuff
     */
    public void mouseClicked(MouseEvent e) {
        myMouseEventHandler.mouseClicked(e);
    }

    public void mouseEntered(MouseEvent e) {
        myMouseEventHandler.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        myMouseEventHandler.mouseExited(e);
    }

    public void mousePressed(MouseEvent e) {
        myMouseEventHandler.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        myMouseEventHandler.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        myMouseEventHandler.mouseDragged(e);
    }

    public void calculateOffsets() {
        calculateOffsets(0, 0);
    }

    /**
     * Useful to calculateOffsets when an initial x and y is passed in.
     */
    public void calculateOffsets(int x, int y) {
        if (dontDragAndDrop) {
            return;
        }
        parentComp = this.getParent();
        xOffset = this.getX() - x;
        yOffset = this.getY() - y;
        while (parentComp != parentPane) {
            xOffset = parentComp.getX() + xOffset;
            yOffset = parentComp.getY() + yOffset;
            parentComp = parentComp.getParent();
        }
        parentComp = (Component) containerPanel;
        contxOffset = containerPanel.getX();
        contyOffset = containerPanel.getY();
        while (parentComp != parentPane) {
            contxOffset = parentComp.getX() + contxOffset;
            contyOffset = parentComp.getY() + contyOffset;
            parentComp = parentComp.getParent();
        }

        CalculatedOffsets = true;
    }

    private boolean isDragAndDropContainer(Component c) {
        try {
            DragAndDropContainer test = (DragAndDropContainer) c;
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Image getImage() {
        return bi;
    }

    /**
     * Extend if you want any other operations done on Drag events...scroll window
     * contents etc...
     */
    protected void runOnDrag(MouseEvent e) {

    }

    public DragAndDropContainer getDragAndDropContainer(int x, int y) {
        Component parentW = SwingUtilities.getDeepestComponentAt(containerPanel, x, y);
        if (isDragAndDropContainer((Component) parentW)) {
            return (DragAndDropContainer) parentW;
        }
        Component parentComp = (Component) parentW;
        while (!isDragAndDropContainer(parentComp) && parentComp.getParent() != null) {
            parentComp = parentComp.getParent();
        }
        if (isDragAndDropContainer((Container) parentW)) {
            return (DragAndDropContainer) parentW;
        }
        return null;
    }

    private Object getObjectToPass() {
        if (objectToPassContainer == null) {
            objectToPassContainer = this;
        }
        return objectToPassContainer;
    }

    public void setObjectToPass(Object ob) {
        objectToPassContainer = ob;
    }

    /**
     * Nice method to allow us to specify what to do on a click...isn't it great
     */
    protected void methodToRunOnClick(MouseEvent evt) {

    }

    protected class myMouseListener extends MouseMotionAdapter implements MouseListener {

        private JPanel parentLabel;
        private DragAndDropContainer lastHightLightedContainer;
        private int clickX;
        private int clickY;
        private Component parent;
        private DragAndDropContainer isDragContainer;
        private int runEveryDrag = 2;
        private int lastRan = 0;

        public myMouseListener(JPanel parent) {
            parentLabel = parent;
        }

        private void initializeGhostedImageToDrag(MouseEvent e) {
            if (dontDragAndDrop) {
                return;
            }
            if (bi == null) {
                lastHightLightedContainer = null;
                calculateOffsets();
                bi = new BufferedImage(parentLabel.getWidth(), parentLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
                parentLabel.paint(bi.getGraphics());
                bi = fadeImage(bi, .7);
            }
            if (ghostedImage.getParent() == null) {
                ghostedImage.setBounds(parentLabel.getBounds());
                ghostedImage.setBackground(new Color(255, 255, 255, 0));

                xOffset -= e.getX();
                yOffset -= e.getY();
                clickX = e.getX();
                clickY = e.getY();
                parentPane.add(ghostedImage, JLayeredPane.DRAG_LAYER);
                ghostedImage.setLocation(-500, -500);
            }
        }

        public void mouseClicked(MouseEvent e) {
            methodToRunOnClick(e);
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mousePressed(java.awt.event.MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
            if (dontDragAndDrop) {
                return;
            }
            if (ghostedImage.getParent() != null) {
                SwingUtilities.invokeLater(
                    new Thread() {
                        public void run() {
                            parentPane.remove(ghostedImage);
                            parentPane.revalidate();
                            parentPane.repaint();
                        }
                    });
                //  added by Jeffrey Davis for Cursor ability
                try
                {
                    Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                        setCursor(hourglassCursor);

                    //Set BI to null so offsets are calculated correctly
                    Object myObjectToPass = getObjectToPass();
                    try {
                        lastHightLightedContainer.highlightMe(false, myObjectToPass, e);
                    } catch (Exception ex) {
                    }
                    try {
                        lastHightLightedContainer.runOnDrop(myObjectToPass, e, bi);
                    } catch (Exception ex) {
                        try {
                            lastHightLightedContainer.runOnDrop(myObjectToPass, e, bi);
                        } catch (Exception exe) {
                        }
                    }
                    bi = null;
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                     //  reset cursor
                    Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    setCursor(normalCursor);
                }
            }
        }

        public DragAndDropContainer isDragDropContainer(Component compToTest) {
            parent = compToTest.getParent();
            if (parent != null && parent != containerPanel) {
                try {
                    return (DragAndDropContainer) compToTest;
                } catch (Exception e) {
                    return isDragDropContainer(parent);
                }
            } else {
                return null;
            }
        }

        public void mouseDragged(MouseEvent e) {
            lastRan++;
            if (lastRan < runEveryDrag) {
                return;
            }
            initializeGhostedImageToDrag(e);
            runOnDrag(e);
            lastRan = 0;
            if (dontDragAndDrop) {
                return;
            }
            if (bi == null) {
                return;
            }
            ghostedImage.setBounds(xOffset + e.getX(), yOffset + e.getY(), ghostedImage.getWidth(), ghostedImage.getHeight());
            Object myObjectToPass = getObjectToPass();
            try {
                isDragContainer = isDragDropContainer(SwingUtilities.getDeepestComponentAt(((Component) containerPanel), e.getX() + (xOffset + clickX), e.getY() + (yOffset + clickY)));
                if (lastHightLightedContainer != null && lastHightLightedContainer != isDragContainer) {
                    lastHightLightedContainer.highlightMe(false, myObjectToPass, e);
                }
                lastHightLightedContainer = isDragContainer;
                isDragContainer.highlightMe(true, myObjectToPass, e);
            } catch (Exception ex) {
                try {
                    lastHightLightedContainer.highlightMe(false, myObjectToPass, e);
                } catch (Exception exe) {
                }
            }
        //ghostedImage.paint(myGraphics);
        }
    }

    private BufferedImage fadeImage(BufferedImage input, double floatAmount) {
        int jjh = -1;
        int jjw = -1;
        while (jjw < 0 || jjh < 0) {
            jjh = input.getHeight(this);
            jjw = input.getWidth(this);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        BufferedImage bi = new BufferedImage(jjw, jjh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D biContext = bi.createGraphics();
        biContext.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) floatAmount));
        biContext.drawImage(input, 0, 0, null);
        return bi;
    }

    private class dragPanel extends JPanel {

        public dragPanel() {
            super();
        }

        public void paintComponent(Graphics g) {
            try {
                super.paintComponent(g);
                g.drawImage(bi, 0, 0, this);

            } catch (Exception e) {
            }

        }
    }
}
