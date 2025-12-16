// EmailRetryWorker.java
package com.kuspidartistmanagement.worker;

import com.kuspidartistmanagement.config.EmailConfig;
import com.kuspidartistmanagement.domain.entity.EmailSend;
import com.kuspidartistmanagement.domain.enums.EmailStatus;
import com.kuspidartistmanagement.repository.EmailSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailRetryWorker {

    private final EmailSendRepository emailSendRepository;
    private final EmailSendWorker emailSendWorker;
    private final EmailConfig emailConfig;

    @Scheduled(fixedDelayString = "${app.email.retry-delay}")
    public void retryFailedEmails() {
        log.debug("Checking for emails to retry...");

        LocalDateTime retryAfter = LocalDateTime.now()
                .minusSeconds(emailConfig.getRetryDelay() / 1000);

        List<EmailSend> emailsToRetry = emailSendRepository.findEmailsForRetry(
                EmailStatus.RETRY,
                emailConfig.getRetryMax(),
                retryAfter
        );

        if (!emailsToRetry.isEmpty()) {
            log.info("Retrying {} failed emails", emailsToRetry.size());

            for (EmailSend emailSend : emailsToRetry) {
                if (emailSend.hasExceededMaxRetries(emailConfig.getRetryMax())) {
                    emailSend.markAsFailed("Max retries exceeded");
                    emailSendRepository.save(emailSend);
                    log.warn("Email permanently failed after max retries: {}", emailSend.getId());
                } else {
                    emailSendWorker.sendEmail(emailSend.getId());
                }
            }
        }
    }
}