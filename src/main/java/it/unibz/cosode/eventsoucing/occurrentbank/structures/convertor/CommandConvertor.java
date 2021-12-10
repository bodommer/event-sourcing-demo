package it.unibz.cosode.eventsoucing.occurrentbank.structures.convertor;

import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.BankCommand;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify.CloseAccount;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify.CreateAccount;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify.DepositMoney;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.modify.WithdrawMoney;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query.GetAccountBalance;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query.GetEvents;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.command.query.GetTotalBalance;

import java.time.LocalDateTime;

/**
 * The class used for transforming string commandline input to commands.
 *
 * @author ajurco
 */
public class CommandConvertor {

    /**
     * The entry method for the conversion.
     *
     * @param input string command to be converted
     * @return a {@link BankCommand} resulting from the conversion, null if the command is invalid
     */
    public static BankCommand convert(String input) {

        String[] items = input.split(" ");

        if (items.length == 0) {
            return null;
        }

        switch (items[0]) {
            case "withdraw":
                if (isTransaction(items)) {
                    return new WithdrawMoney(items[1], LocalDateTime.now(), items[2], Integer.parseInt(items[3]));
                }
                break;
            case "deposit":
                if (isTransaction(items)) {
                    return new DepositMoney(items[1], LocalDateTime.now(), items[2], Integer.parseInt(items[3]));
                }
                break;
            case "open":
                return new CreateAccount(items[1], LocalDateTime.now(), items[2]);
            case "close":
                if (isNotCorrectCloseSize(items.length)) {
                    return null;
                }
                String target = getTarget(items);
                return new CloseAccount(items[1], LocalDateTime.now(), items[2], target);
            case "balance":
                if (isQuery(items)) {
                    return new GetTotalBalance(items[1], LocalDateTime.now());
                }
                break;
            case "acc-balance":
                if (isQuery(items)) {
                    return new GetAccountBalance(items[1], LocalDateTime.now());
                }
                break;
            case "list":
                if (isQuery(items)) {
                    return new GetEvents(items[1], LocalDateTime.now());
                }
                break;
            default:
        }
        return null;
    }

    private static boolean isTransaction(String[] items) {
        if (items.length != 4) {
            return false;
        }

        try {
            Integer.parseInt(items[3]);
        } catch (NumberFormatException e) {
            System.out.printf("Value %s is not an integer!%n", items[3]);
            return false;
        }
        return true;
    }

    private static boolean isNotCorrectCloseSize(int size) {
        return size < 3 || size > 4;
    }

    private static String getTarget(String[] items) {
        if (items.length == 4) {
            return items[3];
        }
        return "";
    }

    private static boolean isQuery(String[] items) {
        return items.length == 2;
    }

    private CommandConvertor() {
    }

}
