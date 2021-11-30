package it.unibz.cosode.eventsoucing.bank;

import it.unibz.cosode.eventsoucing.structures.Account;
import it.unibz.cosode.eventsoucing.structures.Customer;
import it.unibz.cosode.eventsoucing.structures.EventRepository;
import it.unibz.cosode.eventsoucing.structures.event.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * A simple bank implementation - accepts commands from the user and processes queries.
 *
 * @author ajurco
 */
public class Bank {

    private static final String INVALID_INPUT = "Invalid input.";

    private final Scanner scanner;
    private final boolean isFile;
    private final EventRepository repository = new EventRepository();

    /**
     * The constructor.
     *
     * @param stream the stream from which the user input shall be read
     */
    public Bank(InputStream stream) {
        this.scanner = new Scanner(stream);
        this.isFile = stream instanceof FileInputStream;
    }

    public EventRepository getRepository() {
        return repository;
    }

    /**
     * Runs the bank - an endless loop for accepting and processing user input.
     */
    public void run() {
        System.out.println("== Welcome to the bank! ===\n==========================================================\n" +
                "To use the bank, please use following commands:\n" +
                " - 'open <customer> <account name>' to open a bank account\n" +
                " - 'close <customer> <account name> <transferred account>' to close an account and transfer money to " +
                "other account (may be left empty)\n" +
                " - 'deposit <customer> <account> <value>' to deposit money to a bank account\n" +
                " - 'withdraw <customer> <account> <value>' to withdraw money from a bank account\n" +
                " - 'acc-balance <customer>' to see account balances of a customer\n" +
                " - 'balance <customer>' to see total net worth of a customer at the bank\n" +
                " - 'list <customer>' to see the history of operations of the customer\n" +
                " - 'exit' to close the bank\n");

        while (true) {
            String input = getInput();

            // for test readability
            if (isFile) {
                System.out.println(input);
            }

            if (input.equals("exit")) {
                break;
            }
            processInput(input);
        }
    }

    private String getInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    private void processInput(String input) {
        if (input.length() == 0) {
            return;
        }

        String[] items = input.split(" ");
        if (items.length == 0) {
            return;
        }

        boolean success = false;
        switch (items[0]) {
            case "withdraw":
                success = processWithdraw(items);
                break;
            case "deposit":
                success = processDeposit(items);
                break;
            case "open":
                success = openAccount(items);
                break;
            case "close":
                success = closeAccount(items);
                break;
            case "balance":
                success = getTotalBalance(items);
                break;
            case "acc-balance":
                success = getAccountBalance(items);
                break;
            case "list":
                success = getEvents(items);
                break;
            default:
        }
        if (!success) {
            System.out.println(INVALID_INPUT);
        }
    }

    private boolean processWithdraw(String[] items) {
        if (isNotTransaction(items)) {
            return false;
        }
        repository.addEvent(new WithdrawalEvent(items[1], items[2], Integer.parseInt(items[3])));
        return true;
    }

    private boolean processDeposit(String[] items) {
        if (isNotTransaction(items)) {
            return false;
        }
        repository.addEvent(new DepositEvent(items[1], items[2], Integer.parseInt(items[3])));
        return true;
    }

    private boolean openAccount(String[] items) {
        if (items.length != 3) {
            return false;
        }
        repository.addEvent(new NewAccountEvent(items[1], items[2]));
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
        repository.addEvent(new CloseAccountEvent(items[1], items[2], target));
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

        List<Event> events = repository.getEvents(items[1]);
        if (events.isEmpty()) {
            System.out.printf("User %s has no recorded events.%n", items[1]);
        } else {
            System.out.printf("Events of user %s:%n", items[1]);
            for (Event event : events) {
                System.out.println("  " + event);
            }
        }
        return true;
    }

    private Customer fetchCustomer(String name) {
        Customer customer = new Customer(name);
        for (Event event : repository.getEvents(name)) {
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

    /**
     * The run method of the program. No arguments are required and/or processed.
     *
     * @param args arguments of the program (none needed)
     */
    public static void main(String[] args) {
        Bank bank = new Bank(System.in);
        bank.run();
    }

}
