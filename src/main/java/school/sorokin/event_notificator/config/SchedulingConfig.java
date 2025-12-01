package school.sorokin.event_notificator.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import school.sorokin.event_notificator.service.EventNotificationService;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    private final EventNotificationService eventNotificationService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void deleteOldNotifications() {
        log.info("Method deleteOldNotifications begin");
        eventNotificationService.deleteNotification();
    }
}
