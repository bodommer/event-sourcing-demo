package it.unibz.cosode.eventsoucing.occurrentbank.structures.event;

import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class representing a single opening of an account event.
 *
 * @author ajurco
 */
public class NewAccountBankEvent extends BankEvent {

    private final String accountName;

    public NewAccountBankEvent(UUID id, LocalDateTime timestamp, String customerName, String accountName) {
        super(id, timestamp, customerName);
        this.accountName = accountName;
    }

    @Override
    public void performOperation(Customer customer) {
        customer.openAccount(accountName);
    }

    @Override
    public CloudEvent toEvent() {
        Map<String, Object> map = new HashMap<>();
        map.put("account", accountName);
        return  toEvent("new-account", getBytesFromMap(map));
    }

    @Override
    public String toString() {
        return String.format("Open account %s", accountName);
    }
}
