UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
  "id": "80898dee-449f-44dd-9c8e-37d5eb469b1d",
  "name": "Default - Create Holdings and SRS MARC Holdings",
  "description": "Default job profile for creating MARC holdings and corresponding Inventory holdings. Profile cannot be edited or deleted",
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
WHERE id = '80898dee-449f-44dd-9c8e-37d5eb469b1d';

UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb =  '{
  "id": "8aa0b850-9182-4005-8435-340b704b2a19",
  "name": "Default - Create MARC holdings",
  "action": "CREATE",
  "deleted": false,
  "hidden": false,
  "description": "This action profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It cannot be edited, duplicated, or deleted.",
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
WHERE id = '8aa0b850-9182-4005-8435-340b704b2a19';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb =  '{
  "id": "13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a",
  "name": "Default Create MARC holdings and Inventory holdings",
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
  "description": "This field mapping profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It cannot be edited, duplicated, deleted, or linked to additional action profiles.",
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
        "enabled": "false",
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
        "enabled": "false",
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
        "enabled": "false",
        "subfields": []
      }, {
        "name": "effectiveLocationId",
        "path": "holdings.effectiveLocationId",
        "value": "",
        "enabled": "false",
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
        "enabled": "false",
        "subfields": []
      }, {
        "name": "acquisitionMethod",
        "path": "holdings.acquisitionMethod",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "receiptStatus",
        "path": "holdings.receiptStatus",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "illPolicyId",
        "path": "holdings.illPolicyId",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "retentionPolicy",
        "path": "holdings.retentionPolicy",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "digitizationPolicy",
        "path": "holdings.digitizationPolicy",
        "value": "",
        "enabled": "false",
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
        "enabled": "false",
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
        "enabled": "false",
        "subfields": []
      }, {
        "name": "statisticalCodeIds",
        "path": "holdings.statisticalCodeIds[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsItems",
        "path": "holdings.holdingsItems[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "bareHoldingsItems",
        "path": "holdings.bareHoldingsItems[]",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "holdingsInstance",
        "path": "holdings.holdingsInstance",
        "value": "",
        "enabled": "false",
        "subfields": []
      }, {
        "name": "sourceId",
        "path": "holdings.sourceId",
        "value": "",
        "enabled": "false",
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
WHERE id = '13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a';

