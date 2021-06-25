-- job_profiles:
-- revert change values: from MARC_BIB to MARC
UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb = jsonb_set(jsonb, '{dataType}', '"MARC"')
WHERE jsonb ->> 'dataType' IN ('MARC_BIB', 'MARC_AUTHORITY');
