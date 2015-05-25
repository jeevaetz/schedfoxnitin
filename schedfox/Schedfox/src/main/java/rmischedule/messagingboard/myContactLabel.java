/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messagingboard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author vnguyen
 */
public class myContactLabel extends JLabel {
    private Contact myContact;
    private GenericCreateMessageScreen myParent;
    private myContactLabel myself;
    public myContactLabel() {
        super();
    }

    public myContactLabel(final Contact c, GenericCreateMessageScreen parent) {
        super();
        this.myself = this;
        this.myParent = parent;
        this.myContact = c;
        this.setVisible(true);
        this.setText(c.toString());
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    String confirm = "Do you wish to remove " + c.toString() + "from the mailing list?";
                    int response = JOptionPane.showConfirmDialog(getMyParent(),confirm, "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {

                    } else if (response == JOptionPane.YES_OPTION) {

                        getMyParent().destroyContactLabel(getMyself());
                    } else if (response == JOptionPane.CLOSED_OPTION) {

                    }
                }
            }
        });
    }

    /**
     * @return the myContact
     */
    public Contact getMyContact() {
        return myContact;
    }

    /**
     * @param myContact the myContact to set
     */
    public void setMyContact(Contact myContact) {
        this.myContact = myContact;
    }

    /**
     * @return the myParent
     */
    public GenericCreateMessageScreen getMyParent() {
        return myParent;
    }

    /**
     * @param myParent the myParent to set
     */
    public void setMyParent(GenericCreateMessageScreen myParent) {
        this.myParent = myParent;
    }

    /**
     * @return the myself
     */
    public myContactLabel getMyself() {
        return myself;
    }

    /**
     * @param myself the myself to set
     */
    public void setMyself(myContactLabel myself) {
        this.myself = myself;
    }
}
