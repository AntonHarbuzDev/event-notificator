package school.sorokin.event_notificator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventKafkaShowDto {

    private Long eventId;
    private String eventChangeFields;
}
