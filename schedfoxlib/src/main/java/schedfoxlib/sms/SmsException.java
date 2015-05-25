/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.sms;

import java.util.ArrayList;

/**
 *
 * @author vnguyen
 */
public class SmsException extends Exception {
    private ArrayList<String> mistake = new ArrayList<String>();
    private ArrayList<String> failedNumbers = new ArrayList<String>();
    private int errors = 0;

    public SmsException() {
        super();             // call superclass constructor

    }

    public void addErr(String err, String sourceNumber) {
        this.getMistake().add(err);
        this.failedNumbers.add(sourceNumber);
    }

    public boolean hasError() {
        if (this.getMistake().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return the mistake
     */
    public ArrayList<String> getMistake() {
        return mistake;
    }

    /**
     * @param mistake the mistake to set
     */
    public void setMistake(ArrayList<String> mistake) {
        this.mistake = mistake;
    }

    /**
     * @return the failedNumbers
     */
    public ArrayList<String> getFailedNumbers() {
        return failedNumbers;
    }

    /**
     * @param failedNumbers the failedNumbers to set
     */
    public void setFailedNumbers(ArrayList<String> failedNumbers) {
        this.failedNumbers = failedNumbers;
    }

    /**
     * @return the errors
     */
    public int getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(int errors) {
        this.errors = errors;
    }
}
