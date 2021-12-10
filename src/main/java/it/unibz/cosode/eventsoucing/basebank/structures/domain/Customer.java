package it.unibz.cosode.eventsoucing.basebank.structures.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a customer and providing some basic functionality of the user.
 *
 * @author ajurco
 */
public class Customer {

    private final String name;
    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * The constructor.
     *
     * @param name the name of the customer
     */
    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Account getAccount(String accountName) {
        return accounts.get(accountName);
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

    public void openAccount(String accountName) {
        accounts.put(accountName, new Account(accountName));
    }

    public void closeAccount(String accountName) {
        accounts.remove(accountName);
    }

    /**
     * @return customer's net worth - a sum of all cutomer's account balances
     */
    public int getBalance() {
        return accounts.values().stream().map(Account::getBalance).mapToInt(Integer::intValue).sum();
    }

}
