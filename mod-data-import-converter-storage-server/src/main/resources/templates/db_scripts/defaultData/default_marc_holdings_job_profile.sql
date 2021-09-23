INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('80898dee-449f-44dd-9c8e-37d5eb469b1d', '{
  "id": "80898dee-449f-44dd-9c8e-37d5eb469b1d",
  "name": "Default - Create Holdings and SRS MARC Holdings",
  "description": "Load MARC Holdings to create SRS MARC Holdings and Inventory Holdings",
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
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
('8aa0b850-9182-4005-8435-340b704b2a19', '{
  "id": "8aa0b850-9182-4005-8435-340b704b2a19",
  "name": "Default - Create Holdings",
  "action": "CREATE",
  "deleted": false,
  "description": "This action profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It can be edited, duplicated.",
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
}')
ON CONFLICT DO NOTHING;


INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
('13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a', '{
  "id": "13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a",
  "name": "Default - Create holdings",
  "deleted": false,
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
  "description": "This field mapping profile is used with FOLIO''s default job profile for creating Inventory Holdings and SRS MARC Holdings records. It can be edited, duplicated, deleted, or linked to additional action profiles.",
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
        "path": "holdings.formerIds",
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
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_action_profiles (id, jsonb) values
('866a90ce-53b2-4b7b-afb5-d3a564e5087e', '{
  "id": "866a90ce-53b2-4b7b-afb5-d3a564e5087e",
  "order": 0,
  "triggered": false,
  "detailProfileId": "8aa0b850-9182-4005-8435-340b704b2a19",
  "masterProfileId": "80898dee-449f-44dd-9c8e-37d5eb469b1d",
  "detailProfileType": "ACTION_PROFILE",
  "masterProfileType": "JOB_PROFILE"
}')
ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (id, jsonb) values
('42f66e86-cacb-479d-aa80-50a200d0b6b6', '{
  "id": "42f66e86-cacb-479d-aa80-50a200d0b6b6",
  "order": 0,
  "triggered": false,
  "detailProfileId": "13cf7adf-c7a7-4c2e-838f-14d0ac36ec0a",
  "masterProfileId": "8aa0b850-9182-4005-8435-340b704b2a19",
  "detailProfileType": "MAPPING_PROFILE",
  "masterProfileType": "ACTION_PROFILE"
}')
ON CONFLICT DO NOTHING;
