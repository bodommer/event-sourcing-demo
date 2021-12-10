package it.unibz.cosode.eventsoucing;

/**
 * A common interface for all banks implementations.
 *
 * @author ajurco
 */
public interface Bank {

    /**
     * The main interface for processing user input.
     *
     * @param command user input
     * @return true if everything went OK, false when the input was invalid
     */
    boolean serveClients(String command);

    /**
     * @return the number of events in the repository/event store
     */
    int getEventCount();

}
