/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training;

/**
 *
 * @author user
 */
public class TrainingCourse {
    private String courseName;
    private boolean courseExists;
    private String URL;

    public TrainingCourse() {
        courseName = "";
        courseExists = false;
        URL = "";
    }

    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * @return the courseExists
     */
    public boolean isCourseExists() {
        return courseExists;
    }

    /**
     * @param courseExists the courseExists to set
     */
    public void setCourseExists(boolean courseExists) {
        this.courseExists = courseExists;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }
}
