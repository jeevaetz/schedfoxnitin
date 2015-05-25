/*
 * HierarchicalMenu.java
 *
 * Created on November 18, 2005, 7:59 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.components.graphicalcomponents;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class HierarchicalMenu extends JMenu{
    
    private static final int MAX_SIZE_OF_SUBMENU = 20;
    
    /** 
     * Creates a new instance of HierarchicalMenu 
     * Takes a flat menu and converts it to a heirachy menu....
     */
    public HierarchicalMenu(JMenu flatMenu, boolean showMenuCounts) {
        super(flatMenu.getText());
        if (flatMenu.getMenuComponentCount() > 2) {
            createMenu(null, flatMenu, 'A', 'Z', showMenuCounts);
        }
    }
    
    /**
     * Builds a menu given a start character and end character ie:
     * createMenu(someJMenu, 'A', 'M') will create a heirachial menu filing
     * contents of flatMenu under sub menus from A to M....
     */
    public void createMenu(JMenu menuToAddTo, JMenu flatMenu, char startChar, char endChar, boolean showMenuCounts) {
        JMenu myReturnMenu = new JMenu();
        for (char myChar = startChar; myChar <= endChar; myChar++) {
            JMenu currentMenu = new JMenu(Character.toString(myChar));
            for (int i = flatMenu.getMenuComponentCount(); i >= 0 ; i--) {
                try {
                    char MenuChar = ((JMenuItem)flatMenu.getMenuComponent(i)).getText().charAt(0);
                    if (Character.toUpperCase(MenuChar) == Character.toUpperCase(myChar)) {
                        currentMenu.add((JMenuItem)flatMenu.getMenuComponent(i));
                    }
                } catch (Exception e) {}
            }
            if (showMenuCounts) {
                currentMenu.setText(currentMenu.getText() + "       (" + currentMenu.getMenuComponentCount() + ")");
                
            }
            if (currentMenu.getMenuComponentCount() > 0 && menuToAddTo != null) {
                menuToAddTo.add(currentMenu);
            } else if (currentMenu.getMenuComponentCount() > 0) {
                add(currentMenu);
            }
        }
    }
    
}
