INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
('cf6f2718-5aa5-482a-bba5-5bc9b75614da', '{
	"id": "cf6f2718-5aa5-482a-bba5-5bc9b75614da",
	"name": "quickMARC - Default Update instance",
	"description": "This job profile is used by the quickMARC to allow a user to update an SRS MARC bib record and corresponding Inventory instance. Profile cannot be edited or deleted",
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
('c2e2d482-9486-476e-a28c-8f1e303cbe1a',
'{
	"id": "c2e2d482-9486-476e-a28c-8f1e303cbe1a",
	"name": "quickMARC - Default Update MARC bib",
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
	"description": "Updates existing SRS MARC bib record",
	"folioRecord": "MARC_BIBLIOGRAPHIC",
	"childProfiles": [],
	"parentProfiles": []
}
') ON CONFLICT DO NOTHING;


INSERT INTO ${myuniversity}_${mymodule}.match_profiles (id, jsonb) values
('91cec42a-260d-4a8c-a9fb-90d9435ca2f4',
'{
	"id": "91cec42a-260d-4a8c-a9fb-90d9435ca2f4",
	"name": "quickMARC - Default match for existing SRS bib",
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
	"description": "Matches the UUID from the 999 ff $s in the incoming MARC record to the same field in any SRS MARC bib",
	"matchDetails": [{
			"matchCriterion": "EXACTLY_MATCHES",
			"existingRecordType": "MARC_BIBLIOGRAPHIC",
			"incomingRecordType": "MARC_BIBLIOGRAPHIC",
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
	"existingRecordType": "MARC_BIBLIOGRAPHIC",
	"incomingRecordType": "MARC_BIBLIOGRAPHIC"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
('39b265e1-c963-4e5f-859d-6e8c327a265c',
'{
	"id": "39b265e1-c963-4e5f-859d-6e8c327a265c",
	"name": "quickMARC - Default Update MARC bib",
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
	"description": "Updates existing SRS MARC bib record",
	"childProfiles": [],
	"mappingDetails": {
		"name": "marcBib",
		"recordType": "MARC_BIBLIOGRAPHIC",
		"mappingFields": [],
		"marcMappingOption": "UPDATE",
		"marcMappingDetails": []
	},
	"parentProfiles": [],
	"existingRecordType": "MARC_BIBLIOGRAPHIC",
	"incomingRecordType": "MARC_BIBLIOGRAPHIC",
	"marcFieldProtectionSettings": []
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_match_profiles (id, jsonb) values
('83477fa3-1db1-4088-af0e-3c5fccb7e337',
'{
	"id": "83477fa3-1db1-4088-af0e-3c5fccb7e337",
	"order": 0,
	"triggered": false,
	"detailProfileId": "91cec42a-260d-4a8c-a9fb-90d9435ca2f4",
	"masterProfileId": "cf6f2718-5aa5-482a-bba5-5bc9b75614da",
	"detailProfileType": "MATCH_PROFILE",
	"masterProfileType": "JOB_PROFILE"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_action_profiles (id, jsonb) values
('e5f754a0-1b44-487c-b037-bb68eebba383',
'{
	"id": "e5f754a0-1b44-487c-b037-bb68eebba383",
	"order": 0,
	"reactTo": "MATCH",
	"triggered": false,
	"jobProfileId": "cf6f2718-5aa5-482a-bba5-5bc9b75614da",
	"detailProfileId": "c2e2d482-9486-476e-a28c-8f1e303cbe1a",
	"masterProfileId": "91cec42a-260d-4a8c-a9fb-90d9435ca2f4",
	"detailProfileType": "ACTION_PROFILE",
	"masterProfileType": "MATCH_PROFILE"
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (id, jsonb) values
('4e2bf7bf-dee1-4e7a-9074-e2139ef3f031',
'{
	"id": "4e2bf7bf-dee1-4e7a-9074-e2139ef3f031",
	"order": 0,
	"triggered": false,
	"detailProfileId": "39b265e1-c963-4e5f-859d-6e8c327a265c",
	"masterProfileId": "c2e2d482-9486-476e-a28c-8f1e303cbe1a",
	"detailProfileType": "MAPPING_PROFILE",
	"masterProfileType": "ACTION_PROFILE"
}') ON CONFLICT DO NOTHING;
