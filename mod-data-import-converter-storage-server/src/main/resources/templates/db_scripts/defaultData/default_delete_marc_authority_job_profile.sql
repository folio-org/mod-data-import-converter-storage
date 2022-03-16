INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('1a338fcd-3efc-4a03-b007-394eeb0d5fb9', '{
  "id": "1a338fcd-3efc-4a03-b007-394eeb0d5fb9",
  "name": "Default - Delete MARC Authority",
  "description": "This job profile is used for deleting an MARC authority record via the MARC authority app. This profile deletes the authority record stored in source-record-storage and mod-inventory-storage. This job profile cannot be edited, duplicated, or deleted.",
  "dataType": "MARC",
  "deleted": false,
  "hidden": true,
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "parentProfiles": [],
  "childProfiles": [],
  "metadata": {
    "createdDate": "2022-02-16T15:00:00.000",
    "updatedDate": "2022-02-16T15:00:00.000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  }
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
  ('fabd9a3e-33c3-49b7-864d-c5af830d9990', '{
  "id": "fabd9a3e-33c3-49b7-864d-c5af830d9990",
  "name": "Default - Delete MARC Authority via MARC Authority app",
  "description": "This action profile is used with FOLIO''s default job profile for deleting an MARC authority record via the MARC authority app. This profile deletes the authority record stored in source-record-storage and mod-inventory-storage. This action profile cannot be edited, duplicated, or deleted.",
  "action": "DELETE",
  "folioRecord": "MARC_AUTHORITY",
  "deleted": false,
  "hidden": true,
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "parentProfiles": [],
  "childProfiles": [],
  "metadata": {
    "createdDate": "2022-02-16T14:00:00.000",
    "updatedDate": "2022-02-16T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  }
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_profiles (id, jsonb) values
('4be5d1d2-1f5a-42ff-a9bd-fc90609d94b6',
'{
  "id": "4be5d1d2-1f5a-42ff-a9bd-fc90609d94b6",
  "name": "Default - Delete MARC Authority",
  "description": "This match profile is used with FOLIO''s default job profile for deleting an MARC authority record via the MARC authority app. This profile deletes the authority record stored in source-record-storage and mod-inventory-storage. This match profile cannot be edited, duplicated, or deleted.",
  "incomingRecordType": "MARC_AUTHORITY",
  "existingRecordType": "MARC_AUTHORITY",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_AUTHORITY",
      "existingRecordType": "MARC_AUTHORITY",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "999"
          },
          {
            "label": "indicator1",
            "value": "f"
          },
          {
            "label": "indicator2",
            "value": "f"
          },
          {
            "label": "recordSubfield",
            "value": "s"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "999"
          },
          {
            "label": "indicator1",
            "value": "f"
          },
          {
            "label": "indicator2",
            "value": "f"
          },
          {
            "label": "recordSubfield",
            "value": "s"
          }
        ]
      }
    }
  ],
  "deleted": false,
  "hidden": true,
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "parentProfiles": [],
  "childProfiles": [],
  "metadata": {
    "createdDate": "2022-02-16T15:00:00.000",
    "updatedDate": "2022-02-16T15:00:00.000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  }
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_match_profiles (id, jsonb) values
('644e53c2-7be2-4ae5-bc17-131334222d39',
'{
	"id": "644e53c2-7be2-4ae5-bc17-131334222d39",
	"order": 0,
	"triggered": false,
	"detailProfileId": "4be5d1d2-1f5a-42ff-a9bd-fc90609d94b6",
	"masterProfileId": "1a338fcd-3efc-4a03-b007-394eeb0d5fb9",
	"detailProfileType": "MATCH_PROFILE",
	"masterProfileType": "JOB_PROFILE"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_action_profiles (id, jsonb) values
('e0fd6684-fa34-4493-9048-a9e01c58f782',
'{
    "id": "e0fd6684-fa34-4493-9048-a9e01c58f782",
    "masterProfileId": "4be5d1d2-1f5a-42ff-a9bd-fc90609d94b6",
    "detailProfileId": "fabd9a3e-33c3-49b7-864d-c5af830d9990",
    "order": 0,
    "reactTo": "MATCH",
    "triggered": false,
    "masterProfileType": "MATCH_PROFILE",
    "detailProfileType": "ACTION_PROFILE",
    "jobProfileId": "1a338fcd-3efc-4a03-b007-394eeb0d5fb9"
}
') ON CONFLICT DO NOTHING;
