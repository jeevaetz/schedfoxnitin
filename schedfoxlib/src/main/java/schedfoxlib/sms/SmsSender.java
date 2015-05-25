package schedfoxlib.sms;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vnguyen
 */
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vnguyen
 */
public class SmsSender {

    private List<String> sendList;

    private static String serveraddress = "http://sms1.champ.net";
    ;
    //grabs the ip of the sms gateway
    private static int port = 4000;
    //grabs the port of the sms port
    private static String user = "user";
    //grabs the user for authentication tracking on the sms server
    private static String password = "password";
    //grabs the password for authentication to the sms 
    private static HashMap<String, String> errCodes = new HashMap<String, String>();

    /**
     * @return the serveraddress
     */
    public static String getServeraddress() {
        return serveraddress;
    }

    /**
     * @param aServeraddress the serveraddress to set
     */
    public static void setServeraddress(String aServeraddress) {
        serveraddress = aServeraddress;
    }

    /**
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * @param aPort the port to set
     */
    public static void setPort(int aPort) {
        port = aPort;
    }

    /**
     * @return the user
     */
    public static String getUser() {
        return user;
    }

    /**
     * @param aUser the user to set
     */
    public static void setUser(String aUser) {
        user = aUser;
    }

    /**
     * @return the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * @param aPassword the password to set
     */
    public static void setPassword(String aPassword) {
        password = aPassword;
    }
    private String number = "";
    private String[] phoneList;
    private String message = "";
    private static int modem = 0;
    private static int totalModems = 4;
    private static int category = 1;
    private String result = new String();
    private static final int maxMsgSize = 140;
    //private static final int maxMsgSize = 462; // 462 set because modem handles messages up to this length

    public static void main(String[] args) {
        try {
            SmsSender mySender = new SmsSender("817-846-5019", ("It's a sentence of "));
            ArrayList<String> messages1 = mySender.splitMessageApart(("It's a sentence often used to test keyboards and it contains all the letters of the alphabet: ‘The quick brown fox jumps over the lazy dog.’\n" +
"\n" +
"But few of us have expected to be able to use the phrase in real life ? that is, until these incredible photographs were taken.\n" +
"\n" +
"They show a fox leaping over a dog, but it’s not the ambush it might first appear. "), "%20");
            ArrayList<String> messages2 = mySender.splitMessageApart(("aoijewfjawihoijwaoigfhoiajwveoihahgjwoivhaoiveiaoiuewhgoiajewifhiuahepojaioihapojfoiwahgjnoiwhoiawjpwoihviojanwoihvoiwajnvnaoiwenvianvevnoiewhoipwanveoiwaihoinviawnoiahvpoineoibaoivoinawohfoiajvnaoievhoiajevoianoigeioewanvwoijgfoiwajeoifjewoijfoiwajoifejapoiehgoihewarogheoirhboierhb"), "%20");
            ArrayList<String> messages3 = mySender.splitMessageApart(("test this fully"), "%20");
            System.out.println("here");
        } catch (Exception exe) {}
    }

    public SmsSender() {
        
    }
    
    /**
     *
     * @param num -phone number of sms reciever Phone Number formats:
     * (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn
     * @param m - message in a string form
     * @throws SmsException
     */
    public SmsSender(String num, String m) throws SmsException {
        this.sendList = new LinkedList<String>();
        this.number = num;
        this.message = m;
        this.run();
    }

    /**
     *
     * @param nums-phone number of sms recievers Phone Number formats:
     * (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn
     * @param m- message in a string form
     * @throws SmsException
     */
    public SmsSender(String[] nums, String m) throws SmsException {
        this.phoneList = nums;
        this.message = m;
        this.run();
    }

    /**
     *
     * @param phoneNumber
     * @param msg
     * @return
     * @throws SmsException
     */
    public String sendMessage(String phoneNumber, String msg) throws SmsException {
        if (!isPhoneNumberValid(phoneNumber)) {
            String errorMsg = "Invalid phone number format " + phoneNumber + "\n" + "Please use one of the following: (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 ";
            System.out.println(phoneNumber);
            SmsException smsE = new SmsException();
            smsE.addErr(errorMsg, phoneNumber);
            throw smsE;

        } else {
            phoneNumber = phoneNumber.replaceAll("\\D+", "");
            String req = "";
            req += getServeraddress() + ":" + getPort() + "/sendmsg";
            String params = "user=" + getUser() + "&passwd=" + getPassword() + "&cat=" + category;

            params += "&to=" + phoneNumber;
            params += "&text=" + this.encodeMessage(msg);
            return HttpRequestHandler.sendGetRequest(req, params);
        }
    }

