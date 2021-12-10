package it.unibz.cosode.eventsourcing;

import it.unibz.cosode.eventsoucing.Client;
import it.unibz.cosode.eventsoucing.occurrentbank.OccurrentBank;
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
class OccurrentBankTest {

    /**
     * A simple test for executing a set of instructions on Occurrent Bank. Uses FileInputStream to supply commands to
     * the bank.
     *
     * @param file the file whence commands shall be read
     * @throws FileNotFoundException when the resource file cannot be found
     */
    @ParameterizedTest
    @ValueSource(strings = {"simple_bank.txt", "complex_bank.txt"})
    void createEventsAndReadOccurrent(String file) throws FileNotFoundException {
        InputStream input = new FileInputStream("src/test/resources/input/" + file);
        Client client = new Client(input, new OccurrentBank());
        client.run();
        int count = client.countEvents();
        assertTrue(client.countEvents() >= 2);
        assertTrue(client.countEvents() <= 18);
    }
}
