CREATE USER central_module_user WITH PASSWORD 'central_module_user_pass';
CREATE USER printer_module_user WITH PASSWORD 'printer_module_user_pass';

CREATE TABLE IF NOT EXISTS tasks (
    id UUID PRIMARY KEY,
    topic TEXT NOT NULL,
    description TEXT NOT NULL,
    due_date TIMESTAMP NOT NULL,
    printing_status INTEGER DEFAULT 0
);

GRANT SELECT, UPDATE (printing_status) ON tasks TO printer_module_user;
GRANT SELECT, INSERT, UPDATE ON tasks TO central_module_user;
