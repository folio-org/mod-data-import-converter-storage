-- Custom script to create associations_view view. Changes in this file will not result in an update of the view.
-- To change the associations_view view, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE VIEW associations_view
  AS
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('job_to_match_profiles', 'job_profiles', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('job_to_action_profiles', 'job_profiles', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('match_to_match_profiles', 'match_profiles', 'match_profiles', 'MATCH_PROFILE')
     UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('match_to_action_profiles', 'match_profiles', 'action_profiles', 'ACTION_PROFILE')
     UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_match_profiles', 'action_profiles', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_action_profiles', 'action_profiles', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_mapping_profiles', 'action_profiles', 'mapping_profiles', 'MAPPING_PROFILE');
