-- mapping_profiles:
-- change values: set administrative notes for mapping profiles, which haven't one
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_insert(jsonb, '{mappingDetails,mappingFields,9}', '{"name": "administrativeNotes", "path": "instance.administrativeNotes[]", "value": "", "enabled": "true", "subfields": []}')
WHERE jsonb -> 'mappingDetails' ->> 'recordType' = 'INSTANCE' AND jsonb -> 'mappingDetails' ->> 'mappingFields' NOT LIKE '%"name": "administrativeNotes"%';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_insert(jsonb, '{mappingDetails,mappingFields,7}', '{"name": "administrativeNotes", "path": "item.administrativeNotes[]", "value": "", "enabled": "true", "subfields": []}')
WHERE jsonb -> 'mappingDetails' ->> 'recordType' = 'ITEM' AND jsonb -> 'mappingDetails' ->> 'mappingFields' NOT LIKE '%"name": "administrativeNotes"%';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_insert(jsonb, '{mappingDetails,mappingFields,5}', '{"name": "administrativeNotes", "path": "holdings.administrativeNotes[]", "value": "", "enabled": "true", "subfields": []}')
WHERE jsonb -> 'mappingDetails' ->> 'recordType' = 'HOLDINGS' AND jsonb -> 'mappingDetails' ->> 'mappingFields' NOT LIKE '%"name": "administrativeNotes"%';
