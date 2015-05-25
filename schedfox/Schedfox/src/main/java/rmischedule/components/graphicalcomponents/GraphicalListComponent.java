/*
 * IndividualEmployeeToDisplayPanel.java
 *
 * Created on February 22, 2005, 10:01 AM
 */
package rmischedule.components.graphicalcomponents;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import rmischedule.main.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLayeredPane;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;
/**
 *
 * @author  ira
 */
public class GraphicalListComponent extends DragAndDropLabel {
    public  Object myObject;
    protected GraphicalListParent parentP;
    public boolean AmISelected;
    protected SoftBevelBorder myRaisedBorder = new SoftBevelBorder(javax.swing.border.BevelBorder.RAISED);
    protected SoftBevelBorder myLoweredBorder = new SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED);
    private String myText;
    public boolean allowUnSelected = true;
    private boolean isDraggable;
    private java.awt.Color defaultColor;
    private java.awt.Color darkerColor;
    private boolean isDeleted;

    private MouseAdapter mouseAdapter;
    private MouseMotionAdapter mouseMotionAdapter;

    private static final int myHeight = 22;
    
    /**
     * Creates a new component which should then be added to type GraphicalListParent.
     * Param:
     *  ObjectToReturn this is the object that is passed back to the GraphicalListParent when functionToRunOnSelectedShift is
     *  run...
     */
    public GraphicalListComponent(Object ObjectToReturn, GraphicalListParent parentPanel, String text, String text2) {
        initialize(ObjectToReturn, parentPanel, text, text2);
    }

    public GraphicalListComponent(Object ObjectToReturn, GraphicalListParent parentPanel, String text) {
        initialize(ObjectToReturn, parentPanel, text, "");
    }
    
    private void initialize(Object ObjectToReturn, GraphicalListParent parentPanel, String text, String text2) {
        mouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                gotoParentDragged(evt);
            }
        };
        mouseAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleSelected(evt);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setIconToBlue(evt);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setIconBack(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                goToParentPressed(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                goToParentReleased(evt);
            }
        };

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                initComponents();
            } else {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        initComponents();
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        this.addMouseMotionListener(mouseAdapter);
        this.addMouseListener(mouseAdapter);
        IconLabel.addMouseMotionListener(mouseAdapter);
        IconLabel.addMouseListener(mouseAdapter);
        NameLabel.addMouseMotionListener(mouseAdapter);
        NameLabel.addMouseListener(mouseAdapter);
        NameLabel2.addMouseMotionListener(mouseAdapter);
        NameLabel2.addMouseListener(mouseAdapter);

        IconLabel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_LEFT_EVENT_LISTENER, "onclick");
        NameLabel.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_LEFT_EVENT_LISTENER, "onclick");
        NameLabel2.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_LEFT_EVENT_LISTENER, "onclick");


        myObject = ObjectToReturn;
        parentP = parentPanel;
        AmISelected = false;
        NameLabel.setText(text);
        NameLabel2.setText(text2);
        setSelected(false);
        isDraggable = false;
        defaultColor = this.getBackground();
        darkerColor = defaultColor.darker();
        isDeleted = false;
    }

    private void goToParentReleased(MouseEvent evt) {
        if (isDraggable) {
            super.myMouseEventHandler.mousePressed(evt);
       }
    }

    private void gotoParentDragged(java.awt.event.MouseEvent evt) {
        if (isDraggable) {
            super.myMouseEventHandler.mouseDragged(evt);
        }
    }

    private void goToParentPressed(MouseEvent evt) {
        if (isDraggable) {
            super.myMouseEventHandler.mousePressed(evt);
       }
       toggleSelected();
    }

    public void setBackground(boolean darker) {
        isDeleted = darker;
        if (darker) {
            setBackground(darkerColor);
        } else {
            setBackground(defaultColor);
        }
    }
    
    public boolean isMarkedDeleted() {
        if (isDeleted) {
            return true;
        }
        return false;
    }
    
    public void setSizes() {
        setPreferredSize(new Dimension(68, this.getComponentCount() * myHeight));
        setMinimumSize(new Dimension(68, this.getComponentCount() * myHeight));
        setMaximumSize(new Dimension(68000, this.getComponentCount() * myHeight));
    }
    
    public void addSubComp(GraphicalListSubComponent mySub) {
        this.add(mySub);
        parentP.addCompToVector(mySub);
        setSizes();
    }
    
    /**
     * Graphical List Object with Drag and Drop Capability...
     */
    public GraphicalListComponent(Object ObjectToReturn, GraphicalListParent parentPanel, String text,
                                  JLayeredPane panelToDisplayOn, Container panelContainingDropContainers, Object objectToPassOnDrop) {
        super(panelToDisplayOn, panelContainingDropContainers, objectToPassOnDrop);
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                initComponents();
            } else {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        initComponents();
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myObject = ObjectToReturn;
        parentP = parentPanel;
        AmISelected = false;
        NameLabel.setText(text);
        setSelected(false);
        isDraggable = true;
    }
    
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            this.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "graphListSelected");
            setToSelectedColor();
            this.setBorder(myLoweredBorder);
            AmISelected = true;
        } else {
            this.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "graphListUnselected");
            setToUnselectedColor();
            this.setBorder(myRaisedBorder);
            AmISelected = false;
        }
    }
    
    public Object getObject() {
        return myObject;
    }
    
    public void add(GraphicalListComponent newObject) {
        super.add(newObject);
    }
    
    protected void setToSelectedColor() {
        IconLabel.setIcon(Main_Window.Green_Bullet_Icon);
    }
    
    protected void setToUnselectedColor() {
        IconLabel.setIcon(Main_Window.Red_Bullet_Icon);
    }
    
    protected void setToMouseOverColor() {
        IconLabel.setIcon(Main_Window.Yellow_Bullet_Icon);
    }

    private void setIconBack(java.awt.event.MouseEvent evt) {
        setIconToUnselected();
    }

    private void toggleSelected(java.awt.event.MouseEvent evt) {

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        SpacerPanel = new javax.swing.JPanel();
        IconLabel = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        NameLabel2 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(340020, 22));
        setMinimumSize(new java.awt.Dimension(68, 22));
        setPreferredSize(new java.awt.Dimension(68, 22));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        SpacerPanel.setMaximumSize(new java.awt.Dimension(0, 10));
        SpacerPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        SpacerPanel.setOpaque(false);
        SpacerPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanel1.add(SpacerPanel);

        IconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IconLabel.setMaximumSize(new java.awt.Dimension(20, 22));
        IconLabel.setMinimumSize(new java.awt.Dimension(20, 22));
        IconLabel.setPreferredSize(new java.awt.Dimension(20, 22));
        IconLabel.setRequestFocusEnabled(false);
        jPanel1.add(IconLabel);

        NameLabel.setText("Display All");
        NameLabel.setMaximumSize(new java.awt.Dimension(340000, 20));
        jPanel1.add(NameLabel);
        jPanel1.add(NameLabel2);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void setIconToUnselected() {
        if (!AmISelected) {
            setToUnselectedColor();
        }
    }
    
    private void setIconMouseOver() {
        if (!AmISelected) {
            setToMouseOverColor();
        }
    }

    private void setIconToBlue(MouseEvent evt) {
        setIconMouseOver();
    }
    


    protected void toggleSelected() {
         if(!AmISelected || (AmISelected && (parentP.getUnSelectable() && allowUnSelected))){
            parentP.toggleSelectionOfElement(this);
            setSelected(!AmISelected);
        }
    }
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel IconLabel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel NameLabel2;
    protected javax.swing.JPanel SpacerPanel;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
