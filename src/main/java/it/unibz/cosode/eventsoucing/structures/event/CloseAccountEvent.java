package it.unibz.cosode.eventsoucing.structures.event;

import it.unibz.cosode.eventsoucing.structures.Account;
import it.unibz.cosode.eventsoucing.structures.Customer;

/**
 * A class representing a single close account event.
 *
 * @author ajurco
 */
public class CloseAccountEvent extends Event {

    private final String accountName;
    private final String transferredAccount;

    /**
     * The constructor.
     *
     * @param customerName       the name of the customer performing the action
     * @param accountName        the account to be closed
     * @param transferredAccount the account of the customer to which the money shall be transferred (may be empty)
     */
    public CloseAccountEvent(String customerName, String accountName, String transferredAccount) {
        super(customerName);
        this.accountName = accountName;
        this.transferredAccount = transferredAccount;
    }

    @Override
    public void performOperation(Customer customer) {
        if (customer == null) {
            System.err.printf("Unable to perform a new account operation for customer %s" +
                    " - this customer does not exist!%n", customerName);
            return;
        }

        Account account = customer.getAccount(accountName);
        if (account == null) {
            System.err.printf("Invalid account name %s for customer %s. A close account operation was denied.%n",
                    accountName,
                    customerName);
            return;
        }

        Account targetAccount = customer.getAccount(transferredAccount);
        if (targetAccount == null) {
            String firstSentence = "No transferred account provided.";
            if (!transferredAccount.strip().equals("")) {
                firstSentence = String.format("Invalid transferred account name %s for customer %s.",
                        transferredAccount, customerName);
            }
            System.err.printf("%s Balance from closing account %s will be lost.%n", firstSentence,
                    accountName);
        } else {
            targetAccount.deposit(account.getBalance());
        }

        customer.closeAccount(accountName);
    }

    @Override
    public String toString() {
        return String.format("Close account %s", accountName);
    }
}
