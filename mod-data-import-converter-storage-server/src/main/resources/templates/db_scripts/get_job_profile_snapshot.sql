-- Custom script to create get_job_profile_snapshot function. Changes in this file will not result in an update of the function.
-- To change the get_job_profile_snapshot function, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE FUNCTION get_job_profile_snapshot(jobProfileId uuid) RETURNS
TABLE(snapshot json)
  AS $$ WITH RECURSIVE recursive_snapshot AS (

      SELECT association_id, master_id, detail_id, detail_type, detail
      FROM snapshot_view
      WHERE snapshot_view.detail_id = jobProfileId

        UNION ALL

      SELECT snapshot_view.association_id,
      snapshot_view.master_id as master_id,
      snapshot_view.detail_id as detail_id,
      snapshot_view.detail_type as detail_type,
      snapshot_view.detail as detail
      FROM snapshot_view
      INNER JOIN recursive_snapshot ON snapshot_view.master_id = recursive_snapshot.detail_id)
      SELECT row_to_json(row) FROM recursive_snapshot row
    $$
LANGUAGE 'sql' VOLATILE;
