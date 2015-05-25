/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import rmischedule.main.Main_Window;
import rmischedule.mapping.MappingDialog;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;

/**
 *
 * @author user
 */
public class ClientMapAction implements MouseListener {

    private SMainComponent parent;
    private SClient clientData;

    public ClientMapAction(SMainComponent parent, SClient client) {
        this.parent = parent;
        this.clientData = client;
    }

    public void mouseClicked(MouseEvent e) {
        //Assume first employee is one to map to

        Employee empData = null;
        try {
            Vector<SEmployee> emp = parent.getMyParent().getEmployeeList();
            for (int employee = 0; employee < emp.size(); employee++) {
                if (emp.get(employee).getId() != 0) {
                    empData = emp.get(employee).getEmployee();
                }
            }
        } catch (Exception exe) {
        }

        MappingDialog mapDialog = new MappingDialog(Main_Window.parentOfApplication,
                true, empData, clientData.getClientData());
        mapDialog.setVisible(true);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
