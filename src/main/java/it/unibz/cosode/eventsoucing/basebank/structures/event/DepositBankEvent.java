package it.unibz.cosode.eventsoucing.basebank.structures.event;

import it.unibz.cosode.eventsoucing.basebank.structures.domain.Account;
import it.unibz.cosode.eventsoucing.basebank.structures.domain.Customer;

/**
 * A class representing a single money deposit event.
 *
 * @author ajurco
 */
public class DepositBankEvent extends BankEvent {

    private final String accountName;
    private final int value;

    public DepositBankEvent(String customerName, String accountName, int value) {
        super(customerName);
        this.accountName = accountName;
        this.value = value;
    }

    @Override
    public void performOperation(Customer customer) {
        if (customer == null) {
            System.err.printf("Unable to perform a deposit operation for customer %s - this customer does not exist!%n",
                    customerName);
            return;
        }
        Account account = customer.getAccount(accountName);
        if (account == null) {
            System.err.printf("Invalid account name %s for customer %s. A deposit operation was denied.%n", accountName,
                    customerName);
            return;
        }
        account.deposit(value);
    }

    @Override
    public String toString() {
        return String.format("Deposit %d to account %s", value, accountName);
    }
}
