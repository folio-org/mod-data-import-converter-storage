UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
	"id": "6eefa4c6-bbf7-4845-ad82-de7fc5abd0e3",
  "name": "Default - Create SRS MARC Authority",
  "description": "Default job profile for creating MARC authority records. These records are stored in source record storage (SRS). Profile cannot be deleted.",
  "deleted": false,
  "hidden": false,
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
  "hidden": false,
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

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb =  '{
  "id": "6a0ec1de-68eb-4833-bdbf-0741db25c314",
  "name": "Default - Create MARC Authority",
  "deleted": false,
  "hidden": false,
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
  "description": "This field mapping profile is used with FOLIO''s default job profile for creating MARC Authority records. It cannot be edited, duplicated, deleted, or linked to additional action profiles.",
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
}'
WHERE id = '6a0ec1de-68eb-4833-bdbf-0741db25c314';
