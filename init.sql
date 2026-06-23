INSERT INTO users (username, email, password, full_name, role, enabled, created_at, updated_at)
VALUES (
    'admin',
    'admin@system.com',
    '$2a$10$PcSdQSsZG8vbZPfdVNiDHuWnASZ3z.fGND3tDPwY5l.XBl0xadYK6',
    'System Administrator',
    'ADMIN',
    true,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO NOTHING;


SELECT id, username, email, role, enabled, created_at FROM users ORDER BY id;
