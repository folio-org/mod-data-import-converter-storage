-- Custom script to create get_job_profile_snapshot function. Changes in this file will not result in an update of the function.
-- To change the get_job_profile_snapshot function, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE FUNCTION get_job_profile_snapshot(jobProfileId uuid)
RETURNS TABLE(snapshot json)
  AS $$
    WITH RECURSIVE recursive_snapshot AS (
      SELECT
        job_profile.id AS association_id,
        CAST(NULL AS uuid) AS master_id,
        job_profile.id AS detail_id,
        'JOB_PROFILE' detail_type,
        0 AS detail_order,
        json_agg(job_profile.jsonb) detail,
        null AS react_to
      FROM job_profiles AS job_profile
      WHERE job_profile.id = jobProfileId
      GROUP BY job_profile.id
        UNION ALL
      SELECT
        associations_view.association_id,
        associations_view.master_id AS master_id,
        associations_view.detail_id AS detail_id,
        associations_view.detail_type AS detail_type,
        associations_view.detail_order AS detail_order,
        associations_view.detail AS detail,
        associations_view.react_to AS react_to
      FROM associations_view INNER JOIN recursive_snapshot ON associations_view.master_id = recursive_snapshot.detail_id)
    SELECT row_to_json(row) FROM recursive_snapshot row ORDER BY row.detail_order ASC
    $$
language 'sql' VOLATILE;
