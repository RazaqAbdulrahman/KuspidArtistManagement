package com.kuspidartistmanagement.security;

import java.util.UUID;

/**
 * Thread-local storage for current tenant context.
 * Provides tenant isolation across requests in a multi-tenant system.
 * Must be cleared after each request to prevent context leakage.
 *
 * Usage:
 * - Set at the beginning of request processing (in filter/interceptor)
 * - Access throughout service layer for tenant-scoped operations
 * - Clear at the end of request processing
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets the current tenant ID for this thread.
     *
     * @param tenantId the tenant identifier
     */
    public static void setTenantId(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Gets the current tenant ID for this thread.
     *
     * @return the tenant identifier, or null if not set
     */
    public static UUID getTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Clears the current tenant context.
     * Should be called at the end of each request to prevent context leakage.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }

    /**
     * Checks if tenant context is set.
     *
     * @return true if tenant ID is set
     */
    public static boolean isSet() {
        return CURRENT_TENANT.get() != null;
    }
}