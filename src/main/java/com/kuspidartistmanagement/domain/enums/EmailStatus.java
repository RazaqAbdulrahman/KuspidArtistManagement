package com.kuspidartistmanagement.domain.enums;

/**
 * Status tracking for email sending workflow.
 * PENDING: Queued for sending
 * SENT: Successfully delivered
 * FAILED: Delivery failed (after all retries)
 * RETRY: Temporary failure, will retry
 */
public enum EmailStatus {
    PENDING,
    SENT,
    FAILED,
    BOUNCED,
    OPENED,
    RETRY, REPLIED
}