-- Spring Batch Metadata

CREATE TABLE BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT       NOT NULL PRIMARY KEY,
    VERSION         BIGINT,
    JOB_NAME        VARCHAR(100) NOT NULL,
    JOB_KEY         VARCHAR(32)  NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    VERSION          BIGINT,
    JOB_INSTANCE_ID  BIGINT NOT NULL,
    CREATE_TIME      DATETIME(6) NOT NULL,
    START_TIME       DATETIME(6) DEFAULT NULL,
    END_TIME         DATETIME(6) DEFAULT NULL,
    STATUS           VARCHAR(10),
    EXIT_CODE        VARCHAR(2500),
    EXIT_MESSAGE     VARCHAR(2500),
    LAST_UPDATED     DATETIME(6),
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
        references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,
    PARAMETER_NAME   VARCHAR(100) NOT NULL,
    PARAMETER_TYPE   VARCHAR(100) NOT NULL,
    PARAMETER_VALUE  VARCHAR(2500),
    IDENTIFYING      CHAR(1)      NOT NULL,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT       NOT NULL PRIMARY KEY,
    VERSION            BIGINT       NOT NULL,
    STEP_NAME          VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID   BIGINT       NOT NULL,
    CREATE_TIME        DATETIME(6) NOT NULL,
    START_TIME         DATETIME(6) DEFAULT NULL,
    END_TIME           DATETIME(6) DEFAULT NULL,
    STATUS             VARCHAR(10),
    COMMIT_COUNT       BIGINT,
    READ_COUNT         BIGINT,
    FILTER_COUNT       BIGINT,
    WRITE_COUNT        BIGINT,
    READ_SKIP_COUNT    BIGINT,
    WRITE_SKIP_COUNT   BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT     BIGINT,
    EXIT_CODE          VARCHAR(2500),
    EXIT_MESSAGE       VARCHAR(2500),
    LAST_UPDATED       DATETIME(6),
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
        references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ
(
    ID         BIGINT  NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY)
select *
from (select 0 as ID, '0' as UNIQUE_KEY) as tmp
where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ
(
    ID         BIGINT  NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY)
select *
from (select 0 as ID, '0' as UNIQUE_KEY) as tmp
where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ
(
    ID         BIGINT  NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY)
select *
from (select 0 as ID, '0' as UNIQUE_KEY) as tmp
where not exists(select * from BATCH_JOB_SEQ);

-- Users
INSERT INTO users (id, name, email)
VALUES (1, 'John Doe', 'wlscww@kakao.com'),
       (2, 'Jane Smith', 'wlscwww@gmail.com');

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
