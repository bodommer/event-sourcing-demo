package it.unibz.cosode.eventsourcing;

import it.unibz.cosode.eventsoucing.bank.Bank;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * A simple test case for demo.
 *
 * @author ajurco
 */
class BankTest {

    /**
     * A simple test for executing a set of instructions. Uses FileInputStream to supply commands to the bank.
     *
     * @param file the file whence commands shall be read
     * @throws FileNotFoundException when the resource file cannot be found
     */
    @ParameterizedTest
    @ValueSource(strings = {"simple_bank.txt", "complex_bank.txt"})
    void createEventsAndRead(String file) throws FileNotFoundException {
        InputStream input = new FileInputStream("src/test/resources/input/" + file);
        Bank bank = new Bank(input);
        bank.run();
        assertFalse(bank.getRepository().getEvents("john").isEmpty());
    }

}
