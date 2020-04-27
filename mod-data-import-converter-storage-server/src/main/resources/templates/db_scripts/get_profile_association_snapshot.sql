-- Custom script to create get_profile_association_snapshot function. Changes in this file will not result in an update of the function.
-- To change the get_profile_association_snapshot function, update this script and copy it to the appropriate scripts.snippet field of the schema.json
CREATE OR REPLACE FUNCTION
  get_profile_association_snapshot(_association_table text, _master_table text, _master_type text, _detail_table text, _detail_type text)
  RETURNS TABLE(association_id uuid, master_id uuid, master json, master_type text, detail_id uuid, detail_type text, detail_order integer, detail json, react_to text, job_profile_id uuid)
    AS $$
      BEGIN
        RETURN query
          EXECUTE format('
            SELECT
              association.id,
              association.masterprofileid AS master_id,
              json_agg(master.jsonb) AS master,
              ''%s'' AS master_type,
              association.detailprofileid AS detail_id,
              ''%s'' AS detail_type,
              CAST(association.jsonb->>''order'' AS integer) AS detail_order,
              json_agg(detail.jsonb) AS detail,
              CAST(association.jsonb->>''reactTo'' AS text) as react_to,
              CAST(association.jsonb->>''jobProfileId'' AS uuid) as job_profile_id
            FROM %s association
              INNER JOIN %s master ON master.id = association.masterprofileid
              INNER JOIN %s detail ON detail.id = association.detailprofileid
            GROUP BY association.id',
            _master_type, _detail_type, _association_table, _master_table, _detail_table);
      END $$
language plpgsql;
