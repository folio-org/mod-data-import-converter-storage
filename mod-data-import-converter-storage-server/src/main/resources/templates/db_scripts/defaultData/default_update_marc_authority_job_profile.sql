UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
	"id": "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3",
  "name": "Default - Create SRS MARC Authority",
  "description": "Default job profile for creating MARC authority records. These records are stored in source record storage (SRS). Profile cannot be edited or deleted",
  "deleted": false,
  "dataType": "MARC",
  "tags": {
    "tagList": []
  },
  "childProfiles": [],
  "parentProfiles": [],
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "metadata": {
    "createdDate": "2021-03-16T15:00:00.000",
    "updatedDate": "2021-03-16T15:00:00.000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  }
}'
WHERE id = '6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3';

UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb =  '{
  "id": "7915c72e-c6af-4962-969d-403c7238b051",
  "name": "Default - Create MARC Authority",
  "action": "CREATE",
  "deleted": false,
  "description": "This action profile is used with FOLIO''s default job profile for creating a MARC authority record in source-record-storage. It cannot be edited, duplicated, or deleted.",
  "folioRecord": "AUTHORITY",
  "childProfiles": [],
  "parentProfiles": [],
    "metadata": {
      "createdDate": "2021-10-08T14:00:00.000",
      "updatedDate": "2021-10-08T15:00:00.462+0000",
      "createdByUserId": "00000000-0000-0000-0000-000000000000",
      "updatedByUserId": "00000000-0000-0000-0000-000000000000"
    },
    "userInfo": {
      "lastName": "System",
      "userName": "System",
      "firstName": "System"
    }
}'
WHERE id = '7915c72e-c6af-4962-969d-403c7238b051';
