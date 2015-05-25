/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messaging.datacomponents;

import rmischedule.messaging.xMessagingEdit;

/**
 *
 * @author user
 */
public abstract class MessagingFilterType {

    public static int NO_FILTER = 0;
    public static int LESS_THAN_40 = 1;
    public static int AVAILABLE = 2;

    private String displayName;
    protected xMessagingEdit messagingEdit;
    private int type;

    public MessagingFilterType(xMessagingEdit messagingEdit, String displayName, int type) {
        this.displayName = displayName;
        this.messagingEdit = messagingEdit;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public int getType() {
        return this.type;
    }

    public abstract void runFilter();
}
