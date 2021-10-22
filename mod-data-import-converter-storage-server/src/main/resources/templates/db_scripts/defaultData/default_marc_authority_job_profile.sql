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

INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
  ('7915c72e-c6af-4962-969d-403c7238b051', '{
  "id": "7915c72e-c6af-4962-969d-403c7238b051",
  "name": "Default - Create Authorities",
  "action": "CREATE",
  "deleted": false,
  "description": "This action profile is used with FOLIO''s default job profile for creating Inventory Authorities and SRS MARC Authorities records. It can be edited, duplicated.",
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
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
  ('6a0ec1de-68eb-4833-bdbf-0741db25c314', '{
  "id": "6a0ec1de-68eb-4833-bdbf-0741db25c314",
  "name": "Default - Create authorities",
  "deleted": false,
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
  },
  "description": "This field mapping profile is used with FOLIO''s default job profile for creating Inventory Authorities and SRS MARC Authorities records. It can be edited, duplicated, deleted, or linked to additional action profiles.",
  "childProfiles": [],
  "mappingDetails": {
    "name": "authority",
    "recordType": "AUTHORITY",
    "mappingFields": [
      {
        "name": "personalName",
        "path": "authority.personalName",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sftPersonalName",
        "path": "authority.sftPersonalName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftPersonalName",
        "path": "authority.saftPersonalName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "corporateName",
        "path": "authority.corporateName",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sftCorporateName",
        "path": "authority.sftCorporateName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftCorporateName",
        "path": "authority.saftCorporateName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "meetingName",
        "path": "authority.meetingName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      },  {
        "name": "sftMeetingName",
        "path": "authority.sftMeetingName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftMeetingName",
        "path": "authority.saftMeetingName[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "uniformTitle",
        "path": "authority.uniformTitle",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sftUniformTitle",
        "path": "authority.sftUniformTitle[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftUniformTitle",
        "path": "authority.saftUniformTitle[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "topicalTerm",
        "path": "authority.topicalTerm",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sftTopicalTerm",
        "path": "authority.sftTopicalTerm[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftTopicalTerm",
        "path": "authority.saftTopicalTerm[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "subjectHeadings",
        "path": "authority.subjectHeadings",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "geographicName",
        "path": "authority.geographicName",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sftGeographicTerm",
        "path": "authority.sftGeographicTerm[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "saftGeographicTerm",
        "path": "authority.saftGeographicTerm[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "genre",
        "path": "authority.genre",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "identifiers",
        "path": "authority.identifiers[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "notes",
        "path": "authority.notes[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }
    ],
    "marcMappingDetails": []
  },
  "parentProfiles": [],
  "existingRecordType": "AUTHORITY",
  "incomingRecordType": "MARC_AUTHORITY",
  "marcFieldProtectionSettings": []
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_action_profiles (id, jsonb) values
  ('06914a99-9b58-493d-828b-4ff104ba7e49', '{
  "id": "06914a99-9b58-493d-828b-4ff104ba7e49",
  "order": 0,
  "triggered": false,
  "detailProfileId": "7915c72e-c6af-4962-969d-403c7238b051",
  "masterProfileId": "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3",
  "detailProfileType": "ACTION_PROFILE",
  "masterProfileType": "JOB_PROFILE"
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (id, jsonb) values
  ('329911fb-2835-476e-b85e-a7fcdc900f87', '{
  "id": "329911fb-2835-476e-b85e-a7fcdc900f87",
  "order": 0,
  "triggered": false,
  "detailProfileId": "6a0ec1de-68eb-4833-bdbf-0741db25c314",
  "masterProfileId": "7915c72e-c6af-4962-969d-403c7238b051",
  "detailProfileType": "MAPPING_PROFILE",
  "masterProfileType": "ACTION_PROFILE"
}')
ON CONFLICT DO NOTHING;
