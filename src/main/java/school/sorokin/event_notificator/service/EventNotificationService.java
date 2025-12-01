package school.sorokin.event_notificator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import school.sorokin.event_notificator.exception.ConvertToJsonException;
import school.sorokin.event_notificator.model.EventChangeFields;
import school.sorokin.event_notificator.model.EventKafka;
import school.sorokin.event_notificator.model.dto.EventKafkaShowDto;
import school.sorokin.event_notificator.model.entity.EventNotificationEntity;
import school.sorokin.event_notificator.repository.EventNotificationRepository;
import school.sorokin.event_notificator.security.JwtTokenManager;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventNotificationService {

    @Value("${day.notification.storge}")
    private Long dayNotificationStorage;

    private static final Logger log = LoggerFactory.getLogger(EventNotificationService.class);

    private final EventNotificationRepository eventNotificationRepository;
    private final ObjectMapper objectMapper;
    private final JwtTokenManager jwtTokenManager;

    @Transactional
    public void create(EventKafka eventKafka) {
        if (eventKafka.getRegistrationUserIds().isEmpty()) {
            log.info("No one registered for the event {}", eventKafka.getEventId());
        } else {
            eventKafka.getRegistrationUserIds().forEach(
                    id -> {
                        EventNotificationEntity entity = toEntity(eventKafka, id);
                        entity.setDateCreated(OffsetDateTime.now());
                        entity.setIsRead(false);
                        EventNotificationEntity result = eventNotificationRepository.save(entity);
                        log.info("Created entity event notification success {}", result);
                    }
            );
        }
    }

    @Transactional(readOnly = true)
    public List<EventKafkaShowDto> getMyEventsNoRead() {
        Long ownerId = getUserIdFromJwt(getTokenFromRequest());
        List<EventNotificationEntity> entitiesNoRead = eventNotificationRepository.findAllByIsReadAndOwnerNotificationId(false, ownerId);
        log.info("no found no read message, entitiesNoRead - {}", entitiesNoRead.isEmpty());
        entitiesNoRead.forEach(entity -> log.info("read entity - {}", entity));
        return entitiesNoRead.stream().map(this::toEventKafkaShowDto).toList();
    }

    @Transactional
    public void updateEventToRead(List<Long> notificationIds) {
        List<EventNotificationEntity> entities = eventNotificationRepository.findAllById(notificationIds);
        entities.forEach(entity -> entity.setIsRead(true));
        log.info("switch status read is true");
    }

    @Transactional
    public void deleteNotification() {
        OffsetDateTime timeBeforeCreated = OffsetDateTime.now().minusDays(dayNotificationStorage);
        log.info("we are looking for all EventNotifications up to {}", timeBeforeCreated);
        List<EventNotificationEntity> eventEntities =
                eventNotificationRepository.findAllByDateCreatedBefore(timeBeforeCreated);
        if (eventEntities.isEmpty()) {
            log.info("EventNotifications no found '=( ");
        } else {
            log.info("delete all EventNotifications found...");
            eventEntities.forEach(eventNotificationRepository::delete);
        }
    }

    private String getTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request attributes found");
        }
        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No valid Authorization header found");
        }
        return authHeader.substring(7);
    }

    private Long getUserIdFromJwt(String jwtToken) {
        return jwtTokenManager.getUserIdFromToken(jwtToken);
    }

    private EventKafkaShowDto toEventKafkaShowDto(EventNotificationEntity entity) {
        return new EventKafkaShowDto(
                entity.getEventId(),
                entity.getFieldsChangeJson()
        );
    }

    private EventNotificationEntity toEntity(EventKafka eventKafka, Long ownerNotificationId) {
        return EventNotificationEntity.builder()
                .eventId(eventKafka.getEventId())
                .ownerNotificationId(ownerNotificationId)
                .ownerEventId(eventKafka.getOwnerId())
                .userChangedId(eventKafka.getUserChangedId() == null ? null : eventKafka.getUserChangedId())
                .fieldsChangeJson(convertToJson(eventKafka.getEventChangeFields()))
                .build();
    }

    private String convertToJson(EventChangeFields changeFields) {
        if (changeFields == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(changeFields);
        } catch (Exception e) {
            throw new ConvertToJsonException("Error serializing EventChangeFields to JSON", e);
        }
    }
}
