-- 150M
SELECT reltuples, relname FROM pg_catalog.pg_class WHERE relname = 'attributes_copy';

-- 1m 31s
ALTER TABLE attributes_copy  ADD CONSTRAINT fk_attributes_1 FOREIGN KEY (type) REFERENCES attribute_types (id);

-- 1m 31s
ALTER TABLE attributes_copy  ADD CONSTRAINT fk_attributes_2 FOREIGN KEY (owner) REFERENCES representations (id);

ALTER TABLE attributes_copy
    ADD CONSTRAINT fk_attributes_1 FOREIGN KEY (type) REFERENCES attribute_types (id),
    ADD CONSTRAINT fk_attributes_2 FOREIGN KEY (owner) REFERENCES representations (id);

ALTER TABLE attributes_copy DISABLE TRIGGER ALL;
ALTER TABLE attributes_copy ENABLE TRIGGER ALL;

-- attributes 4 indexes total time: 780s  1.3 sec per 1M index

-- 5m 17s
create index idx_attributes_1 on attributes_copy (id) where (((attr_type)::text = 'SA'::text) AND (expression_long IS NOT NULL));

-- 3m 34s
create index idx_attributes_2 on attributes_copy (owner, type);

-- 2m 7s
create index idx_attributes_3 on attributes_copy (type);

-- 2m 12s
create index idx_attributes_4 on attributes_copy (owner);

-- 25M
SELECT reltuples, relname FROM pg_catalog.pg_class WHERE relname = 'attributes_flat';

-- 1m 2s
create index idx_attributes_flat_1 on attributes_flat (owner);

-- relations 6 indexes  total time : 6 * 75M * 1.3 =  585s

--create index idx_relations_source_target    on relations (source, target);
--create index idx_relations_type_source    on relations (type, source);
--create index relation_source    on relations (source);
--create index idx_relations_type_target    on relations (type, target);
--create index relation_target    on relations (target);
--create index relation_type    on relations (type);

-- representations 16 indexes 16 * 25M * 1.3 = 520s

--create unique index idx_representations_signifier_vocabulary    on representations (signifier, vocabulary);
--create index repr_creationdate    on representations (creationdate);
--create index repr_rep_type    on representations (rep_type);
--create index idx_repr_signifier_lower    on representations (lower(signifier::text));
--create index idx_repr_display_name_lower    on representations (lower(display_name::text));
--create index idx_repr_display_name_id    on representations (display_name, id);
--create index term_signifier    on representations (signifier, id);
--create index repr_creator    on representations (creator);
--create index repr_status    on representations (status);
--create index repr_type    on representations (asset_type);
--create index repr_vocabulary    on representations (vocabulary);
--create index idx_representations_rl_asset_type_id    on representations (asset_type, id)    where ((rep_type)::text = 'RL'::text);
--create index idx_representations_wd_asset_type_id    on representations (asset_type, id)    where ((rep_type)::text = 'WD'::text);
--create index idx_representations_cr_asset_type_id    on representations (asset_type, id)    where ((rep_type)::text = 'CR'::text);
--create index idx_representations_vocabulary_display_name_id    on representations (vocabulary, display_name, id);
--create index idx_representations_asset_type_display_name_id    on representations (asset_type, display_name, id);

