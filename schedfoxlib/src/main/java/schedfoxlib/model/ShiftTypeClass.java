/*
 * ShiftTypeClass.java
 *
 * Created on May 9, 2005, 10:01 AM
 */

package schedfoxlib.model;
import java.lang.Math;
/**
 *
 * @author Ira
 * This class was created to allow us to easily store multiple shift attributes using
 * only one field in our database where each place in the number is used to hold
 * different attributes about the shift so that we would not have to modify our
 * database structure each time we need to add a new feature to our shift types...
 */
public class ShiftTypeClass {
    
    //Digit one
    public static final int SHIFT_CONFIRMED                 = 0;
    public static final int SHIFT_UNCONFIRMED               = 1;
    public static final int SHIFT_VIEWED_BY_EMPLOYEE        = 2;
    
    //Digit in tens spot, reconciled data
    public static final int SHIFT_RECONCILED                = 20;
    public static final int SHIFT_UNRECONCILED              = 10;
    
    //Digit in hundreds spot, training data
    public static final int SHIFT_NON_TRAINING              = 100;
    public static final int SHIFT_TRAINING_SHIFT            = 300;
    public static final int SHIFT_NEEDS_TRAINING            = 400;
    public static final int SHIFT_IS_POSTMEETING            = 500;
    public static final int SHIFT_TRAINER_SHIFT             = 600;
    
    
    //Digit in thousands spot, training data
    public static final int SHIFT_PAYABLE                   = 2000;
    public static final int SHIFT_NON_PAYABLE               = 1000;
    public static final int SHIFT_PAYABLE_OT                = 3000;
    
    //Digit in ten thousands spot, training data
    public static final int SHIFT_BILLABLE                  = 20000;
    public static final int SHIFT_NON_BILLABLE              = 10000;
    public static final int SHIFT_BILLABLE_OT               = 30000;
    
    public static final int SHIFT_OVERRIDE_CONFLICT         = 100000;
    public static final int SHIFT_NOTOVERRIDDEN_CONFLICT    = 200000;

    public static final int SHIFT_EXTRA_COVERAGE_SHIFT      = 1000000;
    
    private int trainerId;
    private String trainerName;
    private int weeklyRotation;
    
    private int[] myVal; 
    
    public ShiftTypeClass() {
        myVal = new int[8];
        for (int i = 0; i < myVal.length; i++) {
            myVal[i] = 0;
        }
        trainerId = 0;
        trainerName = new String();
    }
    
    public ShiftTypeClass clone() {
        ShiftTypeClass myClone = new ShiftTypeClass(toString());
        myClone.setTrainerInformation(getTrainerId(), getTrainerName());
        return myClone;
    }
    
    /** Creates a new instance of ShiftTypeClass */
    public ShiftTypeClass(String shiftNum) {
        myVal = new int[8];
        for (int i = 0; i < myVal.length; i++) {
            myVal[i] = 0;
        }
        trainerId = 0;
        trainerName = new String();
        setVal(shiftNum);
    }
    
    /**
     * Set Trainer information, both id and name to be used...
     */
    public void setTrainerInformation(int id, String name) {
        trainerId = id;
        trainerName = name;
    }
    
    public int getTrainerId() {
        return trainerId;
    }
    
    public String getTrainerName() {
        return trainerName;
    }
    
    /**
     * This method should only be called by the constructor as it overwrites all other
     * setting values....
     */
    public void setVal(String shiftNum) {
        try {
            loadData(Integer.parseInt(shiftNum));
        } catch (Exception e) {}
    }
    
    public void setVal(int shiftNum) {
        loadData(shiftNum);
    }
    
    public void updateVal(int data) {
        if (data >= 10000000) {
            myVal[7] = getPlace(10000000, data);
        } else if (data >= 1000000) {
            myVal[6] = getPlace(1000000, data);
        } else if (data >= 100000) {
            myVal[5] = getPlace(100000, data);
        }else if (data >= 10000) {
            myVal[4] = getPlace(10000, data);
        } else if (data >= 1000) {
            myVal[3] = getPlace(1000, data);
        } else if (data >= 100) {
            myVal[2] = getPlace(100, data);
        } else if (data >= 10) {
            myVal[1] = getPlace(10, data);
        } else {
            myVal[0] = getPlace(1, data);
        }
    }
    
    private void loadData(int data) {
        int lastData = 0;
        if (data >= 10000000) {
            myVal[7] = getPlace(10000000, data);
            lastData = myVal[7];
        } else if (data >= 1000000) {
            myVal[6] = getPlace(1000000, data);
            lastData = myVal[6];
        } else if (data >= 100000) {
            myVal[5] = getPlace(100000, data);
            lastData = myVal[5];
        } else if (data >= 10000) {
            myVal[4] = getPlace(10000, data);
            lastData = myVal[4];
        } else if (data >= 1000) {
            myVal[3] = getPlace(1000, data);
            lastData = myVal[3];
        } else if (data >= 100) {
            myVal[2] = getPlace(100, data);
            lastData = myVal[2];
        } else if (data >= 10) {
            myVal[1] = getPlace(10, data);
            lastData = myVal[1];
        } else {
            myVal[0] = getPlace(1, data);
            lastData = -1;
        }
        if (lastData > -1) {
            loadData(data - lastData);
        }
    }
    
    
    public int getVal() {
        int val = 0;
        for (int i = 0; i < myVal.length; i++) {
            val += myVal[i];
        }
        return val;
    }
    
    public String toString() {
        return getVal() + "";
    }
    
    /**
     * Takes either a int or ShiftTypeClass and compares Values...damnit!
     */
    public boolean equals(Object o) {
        Integer intVal;
        ShiftTypeClass typeVal;
        String strVal;
        try {
            typeVal = (ShiftTypeClass)o;
            return isShiftType(typeVal.getVal());
        } catch (Exception e) {}
        try {
            strVal = (String)o;
            return isShiftType(Integer.parseInt(strVal));
        } catch (Exception e) {}
        try {
            intVal = (Integer)o;
            return isShiftType(intVal.intValue());
        } catch (Exception e) {}
        return false;
    }
    
    private int getPlace(int place, int val) {
        return ((val / place) % 10) * place;
    }
    
    public boolean isShiftType(int shiftType) {
        if (shiftType < 10) {
            return myVal[0]     == shiftType;
        } else if (shiftType < 100) {
            return myVal[1]     == shiftType;
        } else if (shiftType < 1000) {
            return myVal[2]     == shiftType;
        } else if (shiftType < 10000) {
            return myVal[3]     == shiftType;
        } else if (shiftType < 100000) {
            return myVal[4]     == shiftType;
        } else if (shiftType < 1000000) {
            return myVal[5]     == shiftType;
        } else if (shiftType < 10000000) {
            return myVal[6]     == shiftType;
        } else {
            return myVal[7]     == shiftType;
        }
    }

    /**
     * @return the weeklyRotation
     */
    public int getWeeklyRotation() {
        return weeklyRotation;
    }

    /**
     * @param weeklyRotation the weeklyRotation to set
     */
    public void setWeeklyRotation(int weeklyRotation) {
        this.weeklyRotation = weeklyRotation;
    }
    
    
}
