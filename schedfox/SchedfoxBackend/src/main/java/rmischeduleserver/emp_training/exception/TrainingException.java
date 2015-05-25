/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training.exception;

/**
 *
 * @author user
 */
public class TrainingException extends Exception {

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
    public enum ErrorType {
        MISSING_EMAIL("EMAIL"), SHORT_USERNAME("USERNAME"), BAD_PASSWORD("PASSWORD"),
        SERVER_ERROR("SERVER");
        private String value;

        ErrorType(String val) {
            this.value = val;
        }
    }

    private ErrorType errorType;
    private String error;

    public TrainingException(ErrorType errorType, String error) {
        this.errorType = errorType;
        this.error = error;
    }


    /**
     * @return the errorType
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * @param errorType the errorType to set
     */
    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }
}
