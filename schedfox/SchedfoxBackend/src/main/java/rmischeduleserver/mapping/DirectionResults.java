/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mapping;
import java.net.URL;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import rmischeduleserver.mapping.gson.Maneuver;
import rmischeduleserver.mapping.gson.Route;

/**
 * Used by GSon w/ objects in mapping.gson package to encapsulate data sent back
 * from Mapquest.
 * @author user
 */
public class DirectionResults implements JRDataSource {

    private Route route;

    private transient Iterator<Maneuver> manueverIterator;
    private transient Maneuver currentManuever;

    public DirectionResults() {
        
    }

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean next() throws JRException {
        if (manueverIterator == null) {
            manueverIterator = getRoute().getLegs().getFirst().getManeuvers().iterator();
        }
        if (manueverIterator.hasNext()) {
            currentManuever = manueverIterator.next();
            return true;
        }
        return false;
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        if (jrf.getName().equals("direction")) {
            return currentManuever.getNarrative();
        } else if (jrf.getName().equals("icon")) {
            try {
                return new URL(currentManuever.getIconUrl());
            } catch (Exception e) {
                return null;
            }
        }
        return "";
    }
}
