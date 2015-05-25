/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.utility;

/**
 *
 * @author dalbers
 */
public class StringHlpr {
    public static void main(String[] args) {
        System.out.println(stripLeadingZeros("000023456"));
    }

    public static String stripLeadingZeros(String toStrip){

        String toTest = toStrip;
        while(toTest.startsWith("0")){
            toTest = toTest.substring(1);
        }

        return toTest;

    }
}
