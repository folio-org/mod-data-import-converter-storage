UPDATE ${myuniversity}_${mymodule}.job_profiles
SET jsonb =  '{
  "id": "d0ebb7b0-2f0f-11eb-adc1-0242ac120002",
  "name": "Inventory Single Record - Default Create Instance",
  "description": "Triggered by an action in Inventory, this job profile imports a single record from an external system, to create an Instance and MARC record",
  "dataType": "MARC",
  "tags": {
    "tagList": []
  },
  "deleted": false,
  "hidden": false,
  "userInfo": {
    "firstName": "System",
    "lastName": "System",
    "userName": "System"
  },
  "childProfiles": [],
  "parentProfiles": [],
  "metadata": {
    "createdDate": "2020-11-23T12:00:00.000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "createdByUsername": "System",
    "updatedDate": "2020-11-24T12:00:00.000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUsername": "System"
  }
}'
WHERE id = 'd0ebb7b0-2f0f-11eb-adc1-0242ac120002';

UPDATE ${myuniversity}_${mymodule}.action_profiles
SET jsonb =  '{
	"id": "d0ebba8a-2f0f-11eb-adc1-0242ac120002",
	"name": "Inventory Single Record - Default Create Instance",
	"action": "CREATE",
	"deleted": false,
	"hidden": false,
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "Creates new Inventory Instances and SRS MARC Bib records based on Inventory single record imports",
	"folioRecord": "INSTANCE",
	"childProfiles": [],
	"parentProfiles": [],
	"metadata": {
        "createdDate": "2020-11-23T12:00:00.000",
        "createdByUserId": "00000000-0000-0000-0000-000000000000",
        "createdByUsername": "System",
        "updatedDate": "2020-11-24T12:00:00.000",
        "updatedByUserId": "00000000-0000-0000-0000-000000000000",
        "updatedByUsername": "System"
	}
}'
WHERE id = 'd0ebba8a-2f0f-11eb-adc1-0242ac120002';

UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb =  '{
	"id": "d0ebbc2e-2f0f-11eb-adc1-0242ac120002",
	"name": "Inventory Single Record - Default Create Instance",
	"deleted": false,
	"hidden": false,
	"userInfo": {
		"lastName": "System",
		"userName": "System",
		"firstName": "System"
	},
	"description": "Creates new Inventory Instances and SRS MARC Bib records based on Inventory single record imports",
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
	"marcFieldProtectionSettings": [],
	"metadata": {
		"createdDate": "2020-11-23T12:00:00.000",
		"createdByUserId": "00000000-0000-0000-0000-000000000000",
		"createdByUsername": "System",
		"updatedDate": "2020-11-24T12:00:00.000",
		"updatedByUserId": "00000000-0000-0000-0000-000000000000",
		"updatedByUsername": "System"
  }
}'
WHERE id = 'd0ebbc2e-2f0f-11eb-adc1-0242ac120002';
