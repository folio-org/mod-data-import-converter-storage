INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('6cb347c6-c0b0-4363-89fc-32cedede87ba', '{
	"id": "6cb347c6-c0b0-4363-89fc-32cedede87ba",
	"name": "quickMARC - Default Update holdings",
	"description": "This job profile is used by the quickMARC to allow a user to update an SRS MARC holdings record and corresponding Inventory holdings. Profile cannot be edited or deleted",
	"deleted": false,
	"hidden": true,
	"dataType": "MARC",
	"metadata": {
		"createdDate": "2022-04-19T09:07:47.667",
		"updatedDate": "2022-04-19T09:09:10.382+0000",
		"createdByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575",
		"updatedByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"childProfiles": [],
	"parentProfiles": []
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
('7e24a466-349b-451d-a18e-38fb21d71b38',
'{
	"id": "7e24a466-349b-451d-a18e-38fb21d71b38",
	"name": "quickMARC - Default Update MARC holdings",
	"action": "UPDATE",
	"deleted": false,
	"hidden": true,
	"metadata": {
		"createdDate": "2020-11-30T09:02:39.96",
		"updatedDate": "2020-11-30T11:57:24.083+0000",
		"createdByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575",
		"updatedByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "Updates existing SRS MARC holdings record",
	"folioRecord": "MARC_HOLDINGS",
	"childProfiles": [],
	"parentProfiles": []
}
') ON CONFLICT DO NOTHING;


INSERT INTO ${myuniversity}_${mymodule}.match_profiles (id, jsonb) values
('2a599369-817f-4fe8-bae2-f3e3987990fe',
'{
	"id": "2a599369-817f-4fe8-bae2-f3e3987990fe",
	"name": "quickMARC - Default match for existing SRS holdings",
	"deleted": false,
	"hidden": true,
	"metadata": {
		"createdDate": "2020-11-30T09:06:01.52",
		"updatedDate": "2020-11-30T09:59:01.248+0000",
		"createdByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575",
		"updatedByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "Matches the UUID from the 999 ff $s in the incoming MARC record to the same field in any SRS MARC holdings",
	"matchDetails": [{
			"matchCriterion": "EXACTLY_MATCHES",
			"existingRecordType": "MARC_HOLDINGS",
			"incomingRecordType": "MARC_HOLDINGS",
			"existingMatchExpression": {
				"fields": [{
						"label": "field",
						"value": "999"
					}, {
						"label": "indicator1",
						"value": "f"
					}, {
						"label": "indicator2",
						"value": "f"
					}, {
						"label": "recordSubfield",
						"value": "s"
					}
				],
				"dataValueType": "VALUE_FROM_RECORD"
			},
			"incomingMatchExpression": {
				"fields": [{
						"label": "field",
						"value": "999"
					}, {
						"label": "indicator1",
						"value": "f"
					}, {
						"label": "indicator2",
						"value": "f"
					}, {
						"label": "recordSubfield",
						"value": "s"
					}
				],
				"dataValueType": "VALUE_FROM_RECORD"
			}
		}
	],
	"childProfiles": [],
	"parentProfiles": [],
	"existingRecordType": "MARC_HOLDINGS",
	"incomingRecordType": "MARC_HOLDINGS"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
('b8a9ca7d-4a33-44d3-86e1-f7c6cb7b265f',
'{
	"id": "b8a9ca7d-4a33-44d3-86e1-f7c6cb7b265f",
	"name": "quickMARC - Default Update MARC holdings",
	"deleted": false,
	"hidden": true,
	"metadata": {
		"createdDate": "2020-11-30T09:02:06.555",
		"updatedDate": "2020-11-30T11:57:46.948+0000",
		"createdByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575",
		"updatedByUserId": "6a010e5b-5421-5b1c-9b52-568b37038575"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "Updates existing SRS MARC holdings record",
	"childProfiles": [],
	"mappingDetails": {
		"name": "marcHoldings",
		"recordType": "MARC_HOLDINGS",
		"mappingFields": [],
		"marcMappingOption": "UPDATE",
		"marcMappingDetails": []
	},
	"parentProfiles": [],
	"existingRecordType": "MARC_HOLDINGS",
	"incomingRecordType": "MARC_HOLDINGS",
	"marcFieldProtectionSettings": []
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_match_profiles (id, jsonb) values
('364ab86a-e11c-4dd2-9ad5-efadbe79347b',
'{
	"id": "364ab86a-e11c-4dd2-9ad5-efadbe79347b",
	"order": 0,
	"triggered": false,
	"detailProfileId": "2a599369-817f-4fe8-bae2-f3e3987990fe",
	"masterProfileId": "6cb347c6-c0b0-4363-89fc-32cedede87ba",
	"detailProfileType": "MATCH_PROFILE",
	"masterProfileType": "JOB_PROFILE"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_action_profiles (id, jsonb) values
('31a69e89-4872-435c-b593-15664146cc2b',
'{
	"id": "31a69e89-4872-435c-b593-15664146cc2b",
	"order": 0,
	"reactTo": "MATCH",
	"triggered": false,
	"jobProfileId": "6cb347c6-c0b0-4363-89fc-32cedede87ba",
	"detailProfileId": "7e24a466-349b-451d-a18e-38fb21d71b38",
	"masterProfileId": "2a599369-817f-4fe8-bae2-f3e3987990fe",
	"detailProfileType": "ACTION_PROFILE",
	"masterProfileType": "MATCH_PROFILE"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (id, jsonb) values
('96876af2-2a2b-40c0-9ffd-9538a5b39dd6',
'{
	"id": "96876af2-2a2b-40c0-9ffd-9538a5b39dd6",
	"order": 0,
	"triggered": false,
	"detailProfileId": "b8a9ca7d-4a33-44d3-86e1-f7c6cb7b265f",
	"masterProfileId": "7e24a466-349b-451d-a18e-38fb21d71b38",
	"detailProfileType": "MAPPING_PROFILE",
	"masterProfileType": "ACTION_PROFILE"
}') ON CONFLICT DO NOTHING;
