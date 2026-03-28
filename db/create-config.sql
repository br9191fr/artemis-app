-- Create role if it doesn't exist, otherwise just ensure the password is correct
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_roles
        WHERE rolname = 'bruno'
    ) THEN
CREATE ROLE bruno LOGIN PASSWORD 'alto';
ELSE
        ALTER ROLE bruno WITH LOGIN PASSWORD 'alto';
END IF;
END
$$;

-- Create database if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_database
        WHERE datname = 'messages'
    ) THEN
        CREATE DATABASE messages OWNER bruno;
END IF;
END
$$;

-- Connect to the target database
\connect messages

-- Make sure schema exists and bruno can use it
GRANT CONNECT ON DATABASE messages TO bruno;
GRANT USAGE ON SCHEMA public TO bruno;

-- If the table already exists, grant the needed rights
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO bruno;

-- Ensure future tables also get the same permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bruno;