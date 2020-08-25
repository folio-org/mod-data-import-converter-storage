-- action_profiles:
-- change values: REPLACE and COMBINE to UPDATE
UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb = jsonb_set(jsonb, '{action}', '"UPDATE"')
WHERE jsonb ->> 'action' IN ('REPLACE', 'COMBINE');

-- profile_snapshots:
-- change values: REPLACE and COMBINE to UPDATE
WITH job_ids AS (
    SELECT match_to_action_profiles.jsonb ->> 'jobProfileId' as jobProfileId
    FROM ${myuniversity}_${mymodule}.match_to_action_profiles
    JOIN ${myuniversity}_${mymodule}.action_profiles ON match_to_action_profiles.detailprofileid = action_profiles.id
         AND action_profiles.jsonb ->> 'action' = 'UPDATE'
    UNION
    SELECT job_to_action_profiles.masterprofileid::text
    FROM ${myuniversity}_${mymodule}.job_to_action_profiles
    JOIN ${myuniversity}_${mymodule}.action_profiles ON job_to_action_profiles.detailprofileid = action_profiles.id
         AND action_profiles.jsonb ->> 'action' = 'UPDATE'
)
UPDATE ${myuniversity}_${mymodule}.profile_snapshots
SET jsonb = regexp_replace(jsonb::text, '\"action\": (\"REPLACE\"|\"COMBINE\")', '"action": "UPDATE"', 'g')::jsonb
WHERE profile_snapshots.jsonb ->> 'profileId' IN (SELECT * FROM job_ids);
