package it.unibz.cosode.eventsoucing.occurrentbank.structures.event;

import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class representing a single close account event.
 *
 * @author ajurco
 */
public class CloseAccountBankEvent extends BankEvent {

    private final String accountName;

    /**
     * The constructor.
     *
     * @param customerName       the name of the customer performing the action
     * @param accountName        the account to be closed
     */
    public CloseAccountBankEvent(UUID id, LocalDateTime timestamp, String customerName, String accountName) {
        super(id, timestamp, customerName);
        this.accountName = accountName;
    }

    @Override
    public void performOperation(Customer customer) {
        customer.closeAccount(accountName);
    }

    @Override
    public CloudEvent toEvent() {
        Map<String, Object> map = new HashMap<>();
        map.put("account", accountName);
        return  toEvent("close-account", getBytesFromMap(map));
    }

    @Override
    public String toString() {
        return String.format("Close account %s", accountName);
    }
}
