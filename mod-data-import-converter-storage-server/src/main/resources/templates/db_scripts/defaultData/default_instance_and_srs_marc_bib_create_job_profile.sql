INSERT INTO ${myuniversity}_${mymodule}.job_profiles (id, jsonb) values
    ('6409dcff-71fa-433a-bc6a-e70ad38a9604', '{
	"id": "6409dcff-71fa-433a-bc6a-e70ad38a9604",
	"name": "Default - Create instance and SRS MARC Bib",
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
	"description": "This job profile creates SRS MARC Bib records and corresponding Inventory Instances using the library''s default MARC-to-Instance mapping. It can be edited, duplicated, or deleted.",
	"childProfiles": [],
	"parentProfiles": []
}
') ON CONFLICT DO NOTHING;


INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
    ('f8e58651-f651-485d-aead-d2fa8700e2d1',
'{
	"id": "f8e58651-f651-485d-aead-d2fa8700e2d1",
	"name": "Default - Create instance",
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
	"description": "This action profile is used with FOLIO''s default job profile for creating Inventory Instances and SRS MARC Bibliographic records. It can be edited, duplicated, or deleted.",
	"folioRecord": "INSTANCE",
	"childProfiles": [],
	"parentProfiles": []
}') ON CONFLICT DO NOTHING;

INSERT INTO tern_mod_data_import_converter_storage.mapping_profiles (id, jsonb) values
    ('991c0300-44a6-47e3-8ea2-b01bb56a38cc',
    '{
	"id": "991c0300-44a6-47e3-8ea2-b01bb56a38cc",
	"name": "Default - Create instance",
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
}
'), 
('2ad182ee-75e8-4bdb-b859-530543a064dc',
    '{
  "id": "2ad182ee-75e8-4bdb-b859-530543a064dc",
  "name": "Default - GOBI monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for GOBI. Edit to add details specific to your library and invoices. If additional GOBI invoice profiles are needed, duplicate this one. If no GOBI invoice profile is needed, delete this one.",
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
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": false,
        "path": "invoice.status",
        "value": "Open",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": " MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "value": false,
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "value": true,
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "description",
        "enabled": true,
        "path": "invoice_line.description",
        "value": "{POL_title}; else IMD+L+050+[4-5]",
        "subfields": []
      },
      {
        "name": "poLineNumber",
        "enabled": true,
        "path": "invoice_line.poLineNumber",
        "value": " RFF+LI[2]",
        "subfields": []
      },
      {
        "name": "invoiceLineNumber",
        "enabled": false,
        "path": "invoice_line.invoiceLineNumber",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorRefNo",
        "enabled": true,
        "path": "invoice_line.vendorRefNo",
        "value": " RFF+SLI[2]",
        "subfields": []
      },
      {
        "name": "invoiceLineStatus",
        "enabled": false,
        "path": "invoice_line.invoiceLineStatus",
        "value": "",
        "subfields": []
      },
      {
        "name": "subscriptionInfo",
        "enabled": true,
        "path": "invoice_line.subscriptionInfo",
        "value": "",
        "subfields": []
      },
      {
        "name": "subscriptionStart",
        "enabled": true,
        "path": "invoice_line.subscriptionStart",
        "value": "",
        "subfields": []
      },
      {
        "name": "subscriptionEnd",
        "enabled": true,
        "path": "invoice_line.subscriptionEnd",
        "value": "",
        "subfields": []
      },
      {
        "name": "comment",
        "enabled": true,
        "path": "invoice_line.comment",
        "value": "",
        "subfields": []
      },
      {
        "name": "lineAccountingCode",
        "enabled": false,
        "path": "invoice_line.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountNumber",
        "enabled": true,
        "path": "invoice_line.accountNumber",
        "value": "",
        "subfields": []
      },
      {
        "name": "quantity",
        "enabled": true,
        "path": "invoice_line.quantity",
        "value": " QTY+47[2]",
        "subfields": []
      },
      {
        "name": "lineSubTotal",
        "enabled": true,
        "path": "invoice_line.subTotal",
        "value": " MOA+203[2]",
        "subfields": []
      },
      {
        "name": "releaseEncumbrance",
        "enabled": true,
        "path": "invoice_line.releaseEncumbrance",
        "value": true,
        "subfields": []
      },
      {
        "name": "fundDistributions",
        "enabled": true,
        "path": "invoice_line.fundDistributions[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "lineAdjustments",
        "enabled": true,
        "path": "invoice_line.adjustments[]",
        "value": "",
        "subfields": []
      }
    ]
  }
}') ON CONFLICT DO NOTHING;

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
