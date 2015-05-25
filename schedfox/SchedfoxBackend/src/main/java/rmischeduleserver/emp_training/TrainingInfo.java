/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training;

import java.util.Vector;

/**
 *
 * @author user
 */
public class TrainingInfo {
    private Vector<TrainingCourse> trainingCourses;
    private TrainingError trainingError;

    public TrainingInfo() {
        trainingCourses = new Vector<TrainingCourse>();
        trainingError = new TrainingError();
    }

    /**
     * @return the trainingCourses
     */
    public Vector<TrainingCourse> getTrainingCourses() {
        return trainingCourses;
    }

    /**
     * @param trainingCourses the trainingCourses to set
     */
    public void setTrainingCourses(Vector<TrainingCourse> trainingCourses) {
        this.trainingCourses = trainingCourses;
    }

    /**
     * @return the trainingError
     */
    public TrainingError getTrainingError() {
        return trainingError;
    }

    /**
     * @param trainingError the trainingError to set
     */
    public void setTrainingError(TrainingError trainingError) {
        this.trainingError = trainingError;
    }


}
