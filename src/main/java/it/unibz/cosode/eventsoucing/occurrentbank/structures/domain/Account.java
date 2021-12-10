package it.unibz.cosode.eventsoucing.occurrentbank.structures.domain;

/**
 * A class representing a customer's account. Provides basic functionality performed on an account
 *
 * @author ajurco
 */
public class Account {

    private int balance;
    private final String name;

    /**
     * The constructor.
     *
     * @param name name of this account
     */
    public Account(String name) {
        this.balance = 0;
        this.name = name;
    }

    /**
     * Deposits the provided amount to this account.
     *
     * @param value amount of money to be added
     */
    public void deposit(int value) {
        balance = balance + value;
    }

    /**
     * Withdraws specified amount of money from the account. If the new balance would become negative, it logs the
     * message to the standard error output.
     *
     * @param value the value to be withdrawn
     */
    public void withdraw(int value) {
        balance = balance - value;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

}
