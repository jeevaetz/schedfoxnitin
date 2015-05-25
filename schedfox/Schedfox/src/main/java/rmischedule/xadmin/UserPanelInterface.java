/*
 * UserPanelInterface.java
 *
 * Created on September 5, 2005, 4:40 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.xadmin;

import schedfoxlib.model.util.Record_Set;
import rmischedule.components.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.*;

import javax.swing.JPanel;
/**
 *
 * @author Owner
 */
public interface UserPanelInterface {
    public GeneralQueryFormat getQuery(String manageId, String userId);
    public void setData(Record_Set rs);
    public String getTitleOfTab();
    public JPanel getComponent();
    public List_View getListView();
    public GeneralQueryFormat getSaveDataQuery(Object OptionalData);
}
