-- mapping_profiles:
-- change values: set administrative notes for mapping profiles, which haven't one
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_insert(jsonb, '{mappingDetails,mappingFields,0}', '{"name": "administrativeNotes", "path": "instance.administrativeNotes[]", "value": "", "enabled": "true", "subfields": []}')
WHERE (jsonb ->> 'mappingDetails')::json ->> 'recordType' IN ('INSTANCE', 'ITEM', 'HOLDINGS') AND (jsonb ->> 'mappingDetails')::json ->> 'mappingFields' NOT LIKE '%"name": "administrativeNotes"%';
