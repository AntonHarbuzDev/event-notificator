package school.sorokin.event_notificator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventKafka {

    private Long eventId;
    private Long ownerId;
    private Long userChangedId;
    private EventChangeFields eventChangeFields;
    private Set<Long> registrationUserIds;
}
