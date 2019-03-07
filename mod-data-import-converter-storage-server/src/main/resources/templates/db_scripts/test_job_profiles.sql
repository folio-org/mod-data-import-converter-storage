INSERT INTO ${myuniversity}_${mymodule}.job_profiles (_id, jsonb) values
('448ae575-daec-49c1-8041-d64c8ed8e5b1', '{
  "id": "448ae575-daec-49c1-8041-d64c8ed8e5b1",
  "name": "Load vendor order records",
  "description": "Ordered on vendor site; load MARC to create bib, holdings, item, and order; keep MARC",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "acq", "daily"
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
('295e28b4-aea2-4458-9073-385a31e1da05', '{
  "id": "295e28b4-aea2-4458-9073-385a31e1da05",
  "name": "Load shelfready cataloging records",
  "description": "Overlay brief bibs, update holdings and item records, create invoice",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "cat", "weekly"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-10-30T12:42:18.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-03T10:01:30.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('bb689511-5365-4050-8084-a03d94728d88', ' {
  "id": "bb689511-5365-4050-8084-a03d94728d88",
  "name": "Approval plan records",
  "description": "Create bibs, holdings, items, orders, and invoices",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "acq", "cat", "weekly"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-10-30T12:45:33.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-11-03T11:22:30.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('3e705998-9169-4f31-b048-90ffdcbd24c1', '{
  "id": "3e705998-9169-4f31-b048-90ffdcbd24c1",
  "name": "Load KB eResource records",
  "description": "Create, update, delete existing eContent bib records",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "monthly", "KB"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-01T10:12:51.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-02T09:05:30.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('15426802-bb0d-4dfb-8eee-90f64fed0cf1', '{
  "id": "15426802-bb0d-4dfb-8eee-90f64fed0cf1",
  "name": "ETL Bib records",
  "description": "Reload bib records that were exported and updated outside of FOLIO",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": []
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-01T10:37:53.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-03T11:45:21.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('87e4ad58-d677-43dd-8b04-9795741b2103', '{
  "id": "87e4ad58-d677-43dd-8b04-9795741b2103",
  "name": "Loading authority records",
  "description": "Load new and updated MARC authority records",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "cat", "monthly"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-02T10:20:32.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-02T10:45:21.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('72af7eb7-d7e2-4d16-93ac-682b9a58a94c', ' {
  "id": "72af7eb7-d7e2-4d16-93ac-682b9a58a94c",
  "name": "DDA discovery records",
  "description": "Load DDA discovery MARC records to create bib and holdings",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "weekly", "DDA"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-02T10:18:44.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-03T14:20:21.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('4d1b5024-2c49-42bd-b781-4330d14cefb0', '{
  "id": "4d1b5024-2c49-42bd-b781-4330d14cefb0",
  "name": "Create orders from spreadsheets",
  "description": "Selector-provided spreadsheet creates instance, holdings, item, and order",
  "dataTypes": [
    "Delimited"
  ],
  "tags": {
    "tagList": [
      "acq"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-01T10:09:51.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-01T15:21:28.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('b32e79bc-01d9-4d31-bc08-a3621fcfc1aa', '{
  "id": "b32e79bc-01d9-4d31-bc08-a3621fcfc1aa",
  "name": "Load EDI Invoice",
  "description": "",
  "dataTypes": [
    "EDIFACT"
  ],
  "tags": {
    "tagList": [
      "acq"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-01T10:18:42.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-05T13:08:12.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}'),
('828a787c-bcf3-4c28-891a-9e6f3ba5068b', '{
  "id": "828a787c-bcf3-4c28-891a-9e6f3ba5068b",
  "name": "Load MARC, then throw away",
  "description": "Ordered on vendor site; load MARC to create bib, holdings, item, and order; then discard MARC",
  "dataTypes": [
    "MARC"
  ],
  "tags": {
    "tagList": [
      "acq", "daily"
    ]
  },
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2018-11-01T11:08:21.000",
    "createdByUserId": "",
    "createdByUsername": "",
    "updatedDate": "2018-12-01T19:01:41.000",
    "updatedByUserId": "",
    "updatedByUsername": ""
  }
}') ON CONFLICT DO NOTHING;
