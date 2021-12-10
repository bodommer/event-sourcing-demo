package it.unibz.cosode.eventsoucing.occurrentbank.structures.command;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A common parent class of all commands of the bank.
 *
 * @author ajurco
 */
public abstract class BankCommand {

    protected final LocalDateTime timestamp;

    protected final UUID commandId = UUID.randomUUID();

    protected final String customerId;

    /**
     * A flag signalizing whether a successful load of the user is required or not.
     */
    protected final boolean requiresUser;

    protected BankCommand(String customerId, LocalDateTime timestamp, boolean requiresUser) {
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.requiresUser = requiresUser;
    }

    public UUID getCommandId() {
        return commandId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean requiresUser() {
        return requiresUser;
    }

    /**
     * @return the function that performs the command - contains all the logic, checks constraints and generates events
     */
    public abstract Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction();

    /**
     * Recreates a customer by applying all events on a new instance in chronological order.
     *
     * @param customerId the ID of the customer to be recreated
     * @param events     events to be applied
     * @return a recreated user
     */
    protected static Customer recreateCustomer(String customerId, Stream<BankEvent> events) {
        Customer customer = new Customer(customerId);
        events.forEach(a -> a.performOperation(customer));
        return customer;
    }

}
