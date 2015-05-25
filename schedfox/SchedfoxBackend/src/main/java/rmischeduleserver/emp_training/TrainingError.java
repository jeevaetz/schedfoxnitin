/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.emp_training;

/**
 *
 * @author user
 */
public class TrainingError {
    private int loginError;
    private int createAccountError;

    public TrainingError() {

    }
    
    /**
     * @return the loginError
     */
    public int getLoginError() {
        return loginError;
    }

    /**
     * @param loginError the loginError to set
     */
    public void setLoginError(int loginError) {
        this.loginError = loginError;
    }

    /**
     * @return the createAccountError
     */
    public int getCreateAccountError() {
        return createAccountError;
    }

    /**
     * @param createAccountError the createAccountError to set
     */
    public void setCreateAccountError(int createAccountError) {
        this.createAccountError = createAccountError;
    }


}
