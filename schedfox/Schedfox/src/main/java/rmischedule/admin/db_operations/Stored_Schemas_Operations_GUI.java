/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Stored_Schemas_Operations_GUI.java
 *
 * Created on Nov 18, 2010, 9:42:08 AM
 */

package rmischedule.admin.db_operations;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.main.Main_Window;

/**
 *
 * @author jdavis
 */
public class Stored_Schemas_Operations_GUI extends javax.swing.JPanel
{
    //  private variable declarations
    private Database_Operations myParent;
    private Schema_Table schemaTable;
    private Stored_Schemas_Operations_GUI thisSSOGUI;
    private ArrayList<Schema_Data> storedList;

    //  private method implementations
    /**
     *  Method Name:  buildTable
     *  Purpose of Method:  initializes the table to be displayed by Schema_Table
    */
    private void buildTable()
    {
        this.jSchemaTable.getColumnModel().getColumn(0).setCellRenderer(new MyCellRenderer(this));
        this.jSchemaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.jSchemaTable.getColumnModel().getColumn(0).setPreferredWidth(61);
        this.jSchemaTable.getColumnModel().getColumn(1).setPreferredWidth(275);
        this.jSchemaTable.getColumnModel().getColumn(2).setPreferredWidth(275);
        this.jSchemaTable.getColumnModel().getColumn(3).setPreferredWidth(275);
    }   

    //  protected method implementations
    /**
     *  Method Name:  getList
     *  @return dataList - the data structure containing Schema_Data
     *  @see Schema_Data
    */
    protected ArrayList<Schema_Data> getList()  {return this.storedList;}

    /**
     *  Called by parent to set this objects internal data
     *  @param dataList - an ArrayList of <code>Schema_Data</code>
     */
    protected void setList(ArrayList<Schema_Data> storedList)
    {
        this.storedList.clear();
        this.storedList = storedList;
        Collections.sort(this.storedList);
        this.schemaTable.fireTableDataChanged();
    }

    /** Creates new form Stored_Schemas_Operations_GUI */
    public Stored_Schemas_Operations_GUI(Database_Operations myParent,
            ArrayList<Schema_Data> dataList)
    {
        this.storedList = dataList;
        Collections.sort(this.storedList);
        this.myParent = myParent;
        this.thisSSOGUI = this;
        this.schemaTable = new Schema_Table(this);
        initComponents();
        this.buildTable();
    }

    //  inner class implementation
    private class Schema_Table extends AbstractTableModel
    {
        //  private variable declarations
        Stored_Schemas_Operations_GUI immediateParent;
        JLabel activeLabel;
        JLabel inactiveLabel;

        //  private method implementation
        /**
         *  Method Name: getIsDisplayActive
         *  Purpose of Method:  determines if the Schema_Data object is active,
         *      returns the activeLabel if true, inactiveLabel if not true
         *  @return JLabel - either activeLabel if true, inactiveLabel if not
         *      true
         */
        private JLabel getIsDisplayActive(int rowIdx)
        {
            if(immediateParent.getList().get(rowIdx).isActive())
                return activeLabel;
            else
                return inactiveLabel;
        }

        //  public method implementations
        /**
         *  Method Name:  Schema_Table
         *  Purpose of Method:  creates an instance of this class
         *  @param myParent - the instance of Stored_Schemas_Operations_GUI
         *      that invokes this class
         */
        public Schema_Table( Stored_Schemas_Operations_GUI immediateParent)
        {
            this.immediateParent = immediateParent;
            activeLabel = new JLabel();
            inactiveLabel = new JLabel();
        }

        /**
         *  Method Name:  getColumnName
         *  Purpose of Method:  returns the value of each column name
         *  @return columnName - a string representing the name of the column
         */
        @Override
        public String getColumnName(int columnIdx)
        {
            StringBuffer columnName = new StringBuffer();
            
            switch(columnIdx)
            {
                case 0:  columnName.append("Active?");
                   break;
                case 1:  columnName.append("Company");
                   break;
                case 2:  columnName.append("File");
                   break;
                case 3:  columnName.append("Schema");
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
        public int getRowCount()
        {
            return immediateParent.getList().size();
        }

        /**
         *  Method Name:  getColumnCount
         *  Purpose of Method returns the number of columns in the table
         *  @return columncount - the number of columns in the table
         */
        @Override
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
                case 0:  return this.getIsDisplayActive(rowIndex);
                case 1:  return immediateParent.getList().get(rowIndex).getCompany();
                case 2:  return immediateParent.getList().get(rowIndex).getFileName();
                case 3:  return immediateParent.getList().get(rowIndex).getSchemaName();
                default:
                    return null;
            }   //  end switch statement
        }   //  end method block
    }   //  end inner class block

    /**
     *  Class Name:  MyCellRenderer
     *  Purpose of Class:  defines how each cell will be rendered within the
     *      table
     */
    private class MyCellRenderer extends DefaultTableCellRenderer
    {
        private Stored_Schemas_Operations_GUI immediateParent;

        /**
         *  Method Name: getIsDisplayActive
         *  Purpose of Method:  determines if the Schema_Data object is active,
         *      returns the activeLabel if true, inactiveLabel if not true
         *  @return JLabel - either activeLabel if true, inactiveLabel if not
         *      true
         */
        private JLabel getIsDisplayActive(int rowIdx, Object value)
        {
            if(immediateParent.getList().get(rowIdx).isActive())
            {
                ((JLabel)value).setIcon(Main_Window.Active_Schema_16x16_px);
                ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
                return ((JLabel) value);
            }
            else
            {
                ((JLabel)value).setIcon(Main_Window.Inactive_Schema_16x16_px);
                ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
                return ((JLabel) value);
            }
        }

        /** Create instance of this class */
        public MyCellRenderer(Stored_Schemas_Operations_GUI immediateParent)
        {
            this.immediateParent = immediateParent;
        }

        /**
         *  Method Name:  getTableCellRendererComponent
         *  Purpose of method:  returns a component which which to render a
         *      particular cell
         *  @param table
         *  @param value
         *  @param isSelected
         *  @param hasFocus
         *  @param row
         *  @param column
         *  @return JLabel - a JLabel with a set icon
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column)
        {
            if(column == 0)
            {
                value = getIsDisplayActive(row, value);
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
