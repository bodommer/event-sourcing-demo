package it.unibz.cosode.eventsoucing.occurrentbank;

import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.Bank;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.convertor.CommandConvertor;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.convertor.EventConvertor;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;
import org.occurrent.eventstore.api.blocking.EventStore;
import org.occurrent.eventstore.api.blocking.EventStream;
import org.occurrent.eventstore.inmemory.InMemoryEventStore;

import java.util.stream.Stream;

/**
 * The bank implementation using the occurrent library.
 *
 * @author ajurco
 */
public class OccurrentBank implements Bank {

    /**
     * The event store instance for storing all {@link CloudEvent} instances
     */
    private final EventStore eventStore;

    public OccurrentBank() {
        this.eventStore = new InMemoryEventStore();
    }

    @Override
    public boolean serveClients(String command) {

        // get all stored commands
        BankCommand bankCommand = CommandConvertor.convert(command);

        if (bankCommand == null) {
            // this will be logged as invalid input
            return false;
        }

        // get customer events
        EventStream<CloudEvent> events = eventStore.read(bankCommand.getCustomerId());

        // check if the customer is present and if it even needed (not needed for new account)
        if (events.version() == 0 && bankCommand.requiresUser()) {
            System.err.printf("Unable to perform a %s for customer %s - this customer does not exist!%n",
                    bankCommand.toString(), bankCommand.getCustomerId());
            // logged here, false would trigger Client logging (duplicate)
            return true;
        }

        // convert to BankEvents
        Stream<BankEvent> bankEvents = events.events().map(EventConvertor::convert);

        // use command function
        Stream<BankEvent> newBankEvents = bankCommand.getCommandFunction().apply(bankEvents);

        // serialize and write
        eventStore.write(bankCommand.getCustomerId(), newBankEvents.map(BankEvent::toEvent));

        // always returns true - invalid input processed and/or logged in command function
        return true;
    }

    @Override
    public int getEventCount() {
        return (int) ((InMemoryEventStore) eventStore).count();
    }
}
