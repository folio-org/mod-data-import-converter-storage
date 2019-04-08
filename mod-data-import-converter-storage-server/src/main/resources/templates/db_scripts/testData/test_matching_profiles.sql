INSERT INTO ${myuniversity}_${mymodule}.match_profiles (_id, jsonb) values
('cdf0ca3a-b515-4abd-82b6-48ce65374963', '{
  "id": "cdf0ca3a-b515-4abd-82b6-48ce65374963",
  "name": "Match ISBN",
  "description": "Match data element: ISBN",
  "match": "Order 998 -> Order ID",
  "tags": {
    "tagList": [
      "ISBN"
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
('a3510db6-5b3a-48ed-96c0-99a03df87b79', '{
  "id": "a3510db6-5b3a-48ed-96c0-99a03df87b79",
  "name": "Match PO number",
  "description": "Match data element: PO Line number in 9xx field",
  "match": "Order 981 -> Order ID",
  "tags": {
    "tagList": [
      "PO", "9xx"
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
 ('ab32efdb-43c2-4cb5-b7dc-49dd45c02106', '{
  "id": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "name": "Match Identifiers",
  "description": "Match data element: Identifiers (001, 010, 020, 035)",
  "match": "Order 663 -> Order ID",
  "tags": {
    "tagList": [
      "Identifiers", "001", "010", "020", "035"
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
('88bb7c9f-79f2-4a97-b600-535f1d913378', '{
  "id": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "name": "Match barcode number",
  "description": "Match data element: barcode number in the 9xx field",
  "match": "Order 191 -> Order ID",
  "tags": {
    "tagList": [
      "barcode", "9xx"
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
('ab05c370-7b9d-400f-962b-cb7953b940dd', '{
  "id": "ab05c370-7b9d-400f-962b-cb7953b940dd",
  "name": "Match OCLC number",
  "description": "Match OCLC number (may be one of several identifiers in the Instance record)",
  "match": "035",
  "tags": {
    "tagList": [
      "OCLC", "035"
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
('afe7eb12-ea47-4970-8d0e-981b988aed0c', '{
  "id": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "name": "UUID or HRID in 001 ",
  "description": "FOLIO UUID or HRID in 001 field",
  "match": "001",
  "tags": {
    "tagList": [
      "UUID", "HRID"
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
