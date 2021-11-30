package it.unibz.cosode.eventsoucing.structures.event;

import it.unibz.cosode.eventsoucing.structures.Customer;

import java.util.Date;
import java.util.UUID;

/**
 * A common parent class of all events of the bank.
 *
 * @author ajurco
 */
public abstract class Event {

    private final UUID id = UUID.randomUUID();
    private final Date created = new Date();

    /**
     * The name of the customer to which is the even related.
     */
    protected final String customerName;

    /**
     * The constructor.
     *
     * @param customerName the name of the customer associated to this instance
     */
    protected Event(String customerName) {
        this.customerName = customerName;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getCustomerName() {
        return customerName;
    }

    /**
     * A common method for all extending classes. This method shall be overridden to perform the core of the event
     * functionality on the Customer instance.
     *
     * @param customer the customer instance on which the event shall be executed
     */
    public abstract void performOperation(Customer customer);

}
