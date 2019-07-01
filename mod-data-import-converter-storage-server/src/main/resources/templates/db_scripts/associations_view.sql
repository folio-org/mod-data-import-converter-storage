-- Custom script to create associations_view view. Changes in this file will not result in an update of the view.
-- To change the associations_view view, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE VIEW associations_view
  AS
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('job_to_match_profiles', 'job_profiles', 'JOB_PROFILE', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id,master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('job_to_action_profiles', 'job_profiles', 'JOB_PROFILE', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id,master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('match_to_match_profiles', 'match_profiles', 'MATCH_PROFILE', 'match_profiles', 'MATCH_PROFILE')
     UNION ALL
    SELECT association_id, master_id,master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('match_to_action_profiles', 'match_profiles','MATCH_PROFILE', 'action_profiles', 'ACTION_PROFILE')
     UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_match_profiles', 'action_profiles', 'ACTION_PROFILE', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id,master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_action_profiles', 'action_profiles', 'ACTION_PROFILE', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail
    FROM get_profile_association_snapshot('action_to_mapping_profiles', 'action_profiles', 'ACTION_PROFILE', 'mapping_profiles', 'MAPPING_PROFILE');


-- Script to create rule which will triggered upon delete query to associations_view view.
CREATE OR REPLACE RULE delete_associations_with_details AS
  ON DELETE TO associations_view
  DO INSTEAD (
    DELETE FROM action_to_action_profiles WHERE action_to_action_profiles.masterprofileid = OLD.master_id;
    DELETE FROM action_to_mapping_profiles WHERE action_to_mapping_profiles.masterprofileid = OLD.master_id;
    DELETE FROM action_to_match_profiles WHERE action_to_match_profiles.masterprofileid = OLD.master_id;
    DELETE FROM job_to_action_profiles WHERE job_to_action_profiles.masterprofileid = OLD.master_id;
    DELETE FROM job_to_match_profiles WHERE job_to_match_profiles.masterprofileid = OLD.master_id;
    DELETE FROM match_to_action_profiles WHERE match_to_action_profiles.masterprofileid = OLD.master_id;
    DELETE FROM match_to_match_profiles WHERE match_to_match_profiles.masterprofileid = OLD.master_id;
  );
