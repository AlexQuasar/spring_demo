create table if not exists "user"
(
    id   serial  not null
        constraint user_pk
            primary key,
    name varchar not null
);

alter table "user"
    owner to postgres;

create unique index user_id_uindex
    on "user" (id);

create unique index user_name_uindex
    on "user" (name);

create table if not exists user_visit
(
    id            serial not null
        constraint user_visit_time_pk
            primary key,
    day           date,
    user_id       integer,
    url           varchar,
    time_spent    integer,
    time_interval interval
);

alter table user_visit
    owner to postgres;

create unique index user_visit_time_id_uindex
    on user_visit (id);