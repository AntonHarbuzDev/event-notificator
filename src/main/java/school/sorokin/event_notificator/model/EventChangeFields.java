package school.sorokin.event_notificator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventChangeFields {

    private FieldChange<String> name;
    private FieldChange<Integer> maxPlaces;
    private FieldChange<OffsetDateTime> date;
    private FieldChange<BigDecimal> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<Status> status;
}
