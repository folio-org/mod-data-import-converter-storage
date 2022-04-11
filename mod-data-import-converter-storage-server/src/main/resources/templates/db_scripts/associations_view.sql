-- Custom script to create associations_view view. Changes in this file will not result in an update of the view.
-- To change the associations_view view, update this script and copy it to the appropriate scripts.snippet field of the schema.json

CREATE OR REPLACE VIEW associations_view
  AS
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail, react_to, job_profile_id
    FROM get_profile_association_snapshot('job_to_match_profiles', 'job_profiles', 'JOB_PROFILE', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('job_to_action_profiles', 'job_profiles', 'JOB_PROFILE', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('match_to_match_profiles', 'match_profiles', 'MATCH_PROFILE', 'match_profiles', 'MATCH_PROFILE')
     UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('match_to_action_profiles', 'match_profiles','MATCH_PROFILE', 'action_profiles', 'ACTION_PROFILE')
     UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('action_to_match_profiles', 'action_profiles', 'ACTION_PROFILE', 'match_profiles', 'MATCH_PROFILE')
      UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('action_to_action_profiles', 'action_profiles', 'ACTION_PROFILE', 'action_profiles', 'ACTION_PROFILE')
      UNION ALL
    SELECT association_id, master_id, master, master_type, detail_id, detail_type, detail_order, detail,  react_to, job_profile_id
    FROM get_profile_association_snapshot('action_to_mapping_profiles', 'action_profiles', 'ACTION_PROFILE', 'mapping_profiles', 'MAPPING_PROFILE')
      UNION ALL
    SELECT ASSOCIATION.ID AS association_id, CAST(ASSOCIATION.JSONB ->> 'jobProfileId' AS UUID) AS master_id, json_agg(JOB.jsonb) AS master, 'JOB_PROFILE' AS master_type, DETAIL.ID AS detail_id, 'ACTION_PROFILE' AS detail_type, 0 AS detail_order, CAST( MASTER.JSONB AS JSON ) AS detail, null AS react_to, CAST(ASSOCIATION.JSONB ->> 'jobProfileId' AS UUID) AS job_profile_id
    FROM MATCH_TO_ACTION_PROFILES ASSOCIATION
    INNER JOIN MATCH_PROFILES MASTER ON MASTER.ID = ASSOCIATION.MASTERPROFILEID
    INNER JOIN ACTION_PROFILES DETAIL ON DETAIL.ID = ASSOCIATION.DETAILPROFILEID
    INNER JOIN JOB_PROFILES JOB ON JOB.ID = CAST(ASSOCIATION.JSONB ->> 'jobProfileId' AS UUID)
    WHERE ASSOCIATION.JSONB ->> 'detailProfileType' = 'ACTION_PROFILE'
    	AND ASSOCIATION.JSONB ->> 'masterProfileType' = 'MATCH_PROFILE'
    GROUP BY ASSOCIATION.ID, MASTER.ID, DETAIL.ID, DETAIL.JSONB;

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
