INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3', '{
  "id": "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3",
  "name": "Default - Create SRS MARC Authority",
  "description": "Load MARC Authority to create SRS MARC Authority",
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
}') ON CONFLICT DO NOTHING;
