CREATE TABLE IF NOT EXISTS tasks (
    id UUID NOT NULL,
    topic VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE NOT NULL,
    printing_status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
