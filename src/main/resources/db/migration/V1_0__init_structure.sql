CREATE TABLE GROUPS
(
    GROUP_ID            integer auto_increment primary key,
    GROUP_INTERNAL_NAME varchar   not null,
    GROUP_CITY          varchar   not null,
    GROUP_CATEGORY      varchar   not null,
    GROUP_ADDED_AT      timestamp not null,
    GROUP_ADDED_BY      varchar   not null
);


CREATE TABLE GROUP_MONTH_STATISTIC
(
    STATISTIC_ID          integer auto_increment primary key,
    STATISTIC_GROUP_ID    integer not null,
    STATISTIC_MEASURED_AT date    not null,
    STATISTIC_MEMBER_COUNT integer not null,
    constraint fk_group FOREIGN KEY (STATISTIC_GROUP_ID) REFERENCES GROUPS (GROUP_ID)
);