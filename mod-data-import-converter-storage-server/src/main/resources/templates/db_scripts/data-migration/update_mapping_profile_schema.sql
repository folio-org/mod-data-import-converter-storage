-- rename field "folioRecord" to "existingRecordType"
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb - 'folioRecord' || jsonb_build_object('existingRecordType', jsonb ->> 'folioRecord');
