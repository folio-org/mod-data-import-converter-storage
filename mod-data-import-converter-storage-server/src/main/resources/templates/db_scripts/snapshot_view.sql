-- Custom script to create snapshot_view view. Changes in this file will not result in an update of the view.
-- To change the snapshot_view view, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE VIEW snapshot_view
AS
  SELECT job_profiles._id as association_id, null as master_id, job_profiles._id as detail_id, 'JOB_PROFILE' detail_type, json_agg(job_profiles.jsonb) detail
  FROM job_profiles
  GROUP BY detail_id
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('job_to_match_profiles', 'job_profiles', 'match_profiles', 'MATCH_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('job_to_action_profiles', 'job_profiles', 'action_profiles', 'ACTION_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('match_to_match_profiles', 'match_profiles', 'match_profiles', 'MATCH_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('match_to_action_profiles', 'match_profiles', 'action_profiles', 'ACTION_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('action_to_match_profiles', 'action_profiles', 'match_profiles', 'MATCH_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('action_to_action_profiles', 'action_profiles', 'action_profiles', 'ACTION_PROFILE')
    UNION ALL
  SELECT association_id, master_id, detail_id, detail_type, detail
  FROM get_profile_association_snapshot('action_to_mapping_profiles', 'action_profiles', 'mapping_profiles', 'MAPPING_PROFILE')
