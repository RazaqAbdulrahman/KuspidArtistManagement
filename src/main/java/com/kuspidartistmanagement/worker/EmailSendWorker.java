// EmailSendWorker.java
package com.kuspidartistmanagement.worker;

import com.kuspidartistmanagement.domain.entity.EmailSend;
import com.kuspidartistmanagement.domain.enums.EmailStatus;
import com.kuspidartistmanagement.email.EmailProvider;
import com.kuspidartistmanagement.repository.EmailSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendWorker {

    private final EmailSendRepository emailSendRepository;
    private final EmailProvider emailProvider;

    @Async("taskExecutor")
    @Transactional
    public void sendEmail(UUID emailSendId) {
        EmailSend emailSend = emailSendRepository.findById(emailSendId)
                .orElseThrow(() -> new RuntimeException("EmailSend not found"));

        try {
            log.debug("Sending email to: {}", emailSend.getArtist().getEmail());

            emailProvider.sendHtml(
                    emailSend.getArtist().getEmail(),
                    emailSend.getSubject(),
                    emailSend.getEmailBody(),
                    Collections.emptyList()
            );

            emailSend.markAsSent();
            emailSendRepository.save(emailSend);

            log.info("Email sent successfully to: {}", emailSend.getArtist().getEmail());

        } catch (Exception e) {
            log.error("Failed to send email to: {}", emailSend.getArtist().getEmail(), e);
            emailSend.setErrorMessage(e.getMessage());
            emailSend.incrementRetry();
            emailSendRepository.save(emailSend);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}