INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (_id, jsonb) values
('354f753a-02f9-4cb4-a7ea-e9154979fb7a', '{
	"id": "354f753a-02f9-4cb4-a7ea-e9154979fb7a",
	"name": "Test mapping profile№1",
	"description": "Lorem ipsum dolor sit amet",
	"mapped": "Order - 1 fields"
	"tags": {
    "tagList": [
		  "Lorem", "ipsum"
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
('ac82ec05-4c9d-41f9-bbe6-763992041909', '{
	"id": "ac82ec05-4c9d-41f9-bbe6-763992041909",
	"name": "Test mapping profile№2",
	"description": "Lorem ipsum dolor sit amet",
  "mapped": "Order - 2 fields"
	"tags": {
    "tagList": [
		  "Lorem", "ipsum"
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
('452434f1-0df0-4aa1-8402-6d51ec96d6a6', '{
	"id": "452434f1-0df0-4aa1-8402-6d51ec96d6a6",
	"name": "Test mapping profile№3",
	"description": "Lorem ipsum dolor sit amet",
	"mapped": "Order - 3 fields"
	"tags": {
    "tagList": [
		  "Lorem", "ipsum"
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
('79fc8de0-b094-43c5-89e0-4207420335f8', '{
	"id": "79fc8de0-b094-43c5-89e0-4207420335f8",
	"name": "Test mapping profile№4",
	"description": "Lorem ipsum dolor sit amet",
	"mapped": "Order - 4 fields"
	"tags": {
    "tagList": [
		  "Lorem", "ipsum"
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
