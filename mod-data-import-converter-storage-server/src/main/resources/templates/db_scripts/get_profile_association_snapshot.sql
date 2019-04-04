-- Custom script to create get_profile_association_snapshot function. Changes in this file will not result in an update of the function.
-- To change the get_profile_association_snapshot function, update this script and copy it to the appropriate scripts.snippet field of the schema.json


CREATE OR REPLACE FUNCTION
  get_profile_association_snapshot(_association_table text, _master_table text, _detail_table text, _detail_type text)
  RETURNS
  TABLE(association_id uuid, master_id uuid, detail_id uuid, detail_type text, detail json)
   AS $$
      BEGIN
        RETURN query
          EXECUTE format('
          SELECT
            association._id,
            association.masterprofileid AS master_id,
            association.detailprofileid AS detail_id,
            ''%s'' AS detail_type,
            json_agg(detail.jsonb) AS detail
          FROM %s association
          INNER JOIN %s master ON master._id = association.masterprofileid
          INNER JOIN %s detail ON detail._id = association.detailprofileid
          GROUP BY association._id',
          _detail_type, _association_table, _master_table, _detail_table);
      END
    $$
language plpgsql;