    /**
     *
     * @param phoneNumber
     * @param msg
     * @return
     * @throws SmsException
     */
    public String multiSendMessage(String phoneNumber, String msg) throws SmsException {
        if (!isPhoneNumberValid(phoneNumber)) {
            String errorMsg = "Invalid phone number format " + phoneNumber + "\n" + "Please use one of the following: (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 ";
            System.out.println(phoneNumber);
            SmsException smsE = new SmsException();
            smsE.addErr(errorMsg, phoneNumber);
            throw smsE;

        } else {
            phoneNumber = phoneNumber.replaceAll("\\D+", "");
            String req = "";
            req += getServeraddress() + ":" + getPort() + "/sendmsg";
            String params = "user=" + getUser() + "&passwd=" + getPassword() + "&cat=" + category;

            params += "&modem=" + modem;

            params += "&to=" + phoneNumber;
            params += "&text=" + this.encodeMessage(msg);
            return HttpRequestHandler.sendSmsGetRequest(params);
        }
    }

    /**
     *
     * @param phonelist
     * @param msg
     * @return
     * @throws SmsException
     */
    public String sendMessage(String[] phonelist, String msg) throws SmsException {
        SmsException sms = new SmsException();
        for (int i = 0; i < phonelist.length; i++) {
            if (!isPhoneNumberValid(phonelist[i])) {
                String errorMsg = "Invalid phone number format " + phonelist[i] + "\n" + "Please use one of the following: (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 ";
                sms.addErr(errorMsg, number);
                throw sms;
            }
            phonelist[i] = phonelist[i].replaceAll("\\D+", "");
        }
        String req = "";
        req += getServeraddress() + ":" + getPort() + "/sendmsg";
        String params = "user=" + getUser() + "&passwd=" + getPassword() + "&cat=" + category;

        params += "&to=\"" + phoneList[0] + "\"";
        for (int i = 1; i < phoneList.length; i++) {
            params += ",\"" + phoneList[i] + "\"";
        }
        params += "&text=" + encodeMessage(msg);
        return HttpRequestHandler.sendGetRequest(req, params);

    }

    /**
     *
     * @param phonelist
     * @param msg
     * @return
     * @throws SmsException
     */
    public String multiSendMessage(String[] phonelist, String msg) throws SmsException {
        SmsException sms = new SmsException();
        for (int i = 0; i < phonelist.length; i++) {
            if (!isPhoneNumberValid(phonelist[i])) {
                String errorMsg = "Invalid phone number format " + phonelist[i] + "\n" + "Please use one of the following: (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 ";
                sms.addErr(errorMsg, number);
                throw sms;
            }
            phonelist[i] = phonelist[i].replaceAll("\\D+", "");
        }

        String req = "";
        req += getServeraddress() + ":" + getPort() + "/sendmsg";
        String params = "user=" + getUser() + "&passwd=" + getPassword() + "&cat=" + category;

        params += "&modem=" + modem;

        params += "&to=\"" + phoneList[0] + "\"";
        for (int i = 1; i < phoneList.length; i++) {
            params += ",\"" + phoneList[i] + "\"";
        }
        params += "&text=" + encodeMessage(msg);
        return HttpRequestHandler.sendGetRequest(req, params);

    }

    /**
     * isPhoneNumberValid: Validate phone number using Java reg ex. This method
     * checks if the input string is a valid phone number.
     *
     * @param phoneNumber String of the number being sent
     * @return boolean: true if phone number is valid, false otherwise.
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        /* Phone Number formats: (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn
         ^\\(? : May start with an option "(" .
         (\\d{3}): Followed by 3 digits.
         \\)? : May have an optional ")"
         [- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
         (\\d{3}) : Followed by 3 digits.
         [- ]? : May have another optional "-" after numeric digits.
         (\\d{4})$ : ends with four digits.

         Examples: Matches following <a href="http://mylife.com">phone numbers</a>:
         (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890

         */
        //Initialize reg ex for phone number.   Sms
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private String encodeMessage(String m) throws SmsException {
        try {
            m.replaceAll("%20", " ");
            m = URLEncoder.encode(m, "UTF-8");
            String temp = new String();

            for (int i = 0; i < m.length(); i++) {
                if (m.charAt(i) == '+') {
                    temp += "%20";
                } else {
                    temp += m.charAt(i);
                }
            }
            return temp;
        } catch (Exception e) {
            SmsException sms = new SmsException();
            sms.addErr("failed encoding message", this.number);
            throw sms;

        }
    }

