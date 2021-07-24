-- job_profiles:
-- change values: from MARC to MARC_BIB
UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb = jsonb_set(jsonb, '{dataType}', '"MARC_BIB"')
WHERE jsonb ->> 'dataType' IN ('MARC');

-- job_profiles:
-- change values: from MARC to MARC_AUTHORITY for default marc authority job
UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb = jsonb_set(jsonb, '{dataType}', '"MARC_AUTHORITY"')
WHERE jsonb ->> 'id' = '6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3';
