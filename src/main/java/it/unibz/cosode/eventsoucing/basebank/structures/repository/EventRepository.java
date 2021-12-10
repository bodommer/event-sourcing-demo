package it.unibz.cosode.eventsoucing.basebank.structures.repository;

import it.unibz.cosode.eventsoucing.basebank.structures.event.BankEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple repository implementation for storing all events of the bank.
 *
 * @author ajurco
 */
public class EventRepository implements IEventRepository {

    private final Map<String, List<BankEvent>> events = new HashMap<>();

    @Override
    public void addEvent(BankEvent event) {
        events.computeIfAbsent(event.getCustomerName(), s -> new ArrayList<>()).add(event);
    }

    @Override
    public List<BankEvent> getEvents(String customerName) {
        return events.getOrDefault(customerName, new ArrayList<>());
    }

    @Override
    public int countEvents() {
        int count = 0;
        for (Map.Entry<String, List<BankEvent>> entry : events.entrySet()) {
            count = count + entry.getValue().size();
        }
        return count;
    }

}
