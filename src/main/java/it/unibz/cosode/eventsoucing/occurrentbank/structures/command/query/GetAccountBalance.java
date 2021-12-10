package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Account;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command (query) representation for retrieving balance of all customer's accounts.
 *
 * @author ajurco
 */
public class GetAccountBalance extends BankCommand {

    public GetAccountBalance(String customerId, LocalDateTime timestamp) {
        super(customerId, timestamp, true);
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            Customer customer = recreateCustomer(getCustomerId(), events);
            System.out.printf("Balance of accounts of customer %s:%n", customer.getName());
            Collection<Account> accounts = customer.getAccounts();
            if (accounts.isEmpty()) {
                System.out.printf("  User %s has no open account at this bank!%n", customer.getName());
            } else {
                for (Account account : customer.getAccounts()) {
                    System.out.printf("  Account %s: %d%n", account.getName(), account.getBalance());
                }
            }
            return Stream.empty();
        };
    }
}
