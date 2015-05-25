/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messagingboard;

/**
 *
 * @author vnguyen
 */
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class AutoCompleteComboBox extends JComboBox implements JComboBox.KeySelectionManager {

    //variables for autocomplete functionality
    private String searchFor; //string that user entered
    private long lap;   //long to represent time, in order to create an interval of checks

    private void initialize() {
        //creates a default Jcombobox and makes it editable
        this.setEditable(true);
        //set initial time
        lap = new java.util.Date().getTime();

        // makes this combobo the keyselection that chooses selection
        setKeySelectionManager(this);
        //creates a var and sets it to be the editor component for this combobx, to be computated on
        JTextField tf;
        tf = (JTextField) getEditor().getEditorComponent();
        tf.setDocument(new CBDocument());
        //add an anonmyous actionListener to autocomplete if any part is matching on set in the combobox
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JTextField tf = (JTextField) getEditor().getEditorComponent();
                String text = tf.getText();
                ComboBoxModel aModel = getModel();
                String current;
                boolean exit = false;
                int i = 0;
                //looks through the combobox for a match
                while (!exit && i < aModel.getSize()) {
                    current = aModel.getElementAt(i).toString();
                    if (current.toLowerCase().startsWith(text.toLowerCase())) {
                        //if matched then text is matched to corresponding value
                        tf.setText(current);
                        //unhighlighted section
                        tf.setSelectionStart(text.length());
                        //total length
                        tf.setSelectionEnd(current.length());
                        //set the chosen as the item selected
                        aModel.setSelectedItem(aModel.getElementAt(i));
                        exit = true;
                    }
                    i++;
                }
            }
        });
    }

    public class CBDocument extends PlainDocument {

        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            //handles case when no string is added
            if (str == null) {
                return;
            }
            // calls super method of JcomboBox to add text
            super.insertString(offset, str, a);
            //makes the options visible
            if (!isPopupVisible() && str.length() != 0) {
                fireActionEvent();
            }
        }
    }

    public AutoCompleteComboBox() {
        super();
        this.initialize();
    }

    @Override
    public void addItem(Object o) {
        super.addItem(o);
        this.setSelectedItem("");
    }

    public AutoCompleteComboBox(Object[] items) {
        super(items);
        this.initialize();
    }

    public int selectionForKey(char aKey, ComboBoxModel aModel) {
        //grabs current date in number of secs from 1970
        long now = new java.util.Date().getTime();

        if (searchFor != null && aKey == KeyEvent.VK_BACK_SPACE && searchFor.length() > 0) {
            //case where either something was delected or backspace was hit; then smaller search is looked for
            searchFor = searchFor.substring(0, searchFor.length() - 1);
        } else {
            if (lap + 1000 < now) {
                //if has been over a second, will look for nothing but the key
                searchFor = "" + aKey;
            } else {
                // if less time, then we will look for previous search plus key pressed
                searchFor = searchFor + aKey;
            }
        }
        //reset timer
        lap = now;
        //grabs the model and sees if txt entered is a prefix of any of our options and if it is, then that index is returned
        // to the corresponding object
        String current;
        for (int i = 0; i < aModel.getSize(); i++) {
            current = aModel.getElementAt(i).toString().toLowerCase();
            if (current.toLowerCase().startsWith(searchFor.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
}
