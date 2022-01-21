INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
    ('6409dcff-71fa-433a-bc6a-e70ad38a9604', '{
	"id": "6409dcff-71fa-433a-bc6a-e70ad38a9604",
	"name": "quickMARC - Derive a new SRS MARC Bib and Instance",
	"deleted": false,
	"dataType": "MARC",
	"metadata": {
		"createdDate": "2021-01-14T14:00:00.000",
		"updatedDate": "2021-01-14T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This job profile is used by the quickMARC Derive action to create a new SRS MARC Bib record and corresponding Inventory Instance. It cannot be edited or deleted.",
	"childProfiles": [],
	"parentProfiles": []
}
') ON CONFLICT DO NOTHING;


INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
    ('f8e58651-f651-485d-aead-d2fa8700e2d1',
'{
	"id": "f8e58651-f651-485d-aead-d2fa8700e2d1",
	"name": "quickMARC Derive - Create Inventory Instance",
	"action": "CREATE",
	"deleted": false,
	"metadata": {
		"createdDate": "2021-01-14T14:00:00.000",
		"updatedDate": "2021-01-14T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This action profile is used by the quickMARC Derive action to create a new Inventory Instance. It cannot be edited or deleted.",
	"folioRecord": "INSTANCE",
	"childProfiles": [],
	"parentProfiles": []
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
    ('991c0300-44a6-47e3-8ea2-b01bb56a38cc',
    '{
	"id": "991c0300-44a6-47e3-8ea2-b01bb56a38cc",
	"name": "quickMARC Derive - Create Inventory Instance",
	"deleted": false,
	"metadata": {
		"createdDate": "2021-01-14T14:00:00.000",
		"updatedDate": "2021-01-14T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This field mapping profile is used by the quickMARC Derive action to create an Inventory Instance. It cannot be edited or deleted.",
	"childProfiles": [],
	"mappingDetails": {
		"name": "instance",
		"recordType": "INSTANCE",
		"mappingFields": [{
				"name": "discoverySuppress",
				"path": "instance.discoverySuppress",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "staffSuppress",
				"path": "instance.staffSuppress",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "previouslyHeld",
				"path": "instance.previouslyHeld",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "hrid",
				"path": "instance.hrid",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "source",
				"path": "instance.source",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "catalogedDate",
				"path": "instance.catalogedDate",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "statusId",
				"path": "instance.statusId",
				"value": "",
				"enabled": "true",
				"subfields": [],
				"acceptedValues": {
					"26f5208e-110a-4394-be29-1569a8c84a65": "Uncataloged",
					"2a340d34-6b70-443a-bb1b-1b8d1c65d862": "Other",
					"52a2ff34-2a12-420d-8539-21aa8d3cf5d8": "Batch Loaded",
					"9634a5ab-9228-4703-baf2-4d12ebc77d56": "Cataloged",
					"daf2681c-25af-4202-a3fa-e58fdf806183": "Temporary",
					"f5cc2ab6-bb92-4cab-b83f-5a3d09261a41": "Not yet assigned"
				}
			}, {
				"name": "modeOfIssuanceId",
				"path": "instance.modeOfIssuanceId",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "statisticalCodeIds",
				"path": "instance.statisticalCodeIds[]",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "title",
				"path": "instance.title",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "alternativeTitles",
				"path": "instance.alternativeTitles[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "indexTitle",
				"path": "instance.indexTitle",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "series",
				"path": "instance.series[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "precedingTitles",
				"path": "instance.precedingTitles[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "succeedingTitles",
				"path": "instance.succeedingTitles[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "identifiers",
				"path": "instance.identifiers[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "contributors",
				"path": "instance.contributors[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "publication",
				"path": "instance.publication[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "editions",
				"path": "instance.editions[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "physicalDescriptions",
				"path": "instance.physicalDescriptions[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "instanceTypeId",
				"path": "instance.instanceTypeId",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "natureOfContentTermIds",
				"path": "instance.natureOfContentTermIds[]",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "instanceFormatIds",
				"path": "instance.instanceFormatIds[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "languages",
				"path": "instance.languages[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "publicationFrequency",
				"path": "instance.publicationFrequency[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "publicationRange",
				"path": "instance.publicationRange[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "notes",
				"path": "instance.notes[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "electronicAccess",
				"path": "instance.electronicAccess[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "subjects",
				"path": "instance.subjects[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "classifications",
				"path": "instance.classifications[]",
				"value": "",
				"enabled": "false",
				"subfields": []
			}, {
				"name": "parentInstances",
				"path": "instance.parentInstances[]",
				"value": "",
				"enabled": "true",
				"subfields": []
			}, {
				"name": "childInstances",
				"path": "instance.childInstances[]",
				"value": "",
				"enabled": "true",
				"subfields": []
			}
		],
		"marcMappingDetails": []
	},
	"parentProfiles": [],
	"existingRecordType": "INSTANCE",
	"incomingRecordType": "MARC_BIBLIOGRAPHIC",
	"marcFieldProtectionSettings": []
}
') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_action_profiles (id, jsonb) values
    ('b168efb3-1443-400b-9bc6-bc7dc2d3050a',
'{
	"id": "b168efb3-1443-400b-9bc6-bc7dc2d3050a",
	"order": 0,
	"triggered": false,
	"detailProfileId": "f8e58651-f651-485d-aead-d2fa8700e2d1",
	"masterProfileId": "6409dcff-71fa-433a-bc6a-e70ad38a9604",
	"detailProfileType": "ACTION_PROFILE",
	"masterProfileType": "JOB_PROFILE"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (id, jsonb) values
    ('85375360-9430-4bb1-a64a-197aee7c9400',
'{
	"id": "85375360-9430-4bb1-a64a-197aee7c9400",
	"order": 0,
	"triggered": false,
	"detailProfileId": "991c0300-44a6-47e3-8ea2-b01bb56a38cc",
	"masterProfileId": "f8e58651-f651-485d-aead-d2fa8700e2d1",
	"detailProfileType": "MAPPING_PROFILE",
	"masterProfileType": "ACTION_PROFILE"
}') ON CONFLICT DO NOTHING;
