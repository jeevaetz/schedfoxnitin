/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.ProblemSolverControllerInterface;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.ServerSideConnection;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.*;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ProblemSolverContact;
import schedfoxlib.model.ProblemSolverContacts;
import schedfoxlib.model.ProblemSolverType;
import schedfoxlib.model.Problemsolver;
import schedfoxlib.model.ProblemsolverEmail;

/**
 *
 * @author user
 */
public class ProblemSolverController implements ProblemSolverControllerInterface {

    private static ProblemSolverController myInstance;
    private String companyId;

    private ProblemSolverController(String companyId) {
        this.companyId = companyId;
    }
    
    public ArrayList<Problemsolver> getProblemSolversByParams(ArrayList<Integer> clientIds, ArrayList<Integer> employeeIds, ArrayList<Integer> types,
            Date startDate, Date endDate, String queryText) {
        ArrayList<Problemsolver> retVal = new ArrayList<Problemsolver>();
        get_problem_solver_by_search_params_query getProblemSolvers = new get_problem_solver_by_search_params_query();
        getProblemSolvers.setCompany(companyId);
        getProblemSolvers.update(clientIds, employeeIds, types, startDate, endDate, queryText);
        try {
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            
            Record_Set rs = conn.executeQuery(getProblemSolvers, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new Problemsolver(new Date(), rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return retVal;
    }

    public static ProblemSolverController getInstance(String companyId) {
        if (myInstance == null) {
            myInstance = new ProblemSolverController(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            try {
                conn.setConnectionObject(new ServerSideConnection());
            } catch (Exception e) {
                System.out.println("Could not set up server!");
            }
        }
        return myInstance;
    }
    
    @Override
    public Integer saveProblemSolver(Problemsolver ps) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_new_problem_solver_query saveQuery = new save_new_problem_solver_query();
        saveQuery.setCompany(companyId);
        saveQuery.update(ps);
        try {
            Record_Set rst = conn.executeQuery(saveQuery, "");
            return rst.getInt("myid");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return 0;
    }

    public ArrayList<ProblemsolverEmail> getUnsentProblemSolverEmails() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_unsent_problem_solvers_query getProblemSolvers = new get_unsent_problem_solvers_query();
        getProblemSolvers.setCompany(companyId);
        getProblemSolvers.setPreparedStatement(new Object[]{});
        ArrayList<ProblemsolverEmail> retVal = new ArrayList<ProblemsolverEmail>();
        try {
            Record_Set rs = conn.executeQuery(getProblemSolvers, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new ProblemsolverEmail(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public void saveProblemSolverEmail(ProblemsolverEmail email) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_problem_solver_email_query saveProblemEmailQuery = new save_problem_solver_email_query();
        saveProblemEmailQuery.setCompany(companyId);
        saveProblemEmailQuery.update(email);
        try {
            conn.executeUpdate(saveProblemEmailQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public ArrayList<ProblemSolverContacts> getProblemSolverContactsForProblem(int ps_id, String type)
            throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ProblemSolverContacts> retVal = new ArrayList<ProblemSolverContacts>();
        get_problemcontacts_for_problem_query groupQuery = new get_problemcontacts_for_problem_query();
        groupQuery.setCompany(companyId);
        groupQuery.setPreparedStatement(new Object[]{ps_id, type});
        try {
            Record_Set rst = conn.executeQuery(groupQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ProblemSolverContacts(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    /**
     * Returns the problems solver contacts used for the last problem solver by
     * client.
     *
     * @param client_id
     * @return
     */
    @Override
    public ProblemSolverContact getProblemSolverContactByClient(int client_id) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_problem_solver_contact_query groupQuery = new get_problem_solver_contact_query();
        groupQuery.setCompany(companyId);
        groupQuery.setPreparedStatement(new Object[]{client_id});
        Record_Set rst = conn.executeQuery(groupQuery, "");
        return new ProblemSolverContact(rst);
    }

    @Override
    public ArrayList<Problemsolver> getProblemsForClient(int client_id) throws RetrieveDataException {
        ArrayList<Problemsolver> problems = new ArrayList<Problemsolver>();
        
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_problems_for_client_query getProblemsQuery = new get_problems_for_client_query();
        getProblemsQuery.setPreparedStatement(new Object[]{client_id, client_id});
        getProblemsQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getProblemsQuery, "");
            
            for (int r = 0; r < rst.length(); r++) {
                Problemsolver problem = new Problemsolver(new Date(), rst);
                problems.add(problem);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        return problems;
    }

    /**
     * This method should be used if you want to reuse the contacts for any
     * reason, phone calls for example, we set the employees that should be
     * contacted, vs emails where a list is put together and just sent out.
     *
     * @param contacts
     * @throws SQLException
     */
    @Override
    public void saveReloadableProblemSolverContacts(ProblemSolverContact contact, ArrayList<ProblemSolverContacts> contacts, String type) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        //Clear out any contacts that haven't already been contacted.
        clear_out_non_contacted_problem_solvers_query clearQuery =
                new clear_out_non_contacted_problem_solvers_query();
        clearQuery.setPreparedStatement(new Object[]{type, contact.getPsId()});
        clearQuery.setCompany(companyId);
        conn.executeUpdate(clearQuery, companyId);

        save_problem_solver_contact_query saveContactQuery = new save_problem_solver_contact_query();
        saveContactQuery.setCompany(companyId);
        saveContactQuery.update(contact, true);
        conn.executeUpdate(saveContactQuery, companyId);

        save_problem_solver_contacts_query saveQuery = new save_problem_solver_contacts_query();
        saveQuery.setCompany(companyId);
        for (int c = 0; c < contacts.size(); c++) {
            check_that_problemcontact_doesnt_exist_query checkQuery = new check_that_problemcontact_doesnt_exist_query();
            checkQuery.setCompany(companyId);
            checkQuery.setPreparedStatement(new Object[]{contact.getPsId(),
                        contacts.get(c).getContactId(), contacts.get(c).getContactType()});
            Record_Set rst = conn.executeQuery(checkQuery, "");
            if (rst.length() == 0) {
                saveQuery.update(contacts.get(c));
                conn.executeUpdate(saveQuery, "");
            }
        }
    }

    /**
     * Saves the problem solver contracts
     *
     * @param contacts
     * @throws SQLException
     */
    @Override
    public void saveProblemSolverContactsWithDate(ProblemSolverContact contact, ArrayList<ProblemSolverContacts> contacts) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_problem_solver_contact_query saveContactQuery = new save_problem_solver_contact_query();
        saveContactQuery.setCompany(companyId);
        saveContactQuery.update(contact, true);
        conn.executeUpdate(saveContactQuery, companyId);
        Date dateOfContact = conn.getSQLConnection().getTime();

        save_problem_solver_contacts_query saveQuery = new save_problem_solver_contacts_query();
        saveQuery.setCompany(companyId);
        for (int c = 0; c < contacts.size(); c++) {
            contacts.get(c).setMessageDeliveredDate(dateOfContact);
            saveQuery.update(contacts.get(c));
            conn.executeUpdate(saveQuery, "");
        }
    }

    public ArrayList<ProblemSolverContacts> getProblemSolverContactById(int problem_solver_contact_id) throws RetrieveDataException {
        get_problem_solver_contacts_query contactsQuery = new get_problem_solver_contacts_query();
        contactsQuery.setCompany(companyId);
        contactsQuery.setPreparedStatement(new Object[]{problem_solver_contact_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ProblemSolverContacts> retVal = new ArrayList<ProblemSolverContacts>();
        try {
            Record_Set rst = conn.executeQuery(contactsQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                ProblemSolverContacts contact = new ProblemSolverContacts(rst);
                retVal.add(contact);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public Integer getNextPrimaryId() throws RetrieveDataException {
        Integer retVal = new Integer(0);
        problem_solver_contact_seq_query seqQuery = new problem_solver_contact_seq_query();
        seqQuery.setCompany(companyId);
        seqQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            Record_Set rst = conn.executeQuery(seqQuery, "");
            retVal = rst.getInt("seq");
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<Problemsolver> getProblemsWithGuardInstructionsWithinXDays(int client_id, int numberDays) throws RetrieveDataException {
        ArrayList<Problemsolver> problems = new ArrayList<Problemsolver>();
        
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_problems_with_guard_instruct_for_x_query getProblemsQuery = new get_problems_with_guard_instruct_for_x_query();
        getProblemsQuery.setPreparedStatement(new Object[]{client_id, client_id, numberDays});
        getProblemsQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getProblemsQuery, "");
            
            for (int r = 0; r < rst.length(); r++) {
                Problemsolver problem = new Problemsolver(new Date(), rst);
                problems.add(problem);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        return problems;
    }

    public void saveProblemSolverTypesToProblem(Integer ps_id, ArrayList<Integer> types) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_problem_solver_types_to_problem_query getProblemTypes = new save_problem_solver_types_to_problem_query();
        getProblemTypes.update(ps_id, types);
        getProblemTypes.setCompany(companyId);
        
        try {
            conn.executeUpdate(getProblemTypes, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    public ArrayList<ProblemSolverType> getProblemsSolverTypesForProblem(Integer ps_id) throws RetrieveDataException {
        ArrayList<ProblemSolverType> retVal = new ArrayList<ProblemSolverType>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_problem_solver_types_for_problem_query getProblemTypes = new get_problem_solver_types_for_problem_query();
        getProblemTypes.setPreparedStatement(new Object[]{ps_id});
        getProblemTypes.setCompany(companyId);
        
        try {
            Record_Set rst = conn.executeQuery(getProblemTypes, "");
            
            for (int r = 0; r < rst.length(); r++) {
                ProblemSolverType problem = new ProblemSolverType(rst);
                retVal.add(problem);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        return retVal;
    }
    
    @Override
    public ArrayList<ProblemSolverType> getProblemSolverTypes() throws RetrieveDataException {
        ArrayList<ProblemSolverType> retVal = new ArrayList<ProblemSolverType>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_problem_solver_types_query getProblemTypes = new get_problem_solver_types_query();
        getProblemTypes.setPreparedStatement(new Object[]{});
        getProblemTypes.setCompany(companyId);
        
        try {
            Record_Set rst = conn.executeQuery(getProblemTypes, "");
            
            for (int r = 0; r < rst.length(); r++) {
                ProblemSolverType problem = new ProblemSolverType(rst);
                retVal.add(problem);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        return retVal;
    }
}
