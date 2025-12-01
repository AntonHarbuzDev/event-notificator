package school.sorokin.event_notificator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sorokin.event_notificator.model.entity.EventNotificationEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotificationEntity, Long> {

    List<EventNotificationEntity> findAllByIsReadAndOwnerNotificationId(Boolean isRead, Long ownerNotificationId);

    List<EventNotificationEntity> findAllByDateCreatedBefore(OffsetDateTime dateCreated);
}
