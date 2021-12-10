package it.unibz.cosode.eventsoucing.occurrentbank.structures.event;

import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class representing a single money deposit event.
 *
 * @author ajurco
 */
public class DepositBankEvent extends BankEvent {

    private final String accountName;
    private final int value;

    public DepositBankEvent(UUID id, LocalDateTime timestamp, String customerName, String accountName, int value) {
        super(id, timestamp, customerName);
        this.accountName = accountName;
        this.value = value;
    }

    @Override
    public void performOperation(Customer customer) {
        customer.getAccount(accountName).deposit(value);
    }

    @Override
    public CloudEvent toEvent() {
        Map<String, Object> map = new HashMap<>();
        map.put("account", accountName);
        map.put("value", value);
        return  toEvent("deposit", getBytesFromMap(map));
    }

    @Override
    public String toString() {
        return String.format("Deposit %d to account %s", value, accountName);
    }
}
