/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.SocketConnection;

import java.util.ArrayList;
import java.util.Hashtable;
import rmischeduleserver.GUI.ServerGUI;
import rmischeduleserver.data_connection_types.ClientConnection;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.SocketClients;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.sqldriver;

/**
 *
 * @author user
 */
public class LocalServer extends ConnectionBackToServer {

    private static IntervalUpdater updater;
    
    public LocalServer(ServerGUI myGui, sqldriver myD) {
        super(myGui, myD);
        if (updater == null) {
            updater = new IntervalUpdater();
            updater.start();
        }
    }

    public void sendCommand(GeneralQueryFormat gqf, String time) {
        ClientConnection currClient = getLocalConnection();
        if (currClient != null) {
            SocketCommandStructure myCommand = new SocketCommandStructure(gqf);
            Hashtable<String, GeneralQueryFormat> queries = currClient.getMyHashOfHeartbeatQueries();
            GeneralQueryFormat myQuery = null;
            if (myCommand.Command == GeneralQueryFormat.UPDATE_SCHEDULE) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.SCHEDULE_QUERY, myCommand.Branch, myCommand.Company);
            } else if (myCommand.Command == GeneralQueryFormat.UPDATE_CHECK_IN) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.CHECKIN_QUERY, "-1", myCommand.Company);
                myQuery.setCompany(myCommand.Company);
            } else if (myCommand.Command == GeneralQueryFormat.UPDATE_AVAILABILITY) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.AVAILABILITY_QUERY, myCommand.Branch, myCommand.Company);
            } else if (myCommand.Command == GeneralQueryFormat.UPDATE_BANNED) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.BANNED_QUERY, myCommand.Branch, myCommand.Company);
            } else if (myCommand.Command == GeneralQueryFormat.UPDATE_CLIENT_CERT) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.CERTIFICATION_QUERY, myCommand.Branch, myCommand.Company);
            } else if (myCommand.Command == GeneralQueryFormat.UPDATE_EMPLOYEE_CERT) {
                myQuery = currClient.getHeartbeatQuery(SocketClients.EMPLOYEE_CERT_QUERY, myCommand.Branch, myCommand.Company);
            }
            if (myQuery != null) {
                try {
                    this.forceUpdate(myQuery, myCommand, currClient);
                } catch (Exception e) {}
            }
        }
    }

    public void addHearbeatItem(GeneralQueryFormat gqf) {
        getConnectedClients().addHeartbeatQuery(gqf);
    }

    public void sendMessage(String message, String userId) {
    }

    public void sendCommandToClients(SocketCommandStructure myCommand) {
        
    }

    public void forceUpdate(GeneralQueryFormat query, SocketCommandStructure myCommand, ClientConnection currClient) {
        try {
            String time = getSQLDriver().saveTime(query);
            if (query.getLastUpdated() == null || query.getLastUpdated().length() == 0) {
                query.setLastUpdated(time);
            }
            try {
                ArrayList<Record_Set> results = new ArrayList<Record_Set>();
                if (query instanceof RunQueriesEx) {
                    results = getSQLDriver().executeQueryEx((RunQueriesEx)query);
                } else {
                    results.add(getSQLDriver().executeQuery(query));
                }
                int sizeOfResult = 0;
                for (int r = 0; r < results.size(); r++) {
                    sizeOfResult += results.get(r).length();
                }
                if (sizeOfResult > 0) {
                    myCommand.setRecordSet(results);
                    if (query.isEmployeeCertQuery()) {
                        myCommand.Command = GeneralQueryFormat.UPDATE_EMPLOYEE_CERT;
                    } else if (query.isAvailabilityQuery()) {
                        myCommand.Command = GeneralQueryFormat.UPDATE_AVAILABILITY;
                    } else if (query.isBannedQuery()) {
                        myCommand.Command = GeneralQueryFormat.UPDATE_BANNED;
                    } else if (query.isCheckInQuery()) {
                        myCommand.Command = GeneralQueryFormat.UPDATE_CHECK_IN;
                    } else if (query.isScheduleQuery()) {
                        myCommand.Command = GeneralQueryFormat.UPDATE_SCHEDULE;
                    }

                    currClient.sendDataToThisClient(myCommand);
                    query.setLastUpdated(time);
                }

            } catch (Exception e) {
                query.setLastUpdated(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClientConnection getLocalConnection() {
        ClientConnection retVal = null;
        if (getConnectedClients().size() > 0) {
            retVal = getConnectedClients().get(0);
        }
        return retVal;
    }

    private class IntervalUpdater extends Thread {

        public IntervalUpdater() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(5000);
                } catch (Exception e) {
                }
                ClientConnection currClient = getLocalConnection();
                if (currClient != null) {
                    Hashtable<String, GeneralQueryFormat> queries = currClient.getMyHashOfHeartbeatQueries();
                    Object[] keys = queries.keySet().toArray();
                    for (int k = 0; k < keys.length; k++) {
                        try {
                            String key = keys[k].toString();
                            GeneralQueryFormat query = queries.get(key);

                            SocketCommandStructure myCommand = new SocketCommandStructure(query);

                            if (query != null) {
                                forceUpdate(query, myCommand, currClient);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
