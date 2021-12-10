package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Account;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.CloseAccountBankEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.DepositBankEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command representation for the close account.
 *
 * @author ajurco
 */
public class CloseAccount extends BankCommand {

    private final String accountName;
    private final String transferredAccount;

    /**
     * The constructor.
     *
     * @param customerId         ID of the customer for which an account is to be closed
     * @param timestamp          the timestamp of the event
     * @param accountName        the name of the account to be closed
     * @param transferredAccount the account to which the money are to be transferred or an empty string
     */
    public CloseAccount(String customerId, LocalDateTime timestamp, String accountName, String transferredAccount) {
        super(customerId, timestamp, true);
        this.accountName = accountName;
        this.transferredAccount = transferredAccount;
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            Customer customer = recreateCustomer(getCustomerId(), events);

            if (customer.getAccount(accountName) == null) {
                System.err.printf("Invalid account name %s for customer %s. A close account operation was denied.%n",
                        accountName, getCustomerId());
                // do nothing
                return Stream.empty();
            }

            List<BankEvent> newEvents = new ArrayList<>();
            Account targetAccount = customer.getAccount(transferredAccount);
            if (targetAccount == null) {
                String secondSentence = transferredAccount.strip().isEmpty() ? "No transferred account provided." :
                        String.format("Invalid transferred account name %s for customer %s.", transferredAccount,
                                getCustomerId());
                System.err.printf("%s Balance from closing account %s will be lost.%n", secondSentence, accountName);
            } else {
                newEvents.add(new DepositBankEvent(UUID.randomUUID(), getTimestamp(), getCustomerId(),
                        transferredAccount, customer.getAccount(accountName).getBalance()));
            }

            newEvents.add(new CloseAccountBankEvent(UUID.randomUUID(), getTimestamp(), getCustomerId(), accountName));
            return newEvents.stream();
        };
    }
}
