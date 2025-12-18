-- =========================================
-- V6: Ensure admin exists and reset password
-- =========================================

-- 0. Ensure UUID generation is available
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1. Ensure default tenant exists
INSERT INTO tenants (
    id,
    name,
    email,
    active,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    'Default Tenant',
    'kuspidmusic@gmail.com',
    TRUE,
    NOW(),
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM tenants WHERE email = 'kuspidmusic@gmail.com'
);

-- 2. Ensure admin exists (linked to correct tenant)
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
    '$2a$12$u5V1Wq3mZ1q3Tz4mZqJ1OuQZ4rYk5hB2wQ0yVh2cX8bZxGqzQ9B8e',
    'Kuspid',
    'Admin',
    t.id,
    TRUE,
    NOW(),
    NOW()
FROM tenants t
WHERE t.email = 'kuspidmusic@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM admins WHERE email = 'kuspidmusic@gmail.com'
  );

-- 3. ALWAYS reset admin password + activate account
UPDATE admins
SET
    password = '$2a$12$u5V1Wq3mZ1q3Tz4mZqJ1OuQZ4rYk5hB2wQ0yVh2cX8bZxGqzQ9B8e',
    active = TRUE,
    updated_at = NOW()
WHERE email = 'kuspidmusic@gmail.com';
