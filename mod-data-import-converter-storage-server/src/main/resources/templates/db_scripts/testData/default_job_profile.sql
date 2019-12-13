INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('22fafcc3-f582-493d-88b0-3c538480cd83', '{
  "id": "22fafcc3-f582-493d-88b0-3c538480cd83",
  "name": "Create MARC Bibs",
  "description": "Load MARC Bibs to create SRS MARC Bibs and Inventory instances",
  "dataType": "MARC",
  "tags": {
    "tagList": []
  },
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "metadata": {
    "createdDate": "2018-05-07T12:41:22.000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "createdByUsername": "System",
    "updatedDate": "2018-05-08T12:09:51.000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUsername": "System"
  }
}') ON CONFLICT DO NOTHING;
