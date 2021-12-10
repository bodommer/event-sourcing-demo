package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.DepositBankEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command representation for account deposit.
 *
 * @author ajurco
 */
public class DepositMoney extends BankCommand {

    private final String accountName;
    private final int amount;

    /**
     * The constructor.
     *
     * @param customerId  ID of the customer for which a deposit is commanded
     * @param timestamp   the timestamp of the event
     * @param accountName the name of the account to which the money shall be deposited
     */
    public DepositMoney(String customerId, LocalDateTime timestamp, String accountName, int amount) {
        super(customerId, timestamp, true);
        this.accountName = accountName;
        this.amount = amount;
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            Customer customer = recreateCustomer(getCustomerId(), events);

            if (customer.getAccount(accountName) == null) {
                System.err.printf("Invalid account name %s for customer %s. A deposit money operation was denied.%n",
                        accountName, getCustomerId());
                // do nothing
                return Stream.empty();
            }

            List<BankEvent> newEvent = new ArrayList<>();
            newEvent.add(new DepositBankEvent(UUID.randomUUID(), getTimestamp(), getCustomerId(), accountName, amount));
            return newEvent.stream();
        };
    }
}
