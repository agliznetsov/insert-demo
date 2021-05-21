drop index if exists idx_attributes_1;
drop index if exists idx_attributes_2;
drop index if exists idx_attributes_3;
drop index if exists idx_attributes_4;
drop table if exists attributes_copy;

create table attributes_copy
(
    id uuid not null primary key,
    type uuid not null,
    creator uuid,
    modifiedby uuid,
    owner uuid,
    creationdate bigint,
    lastmodified bigint,
    date_time bigint,
    numeric_value double precision,
    opt_lock integer,
    is_system boolean,
    boolean_value boolean,
    attr_type varchar(3) not null,
    expression_long text
);
