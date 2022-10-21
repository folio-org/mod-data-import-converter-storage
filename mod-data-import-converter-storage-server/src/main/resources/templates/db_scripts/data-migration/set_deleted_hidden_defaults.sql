-- job_profiles:
-- set default values: deleted and hidden to false
UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb = jsonb_set(jsonb, '{hidden}', 'false')
WHERE jsonb ->> 'hidden' is null;
UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb = jsonb_set(jsonb, '{deleted}', 'false')
WHERE jsonb ->> 'deleted' is null;

-- match_profiles:
-- set default values: deleted and hidden to false
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = jsonb_set(jsonb, '{hidden}', 'false')
WHERE jsonb ->> 'hidden' is null;
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = jsonb_set(jsonb, '{deleted}', 'false')
WHERE jsonb ->> 'deleted' is null;

-- action_profiles:
-- set default values: deleted and hidden to false
UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb = jsonb_set(jsonb, '{hidden}', 'false')
WHERE jsonb ->> 'hidden' is null;
UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb = jsonb_set(jsonb, '{deleted}', 'false')
WHERE jsonb ->> 'deleted' is null;

-- mapping_profiles:
-- set default values: deleted and hidden to false
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_set(jsonb, '{hidden}', 'false')
WHERE jsonb ->> 'hidden' is null;
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb_set(jsonb, '{deleted}', 'false')
WHERE jsonb ->> 'deleted' is null;