package school.sorokin.event_notificator.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_notificator.model.dto.EventKafkaShowDto;
import school.sorokin.event_notificator.model.dto.NotificationIdsDto;
import school.sorokin.event_notificator.service.EventNotificationService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventNotificationController {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationController.class);

    private final EventNotificationService eventNotificationService;

    @GetMapping("/notifications")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<EventKafkaShowDto>> getNoReadMyNotifications() {
        log.info("start method getNoReadMyNotifications!!!");
        List<EventKafkaShowDto> result = eventNotificationService.getMyEventsNoRead();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/notifications")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Void> makeReadNotifications(@RequestBody NotificationIdsDto request) {
        eventNotificationService.updateEventToRead(request.getNotificationIds());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
