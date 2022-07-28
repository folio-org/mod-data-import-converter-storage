UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb =  '{
	"id": "fa45f3ec-9b83-11eb-a8b3-0242ac130003",
	"name": "Default - Create instance",
	"action": "CREATE",
	"deleted": false,
	"hidden": false,
	"metadata": {
		"createdDate": "2021-04-13T14:00:00.000",
		"updatedDate": "2021-04-13T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This action profile is used with FOLIO''s default job profile for creating Inventory Instances and SRS MARC Bibliographic records. It can be edited, duplicated, or deleted.",
	"folioRecord": "INSTANCE",
	"childProfiles": [],
	"parentProfiles": []
}'
WHERE id = 'fa45f3ec-9b83-11eb-a8b3-0242ac130003';

UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
	"id": "e34d7b92-9b83-11eb-a8b3-0242ac130003",
	"name": "Default - Create instance and SRS MARC Bib",
	"deleted": false,
	"hidden": false,
	"dataType": "MARC",
	"metadata": {
		"createdDate": "2021-04-13T14:00:00.000",
		"updatedDate": "2021-04-13T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This job profile creates SRS MARC Bib records and corresponding Inventory Instances using the library''s default MARC-to-Instance mapping. It can be edited, duplicated, or deleted.",
	"childProfiles": [],
	"parentProfiles": []
}'
WHERE id = 'e34d7b92-9b83-11eb-a8b3-0242ac130003';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb =  '{
	"id": "bf7b3b86-9b84-11eb-a8b3-0242ac130003",
	"name": "Default - Create instance",
	"deleted": false,
	"hidden": false,
	"metadata": {
		"createdDate": "2021-04-13T14:00:00.000",
		"updatedDate": "2021-04-13T15:00:00.462+0000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000"
	},
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "This field mapping profile is used with FOLIO''s default job profile for creating Inventory Instances and SRS MARC Bibliographic records. It can be edited, duplicated, deleted, or linked to additional action profiles.",
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
}'
WHERE id = 'bf7b3b86-9b84-11eb-a8b3-0242ac130003';