    public ArrayList<String> splitMessageApart(String input, String delim) {
        ArrayList<String> messages = new ArrayList<String>();
        String[] messageSplit = input.split(delim);

        StringBuilder currMessage = new StringBuilder();
        for (int m = 0; m < messageSplit.length; m++) {
            String nextToken = messageSplit[m];
            if (currMessage.length() + nextToken.length() + delim.length() < maxMsgSize) {
                currMessage.append(nextToken).append(delim);
            } else if (currMessage.length() == 0 && nextToken.length() + delim.length()  >= maxMsgSize) {
                currMessage.append(nextToken.substring(0, maxMsgSize - 1));
            } else {
                messages.add(currMessage.toString());
                currMessage = new StringBuilder();
                currMessage.append(nextToken).append(delim);
            }
        }
        if (currMessage.length() > 0) {
            messages.add(currMessage.toString());
        }
        return messages;
    }

    /**
     *
     * @throws SmsException
     */
    public void run() throws SmsException {
        String encodedMessage = message;
        //checks if message after encoding surpasses max size
        if (encodedMessage.length() < maxMsgSize) {
            if (phoneList == null) {
                result = this.sendMessage(this.number, encodedMessage);
            } else {
                result = this.sendMessage(this.phoneList, encodedMessage);
            }
        } else {
            ArrayList<String> messages = splitMessageApart(encodedMessage, " ");
            String temp = new String();
            if (this.phoneList == null) {
                for (int i = 0; i < messages.size(); i++) {
                    String m = "(" + (i + 1) + "/" + messages.size() + ") " + messages.get(i);
                    temp += this.multiSendMessage(number, m) + ",";
                }
            } else {
                for (int i = 0; i < messages.size(); i++) {
                    String m = "(" + (i + 1) + "/" + messages.size() + ") " + messages.get(i);
                    temp += this.multiSendMessage(phoneList, m) + ",";
                }
            }
            result = temp;
        }
    }

    /**
     *
     * @throws SmsException
     */
    public void confirmation() throws SmsException {
        String[] sentConf = this.result.split(",");
        SmsException exception = new SmsException();
        for (int i = 0; i < sentConf.length; i++) {
            if (sentConf[i].startsWith("ID")) {
                //do nothing no exception made
            } else {
                exception.addErr(sentConf[i], this.number);
            }

        }
        if (exception.hasError()) {
            throw exception;
        }

    }

    /**
     *
     * @param code
     * @return
     */
    public String errorCode(String code) {
        if (SmsSender.errCodes.isEmpty()) {
            fillCode();
        }

        return SmsSender.errCodes.get(code);
    }

    private static void fillCode() {
        SmsSender.errCodes.clear();
        errCodes.put("Err: 601", "Authentication Failed");
        errCodes.put("Err: 602", "Parse Error");
        errCodes.put("Err: 603", "Invalid Category");
        errCodes.put("Err: 604", "SMS message size is greater then 160 chars encoded");
        errCodes.put("Err: 605", "Recipent Overflow");
        errCodes.put("Err: 606", "Invalid Recipient");
        errCodes.put("Err: 607", "No Recipient");
        errCodes.put("Err: 608", "MultiModem iSMS is busy, can’t accept this request");
        errCodes.put("Err: 609", "Timeout waiting for a TCP API request");
        errCodes.put("Err: 610", "Unkown Action Trigger");
        errCodes.put("Err: 611", "Error in broadcast trigger");
        errCodes.put("Err: 612", "System Error-Memory Allocation Failure");
        errCodes.put("Err: 613", "Invalid Modem Index In SF 400, if modem index given is not within 0 to "
                + "4In SF 800, if modem index given is not within 0 to 8");
        errCodes.put("Err: 614", "Invalid device model number "
                + "(model number is not detected properly to validate the modem index)");
    }
}
