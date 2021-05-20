drop index if exists idx_attributes_1;
drop index if exists idx_attributes_2;
drop index if exists idx_attributes_3;
drop index if exists idx_attributes_4;
drop table if exists attributes_copy;

create table attributes_copy
(
    id uuid not null
        primary key,
    type uuid not null
        constraint fk_attributes_1
            references attribute_types,
    creator uuid,
    modifiedby uuid,
    owner uuid
        constraint fk_attributes_2
            references representations,
    creationdate bigint,
    lastmodified bigint,
    date_time bigint,
    numeric_value double precision,
    opt_lock integer,
    is_system boolean,
    boolean_value boolean,
    attr_type varchar(3) not null
        constraint check_attributes_1
            check ((attr_type)::text = ANY ((ARRAY['SCA'::character varying, 'BO'::character varying, 'VA'::character varying, 'NA'::character varying, 'DA'::character varying, 'SA'::character varying, 'DT'::character varying, 'MA'::character varying])::text[])),
    expression_long text
);

create index idx_attributes_1
    on attributes_copy (id)
    where (((attr_type)::text = 'SA'::text) AND (expression_long IS NOT NULL));

create index idx_attributes_2 on attributes_copy (owner, type);
create index idx_attributes_3 on attributes_copy (type);
create index idx_attributes_4 on attributes_copy (owner);

