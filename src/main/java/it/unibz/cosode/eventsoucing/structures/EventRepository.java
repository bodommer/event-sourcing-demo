package it.unibz.cosode.eventsoucing.structures;

import it.unibz.cosode.eventsoucing.structures.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple repository for storing all events of the bank.
 *
 * @author ajurco
 */
public class EventRepository {

    private final Map<String, List<Event>> events = new HashMap<>();

    /**
     * Adds a new customer to the map of events in the repository. If there is no entry for the customer, it creates
     * a new entry and adds the event.
     *
     * @param event the event to be added into the repository for the customer
     */
    public void addEvent(Event event) {
        events.computeIfAbsent(event.getCustomerName(), s -> new ArrayList<>()).add(event);
    }

    /**
     * @param customerName the name of the customer whose events we want to retrieve
     * @return a list of all customer's events, an empty list if the customer has no entries in the map
     */
    public List<Event> getEvents(String customerName) {
        return events.getOrDefault(customerName, new ArrayList<>());
    }

}
