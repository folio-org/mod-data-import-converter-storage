INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('80898dee-449f-44dd-9c8e-37d5eb469b1d', '{
  "id": "80898dee-449f-44dd-9c8e-37d5eb469b1d",
  "name": "Default - Create SRS MARC Holdings",
  "description": "Load MARC Holdings to create SRS MARC Holdings",
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
