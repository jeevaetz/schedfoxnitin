/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.GenericRateCode;

/**
 *
 * @author user
 */
public class RateCodeModel<T extends GenericRateCode> extends AbstractTableModel {

        private ArrayList<T> rateCodes;
        private int companyId;
        
        private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        public RateCodeModel(int companyId) {
            rateCodes = new ArrayList<T>();
            this.companyId = companyId;
        }

        public T getClientRateCode(int row) {
            return rateCodes.get(row);
        }

        public int getRowCount() {
            return rateCodes.size();
        }

        public void clearData() {
            rateCodes = new ArrayList<T>();
            this.fireTableDataChanged();
        }

        public void addRateCode(T rateCode) {
            rateCodes.add(rateCode);
            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Code";
            } else if (columnIndex == 1) {
                return "Hour Type";
            } else if (columnIndex == 2) {
                return "Bill Amt";
            } else if (columnIndex == 3) {
                return "Overtime Bill";
            } else if (columnIndex == 4) {
                return "Pay Amt";
            } else if (columnIndex == 5) {
                return "Overtime Pay";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            GenericRateCode rateCode = rateCodes.get(rowIndex);
            
            if (columnIndex == 0) {
                return rateCode.getRateCode(companyId);
            } else if (columnIndex == 1) {
                return rateCode.getHourTypeObj(companyId).getHourType();
            } else if (columnIndex == 2) {
                if (rateCode.getBillAmount() == null || rateCode.getBillAmount().doubleValue() == 0) {
                    return "Not Set";
                }
                return currencyFormat.format(rateCode.getBillAmount().doubleValue());
            } else if (columnIndex == 3) {
                if (rateCode.getOvertimeBill() == null || rateCode.getOvertimeBill().doubleValue() == 0) {
                    return "Not Set";
                }
                return currencyFormat.format(rateCode.getOvertimeBill().doubleValue());
            } else if (columnIndex == 4) {
                if (rateCode.getPayAmount() == null || rateCode.getPayAmount().doubleValue() == 0) {
                    return "Not Set";
                }
                return currencyFormat.format(rateCode.getPayAmount().doubleValue());
            } else if (columnIndex == 5) {
                if (rateCode.getOvertimeAmount() == null || rateCode.getOvertimeAmount().doubleValue() == 0) {
                    return "Not Set";
                }
                return currencyFormat.format(rateCode.getOvertimeAmount().doubleValue());
            }
            return "";
        }

    }
