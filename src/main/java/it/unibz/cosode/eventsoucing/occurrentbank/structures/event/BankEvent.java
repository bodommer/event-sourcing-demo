package it.unibz.cosode.eventsoucing.occurrentbank.structures.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.convertor.EventConvertor;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

/**
 * A common parent class of all events of the bank.
 *
 * @author ajurco
 */
public abstract class BankEvent {

    protected static final URI SOURCE = URI.create("urn:cosode-bank");
    protected static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    private final UUID id;
    private final LocalDateTime timestamp;

    /**
     * The name of the customer to which is the even related.
     */
    protected final String customerName;

    /**
     * The constructor.
     *
     * @param customerName the name of the customer associated to this instance
     */
    protected BankEvent(UUID id, LocalDateTime timestamp, String customerName) {
        this.id = id;
        this.customerName = customerName;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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

    /**
     * @return itself represented as a CloudEvent
     */
    public abstract CloudEvent toEvent();

    /**
     * @return itself represented as a CloudEvent
     */
    protected CloudEvent toEvent(String type, byte[] bytes) {
        return CloudEventBuilder.v1()
                .withId(getId().toString())
                .withSource(SOURCE)
                .withType(type)
                .withTime(LocalDateTime.now().atOffset(ZONE_OFFSET))
                .withSubject(getCustomerName())
                .withDataContentType("application/json")
                .withData(bytes)
                .build();
    }

    /**
     * @param map map to be transformed to a byte array
     * @return the byte array representing the map
     */
    protected byte[] getBytesFromMap(Map<String, Object> map) {
        try {
            return EventConvertor.getMapper().writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            System.err.println("Unable to serialize " + this.toString());
            return new byte[]{};
        }
    }

}
