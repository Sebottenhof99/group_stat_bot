CREATE TABLE GROUPS(
    GROUP_ID  integer PRIMARY KEY auto_increment,
    GROUP_INTERNAL_NAME varchar not null ,
    GROUP_CITY varchar not null,
    GROUP_CATEGORY  varchar not null,
    GROUP_ADDED_AT  DATE    not null,
    GROUP_ADDED_BY  varchar not null,
    IS_GROUP_PAUSED boolean default false
);


CREATE TABLE GROUP_MONTH_STATISTIC(
    STATISTIC_ID integer  PRIMARY KEY auto_increment,
    STATISTIC_GROUP_ID integer not null,
    STATISTIC_MEASURED_AT DATE not null ,
    STATISTIC_MEMBER_COUNT integer not null,
    constraint fk_group FOREIGN KEY (STATISTIC_GROUP_ID) REFERENCES GROUPS(GROUP_ID)
);