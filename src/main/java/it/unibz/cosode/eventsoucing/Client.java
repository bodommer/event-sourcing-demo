package it.unibz.cosode.eventsoucing;

import it.unibz.cosode.eventsoucing.basebank.BaseBank;
import it.unibz.cosode.eventsoucing.occurrentbank.OccurrentBank;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * A client for interacting with the user.
 *
 * @author ajurco
 */
public class Client {

    private static final String INVALID_INPUT = "Invalid input.";

    private final Scanner scanner;
    private final boolean isFile;
    private final Bank bank;

    /**
     * The constructor.
     *
     * @param stream the stream from which the user input shall be read
     */
    public Client(InputStream stream, Bank bank) {
        this.scanner = new Scanner(stream);
        this.isFile = stream instanceof FileInputStream;
        this.bank = bank;
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

    public int countEvents() {
        return bank.getEventCount();
    }

    private String getInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    private void processInput(String input) {
        if (input.length() == 0) {
            return;
        }

        if (!bank.serveClients(input)) {
            System.out.println(INVALID_INPUT);
        }
    }

    /**
     * The run method of the program. No arguments are required and/or processed.
     *
     * @param args arguments of the program (none needed)
     */
    public static void main(String[] args) {
        if (args.length == 1 || (args.length > 1 && args[1].equals("base"))) {
            (new Client(System.in, new BaseBank())).run();
        } else {
            (new Client(System.in, new OccurrentBank())).run();
        }
    }

}
