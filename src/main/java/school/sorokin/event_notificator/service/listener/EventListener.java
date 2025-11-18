package school.sorokin.event_notificator.service.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import school.sorokin.event_notificator.model.EventKafka;
import school.sorokin.event_notificator.service.EventNotificationService;

@Component
@RequiredArgsConstructor
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final EventNotificationService eventNotificationService;

    @KafkaListener(topics = "event", groupId = "event-notificator")
    public void listenEvents(EventKafka eventKafka) {
        log.info("Received an event = {} from Kafka.", eventKafka);
        eventNotificationService.create(eventKafka);
    }
}
