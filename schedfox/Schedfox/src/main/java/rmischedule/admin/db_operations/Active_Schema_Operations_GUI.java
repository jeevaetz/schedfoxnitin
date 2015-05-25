/**
 *  FileName:  Active_Schema_Operations_GUI.java
 *  @ author Jeffrey N. Davis
 *  Date Created:  11/12/2010
 *  Purpose of File:  file contains the Active_Schema_Operations_GUI class, a class
 *      designed to display all informatin related to Active Schema operations
 */

//  package declarations
package rmischedule.admin.db_operations;

//  import declarations
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.main.Main_Window;
import rmischedule.misc.SchemaServletLoader;

/**
 *  Class Name:  Active_Schema_Operations_GUI
 *  @author jdavis
 *  Purpose of Class:  a class designed to display all informatin related
 *      to Schema operations
 *  @extends javax.swing.JPanel
 */
public class Active_Schema_Operations_GUI extends javax.swing.JPanel
{
    //  private variable declarations
    private Schema_Table schemaTable;
    private Active_Schema_Operations_GUI thisASOGUI;
    private Database_Operations myParent;
    private ArrayList<Schema_Data> activeList;

    //  private method implementations
    /**
     *  Method Name:  buildTable
     *  Purpose of Method:  initializes the table to be displayed by Schema_Table
    */
    private void buildTable()
    {
        this.jSchemaTable.getColumnModel().getColumn(3).setCellRenderer(new MyCellRenderer());
        this.jSchemaTable.getColumnModel().getColumn(4).setCellRenderer(new MyCellRenderer());
        this.jSchemaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.jSchemaTable.getColumnModel().getColumn(0).setPreferredWidth(270);
        this.jSchemaTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        this.jSchemaTable.getColumnModel().getColumn(2).setPreferredWidth(270);
        this.jSchemaTable.getColumnModel().getColumn(3).setPreferredWidth(58);
        this.jSchemaTable.getColumnModel().getColumn(4).setPreferredWidth(58);
        this.jSchemaTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if ( e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    switch(column)
                    {
                        case 3:  thisASOGUI.saveAction(row);
                            break;
                        case 4:  thisASOGUI.removeAction(row);
                            break;
                    }   //  end switch
                }   //  end if
            }   //  end mouseClicked
        }); //  end mouseListener
    }   //  end method

    /**
     *  Method Name:  saveAction
     *  Purpose of Method:  uses SchemaServletLoader to call the server and
     *      save a schema, then displays pass/fail
     *  @param rowIdx - the position of the schema in the data structure to be
     *      saved
     *  @see SchemaServletLoader
    */
    private void saveAction(int rowIdx)
    {
        String schema = this.getList().get(rowIdx).getSchemaName();
        int decision = JOptionPane.showConfirmDialog(this, "Would you like to " +
               "save:  " + schema + "?",
               "Save Schema?", JOptionPane.OK_CANCEL_OPTION);
        if(decision == 0)
        {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
            boolean success = SchemaServletLoader.saveSchema(schema);
            if(success)
            {
                JOptionPane.showMessageDialog(this, schema + " saved successfully.",
                    "Save Schema?", JOptionPane.INFORMATION_MESSAGE);
                this.myParent.reload();
            }
            else
                JOptionPane.showMessageDialog(this, "Error saving:  " + schema +
                    " , please contact Schedfox administrators.", "Save Schema",
                    JOptionPane.ERROR_MESSAGE);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
       }
    }

    /**
     *  Method Name:  removeAction
     *  Purpose of Method:  uses SchemaServletLoader to call the server and
     *      delete a schema, then displays pass/fail
     *  @param rowIdx - the position of the schema in the data structure to be
     *      deleted
     *  @see SchemaServletLoader
     */
    private void removeAction(int rowIdx)
    {
        if(this.getList().get(rowIdx).getFileName().isEmpty())
        {
            JOptionPane.showMessageDialog(myParent, "Error:  Cannot remove schema prior to save.",
                "Error Saving!", JOptionPane.ERROR_MESSAGE);
        }
        else {
        String schema = this.getList().get(rowIdx).getSchemaName();
        String file = this.getList().get(rowIdx).getFileName();
        int decision = JOptionPane.showConfirmDialog(this, "Would you like to " +
               "remove:  " + schema + "?",
               "Remove Schema?", JOptionPane.OK_CANCEL_OPTION);
        if(decision == 0)
        {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);
            boolean success = SchemaServletLoader.removeSchema(schema);
            if(success)
            {
                JOptionPane.showMessageDialog(this, schema + " removed successfully.",
                    "Remove Schema", JOptionPane.INFORMATION_MESSAGE);
                this.myParent.reload();
            }
            else
                JOptionPane.showMessageDialog(this, "Error deleting:  " + schema +
                    " , please contact Schedfox administrators.", "Delete Schema",
                    JOptionPane.ERROR_MESSAGE);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
       }
        }
    }

    /**
     *  Method Name:  getList
     *  @return dataList - the data structure containing Schema_Data
     *  @see Schema_Data
    */
    protected ArrayList<Schema_Data> getList()  {return this.activeList;}

    /**
     *  Called by parent to set this objects internal data
     *  @param dataList - an ArrayList of <code>Schema_Data</code>
     */
    protected void setList(ArrayList<Schema_Data> activeList)
    {
        this.activeList.clear();
        this.activeList = activeList;
        Collections.sort(this.activeList);
        this.schemaTable.fireTableDataChanged();
    }

    //  public method implemenations
    /** Creates new form Active_Schema_Operations_GUI */
    public Active_Schema_Operations_GUI(Database_Operations myParent, ArrayList<Schema_Data> dataList)
    {
        this.activeList = dataList;
        Collections.sort(this.activeList);
        this.myParent = myParent;

        schemaTable = new Schema_Table(this);
        thisASOGUI = this;
        initComponents();
        this.buildTable();
     }

    //  inner class implementation
    /**
     *  Class Name:  Schema_Table
     *  Purpose of Class:  extends Abstract Table Model, displays all the
     *      information for schema operations
     *  @extends Abstract Table Model
     */
    private class Schema_Table extends AbstractTableModel
    {
        //  private variable declarations
        Active_Schema_Operations_GUI immediateParent;
        JLabel saveLabel;
        JLabel deleteLabel;
        
        //  private method implementations

        //  public method implementations
        /**
         *  Method Name:  Schema_Table
         *  Purpose of Method:  creates and instance of this class
         *  @param myParent - the instance of Active_Schema_Operations_GUI that invokes
         *      this class
         */
        public Schema_Table( Active_Schema_Operations_GUI myParent )
        {
            this.immediateParent = myParent;
            saveLabel = new JLabel();
            deleteLabel = new JLabel();
        }

        /**
         *  Method Name:  getColumnName
         *  Purpose of Method:  returns the value of each column name
         *  @return columnName - the name of the column
         */
        @Override
        public String getColumnName(int columnIdx)
        {
            StringBuffer columnName = new StringBuffer();

            switch(columnIdx)
            {
                case 0:  columnName.append("Schema Name");
                    break;
                case 1:  columnName.append("Company");
                    break;
                case 2:  columnName.append("Last Used");
                    break;
                case 3:  columnName.append("Save");
                    break;
                case 4:  columnName.append("Remove");
                    break;
            }

            return columnName.toString();
        }

        /**
         *  Method Name:  getRowCount
         *  Purpose of Method:  returns the number of rows in the table
         *  @return rowCount - the number of rows in the table
         */
        @Override
        public int getRowCount()  {return immediateParent.getList().size();}
        
        /**
         *  Method Name:  getColumnCount
         *  Purpose of Method:  returns the number of columns in the table
         *  @return columnCount - the number of columns in the table
         */
        @Override
        public int getColumnCount() {return 5;}
        
        /**
         *  Method Name:  getValueAt
         *  Purpose of Method:  returns the value of the table cell passed in
         *      by the params
         * @param rowIndex - the row number
         * @param columnIndex - the column number
         * @return Object - the object contained within the table
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            //  return value
            switch(columnIndex)
            {
                case 0:  return immediateParent.getList().get(rowIndex).getSchemaName();
                case 1:  return immediateParent.getList().get(rowIndex).getCompany();
                case 2:  return immediateParent.getList().get(rowIndex).getDateLastUsed();
                case 3:  return saveLabel;
                case 4:  return deleteLabel;
                default:
                    return null;
            }
        }
    }

    /**
     *  Class Name:  MyCellRenderer
     *  Purpose of Class:  defines how each cell will be rendered within the table
     */
    private class MyCellRenderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column)
        {
            if(column == 3)
            {
                ((JLabel)value).setIcon(Main_Window.Save_Schema_16x16_px);
                ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
            }
            else if(column == 4)
            {
                ((JLabel)value).setIcon(Main_Window.Remove_Schema_16x16_px);
                ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
            }
        
            return ((JLabel) value);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTablePanel = new javax.swing.JPanel();
        jScrollTablePane = new javax.swing.JScrollPane();
        jSchemaTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTablePanel.setLayout(new java.awt.BorderLayout());

        jSchemaTable.setModel(schemaTable);
        jScrollTablePane.setViewportView(jSchemaTable);

        jTablePanel.add(jScrollTablePane, java.awt.BorderLayout.CENTER);

        add(jTablePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jSchemaTable;
    private javax.swing.JScrollPane jScrollTablePane;
    private javax.swing.JPanel jTablePanel;
    // End of variables declaration//GEN-END:variables

};
