/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import com.sun.mail.imap.IMAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import microsoft.exchange.webservices.data.Appointment;
import microsoft.exchange.webservices.data.CalendarFolder;
import microsoft.exchange.webservices.data.CalendarView;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import schedfoxlib.controller.OutlookControllerInterface;
import schedfoxlib.model.MessagingCommunication;
import schedfoxlib.model.SalesItinerary;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class OutlookController implements OutlookControllerInterface {

    private String companyId;
    private static String url = "https://mail.champ.net/ews/Exchange.asmx";

    private OutlookController(String companyId) {
        this.companyId = companyId;
    }

    public static OutlookController getInstance(String companyId) {
        return new OutlookController(companyId);
    }

    public static void main(String[] args) {
        OutlookController.getInstance("2").downloadAllCalendars();
    }

    public void downloadAllCalendars() {
        try {
            UserController userService = new UserController(companyId);
            ArrayList<User> usersWithPass = userService.getUsersWithEmailPassword();
            for (int u = 0; u < usersWithPass.size(); u++) {
                downloadCalendar(usersWithPass.get(u).getId());
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public void detectBouncedEmails() {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imap");
            
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imap");
            store
                    .connect("cpanel2.champ.net", "bounced@schedfox.com",
                            "bounced01");

            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages(1, inbox.getMessageCount());
            HashMap<String, String> messageFailures = new HashMap<String, String>();
            for (int m = 0; m < messages.length; m++) {
                IMAPMessage message = (IMAPMessage)messages[m];
                InputStream iStream = message.getRawInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int byteRead = -1;
                while ((byteRead = iStream.read(buffer)) != -1) {
                    output.write(buffer, 0, byteRead);
                }
                output.flush();;
                byte[] data = output.toByteArray();
                String strData = new String(data);
                
                Pattern pattern = Pattern.compile("To: (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

                Matcher matcher = pattern.matcher(strData);
                while (matcher.find()) {
                    String email = matcher.group();
                    if (!email.contains("@schedfoxws.uniquesec.champ.net")) {
                        email = email.replaceAll("To: ", "");
                        if (email.startsWith("<")) {
                            email = email.replaceAll("<", "").replaceAll(">", "");
                        }
                        messageFailures.put(email, "");
                    }
                }
                message.setFlag(Flags.Flag.SEEN, true);
                message.setFlag(Flags.Flag.DELETED, true);
            }
            Iterator<String> keys = messageFailures.keySet().iterator();
            MessagingController messagingController = new MessagingController("2");
            while (keys.hasNext()) {
                String email = keys.next();
                MessagingCommunication messaging = messagingController.getMessageCommunication(email);
                if (messaging.getMessagingCommunicationId() != null) {
                    messaging.setDatetimesent(null);
                    messaging.setErrorNum(messaging.getErrorNum() == null ? 1 : messaging.getErrorNum() + 1);
                    messagingController.saveMessagingCommunication(messaging);
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public boolean attemptEmailLogin(Integer userId) {
        try {
            User user = new UserController(companyId).getUserById(userId);
            if (user.getEmailPassword() == null || user.getEmailPassword().length() == 0) {
                return false;
            }

            String email = user.getEmailAddress();
            if (email.contains("@")) {
                email = email.substring(0, email.indexOf("@"));
            }

            ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
            ExchangeCredentials credentials = new WebCredentials(email, user.getEmailPassword());
            service.setCredentials(credentials);
            service.setUrl(new URI(url));
            service.setPreAuthenticate(true);
            CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
            return true;
        } catch (Exception exe) {
            return false;
        }
    }

    public void downloadCalendar(Integer userId) {
        try {
            SalesController salesService = SalesController.getInstance(companyId);
            UserController userService = new UserController(companyId);
            User user = userService.getUserById(userId);
            String email = user.getEmailAddress();
            if (email.contains("@")) {
                email = email.substring(0, email.indexOf("@"));
            }
            ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
            ExchangeCredentials credentials = new WebCredentials(email, user.getEmailPassword());
            service.setCredentials(credentials);
            service.setUrl(new URI(url));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            startCal.add(Calendar.MONTH, -2);
            endCal.add(Calendar.MONTH, 2);

            CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
            FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startCal.getTime(), endCal.getTime()));

            service.loadPropertiesForItems(findResults, PropertySet.FirstClassProperties);
            ArrayList<String> apptIds = new ArrayList<String>();
            for (int a = 0; a < findResults.getTotalCount(); a++) {
                Appointment appt = findResults.getItems().get(a);

                apptIds.add(appt.getId().toString());

                SalesItinerary itinerary = new SalesItinerary();
                itinerary = salesService.getSalesItineraryByExternalGid(appt.getId().toString(), userId);
                itinerary.setExternalGid(appt.getId().toString());
                itinerary.setSubject(appt.getSubject());
                itinerary.setDateOfItinerary(appt.getStart());
                itinerary.setStimeOfItinerary(Integer.parseInt(timeFormat.format(appt.getStart())));
                itinerary.setEtimeOfItinerary(Integer.parseInt(timeFormat.format(appt.getEnd())));
                itinerary.setSalesItineraryTypeId(3);
                itinerary.setUserId(user.getId());
                itinerary.setActive(true);
                itinerary.setMeetingText(appt.getBody().toString());

                try {
                    salesService.saveSalesItinerary(itinerary);
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
            salesService.deactivateAppointments(user, apptIds);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public boolean checkEmailPassword(String email, String password) {
        try {
            if (email.contains("@")) {
                email = email.substring(0, email.indexOf("@"));
            }
            ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
            ExchangeCredentials credentials = new WebCredentials(email, password);
            service.setCredentials(credentials);
            service.setUrl(new URI(url));
            service.setTraceEnabled(true);

            CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
            return true;
        } catch (Exception exe) {
            exe.printStackTrace();
            return false;
        }
    }
}
