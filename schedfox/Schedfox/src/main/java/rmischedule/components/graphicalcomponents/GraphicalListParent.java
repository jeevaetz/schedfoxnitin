/*
 * GraphicalListParent.java
 *
 * Created on February 23, 2005, 10:48 AM
 */
package rmischedule.components.graphicalcomponents;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.util.Vector;
import java.awt.Point;

/**
 * 
 * @author ira
 */
public abstract class GraphicalListParent extends JScrollPane {
    
    private JPanel myContainerPanel;
    private Vector<GraphicalListComponent> myComponents;
    private boolean unSelectable = false;
    private Object holdComp;
    private boolean allowMultipleSelection;
    
    public GraphicalListParent() {
        super();
        allowMultipleSelection = false;
        myContainerPanel = new JPanel();
        myContainerPanel.setLayout(new BoxLayout(myContainerPanel, BoxLayout.Y_AXIS));
        this.add(myContainerPanel);
        myContainerPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        myContainerPanel.setMaximumSize(null);
        myContainerPanel.setMinimumSize(null);
        myContainerPanel.setPreferredSize(null);
        setViewportView(myContainerPanel);
        myComponents = new Vector<GraphicalListComponent>(40);
        getVerticalScrollBar().setUnitIncrement(30);
    }

    /**
     * What should be done on clear data from parent form
     */
    public void runOnClear() {
        if (!this.allowMultipleSelection) {
            for (int i = 0; i < myComponents.size(); i++) {
                myComponents.get(i).setSelected(false);
            }
        }
    }

    public void setMutlipleSelection(boolean setMult) {
        this.allowMultipleSelection = setMult;
    }
    
    public GraphicalListComponent getListComponentAt(int i) {
        return (GraphicalListComponent)myComponents.get(i);
    }
    
    public int getNumberOfComponents() {
        return myComponents.size();
    }
    
    public void setUnSelectable(boolean f){
        unSelectable = f;
    }

    public boolean getUnSelectable(){return unSelectable;}
   
    public void setSelected(int i){
        if(i < myComponents.size()){
            ((GraphicalListComponent)myComponents.get(i)).setSelected(true);
        }
    }
    
    public void setHoldComp(Object o){
        holdComp = o;
    }
    public Object getComponentObject(int i){
        if(i < myComponents.size()){
            return ((GraphicalListComponent)myComponents.get(i)).myObject;
        }        
        return null;
    }
    
    /**
     * Pass in the Object type that the GraphicalListComponenet encapsulates and 
     * this sub will remove it for you from the list...
     */
    public void removeItem(Object o) {
        for (int i = 0; i < myComponents.size(); i++) {
            if (((GraphicalListComponent)myComponents.get(i)).getObject().equals(o)) {
                myContainerPanel.remove((GraphicalListComponent)myComponents.get(i));
                myComponents.remove(i);
            }
        }
    }

    public void selectOneDeselectOthers(GraphicalListComponent glc) {
        for (int i = 0; i < myComponents.size(); i++) {
            if (
                ((GraphicalListComponent)myComponents.get(i)) != glc &&
                (myComponents.get(i)) != holdComp
            ) {
                ((GraphicalListComponent)myComponents.get(i)).setSelected(false);
            }
            holdComp = null;
        }
    }

    public void toggleSingleSelection(GraphicalListComponent glc) {
        for (int i = 0; i < myComponents.size(); i++) {
            GraphicalListComponent comp = myComponents.get(i);
            if (comp == glc && comp.AmISelected) {
                //comp.setSelected(false);
            }

        }
    }

    public void toggleSelectionOfElement(GraphicalListComponent glc) {
        this.functionToRunOnSelectedShift(glc.myObject, glc.AmISelected);
        if (allowMultipleSelection) {
            toggleSingleSelection(glc);
        } else {
            selectOneDeselectOthers(glc);
        }
    }

    @Override
    public void removeAll() {
        myContainerPanel.removeAll();
    }
    
    public void setFocusOn(int i) {
        this.getViewport().setViewPosition(new Point(0, ((GraphicalListComponent)myComponents.get(i)).getLocation().y));
    }
    
    /**
     * Method used to process what should be done with stuff when stuff happens...yeah
     */
    public abstract void functionToRunOnSelectedShift(Object objectContained, boolean selected);

    public Vector<GraphicalListComponent> getAddedComponents() {
        return myComponents;
    }

    public void add(GraphicalListComponent newObject) {
        myComponents.add(newObject);
        myContainerPanel.add(newObject);
    }
    
    public void addCompToVector(GraphicalListComponent newObject) {
        myComponents.add(newObject);
    }
    
}
