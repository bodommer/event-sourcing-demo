package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command (query) representation for retrieving all customer's events.
 *
 * @author ajurco
 */
public class GetEvents extends BankCommand {

    private static final String USER_EVENTS = "Events of user %s:";
    private static final String EMPTY_EVENTS = "User %s has no recorded events.%n";

    public GetEvents(String customerId, LocalDateTime timestamp) {
        super(customerId, timestamp, true);
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            StringBuilder builder = new StringBuilder(String.format(USER_EVENTS, getCustomerId()));
            events.forEach(event -> builder.append("\n").append("  ").append(event));
            String output = builder.toString();
            System.out.println(output.equals(USER_EVENTS) ? String.format(EMPTY_EVENTS, customerId) : output);
            return Stream.empty();
        };
    }
}
