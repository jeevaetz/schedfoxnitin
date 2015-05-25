/**
 *  FileName:  Inactive_Schema_Operations_GUI.java
 *  Date Created:  11/23/2010
 *  @author Jeffrey Davis
 *  Purpose of File:  file contains the Inactive_Schema_Operations_GUI class,
 *      a class designed to display all informatin related to Inactive Schema
 *      operations
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
 *  Class Name:  Inactive_Schema_Operations_GUI
 *  Purpose of Class:  a class designed to display all informatin related to
 *      Inactive Schema operations
 *  Date Created:  11/23/2010
 *  @author jdavis
 */
public class Inactive_Schema_Operations_GUI extends javax.swing.JPanel
{
    //  private variable declarations
    private Database_Operations myParent;
    private Schema_Table schemaTable;
    private ArrayList<Schema_Data> inactiveList;
    private Inactive_Schema_Operations_GUI thisISOGUI;

    //  private method implementations
    /**
     *  Method Name:  restoreAction
     *  Purpose of Method:  uses SchemaServletLoader to call the server and
     *      restore a schema, then displays pass/fail
     *  @param rowIdx - the position of the schema in the data structure to be
     *      restored
     *  @see SchemaServletLoader
     */
    private void restoreAction(int rowIdx)
    {
        String fileName = this.getList().get(rowIdx).getFileName();
        int decision = JOptionPane.showConfirmDialog(this, "Would you like to " +
               "restore:  " + fileName + "?",
               "Restore Schema?", JOptionPane.OK_CANCEL_OPTION);
        if(decision == 0)
        {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);
            boolean success = SchemaServletLoader.restoreSchema(fileName);
            if(success)
            {
                JOptionPane.showMessageDialog(this, fileName + " restored successfully.",
                    "Restore Schema", JOptionPane.INFORMATION_MESSAGE);
                this.myParent.reload();
            }
            else
                JOptionPane.showMessageDialog(this, "Error restoring:  " + fileName +
                    " , please contact Schedfox administrators.", "Restore Schema",
                    JOptionPane.ERROR_MESSAGE);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
       }
    }

    /**
     *  Method Name:  deleteAction
     *  Purpose of Method:  uses SchemaServletLoader to call the server and
     *      delete a schema permanently from the stored folder, then displays
     *      pass / fail
     *  @param rowIdx - the position of the schema in the data structure to be
     *      deleted
     *  @see SchemaServletLoader
     */
    private void deleteAction(int rowIdx)
    {
         String fileName = this.getList().get(rowIdx).getFileName();
        int decision = JOptionPane.showConfirmDialog(this, "Would you like to " +
               "delete:  " + fileName + "?",
               "Delete Schema?", JOptionPane.OK_CANCEL_OPTION);
        if(decision == 0)
        {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);
            boolean success = SchemaServletLoader.permDeleteSchema(fileName);
            if(success)
            {
                JOptionPane.showMessageDialog(this, fileName + " deleted successfully.",
                    "Delete Schema", JOptionPane.INFORMATION_MESSAGE);
                this.myParent.reload();
            }
            else
                JOptionPane.showMessageDialog(this, "Error deleting:  " + fileName +
                    " , please contact Schedfox administrators.", "Restore Schema",
                    JOptionPane.ERROR_MESSAGE);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
       }
    }

    /**
     *  Method Name:  buildTable
     *  Purpose of Method:  initializes the table to be displayed by
     *      jSchemaTable
     */
    private void buildTable()
    {
        this.jSchemaTable.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        this.jSchemaTable.getColumnModel().getColumn(3).setCellRenderer(new MyCellRenderer());
        this.jSchemaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.jSchemaTable.getColumnModel().getColumn(0).setPreferredWidth(378);
        this.jSchemaTable.getColumnModel().getColumn(1).setPreferredWidth(378);
        this.jSchemaTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        this.jSchemaTable.getColumnModel().getColumn(3).setPreferredWidth(65);
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
                        case 2:  thisISOGUI.restoreAction(row);
                            break;
                        case 3:  thisISOGUI.deleteAction(row);
                            break;
                    }   //  end switch
                }   //  end if
            }   //  end mouseClicked
        }); //  end mouseListener
    }

    //  protected method implementations
    /**
     *  Method Name:  getList
     *  @return dataList - the data structure containing Schema_Data
     *  @see Schema_Data
    */
    protected ArrayList<Schema_Data> getList()  {return this.inactiveList;}

    /**
     *  Called by parent to set this objects internal data
     *  @param dataList - an ArrayList of <code>Schema_Data</code>
     */
    protected void setList(ArrayList<Schema_Data> inactiveList)
    {
        this.inactiveList.clear();
        this.inactiveList = inactiveList;
        Collections.sort(this.inactiveList);
        this.schemaTable.fireTableDataChanged();
    }

    //  public method implementations
    /**
     *  Method Name:  Inactive_Schema_Operations_GUI
     *  Purpose of Method:  creates an instance of this class
     *  @param myParent - an instance of Database_Operations that creates
     *      this class
     *  @see Database_Operations
     */
    public Inactive_Schema_Operations_GUI(Database_Operations myParent,
            ArrayList<Schema_Data> inactiveList)
    {
        //  set class variables
        this.myParent = myParent;
        this.inactiveList = inactiveList;
        Collections.sort(this.inactiveList);
        this.thisISOGUI = this;
        schemaTable = new Schema_Table(this);

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
        Inactive_Schema_Operations_GUI immediateParent;
        JLabel restoreLabel;
        JLabel deleteLabel;

         //  public method implementations
        /**
         *  Method Name:  Schema_Table
         *  Purpose of Method:  creates and instance of this class
         *  @param myParent - the instance of Active_Schema_Operations_GUI that invokes
         *      this class
         */
        public Schema_Table( Inactive_Schema_Operations_GUI myParent )
        {
            this.immediateParent = myParent;
            restoreLabel = new JLabel();
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
                case 0:  columnName.append("Company");
                    break;
                case 1:  columnName.append("File");
                    break;
                case 2:  columnName.append("Restore");
                    break;
                case 3:  columnName.append("Delete");
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
        public int getRowCount()    {return immediateParent.getList().size();}

        /**
         *  Method Name:  getColumnCount
         *  Purpose of Method:  returns the number of columns in the table
         *  @return columnCount - the number of columns in the table
         */
        public int getColumnCount() {return 4;}
    
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
                case 0: return immediateParent.getList().get(rowIndex).getCompany();
                case 1: return immediateParent.getList().get(rowIndex).getFileName();
                case 2: return restoreLabel;
                case 3: return deleteLabel;
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
            if(column == 2)
            {
                ((JLabel)value).setIcon(Main_Window.Restore_Schema_16x16_px);
                ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
            }
            else if(column == 3)
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
        jScrollTabelPane = new javax.swing.JScrollPane();
        jSchemaTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTablePanel.setLayout(new java.awt.BorderLayout());

        jSchemaTable.setModel(schemaTable);
        jScrollTabelPane.setViewportView(jSchemaTable);

        jTablePanel.add(jScrollTabelPane, java.awt.BorderLayout.CENTER);

        add(jTablePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jSchemaTable;
    private javax.swing.JScrollPane jScrollTabelPane;
    private javax.swing.JPanel jTablePanel;
    // End of variables declaration//GEN-END:variables
};
