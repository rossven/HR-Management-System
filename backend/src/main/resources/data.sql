INSERT INTO candidates (first_name, last_name, position, military_status, phone, email, notice_period_months, notice_period_days, created_at, updated_at)
VALUES 
('John', 'Doe', 'Software Developer', 'COMPLETED', '5551234567', 'john@example.com', 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane', 'Smith', 'Project Manager', 'EXEMPT', '5559876543', 'jane@example.com', 1, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mike', 'Johnson', 'UI Designer', 'POSTPONED', '5555555555', 'mike@example.com', 0, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 