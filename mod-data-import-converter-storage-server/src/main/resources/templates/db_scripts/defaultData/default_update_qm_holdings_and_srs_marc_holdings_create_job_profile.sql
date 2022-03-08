UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
  "id": "fa0262c7-5816-48d0-b9b3-7b7a862a5bc7",
  "name": "quickMARC - Create Holdings and SRS MARC Holdings",
  "description": "This job profile is used by the quickMARC to allow a user to create a new SRS MARC holdings record and corresponding Inventory holdings. Profile cannot be edited or deleted",
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
WHERE id = 'fa0262c7-5816-48d0-b9b3-7b7a862a5bc7';

UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb =  '{
  "id": "f5feddba-f892-4fad-b702-e4e77f04f9a3",
  "name": "quickMARC Derive - Create Inventory Holdings",
  "action": "CREATE",
  "deleted": false,
  "hidden": false,
  "description": "This action profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It cannot be edited or deleted.",
  "folioRecord": "HOLDINGS",
  "childProfiles": [],
  "parentProfiles": [],
    "metadata": {
      "createdDate": "2021-08-05T14:00:00.000",
      "updatedDate": "2021-08-05T15:00:00.462+0000",
      "createdByUserId": "00000000-0000-0000-0000-000000000000",
      "updatedByUserId": "00000000-0000-0000-0000-000000000000"
    },
    "userInfo": {
      "lastName": "System",
      "userName": "System",
      "firstName": "System"
    }
}'
WHERE id = 'f5feddba-f892-4fad-b702-e4e77f04f9a3';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb =  '{
  "id": "e0fbaad5-10c0-40d5-9228-498b351dbbaa",
  "name": "quickMARC - Create MARC holdings and Inventory holdings",
  "deleted": false,
  "hidden": false,
  "metadata": {
    "createdDate": "2021-08-05T14:00:00.000",
    "updatedDate": "2021-08-05T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "description": "This field mapping profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It cannot be edited, duplicated, or deleted.",
  "childProfiles": [],
  "mappingDetails": {
    "name": "holdings",
    "recordType": "HOLDINGS",
    "mappingFields": [
      {
        "name": "hrid",
        "path": "holdings.hrid",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsTypeId",
        "path": "holdings.holdingsTypeId",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "discoverySuppress",
        "path": "holdings.discoverySuppress",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "formerIds",
        "path": "holdings.formerIds[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "instanceId",
        "path": "holdings.instanceId",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "permanentLocationId",
        "path": "holdings.permanentLocationId",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "temporaryLocationId",
        "path": "holdings.temporaryLocationId",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "effectiveLocationId",
        "path": "holdings.effectiveLocationId",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "callNumberTypeId",
        "path": "holdings.callNumberTypeId",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "callNumberPrefix",
        "path": "holdings.callNumberPrefix",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "callNumber",
        "path": "holdings.callNumber",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "callNumberSuffix",
        "path": "holdings.callNumberSuffix",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "shelvingTitle",
        "path": "holdings.shelvingTitle",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "acquisitionFormat",
        "path": "holdings.acquisitionFormat",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "acquisitionMethod",
        "path": "holdings.acquisitionMethod",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "receiptStatus",
        "path": "holdings.receiptStatus",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "illPolicyId",
        "path": "holdings.illPolicyId",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "retentionPolicy",
        "path": "holdings.retentionPolicy",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "digitizationPolicy",
        "path": "holdings.digitizationPolicy",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "copyNumber",
        "path": "holdings.copyNumber",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "numberOfItems",
        "path": "holdings.numberOfItems",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "electronicAccess",
        "path": "holdings.electronicAccess[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "notes",
        "path": "holdings.notes[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsStatements",
        "path": "holdings.holdingsStatements[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsStatementsForIndexes",
        "path": "holdings.holdingsStatementsForIndexes[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsStatementsForSupplements",
        "path": "holdings.holdingsStatementsForSupplements[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "receivingHistory",
        "path": "holdings.receivingHistory",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "statisticalCodeIds",
        "path": "holdings.statisticalCodeIds[]",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "holdingsItems",
        "path": "holdings.holdingsItems[]",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "bareHoldingsItems",
        "path": "holdings.bareHoldingsItems[]",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "holdingsInstance",
        "path": "holdings.holdingsInstance",
        "value": "",
        "enabled": "true",
        "subfields": []
      }, {
        "name": "sourceId",
        "path": "holdings.sourceId",
        "value": "",
        "enabled": "true",
        "subfields": []
      }
    ],
    "marcMappingDetails": []
  },
  "parentProfiles": [],
  "existingRecordType": "HOLDINGS",
  "incomingRecordType": "MARC_HOLDINGS",
  "marcFieldProtectionSettings": []
}'
WHERE id = 'e0fbaad5-10c0-40d5-9228-498b351dbbaa';
