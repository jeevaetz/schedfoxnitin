/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class EmployeeTestScore {
    private int employeeTestScoreId;
    private int employeeId;
    private int testId;
    private double score;
    private boolean scorePassed;
    private Date dateTaken;
    private EmployeeTest employeeTest;

    public EmployeeTestScore(Record_Set rst) {
        try {
            this.employeeTestScoreId = rst.getInt("employee_test_score_id");
        } catch (Exception e) {
            System.out.println("Bad Employee Test Score ID");
        }
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {
            System.out.println("Bad Employee ID");
        }
        try {
            if (!rst.getString("employee_test_id").equals("")) {
                this.testId = rst.getInt("employee_test_id");
            } else {
                this.testId = rst.getInt("test_id");
            }
        } catch (Exception e) {
            System.out.println("Bad Test ID");
        }
        try {
            this.score = Double.parseDouble(rst.getString("score"));
        } catch (Exception e) {
            System.out.println("Bad Score");
        }
        try {
            this.scorePassed = rst.getBoolean("passed");
        } catch (Exception e) {
            System.out.println("Bad Score Passed");
        }
        try {
            this.dateTaken = rst.getDate("date_taken");
        } catch (Exception e) {
            System.out.println("Bad Date Taken");
        }

    }

    /**
     * @return the employeeTestScoreId
     */
    public int getEmployeeTestScoreId() {
        return employeeTestScoreId;
    }

    /**
     * @param employeeTestScoreId the employeeTestScoreId to set
     */
    public void setEmployeeTestScoreId(int employeeTestScoreId) {
        this.employeeTestScoreId = employeeTestScoreId;
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the testId
     */
    public int getTestId() {
        return testId;
    }

    /**
     * @param testId the testId to set
     */
    public void setTestId(int testId) {
        this.testId = testId;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the scorePassed
     */
    public boolean isScorePassed() {
        return scorePassed;
    }

    /**
     * @param scorePassed the scorePassed to set
     */
    public void setScorePassed(boolean scorePassed) {
        this.scorePassed = scorePassed;
    }

    /**
     * @return the dateTaken
     */
    public Date getDateTaken() {
        return dateTaken;
    }

    /**
     * @param dateTaken the dateTaken to set
     */
    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public EmployeeTest getEmployeeTest(String companyId) {
        if (this.employeeTest == null) {
            EmployeeControllerInterface employeeController = ControllerRegistryAbstract.getEmployeeController(companyId);
            try {
                this.employeeTest = employeeController.getEmployeeTest(this.getTestId());
            } catch (Exception exe) {
                this.employeeTest = new EmployeeTest();
            }
        }
        return this.employeeTest;
    }
}
