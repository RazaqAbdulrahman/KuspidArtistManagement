-- ================================
-- Seed Default Tenant and Admin
-- ================================

-- 1. Create default tenant (idempotent)
INSERT INTO tenants (
    id,
    name,
    email,
    active,
    created_at,
    updated_at
)
VALUES (
    gen_random_uuid(),
    'Default Tenant',
    'kuspidmusic@gmail.com',
    TRUE,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO NOTHING;

-- 2. Create admin user for the tenant (idempotent)
WITH tenant_cte AS (
    SELECT id
    FROM tenants
    WHERE email = 'kuspidmusic@gmail.com'
)
INSERT INTO admins (
    id,
    email,
    password,
    first_name,
    last_name,
    tenant_id,
    active,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    'kuspidmusic@gmail.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5Rvqz.UEfXQKi',
    'Kuspid',
    'Admin',
    id,
    TRUE,
    NOW(),
    NOW()
FROM tenant_cte
ON CONFLICT (email) DO NOTHING;
