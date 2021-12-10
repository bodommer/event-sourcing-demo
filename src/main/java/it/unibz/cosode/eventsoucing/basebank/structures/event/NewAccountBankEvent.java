package it.unibz.cosode.eventsoucing.basebank.structures.event;

import it.unibz.cosode.eventsoucing.basebank.structures.domain.Customer;

/**
 * A class representing a single opening of an account event.
 *
 * @author ajurco
 */
public class NewAccountBankEvent extends BankEvent {

    private final String accountName;

    public NewAccountBankEvent(String customerName, String accountName) {
        super(customerName);
        this.accountName = accountName;
    }

    @Override
    public void performOperation(Customer customer) {
        if (customer == null) {
            System.err.printf("Unable to perform a new account operation for customer %s" +
                    " - this customer does not exist!%n", customerName);
            return;
        }
        customer.openAccount(accountName);
    }

    @Override
    public String toString() {
        return String.format("Open account %s", accountName);
    }
}
