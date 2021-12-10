package it.unibz.cosode.eventsoucing.basebank;

import it.unibz.cosode.eventsoucing.Bank;
import it.unibz.cosode.eventsoucing.basebank.structures.domain.Account;
import it.unibz.cosode.eventsoucing.basebank.structures.domain.Customer;
import it.unibz.cosode.eventsoucing.basebank.structures.event.*;
import it.unibz.cosode.eventsoucing.basebank.structures.repository.EventRepository;

import java.util.Collection;
import java.util.List;

/**
 * A simple bank implementation - accepts commands from the user and processes queries.
 *
 * @author ajurco
 */
public class BaseBank implements Bank {

    private final EventRepository repository = new EventRepository();

    public EventRepository getRepository() {
        return repository;
    }

    @Override
    public boolean serveClients(String command) {

        String[] commandItems = command.split(" ");

        if (commandItems.length == 0) {
            return false;
        }
        switch (commandItems[0]) {
            case "withdraw":
                return processWithdraw(commandItems);
            case "deposit":
                return processDeposit(commandItems);
            case "open":
                return openAccount(commandItems);
            case "close":
                return closeAccount(commandItems);
            case "balance":
                return getTotalBalance(commandItems);
            case "acc-balance":
                return getAccountBalance(commandItems);
            case "list":
                return getEvents(commandItems);
            default:
                return false;
        }
    }

    private boolean processWithdraw(String[] items) {
        if (isNotTransaction(items)) {
            return false;
        }
        repository.addEvent(new WithdrawalBankEvent(items[1], items[2], Integer.parseInt(items[3])));
        return true;
    }

    private boolean processDeposit(String[] items) {
        if (isNotTransaction(items)) {
            return false;
        }
        repository.addEvent(new DepositBankEvent(items[1], items[2], Integer.parseInt(items[3])));
        return true;
    }

    private boolean openAccount(String[] items) {
        if (items.length != 3) {
            return false;
        }
        repository.addEvent(new NewAccountBankEvent(items[1], items[2]));
        return true;
    }

    private boolean closeAccount(String[] items) {
        if (items.length < 3 || items.length > 4) {
            return false;
        }

        String target = "";
        if (items.length == 4) {
            target = items[3];
        }
        repository.addEvent(new CloseAccountBankEvent(items[1], items[2], target));
        return true;
    }

    private boolean getTotalBalance(String[] items) {
        if (items.length != 2) {
            return false;
        }
        Customer customer = fetchCustomer(items[1]);
        System.out.printf("Overall balance of customer %s is %d.%n", items[1], customer.getBalance());
        return true;
    }

    private boolean getAccountBalance(String[] items) {
        if (items.length != 2) {
            return false;
        }
        Customer customer = fetchCustomer(items[1]);
        System.out.printf("Balance of accounts of customer %s:%n", customer.getName());
        Collection<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.printf("  User %s has no open account at this bank!%n", customer.getName());
        } else {
            for (Account account : customer.getAccounts()) {
                System.out.printf("  Account %s: %d%n", account.getName(), account.getBalance());
            }
        }
        return true;
    }

    private boolean getEvents(String[] items) {
        if (items.length != 2) {
            return false;
        }

        List<BankEvent> events = repository.getEvents(items[1]);
        if (events.isEmpty()) {
            System.out.printf("User %s has no recorded events.%n", items[1]);
        } else {
            System.out.printf("Events of user %s:%n", items[1]);
            for (BankEvent event : events) {
                System.out.println("  " + event);
            }
        }
        return true;
    }

    private Customer fetchCustomer(String name) {
        Customer customer = new Customer(name);
        for (BankEvent event : repository.getEvents(name)) {
            event.performOperation(customer);
        }
        return customer;
    }

    private boolean isNotTransaction(String[] items) {
        if (items.length != 4) {
            return true;
        }

        try {
            Integer.parseInt(items[3]);
        } catch (NumberFormatException e) {
            System.out.printf("Value %s is not an integer!%n", items[3]);
            return true;
        }
        return false;
    }

    @Override
    public int getEventCount() {
        return repository.countEvents();
    }

}
