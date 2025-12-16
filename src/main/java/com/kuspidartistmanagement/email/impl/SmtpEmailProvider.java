package com.kuspidartistmanagement.email.impl;

import com.kuspidartistmanagement.email.EmailProvider;
import com.kuspidartistmanagement.email.exception.EmailProviderUnavailableException;
import com.kuspidartistmanagement.email.exception.EmailSendException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpEmailProvider implements EmailProvider {

    private static final String CB_NAME = "emailProvider";

    private final JavaMailSender mailSender;

    @Override
    @Retry(name = CB_NAME)
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSend")
    public void send(String to, String subject, String body, List<String> attachmentUrls) throws Throwable {
        sendInternal(to, subject, body, false, attachmentUrls);
    }

    @Override
    @Retry(name = CB_NAME)
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSendHtml")
    public void sendHtml(String to, String subject, String htmlBody, List<String> attachmentUrls) throws Throwable {
        sendInternal(to, subject, htmlBody, true, attachmentUrls);
    }

    private void sendInternal(String to, String subject, String content, boolean html, List<String> attachmentUrls) throws Throwable {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);

            // TODO: Add attachments if needed
            // for (String url : attachmentUrls) { helper.addAttachment(...); }

            mailSender.send(message);
            log.info("Email sent to {} (html={})", to, html);

        } catch (MailException ex) {
            log.error("SMTP provider error", ex);
            throw ex; // propagate to trigger CircuitBreaker
        } catch (Exception ex) {
            log.error("Unexpected email error", ex);
            throw new Throwable("Failed to send email", ex);
        }
    }

    // Fallback for send (plain text)
    private void fallbackSend(String to, String subject, String body, List<String> attachmentUrls, Throwable ex) throws EmailProviderUnavailableException {
        handleFallback(to, subject, ex);
    }

    // Fallback for sendHtml (HTML)
    private void fallbackSendHtml(String to, String subject, String htmlBody, List<String> attachmentUrls, Throwable ex) throws EmailProviderUnavailableException {
        handleFallback(to, subject, ex);
    }

    private void handleFallback(String to, String subject, Throwable ex) throws EmailProviderUnavailableException {
        log.error("Email provider unavailable. Circuit OPEN. Email not sent. to={}, subject={}", to, subject, ex);

        // Optional: enqueue email for later retry
        // emailQueueService.enqueue(to, subject, ...);

        throw new EmailProviderUnavailableException("Email service temporarily unavailable", ex);
    }
}
