package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Account;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.WithdrawalBankEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command representation for account withdrawal.
 *
 * @author ajurco
 */
public class WithdrawMoney extends BankCommand {

    private final String accountName;
    private final int value;

    /**
     * The constructor.
     *
     * @param customerId  ID of the customer for which a withdrawal is commanded
     * @param timestamp   the timestamp of the event
     * @param accountName the name of the account from which the money shall be withdrawn
     */
    public WithdrawMoney(String customerId, LocalDateTime timestamp, String accountName, int value) {
        super(customerId, timestamp, true);
        this.accountName = accountName;
        this.value = value;
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            Customer customer = recreateCustomer(getCustomerId(), events);

            Account account = customer.getAccount(accountName);
            if (account == null) {
                System.err.printf("Invalid account name %s for customer %s. A withdrawal operation was denied.%n",
                        accountName, getCustomerId());
                return Stream.empty();
            }
            List<BankEvent> newEvent = new ArrayList<>();
            if (account.getBalance() >= 0 && account.getBalance() - value < 0) {
                System.err.printf("Account %s is overdrawn!%n", getCustomerId());
            }
            newEvent.add(new WithdrawalBankEvent(UUID.randomUUID(), getTimestamp(), getCustomerId(), accountName, value));
            return newEvent.stream();
        };
    }
}
