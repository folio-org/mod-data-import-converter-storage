INSERT INTO ${myuniversity}_${mymodule}.action_profiles (_id, jsonb) values
('738e3780-da45-451f-bd9f-f0b7a78c29a5', '{
	"id": "738e3780-da45-451f-bd9f-f0b7a78c29a5",
	"name": "Create holdings record",
	"description": "Create Law holdings record on the existing instance",
	"action": "Create Law holdings record",
	"mapping": "EDI orders",
	"tags": {
    "tagList": [
		  "Law", "holdings"
	  ]
	},
	"userInfo": {
      "firstName": "DIKU",
      "lastName": "ADMINISTRATOR",
      "userName": "diku_admin"
	},
	"metadata": {
      "createdDate": "2018-10-30T12:41:22.000",
      "createdByUserId": "",
      "createdByUsername": "",
      "updatedDate": "2018-11-02T12:09:51.000",
      "updatedByUserId": "",
      "updatedByUsername": ""
    }
}'),
('8176badd-bd7e-430d-aeb4-9f6ac342885f', '{
	"id": "8176badd-bd7e-430d-aeb4-9f6ac342885f",
	"name": "Update the Inventory Instance",
	"description": "Update the Inventory Instance, using the standard FOLIO-wide MARC-to-instance mappings plus customized Instance mappings",
	"action": "Update the Inventory Instance",
	"mapping": "EDI orders",
	"tags": {
    "tagList": [
		  "Inventory", "Instance"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('93691541-5fb7-4ef9-951d-dbb8d762f9bb', '{
	"id": "93691541-5fb7-4ef9-951d-dbb8d762f9bb",
	"name": "Create Bib",
	"description": "Create the Source Storage MARC Bib and link it to this existing Instance",
	"action": "Create MARC Bib",
	"mapping": "EDI orders",
	"tags": {
    "tagList": [
		  "Inventory", "Instance", "Bib"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('c9573d66-c5e3-4bf7-a6a0-00a80e92cad5', '{
	"id": "c9573d66-c5e3-4bf7-a6a0-00a80e92cad5",
	"name": "Merge MARC SRS bib",
	"description": "If match on bib, Merge MARC SRS bib, folding in the fields that are specified in the data import profile",
	"action": "Merge MARC SRS bib",
	"mapping": "Custom",
	"tags": {
    "tagList": [
		  "Merge", "bib"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4', '{
	"id": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
	"name": "Create an Inventory holding",
	"description": "Create an Inventory holding using MARC mapping. Location can either be mapped from a field in the record or it can be a constant value in the import profile, depending on the vendor setup.",
	"action": "Create an Inventory holding",
	"mapping": "EDI orders",
	"tags": {
    "tagList": [
		  "Create", "Inventory", "holding"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('4b20facc-fbe8-4523-8f18-04ae35126ba6', '{
	"id": "4b20facc-fbe8-4523-8f18-04ae35126ba6",
	"name": "Create an Order and order line record",
	"description": "Create an Order and order line record, using customized Order mappings, linked to that Inventory Instance, the Law Holdings, and the Law Item",
	"action": "Create an Order",
	"mapping": "EDI orders",
	"tags": {
    "tagList": [
		  "Create", "Order"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('c4ce10c8-10fc-4213-b5ca-8210fcc65da6', '{
	"id": "c4ce10c8-10fc-4213-b5ca-8210fcc65da6",
	"name": "Create instance and associated inventory",
	"description": "Create instance and associated inventory",
	"action": "Create instance",
	"mapping": "856",
	"tags": {
    "tagList": [
		  "Instance", "Inventory"
	  ]
	},
	"userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
	},
	"metadata": {
    "createdDate": "2018-10-30T12:41:22.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-02T12:09:51.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}') ON CONFLICT DO NOTHING;
