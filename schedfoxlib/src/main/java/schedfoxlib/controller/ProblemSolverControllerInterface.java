/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
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
public interface ProblemSolverControllerInterface {

    public ArrayList<Problemsolver> getProblemsWithGuardInstructionsWithinXDays(int client_id, int numberDays) throws RetrieveDataException;
    
    public Integer saveProblemSolver(Problemsolver ps) throws SaveDataException;
    
    public void saveProblemSolverEmail(ProblemsolverEmail email) throws SaveDataException;
    
    public ArrayList<ProblemSolverType> getProblemSolverTypes() throws RetrieveDataException;
    
    /**
     * Returns the problems solver contacts used for the last problem solver by
     * client.
     *
     * @param client_id
     * @return
     */
    ProblemSolverContact getProblemSolverContactByClient(int client_id) throws SQLException;

    ArrayList<ProblemSolverContacts> getProblemSolverContactsForProblem(int ps_id, String type) throws RetrieveDataException;

    ArrayList<Problemsolver> getProblemsForClient(int client_id) throws RetrieveDataException;

    public ArrayList<ProblemSolverContacts> getProblemSolverContactById(int problem_solver_contact_id) throws RetrieveDataException;
    
    public Integer getNextPrimaryId() throws RetrieveDataException;
    
    /**
     * Saves the problem solver contracts
     *
     * @param contacts
     * @throws SQLException
     */
    void saveProblemSolverContactsWithDate(ProblemSolverContact contact, ArrayList<ProblemSolverContacts> contacts) throws SQLException;

    /**
     * This method should be used if you want to reuse the contacts for any
     * reason, phone calls for example, we set the employees that should be
     * contacted, vs emails where a list is put together and just sent out.
     *
     * @param contacts
     * @throws SQLException
     */
    void saveReloadableProblemSolverContacts(ProblemSolverContact contact, ArrayList<ProblemSolverContacts> contacts, String type) throws SQLException;
    
}
