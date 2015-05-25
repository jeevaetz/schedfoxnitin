/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import rmischedule.schedule.Schedule_View_Panel;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class STimeOff extends SMainComponent {

    private String title;
    private Integer timeOffId;

    /** Our Constructor */
    public STimeOff(Schedule_View_Panel myParent, Record_Set rs, String title, Integer timeOffId) {
        super(myParent, rs);

        this.title = title;
        this.timeOffId = timeOffId;

        super.initializeComponent();
    }

    public STimeOff(String id) {
        super(id);
        super.initializeComponent();
    }
    
    @Override
    public void addMyBlankSchedule() {
        
    }

    @Override
    public String generateClientName() {
        return this.title;
    }

    @Override
    public void plantShifts() {
        SortedSet myMap = parent.mySchedules.getClientSchedules(this);
        Iterator<SSchedule> myIterator = myMap.iterator();
        while (myIterator.hasNext()) {
            myIterator.next().plantWeeks();
        }
    }

    @Override
    public boolean shouldHaveBlankSchedule() {
        return false;
    }

    @Override
    public void loadTrainingInfo(String employeeId, double trainingTime, boolean override) {
        
    }

    @Override
    public MouseAdapter getClientMouseAdapter() {
        return new EditClientListener();
    }

    @Override
    public MouseListener getPrinterMouseListener() {
        return new MouseListener() {
            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        };
    }

    @Override
    public void setClientHeader() {
        int i, c;
        c = parent.getWeekCount();

        for (i = 0; i < c; i++) {
            getHeader().add(new IndividualClientHeader(this, new EditClientListener()));
            //getHeader().add(new ClientPrinterPanel(this, new ClientMapAction(this, this.getClientData()), i));
        }
        setOpaque(false);
    }

    @Override
    public SSchedule getMyBlankSchedule() {
        return null;
    }

    @Override
    public boolean isDefaultToNonBillable() {
        return false;
    }

    @Override
    public Client getClientData() {
        return null;
    }

    @Override
    public boolean hasNotes() {
        return false;
    }

    @Override
    public Integer getRate() {
        return 0;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public double getTrainingTime() {
        return 0;
    }

    @Override
    public String getClientName() {
        return this.title;
    }

    @Override
    public String getFullClientName() {
        return this.title;
    }

    @Override
    public void setClientName(String clientName) {
        
    }

    @Override
    public void setDeleted(Integer del) {
        
    }

    @Override
    public int getIdInt() {
        return this.timeOffId;
    }

    @Override
    public String getId() {
        return this.timeOffId.toString();
    }

    @Override
    public SortedSet getSchedules() {
        return parent.mySchedules.getClientSchedules(this);
    }

    @Override
    public boolean isEmployeeBanned(String eid) {
        return false;
    }

    @Override
    public Color getMyColor() {
        return Color.LIGHT_GRAY;
    }

}
