package school.sorokin.event_notificator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldChange<T> {

    private T oldField;
    private T newField;
}
