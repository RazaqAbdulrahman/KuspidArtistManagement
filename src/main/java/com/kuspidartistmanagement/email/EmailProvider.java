// EmailProvider.java (Interface)
package com.kuspidartistmanagement.email;

import java.util.List;

public interface EmailProvider {
    void send(String to, String subject, String body, List<String> attachmentUrls) throws Throwable;
    void sendHtml(String to, String subject, String htmlBody, List<String> attachmentUrls) throws Throwable;
}