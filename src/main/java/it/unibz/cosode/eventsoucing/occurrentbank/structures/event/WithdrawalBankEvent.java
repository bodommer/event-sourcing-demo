package it.unibz.cosode.eventsoucing.occurrentbank.structures.event;

import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class representing a single money withdrawal event.
 *
 * @author ajurco
 */
public class WithdrawalBankEvent extends BankEvent {

    private final String accountName;
    private final int value;

    public WithdrawalBankEvent(UUID id, LocalDateTime timestamp, String customerName, String accountName, int value) {
        super(id, timestamp, customerName);
        this.accountName = accountName;
        this.value = value;
    }

    @Override
    public void performOperation(Customer customer) {
        customer.getAccount(accountName).withdraw(value);
    }

    @Override
    public CloudEvent toEvent() {
        Map<String, Object> map = new HashMap<>();
        map.put("account", accountName);
        map.put("value", value);
        return  toEvent("withdraw", getBytesFromMap(map));
    }

    @Override
    public String toString() {
        return String.format("Withdraw %d from account %s", value, accountName);
    }
}
