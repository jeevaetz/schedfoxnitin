/*
 * List_View.java
 *
 * Created on February 5, 2004, 1:18 PM
 *  Date Last Modified:  07/21/2010
 *  Last Modified By:  Jeffrey Davis
 *  Reason for Modification:  overwritten fireTableDataChanged added so that
 *      sort becomes optional.
 */

package rmischedule.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.util.*;
import rmischedule.components.*;
import schedfoxlib.model.util.Record_Set;
/**
 * @author  jason.allen
 *
 * Need to add Column width functions.
 * Probably would not be a bad idea to add an optimum width function later on.
 *
 */
public class List_View extends AbstractTableModel {
    
    /*
     *Statics for column type
     */
    public final static int STRING  = 0;
    public final static int NUMBER  = 1;
    public final static int BOOLEAN = 2;
    public final static int BUTTON = 3;
    
    private StringIntHashtable columnNames;
    private Column_Header[] columnHeaders;

    private int[] visibleColumns;
    
    private Vector<Vector<Object>> data;
    private Vector<Vector<Boolean>> isEnabled;
    
    private Row[] rows;
    
    public JTable myTable;
    public JScrollPane myScrollPane;
    
    private int sortBy = 0;
    
    private ListSelectionListener myListSelectionListener;
    private int selectedRow;
    private boolean isPrintable;
    private Vector<listViewButtonClass> myButtonListeners;
    
    /*
     * Creates a new instance of List_View 
     */
    public List_View(){
        initialize();
        isPrintable = false;
    }
    
    /**
     * Constructor should be used to construct a printable List_View....
     */ 
    public List_View(boolean printable) {
        initialize();
        isPrintable = printable;
    }
    
    private void initialize() {
        myButtonListeners = new Vector();
        rows = new Row[0];
        data = new Vector<Vector<Object>>();
        isEnabled = new Vector<Vector<Boolean>>();
        columnHeaders = new Column_Header[0];
        columnNames = new StringIntHashtable();
    }
    
    /*
     * Create our scroll pane and our table 
     */
    public void buildTable(){
        myTable = new JTable(this);
        myTable.setRowSelectionAllowed(true);
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        myTable.setAutoResizeMode(myTable.AUTO_RESIZE_OFF);
        //myTable.setAutoResizeMode(myTable.AUTO_RESIZE_ALL_COLUMNS);
        addMouseListener();        
        myScrollPane = new JScrollPane(myTable);

        /**
         *this will set the default column width
         */
        
        TableColumn column = null;
        for(int i=0;i<getColumnCount();i++){
            int width = columnHeaders[visibleColumns[i]].width;
            myTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        
        /* this will keep our selectedRow up to date */        
        myTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener(){
            	public void valueChanged(ListSelectionEvent e){
                    if (e.getValueIsAdjusting()) return;        
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if(!lsm.isSelectionEmpty()) {
                        selectedRow = lsm.getMinSelectionIndex();
                        if(myListSelectionListener != null){
                            myListSelectionListener.valueChanged(e);
                        }
                    }
                }
        }
        );
        try {
            myTable.setDefaultRenderer(Class.forName("java.lang.Object"), new coloredTableCellRenderer());
            myTable.setDefaultRenderer(Class.forName("javax.swing.JButton"), new jbuttonRenderer());
            //Chooses whether to render in checkbox or text...depending on printing or not...
            if (isPrintable) {
                myTable.setDefaultRenderer(Class.forName("java.lang.Boolean"), new coloredTableCellRenderer());
                myTable.setDefaultRenderer(Class.forName("java.lang.Integer"), new LabelRenderer());
            } else {
                myTable.setDefaultRenderer(Class.forName("java.lang.Boolean"), new CheckboxRenderer());
                myTable.setDefaultRenderer(Class.forName("java.lang.Integer"), new LabelRenderer());
            }
        } catch (Exception e) {}
    }
    
    public Column_Header[] getColumns() {
        return columnHeaders;
    }
    
