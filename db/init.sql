-- Create table for storing messages
CREATE TABLE IF NOT EXISTS received_messages (
                                                 id SERIAL PRIMARY KEY,
                                                 message_id VARCHAR(255) UNIQUE,
    xml_content TEXT NOT NULL,
    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Optional index (faster lookups)
CREATE INDEX IF NOT EXISTS idx_message_id ON received_messages(message_id);
