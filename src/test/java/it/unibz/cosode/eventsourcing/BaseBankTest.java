package it.unibz.cosode.eventsourcing;

import it.unibz.cosode.eventsoucing.Client;
import it.unibz.cosode.eventsoucing.basebank.BaseBank;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A simple test case for demo.
 *
 * @author ajurco
 */
class BaseBankTest {

    /**
     * A simple test for executing a set of instructions on Base Bank. Uses FileInputStream to supply commands to the
     * bank.
     *
     * @param file the file whence commands shall be read
     * @throws FileNotFoundException when the resource file cannot be found
     */
    @ParameterizedTest
    @ValueSource(strings = {"simple_bank.txt", "complex_bank.txt"})
    void createEventsAndRead(String file) throws FileNotFoundException {
        InputStream input = new FileInputStream("src/test/resources/input/" + file);
        Client client = new Client(input, new BaseBank());
        client.run();
        assertTrue(client.countEvents() >= 2);
        assertTrue(client.countEvents() <= 18);
    }
}