    public void setColumns(Column_Header[] tempColumns) {
        for (int i = 0; i < tempColumns.length; i++) {
            addColumn(tempColumns[i]);
        }
    }
    
    public void maximizeTable() {
        myTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    /*
     * This overrides our data change method so that we can rebuild the
     * arrays.
     */
    public void fireTableDataChanged(){
        Arrays.sort(rows);
        super.fireTableDataChanged();
    }

    //  addition by Jeffrey Davis on 07/21/2010
    /**
     *  Method Name:  fireTableDataChanged
     *  Purpose of Method:  overrides fireTableDataChanged so that the proggam
     *      must inform the the class is it wishes to sort the table prior
     *      to calling the super.fireTableDataChanged.  
     *  Arguments:  a boolean describing if a sort should occur
     *  Returns:  void
     *  Preconditions:  table data has changed, gui must be updated
     *  Postconditions:  super called to redisplay GUI
     *  WRITTEN FOR:  this method was overwritten specifically for the 
     *      messaging List_View
     */
    public void fireTableDataChanged(boolean doSort)
    {
        if(doSort)
            Arrays.sort(rows);
        super.fireTableDataChanged();
    }
    //  addition by Jeffrey Davis on 07/21/2010 complete
    
    /*
     * Sort our table
     */
    public void sort(int c){
        sortBy = c;
        fireTableDataChanged();
    }
    
    public int getSelectedRow(){
        return selectedRow;
    }
    
    public Object getSelectedCell(int col){
        return getTrueValueAt(selectedRow, col);
    }
    /*
     *  This makes adding actions soooo much easier.
     */
    public void addListSelectionListener(ListSelectionListener l){
        myListSelectionListener = l;
    }
    
    public void addMouseListener(MouseListener ml) {
        myTable.addMouseListener(ml);
    }
    
    /*
     *  Add the action listener for the column headers to sort column
     */
    public void addMouseListener(){
        myTable.getTableHeader().addMouseListener(
            new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    super.mouseClicked(event);
                    if(event.getClickCount() < 2){
                        return;
                    }
                    
                    int tableColumn = myTable.columnAtPoint(event.getPoint());
                    int modelColumn = myTable.convertColumnIndexToModel(tableColumn);
                    sort(modelColumn);
                }
            }
        );
    }
    
    public int getColumnCount() {
        return visibleColumns.length;
    }
    
    public int getTrueColumnCount() {
        return columnNames.size();
    }
    
    public int getRowCount(){
        return data.size();
    }
    
    public String getColumnName(int columnIndex){
        if(columnIndex < getColumnCount()){
            String name = columnHeaders[visibleColumns[columnIndex]].name;
            return(name);
        }else{
            return null;
        }
    }
    
    public String getTrueColumnName(int columnIndex){
        if(columnIndex < getColumnCount()){
            String name = columnHeaders[columnIndex].name;
            return(name);
        }else{
            return null;
        }
    }
    
    public Class getColumnClass(int columnIndex){
        return columnHeaders[visibleColumns[columnIndex]].getMyClass();
    }

    public Class getTrueColumnClass(int columnIndex){
        return columnHeaders[columnIndex].getMyClass();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex){       
        if((rowIndex < getRowCount()) && (columnIndex < getColumnCount())){            
            return data.get(rows[rowIndex].index).get(visibleColumns[columnIndex]);
        }else{
            return null;
        }        
    }

    @Override
    public void setValueAt(Object value, int row, int col){
        if((row < getRowCount()) && (col < getColumnCount())){
            if (data.get(rows[row].index) == null) {
                data.add(new Vector<Object>());
            }
            data.get(rows[row].index).set(visibleColumns[col], value);
            columnHeaders[visibleColumns[col]].doAction(row, col);
        }        
    }
    
    public Object getTrueValueAt(int rowIndex, int columnIndex){
        if((rowIndex < getRowCount()) && (columnIndex < getTrueColumnCount())){
            try {
                return data.get(rows[rowIndex].index).get(columnIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public void setTrueValueAt(Object value, int row, int col){
        if((row < getRowCount()) && (col < getTrueColumnCount())){
            data.get(rows[row].index).set(col, value);
        }        
    }
    
    public void clearRows(){
        data = new Vector<Vector<Object>>();
        isEnabled = new Vector<Vector<Boolean>>();
        rows = new Row[0];
    }
    

    /*
     * This adds a row.
     *  -- I know using arraycopy isn't the best thing in the world but I'm 
     *     not really sure if there is a better way of doing it.  I think
     *     the ArrayList does something similar.
     */    
    public boolean addRow(Object[] row){
        if(getTrueColumnCount() == row.length) {
            Vector<Object> newRow = new Vector<Object>();
            Vector<Boolean> isEnabledRow = new Vector<Boolean>();
            for (int d = 0; d < row.length; d++) {
                newRow.add(row[d]);
                isEnabledRow.add(true);
            }
            data.add(newRow);
            isEnabled.add(isEnabledRow);
            addRowIndex();

            return true;
        }else{
            return false;
        }
    }
    
    /**
     *  Method Name:  addRowSetEnabledBooleans
     *  Purpose for Method:  adds a row, yet also sets whether the boolean
     *      within the row is editiable; designed specifically for employee
     *      messaging list
     *  Arguments:  an object describing the components of the row
     *  Returns:  true for addition succesful, false if not
     *  Preconditions:  data known for row insertion, not inserted into class
     *  Postconditions:  data inserted
     */
      public boolean addRowSetEnabledBooleans(Object[] row){
        if(getTrueColumnCount() == row.length) {
            Vector<Object> newRow = new Vector<Object>();
            Vector<Boolean> isEnabledRow = new Vector<Boolean>();
            for (int d = 0; d < row.length; d++)
            {
                //  check if boolean should be enabled
                if(row[d].toString().matches("false"))
                {
                    isEnabledRow.add(false);
                    //  initialize button to false
                    newRow.add(false);
                }
                else if(row[d].toString().matches("true"))
                {
                    isEnabledRow.add(true);
                    //  initialize button to false
                    newRow.add(false);
                }
                //  case for non-boolean elements of row
                else
                {
                    newRow.add(row[d]);
                    isEnabledRow.add(true);
                }
            }
            data.add(newRow);
            isEnabled.add(isEnabledRow);
            addRowIndex();

            return true;
        }else{
            return false;
        }
    }

    public boolean addRow(Record_Set rs) {
        if (getTrueColumnCount() == rs.getColumnSize()) {
            Vector<Object> newRow = new Vector<Object>();
            Vector<Boolean> isEnabledRow = new Vector<Boolean>();
            
            for (int i = 0; i < rs.getColumnSize(); i++) {
                if (this.getTrueColumnClass(i).toString().equals("class java.lang.String")) {
                    newRow.add(rs.getString(i));
                } else if (this.getTrueColumnClass(i).toString().equals("class java.lang.Integer")) {
                    newRow.add(rs.getInt(i));
                } else {
                    newRow.add(rs.getBoolean(i));
                }
                isEnabledRow.add(true);
            }

            data.add(newRow);
            isEnabled.add(isEnabledRow);
            addRowIndex();
            return true;
        }
        return false;
    }
    
    /*
     * For the nodes that return an array list
     */
    public boolean addRow(ArrayList row){
        if(getTrueColumnCount() == row.size()){
            Vector<Object> newRow = new Vector<Object>();
            Vector<Boolean> isEnabledRow = new Vector<Boolean>();
            newRow.addAll(row);

            data.add(newRow);
            isEnabled.add(isEnabledRow);
            
            addRowIndex();
            return true;
        }else{
            return false;
        }
    }
    
    public void addListViewButton(listViewButtonClass myButtonListener) {
        myButtonListeners.add(myButtonListener);
    }
    
    /*
     *  Adds a new Row() for our sorting.
     */
    public void addRowIndex(){
        Row[] tmpRows = new Row[rows.length + 1];
        System.arraycopy(rows, 0, tmpRows, 0, rows.length);
        tmpRows[rows.length] = new Row();
        tmpRows[rows.length].index = rows.length;
        rows = null;
        rows = tmpRows;
    }

    @Override
    public boolean isCellEditable(int row, int col){
        boolean retVal = columnHeaders[visibleColumns[col]].editable;
        try {
            retVal = retVal && this.isEnabled.get(row).get(col);
        } catch (Exception e) {

        }
        return retVal;
    }
    
    public boolean addColumn(String name, int type){
        return addColumn(name, type, false);
    }

    public boolean addColumn(String name, int type, int width){
        return addColumn(name, type, false, true, width);
    }

    public boolean addColumn(String name, int type, boolean editable, int width){
        return addColumn(name, type, editable, true, width);
    }

    public boolean addColumn(String name, int type, boolean editable){
        return addColumn(name, type, editable, true);
    }

    public boolean addColumn(String name, int type, boolean editable, boolean visible){
        return addColumn(name, type, editable, visible, 100);
    }
    
    public boolean addColumn(Column_Header tempCol) {
        return addColumn(tempCol.name, tempCol.type, false, tempCol.visible, tempCol.width);
    }

    public boolean addColumn(String name, int type, boolean editable, boolean visible, int width){
        int i, c, a;       
        Object o;
        
        Column_Header tmpHeader = new Column_Header(name, type, editable, visible, width);        

        a = columnHeaders.length;
        
        Column_Header[] tmpObject = new Column_Header[a + 1];        
        System.arraycopy(columnHeaders, 0, tmpObject, 0, a);
        tmpObject[a] = tmpHeader;
        columnHeaders = null;
        columnHeaders = tmpObject;
        
        columnNames.add(name, a);
        
        a++;
        
        c = data.size();
        switch(type){
            
            case BOOLEAN:
                o = new Boolean(false);
                break;
            
            case NUMBER:
                o = new Integer(0);
                break;
            
            case STRING:
                o = new String();
                break;

            case BUTTON:
                o = new String();
                break;
                
            default:
                o = new Object();
        }
        
//        int b = a - 1;
//        for(i=0;i<c;i++){
//            Object[] ao = new Object[a];
//            System.arraycopy(data.get(i),0,ao,0,b);
//            ao[b] = o;
//            Vector<Object> vals = new Vector<Object>();
//            for (int tao = 0; tao < ao.length; tao++) {
//                vals.add(ao[tao]);
//            }
//            data.set(i, vals);
//        }

        buildVisibleList();
        
        return true;
    }
    
    public void setEditColumnAction(int col, List_View_Edit_Action lvea){
        columnHeaders[col].lvea = lvea;
    }
    
    public void buildVisibleList(){
        int i, c, e, vc;
        
        c = columnHeaders.length;
        e = 0;
        vc = 0;
        
        for(i=0;i<c;i++){
            if(columnHeaders[i].visible){
                vc++;
            }
        }
        
        visibleColumns = new int[vc];
        
        for(i=0;i<c;i++){
            if(columnHeaders[i].visible){
                visibleColumns[e] = i;
                e++;
            }
        }
        
    }
    
    /**
     * Please do not change this, toString is used by some forms to determine if our data has changed...
     */
    public String toString() {
        StringBuilder myRetString = new StringBuilder();
        for (int i = 0; i < getRowCount(); i++) {
            for (int col = 0; col <= getTrueColumnCount(); col++) {
                try {
                    if (getColumns()[col].editable) {
                        try {
                            if (getColumns()[col].type == STRING) {
                                myRetString.append(((String)getTrueValueAt(i, col)).toString());
                            } else if (getColumns()[col].type == BOOLEAN) {
                                if (((Boolean)getTrueValueAt(i, col)).booleanValue()) {
                                    myRetString.append("t");
                                } else {
                                    myRetString.append("f");
                                }
                            }
                        } catch (Exception ex) {}
                    }
                } catch (Exception e) {}
            }
        }
        return myRetString.toString();
    }


    

/*------------------------------------------------------------------------------
   This is our sub class section.
------------------------------------------------------------------------------*/    
    
    /**
     * Small class to just spruce up how our table looks if you want to use it...
     */
    private class coloredTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Object valToSend = value;
            try {
                Boolean val = (Boolean)value;
                if (val == true) {
                    valToSend = new String("Yes");
                } else {
                    valToSend = new String("No");
                }
            } catch (Exception e) {}
            setEnabled(table == null || table.isEnabled()); 
            setBackground(getColorByRow(row));
            super.getTableCellRendererComponent(table, valToSend, isSelected, hasFocus, row, column);
            return this;
        }
    }
    
    private class jbuttonRenderer extends JButton implements TableCellRenderer {
        
        private boolean alreadyRan;
        
        public jbuttonRenderer() {
            super();
            alreadyRan = false;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((String)value);
            if (isSelected && alreadyRan == false) {
                alreadyRan = true;
                for (int i = 0; i < myButtonListeners.size(); i++) {
                    myButtonListeners.get(i).mouseClicked(row, column);
                }
                setSelected(false);
            } else if (!isSelected) {
                alreadyRan = false;
            }
            return this;
        }
    }
    
    private java.awt.Color getColorByRow(int row) {
        if (row % 2 == 0) {
            return (java.awt.Color.WHITE);
        } else {
            return (new java.awt.Color(221, 217, 233));
        }
    }
    
    /**
     * Used for checkboxes cause for some reason they render differently and background was not set right...
     */
    class CheckboxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckboxRenderer() {
            super();
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ((Boolean)value) {
                setSelected(true);
            } else {
                setSelected(false);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            if (table.getSelectedRow() == row) {
                setBackground (table.getSelectionBackground());
            } else { 
                setBackground(getColorByRow(row));
            }
            return this;
        }
    }
    
    /**
     * Used for checkboxes cause for some reason they render differently and background was not set right...
     */
    class LabelRenderer extends JLabel implements TableCellRenderer {
        public LabelRenderer() {
            super();
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setText(String.valueOf((Integer)value));
            setOpaque(true);
            if (table.getSelectedRow() == row) {
                setBackground (table.getSelectionBackground());
            } else { 
                setBackground(getColorByRow(row));
            }
            return this;
        }
    }
    
    
    /*
     *  Our row object.  This is basicly for sorting with out have to 
     *  screw with our initial array.  as well as retrieve information
     *  for said array.
     */
    class Row implements Comparable{
        private int index;
        
        public Row(){        
            
        }
        
        public int compareTo(Object o){
            Row otherRow = (Row)o;
            Object a = data.get(index).get(visibleColumns[sortBy]);
            Object b = data.get(otherRow.index).get(visibleColumns[sortBy]);
            if(a instanceof Comparable){
                if (a instanceof Boolean) {
                    return - ((Comparable)a).compareTo(b);
                } else if (a instanceof String && b instanceof String) {
                    return ((String)a).compareToIgnoreCase((String)b);
                }
                return ((Comparable)a).compareTo(b);
            }else{
                return index - otherRow.index;
            }
        }        
    }
    
    public static abstract class listViewButtonClass {
        public listViewButtonClass() {
            
        }
        
        public abstract void mouseClicked(int row, int column);
    }
    
    /*
     *  Our column header class gives us information about the type of
     *  column the column is, as well as other information.
     */
    class Column_Header{
        public int type;
        public int width;

        public String name;
        
        public boolean editable;
        public boolean visible;
        
        public List_View_Edit_Action lvea;
        
        public Column_Header(String n, int t, boolean e, boolean v, int w){
            type     = t;
            name     = n;
            editable = e;
            visible  = v;
            width    = w;
        }

        public Class getMyClass(){
            switch(type){            
                case List_View.BOOLEAN:
                    return Boolean.class;
                case List_View.NUMBER:
                    return Integer.class;
                case List_View.STRING:
                    return String.class;
                case List_View.BUTTON:
                    return JButton.class;
                default:
                    return String.class;
            }        
        }
        
        public void doAction(int row, int col){
            if(lvea != null){
                lvea.editAction(row);
            }
        }
    }
}