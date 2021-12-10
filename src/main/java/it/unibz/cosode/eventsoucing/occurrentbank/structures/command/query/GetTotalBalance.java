package it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.BankEvent;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Command (query) representation for retrieving the total balance of the customer.
 *
 * @author ajurco
 */
public class GetTotalBalance extends BankCommand {

    public GetTotalBalance(String customerId, LocalDateTime timestamp) {
        super(customerId, timestamp, true);
    }

    @Override
    public Function<Stream<BankEvent>, Stream<BankEvent>> getCommandFunction() {
        return events -> {
            Customer customer = recreateCustomer(getCustomerId(), events);
            System.out.printf("Overall balance of customer %s is %d.%n", getCustomerId(), customer.getBalance());
            return Stream.empty();
        };
    }
}
