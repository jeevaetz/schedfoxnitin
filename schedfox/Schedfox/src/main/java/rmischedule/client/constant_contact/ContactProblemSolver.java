/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ContactProblemSolver.java
 *
 * Created on Mar 7, 2011, 12:48:10 PM
 */

package rmischedule.client.constant_contact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.client.components.EditProblemSolverDialog;
import rmischedule.client.components.ProblemSolverCellRenderer;
import rmischedule.client.components.ProblemsolverEmailDialog;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.Problemsolver;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.get_last_problem_solver_query;
import rmischeduleserver.mysqlconnectivity.queries.client.get_problem_solvers_query;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.save_problem_for_client_query;

/**
 *
 * @author user
 */
public class ContactProblemSolver extends GenericEditSubForm {

    private ProblemSolverCellRenderer problemSolverRenderer;
    
    /** Creates new form ContactProblemSolver */
    public ContactProblemSolver() {
        initComponents();
    }

    private ProblemSolverCellRenderer getCellRenderer() {
        if (problemSolverRenderer == null) {
            problemSolverRenderer = new ProblemSolverCellRenderer();
        }
        return problemSolverRenderer;
    }

    public void displayEmailFormForProblemSolver(Problemsolver ps) {
        Client client = (Client) super.myparent.getSelectedObject();
        ArrayList<ClientContact> contacts = client.getContacts(super.myparent.getConnection().myCompany);

        ProblemsolverEmailDialog emailDiag = new ProblemsolverEmailDialog(
                Main_Window.parentOfApplication,
                true,
                ps,
                "-1",
                super.myparent.getConnection(),
                contacts,
                this.getReportOfCorpCommunicator(ps));
        emailDiag.setVisible( true );
    }

    /**
     * This retrieves the problem solver from the database. Mainly to get the id
     * for our email function.
     * @param ps
     * @return
     */
    public Problemsolver retrieveProblemsolverFromDB(Problemsolver ps) {
        get_last_problem_solver_query getLastProblem = new get_last_problem_solver_query();
        getLastProblem.setPreparedStatement(new Object[]{ps.getClientId(), ps.getProblem()});
        this.myparent.getConnection().prepQuery(getLastProblem);
        return new Problemsolver(new Date(), myparent.getConnection().executeQuery(getLastProblem));
    }

    private JasperPrint getReportOfCorpCommunicator(Problemsolver ps) {
        try {
            Company myCompany = Main_Window.parentOfApplication.getCompanyById(super.myparent.getConnection().myCompany);
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/corporate_communicator.jasper");


            HashMap parameters = new HashMap();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
            parameters.put("active_db", myCompany.getDB());
            parameters.put("ps_id", ps.getPsId());
            return JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
        } catch (Exception e) {
            return null;
        }
    }

    public void saveProblemSolver(Problemsolver ps) {
        save_problem_for_client_query saveQuery = new save_problem_for_client_query();
        saveQuery.update(ps);
        this.myparent.getConnection().prepQuery(saveQuery);
        this.myparent.getConnection().executeUpdate(saveQuery);
    }

    public void reloadData() {
        this.clearData();
        get_problem_solvers_query getQuery = new get_problem_solvers_query(myparent.getMyIdForSave());
        getQuery.setPreparedStatement(new Object[]{Integer.parseInt(myparent.getMyIdForSave())});
        this.myparent.getConnection().prepQuery(getQuery);
        this.loadData(this.myparent.getConnection().executeQuery(getQuery));
    }

    private JasperPrint getReportOfCorpCommunictor() {
        Problemsolver ps = (Problemsolver) this.problemList.getSelectedValue();

        try {
            Company myCompany = Main_Window.parentOfApplication.getCompanyById(super.myparent.getConnection().myCompany);
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/corporate_communicator.jasper");


            HashMap parameters = new HashMap();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
            parameters.put("active_db", myCompany.getDB());
            parameters.put("ps_id", ps.getPsId());
            return JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
        } catch (Exception e) {
            return null;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        problemList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        problemList.setModel(new DefaultListModel());
        problemList.setCellRenderer(getCellRenderer());
        problemList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                problemListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(problemList);

        add(jScrollPane1);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setMinimumSize(new java.awt.Dimension(59, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 32));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void problemListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_problemListMouseClicked
        if (evt.getClickCount() > 1) {
            EditProblemSolverDialog ed = new EditProblemSolverDialog(Main_Window.parentOfApplication, true,
                    (Problemsolver) this.problemList.getSelectedValue(),
                    Integer.parseInt(super.myparent.getConnection().myCompany));
            ed.setVisible(true);

            if (ed.getSavedPS() != null) {
                Problemsolver ps = ed.getSavedPS();
                try {
                    ps.setClientId(Integer.parseInt(this.myparent.getMyIdForSave()));
                } catch (Exception e) {
                    ps.setClientId(0);
                }

                this.saveProblemSolver(ps);
                this.reloadData();
            }
            ed.dispose();
        }
    }//GEN-LAST:event_problemListMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        EditProblemSolverDialog ed = new EditProblemSolverDialog(Main_Window.parentOfApplication, true,
                Integer.parseInt(super.myparent.getConnection().myCompany));
        ed.setVisible(true);

        if (ed.getSavedPS() != null) {
            Problemsolver ps = ed.getSavedPS();
            try {
                ps.setClientId(Integer.parseInt(this.myparent.getMyIdForSave()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Error Saving", "Error Saving", JOptionPane.ERROR_MESSAGE);
            }
            try {
                ps.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            } catch (Exception e) {
                ps.setUserId(0);
            }

            this.saveProblemSolver(ps);
            try {
                this.displayEmailFormForProblemSolver(retrieveProblemsolverFromDB(ps));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.reloadData();
        }
        ed.dispose();
}//GEN-LAST:event_addButtonActionPerformed

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        get_problem_solvers_query query =  new get_problem_solvers_query(this.myparent.getMyIdForSave());
        query.setPreparedStatement(new Object[]{Integer.parseInt(this.myparent.getMyIdForSave())});
        this.myparent.getConnection().prepQuery(query);
        return query;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }

    @Override
    public void loadData(Record_Set rs) {
        Problemsolver[] problemSolvers = new Problemsolver[rs.length()];
        for (int i = 0; i < rs.length(); i++) {
            Problemsolver ps = new Problemsolver(new Date(), rs);
            problemSolvers[i] = ps;
            rs.moveNext();
        }
        this.problemList.setListData(problemSolvers);
        this.problemList.revalidate();
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Previous CC's";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
        
    }

    @Override
    public boolean userHasAccess() {
        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList problemList;
    // End of variables declaration//GEN-END:variables

}
