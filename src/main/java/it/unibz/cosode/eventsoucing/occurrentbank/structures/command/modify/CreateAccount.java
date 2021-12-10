package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.NewAccountBankEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command representation for the create account.
 *
 * @author ajurco
 */
public class CreateAccount extends BankCommand {

    private final String accountName;

    /**
     * The constructor.
     *
     * @param customerId  ID of the customer for which an account is to be created
     * @param timestamp   the timestamp of the event
     * @param accountName the name of the account to be created
     */
    public CreateAccount(String customerId, LocalDateTime timestamp, String accountName) {
        super(customerId, timestamp, false);
        this.accountName = accountName;
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            List<BankEvent> newEvent = new ArrayList<>();
            // we do not check if the account with such name already exists
            newEvent.add(new NewAccountBankEvent(UUID.randomUUID(), getTimestamp(), getCustomerId(), accountName));
            return newEvent.stream();
        };
    }
}
