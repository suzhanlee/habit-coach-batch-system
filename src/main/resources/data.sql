-- Users
INSERT INTO users (id, name, email)
VALUES (1, 'John Doe', 'john@example.com'),
       (2, 'Jane Smith', 'jane@example.com');

-- Habits
INSERT INTO habits (id, name, description, user_id, badge)
VALUES (1, 'Morning Jog', 'Jog for 30 minutes every morning', 1, 'BRONZE'),
       (2, 'Read Books', 'Read for 1 hour before bed', 1, 'SILVER'),
       (3, 'Meditate', 'Meditate for 15 minutes daily', 2, 'GOLD');

-- Habit Formation Stages
INSERT INTO habit_formation_stages (id, stage, feedback, habit_id)
VALUES (1, 1, 'Great start! Keep it up.', 1),
       (2, 2, 'You''re making progress!', 2),
       (3, 3, 'You''re doing fantastic!', 3);

-- Habit Formation Stage Questions
INSERT INTO habit_formation_stage_entity_questions (habit_formation_stage_entity_id, questions)
VALUES (1, 'How do you feel after jogging?'),
       (1, 'What time do you usually jog?'),
       (2, 'What genre of books do you enjoy?'),
       (2, 'How many pages do you read per day?'),
       (3, 'What meditation technique do you use?'),
       (3, 'How do you feel after meditation?');

-- Habit Formation Stage Answers
INSERT INTO habit_formation_stage_entity_answers (habit_formation_stage_entity_id, answers)
VALUES (1, 'Energized and ready for the day'),
       (1, 'Usually around 6 AM'),
       (2, 'I enjoy fiction and self-help books'),
       (2, 'I try to read about 30 pages per day'),
       (3, 'I use mindfulness meditation'),
       (3, 'I feel calm and focused');

-- Habit Trackings
INSERT INTO habit_trackings (id, completed_date, habit_id)
VALUES (1, '2024-09-01', 1),
       (2, '2024-09-02', 1),
       (3, '2024-09-03', 1),
       (4, '2024-09-01', 2),
       (5, '2024-09-02', 2),
       (6, '2024-09-01', 3),
       (7, '2024-09-02', 3),
       (8, '2024-09-03', 3),
       (9, '2024-09-04', 3);
