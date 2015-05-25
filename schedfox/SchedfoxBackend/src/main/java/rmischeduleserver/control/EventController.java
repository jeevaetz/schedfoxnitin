/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.event.get_all_event_followup_types_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_event_followup_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_event_followup_types_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_event_type_groups_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_event_types_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_events_for_user_group_followup_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_events_with_followup_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_events_with_no_followup_query;
import rmischeduleserver.mysqlconnectivity.queries.event.get_non_closed_event_followup_for_user_query;
import rmischeduleserver.mysqlconnectivity.queries.event.save_event_followup_query;
import rmischeduleserver.mysqlconnectivity.queries.event.save_event_query;
import rmischeduleserver.mysqlconnectivity.queries.event.save_groups_to_event_type_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventFollowType;
import schedfoxlib.model.EventFollowup;
import schedfoxlib.model.EventType;
import schedfoxlib.model.Group;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EventController {
    
    private String companyId;
    
    public EventController() {
    }

    public EventController(String companyId) {
        this.companyId = companyId;
    }

    public static EventController getInstance(String companyId) {
        return new EventController(companyId);
    }
    
    public ArrayList<Group> getGroupsForEventType(Integer eventType) throws RetrieveDataException {
        ArrayList<Group> retVal = new ArrayList<Group>();
        try {
            get_event_type_groups_query getQuery = new get_event_type_groups_query();
            getQuery.setPreparedStatement(new Object[]{eventType});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new Group(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public void saveGroupsToEventType(Integer eventType, ArrayList<Integer> groupIds) throws SaveDataException {
        try {
            save_groups_to_event_type_query saveGroupQuery = new save_groups_to_event_type_query();
            saveGroupQuery.update(eventType, groupIds);
            saveGroupQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            conn.executeUpdate(saveGroupQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    public ArrayList<EventFollowup> getEventFollowup(Integer eventId) throws RetrieveDataException {
        ArrayList<EventFollowup> retVal = new ArrayList<EventFollowup>();
        try {
            get_event_followup_query getQuery = new get_event_followup_query();
            getQuery.setPreparedStatement(new Object[]{eventId});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new EventFollowup(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<EventFollowType> getEventFollowUpTypes() throws RetrieveDataException {
        ArrayList<EventFollowType> retVal = new ArrayList<EventFollowType>();
        try {
            get_all_event_followup_types_query getQuery = new get_all_event_followup_types_query();
            getQuery.setPreparedStatement(new Object[]{});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new EventFollowType(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<EventFollowType> getEventFollowUpTypes(Integer eventFollowGroupId) throws RetrieveDataException {
        ArrayList<EventFollowType> retVal = new ArrayList<EventFollowType>();
        try {
            get_event_followup_types_query getQuery = new get_event_followup_types_query();
            getQuery.setPreparedStatement(new Object[]{eventFollowGroupId});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new EventFollowType(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<EventType> getEventTypes(Integer eventGroupId) throws RetrieveDataException {
        ArrayList<EventType> retVal = new ArrayList<EventType>();
        try {
            get_event_types_query getQuery = new get_event_types_query();
            getQuery.setPreparedStatement(new Object[]{eventGroupId});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new EventType(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<Event> getEventsWithRequestedFollowupForUserOrGroup(Integer userId, Date startDate, Date endDate, ArrayList<Integer> eventTypes) throws RetrieveDataException {
        ArrayList<Event> retVal = new ArrayList<Event>();
        try {
            get_events_for_user_group_followup_query getQuery = new get_events_for_user_group_followup_query();
            getQuery.update(userId, startDate, endDate, eventTypes);
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new Event(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public Event getEventById(Integer eventId) throws RetrieveDataException {
        Event retVal = new Event();
        try {
            get_events_with_followup_query getQuery = new get_events_with_followup_query();
            getQuery.setPreparedStatement(new Object[]{});
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                //retVal.add(new Event(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        throw new RetrieveDataException();
        //return retVal;
    }
    
    public ArrayList<Event> getEventsWithFollowup(Date startDate, Date endDate, ArrayList<Integer> eventTypes) throws RetrieveDataException {
        ArrayList<Event> retVal = new ArrayList<Event>();
        try {
            get_events_with_followup_query getQuery = new get_events_with_followup_query();
            getQuery.update(startDate, endDate, eventTypes);
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new Event(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<Event> getEventsWithNoFollowup(ArrayList<Integer> branches, Boolean showEventsWithoutFollowupOnly, Date startDate, Date endDate, ArrayList<Integer> eventTypeIds) throws RetrieveDataException {
        ArrayList<Event> retVal = new ArrayList<Event>();
        try {
            get_events_with_no_followup_query getQuery = new get_events_with_no_followup_query();
            getQuery.setPreparedStatement(new Object[]{});
            getQuery.update(branches, showEventsWithoutFollowupOnly, startDate, endDate, eventTypeIds);
            getQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(getQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new Event(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public EventFollowup getNonClosedFollowupsForUser(Integer eventId, Integer userId) throws RetrieveDataException {
        EventFollowup retVal = null;
        try {
            get_non_closed_event_followup_for_user_query grabEventsQuery = new get_non_closed_event_followup_for_user_query();
            grabEventsQuery.setPreparedStatement(eventId, userId, userId);
            grabEventsQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(grabEventsQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal = new EventFollowup(rs);
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public Integer saveEventFollowup(EventFollowup eventFollowup) throws SaveDataException {
        try {
            save_event_followup_query schemaQuery = new save_event_followup_query();
            schemaQuery.update(eventFollowup);
            schemaQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(schemaQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                eventFollowup.setEventId(rs.getInt("myid"));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new SaveDataException();
        }
        return eventFollowup.getEventFollowupId();
    }
    
    public Integer saveEvent(Event event) throws SaveDataException {
        try {
            save_event_query schemaQuery = new save_event_query();
            schemaQuery.update(event);
            schemaQuery.setCompany(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(schemaQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                event.setEventId(rs.getInt("myid"));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new SaveDataException();
        }
        return event.getEventId();
    }
    
}
