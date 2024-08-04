CREATE TABLE STAT_USERS
(
    STAT_USER_ID              integer auto_increment primary key,
    STAT_USER_NAME            varchar   not null unique,
    STAT_USER_ADDED_AT        timestamp not null,
    STAT_USER_ADDED_BY        varchar   not null,
    STAT_USER_IS_ADMIN        boolean   not null,
    STAT_USER_HAS_READ_ACCESS boolean   not null,
    STAT_USER_IS_SUBSCRIBED   boolean   not null default false
);

INSERT INTO STAT_USERS(STAT_USER_NAME, STAT_USER_ADDED_AT, STAT_USER_ADDED_BY, STAT_USER_IS_ADMIN,
                       STAT_USER_HAS_READ_ACCESS)
VALUES ('f_zoidberg', '2024-08-03', 'f_zoidberg', true, true);


CREATE TABLE USER_ASSISTANT
(
    USER_ASSISTANT_ID                integer auto_increment primary key,
    USER_ASSISTANT_USER_ID           int not null,
    USER_ASSISTANT_ACTIVE_OPERATION  varchar,
    USER_ASSISTANT_LAST_GIVEN_ANSWER varchar,
    constraint fk_assistant_to_user FOREIGN KEY (USER_ASSISTANT_USER_ID) REFERENCES STAT_USERS (STAT_USER_ID) ON DELETE CASCADE
);