package it.unibz.cosode.eventsoucing.basebank.structures.repository;

import it.unibz.cosode.eventsoucing.basebank.structures.event.BankEvent;

import java.util.List;

/**
 * The definition interface for all EventRepository implementations.
 *
 * @author ajurco
 */
public interface IEventRepository {

    /**
     * Adds a new customer to the map of events in the repository. If there is no entry for the customer, it creates
     * a new entry and adds the event.
     *
     * @param event the event to be added into the repository for the customer
     */
    void addEvent(BankEvent event);

    /**
     * @param customerName the name of the customer whose events we want to retrieve
     * @return a list of all customer's events, an empty list if the customer has no entries in the map
     */
    List<BankEvent> getEvents(String customerName);

    /**
     * @return the amount of all events in the repository
     */
    int countEvents();

}
