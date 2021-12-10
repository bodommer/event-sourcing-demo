package it.unibz.cosode.eventsoucing.occurrentbank.structures.convertor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import it.unibz.cosode.eventsoucing.occurrentbank.structures.event.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A class for deserialization of the {@link BankEvent} from the {@link CloudEvent}.
 *
 * @author ajurco
 */
public class EventConvertor {

    /**
     * The object mapper to be used for (de)serializing map objects.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ACCOUNT = "account";

    /**
     * The entry method for deserialization.
     *
     * @param event the event to be deserialized
     * @return deserialized {@link BankEvent} or null if the type is unknown
     */
    public static BankEvent convert(CloudEvent event) {
        UUID id = UUID.fromString(event.getId());
        LocalDateTime timestamp = event.getTime().toLocalDateTime();
        String customerId = event.getSubject();
        Map<String, Object> data = deserializeData(event);

        switch (event.getType()) {
            case "withdraw":
                return new WithdrawalBankEvent(id, timestamp, customerId, (String) data.get(ACCOUNT),
                        (int) data.get("value"));
            case "deposit":
                return new DepositBankEvent(id, timestamp, customerId, (String) data.get(ACCOUNT),
                        (int) data.get("value"));
            case "new-account":
                return new NewAccountBankEvent(id, timestamp, customerId, (String) data.get(ACCOUNT));
            case "close-account":
                return new CloseAccountBankEvent(id, timestamp, customerId, (String) data.get(ACCOUNT));
            default:
                // should never happen
                return null;
        }
    }

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    @SuppressWarnings("ConstantConditions")
    private static Map<String, Object> deserializeData(CloudEvent c) {
        try {
            return objectMapper.readValue(c.getData().toBytes(), new TypeReference<>() {
            });
        } catch (Exception e) {
            System.err.println("Unable to serialize deserialize data of " + c.getId());
        }
        return new HashMap<>();
    }

    private EventConvertor() {
    }

}
