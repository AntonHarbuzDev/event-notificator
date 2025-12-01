package school.sorokin.event_notificator.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Table(name = "notifications")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "owner_notification_id", nullable = false)
    private Long ownerNotificationId;

    @Column(name = "owner_event_id", nullable = false)
    private Long ownerEventId;

    @Column(name = "user_changed_id")
    private Long userChangedId;

    @Column(name = "fields_change_json", columnDefinition = "text")
    private String fieldsChangeJson;

    @Column(name = "date_created", nullable = false)
    private OffsetDateTime dateCreated;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
}

