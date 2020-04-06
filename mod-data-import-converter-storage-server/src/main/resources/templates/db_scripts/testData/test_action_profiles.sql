INSERT INTO ${myuniversity}_${mymodule}.action_profiles (id, jsonb) values
('cf3d7393-8534-44fb-93fd-7495d220f4f6', '{
  "id": "cf3d7393-8534-44fb-93fd-7495d220f4f6",
  "name": "Create preliminary instance",
  "description": "Brief instance created by order, will be updated later",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('dce26cfa-4aa7-4e55-abf5-daba1e4acd37', '{
  "id": "dce26cfa-4aa7-4e55-abf5-daba1e4acd37",
  "name": "Create preliminary holding",
  "description": "Brief holding created by order, will be updated later",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
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
('397430e8-d9e1-4f10-9254-730601daf286', '{
  "id": "397430e8-d9e1-4f10-9254-730601daf286",
  "name": "Create preliminary item",
  "description": "Brief item created by order, will be updated later",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ITEM",
  "deleted": false,
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
('968cfe23-909e-422b-8a24-2387971b9d0b', '{
  "id": "968cfe23-909e-422b-8a24-2387971b9d0b",
  "name": "Create order",
  "description": "Order data in 9xx fields",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ORDER",
  "deleted": false,
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
('93691541-5fb7-4ef9-951d-dbb8d762f9bb', '{
  "id": "93691541-5fb7-4ef9-951d-dbb8d762f9bb",
  "name": "Create MARC Bib",
  "description": "Create new MARC bib",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('3d1d7155-02ad-46d3-8704-eb6eda2bd19a', '{
  "id": "3d1d7155-02ad-46d3-8704-eb6eda2bd19a",
  "name": "Update SRS MARC",
  "description": "Update the existing SRS MARC bib",
  "tags": {
    "tagList": [
      "acq-related", "shelfready"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('5db3d727-1269-4ae3-99da-418b01b8da47', '{
  "id": "5db3d727-1269-4ae3-99da-418b01b8da47",
  "name": "Update instance-SR",
  "description": "Update brief instance to full cataloging",
  "tags": {
    "tagList": [
      "acq-related", "shelfready"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('8d73d518-6b64-4bcd-a10c-cd9d87b3931d', '{
  "id": "8d73d518-6b64-4bcd-a10c-cd9d87b3931d",
  "name": "Update holdings-SR",
  "description": "Update brief holdings with vendor shelfready data",
  "tags": {
    "tagList": [
      "acq-related", "shelfready"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
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
('96db6b49-607a-4544-b7e1-60115145b69d', '{
  "id": "96db6b49-607a-4544-b7e1-60115145b69d",
  "name": "Update item-SR",
  "description": "Update brief item with vendor shelfready data",
  "tags": {
    "tagList": [
      "acq-related", "shelfready"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "ITEM",
  "deleted": false,
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
('3bb0b2b3-b683-4cfb-b52c-175355d7f30c', '{
  "id": "3bb0b2b3-b683-4cfb-b52c-175355d7f30c",
  "name": "Create MARC invoice",
  "description": "Create invoice from MARC 9xx data",
  "tags": {
    "tagList": [
      "acq-related", "marc-invoice"
    ]
  },
  "reactTo": "MATCH",
  "action": "CREATE",
  "folioRecord": "INVOICE",
  "deleted": false,
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
}'),
('c9573d66-c5e3-4bf7-a6a0-00a80e92cad5', '{
  "id": "c9573d66-c5e3-4bf7-a6a0-00a80e92cad5",
  "name": "Merge SRS MARC",
  "description": "Merge incoming and existing SRS and MARCcat MARC bibs",
  "tags": {
    "tagList": [
      "acq-related", "approvals"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('7bbc68b6-fcfc-4b7c-9d8d-e1ec8a831142', '{
  "id": "7bbc68b6-fcfc-4b7c-9d8d-e1ec8a831142",
  "name": "Create SRS MARC",
  "description": "Create new SRS MARC from ???",
  "tags": {
    "tagList": [
      "acq-related", "approvals"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('36d26d5d-720d-4ab0-954f-6b75dde517cb', '{
  "id": "36d26d5d-720d-4ab0-954f-6b75dde517cb",
  "name": "Create Instance-AP",
  "description": "Create new instance for approval plan purchases",
  "tags": {
    "tagList": [
      "acq-related", "approvals"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('28d5e15c-d357-424e-838c-01c40a5b14e6', '{
  "id": "28d5e15c-d357-424e-838c-01c40a5b14e6",
  "name": "Create holdings-AP",
  "description": "Create new holdings for approval plan purchases",
  "tags": {
    "tagList": [
      "acq-related", "approvals"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
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
('70a3fe3a-7704-480d-8a93-8f0b6a72fcb0', '{
  "id": "70a3fe3a-7704-480d-8a93-8f0b6a72fcb0",
  "name": "Create item-AP",
  "description": "Create new item record for approval plan purchases",
  "tags": {
    "tagList": [
      "acq-related", "approvals"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ITEM",
  "deleted": false,
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
('bed8da3b-22d0-4b41-94b5-03a709bebf59', '{
  "id": "bed8da3b-22d0-4b41-94b5-03a709bebf59",
  "name": "GOBI AP order from MARC",
  "description": "Create closed order for approval plan purchases",
  "tags": {
    "tagList": [
      "acq-related", "approvals", "orders"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ORDER",
  "deleted": false,
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
('ebfeecce-9ac2-447c-86d7-9509f1eac227', '{
  "id": "ebfeecce-9ac2-447c-86d7-9509f1eac227",
  "name": "GOBI AP invoice from MARC",
  "description": "Create invoice for approval plan purchases",
  "tags": {
    "tagList": [
      "acq-related", "approvals", "invoice"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INVOICE",
  "deleted": false,
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
('0f8843e9-9282-4175-af88-6a0afbf875f3', '{
  "id": "0f8843e9-9282-4175-af88-6a0afbf875f3",
  "name": "Update MARC URL",
  "description": "Replace the URL in the MARC record",
  "tags": {
    "tagList": [
      "url-updates"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('a2277721-0063-49aa-8643-3c7dbfd15c52', '{
  "id": "a2277721-0063-49aa-8643-3c7dbfd15c52",
  "name": "Update Instance URL",
  "description": "Replace the URL in the Instance record",
  "tags": {
    "tagList": [
      "url-updates"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('ae000c19-cac7-441f-bcc1-60d3b3f04ba7', '{
  "id": "ae000c19-cac7-441f-bcc1-60d3b3f04ba7",
  "name": "Replace Instance`s whole MARC",
  "description": "",
  "tags": {
    "tagList": [
      "replace-marc"
    ]
  },
  "reactTo": "MATCH",
  "action": "REPLACE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
}'),
('f8c44c83-3101-4b8f-ada0-f7f0e5fd986f', '{
  "id": "f8c44c83-3101-4b8f-ada0-f7f0e5fd986f",
  "name": "Replace whole MARC-strip 9xx",
  "description": "Remove 9xx fields before saving MARC",
  "tags": {
    "tagList": [
      "marc-modify"
    ]
  },
  "reactTo": "MATCH",
  "action": "MODIFY",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('f76e1b9c-b312-4fb5-9d6c-b1f855dced9a', '{
  "id": "f76e1b9c-b312-4fb5-9d6c-b1f855dced9a",
  "name": "Update holdings-ETL",
  "description": "Update holdings after adjusting outside of FOLIO",
  "tags": {
    "tagList": [
      "etl", "holdings"
    ]
  },
  "reactTo": "MATCH",
  "action": "COMBINE",
  "folioRecord": "MARC_HOLDINGS",
  "deleted": false,
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
('8eeabfcc-4c11-4ec7-b273-9c04dc0013bb', '{
  "id": "8eeabfcc-4c11-4ec7-b273-9c04dc0013bb",
  "name": "Replace authority MARC",
  "description": "",
  "tags": {
    "tagList": [
      "authorities"
    ]
  },
  "reactTo": "MATCH",
  "action": "REPLACE",
  "folioRecord": "MARC_AUTHORITY",
  "deleted": false,
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
('ba8d822f-97db-4794-8393-562a0b2cf755', '{
  "id": "ba8d822f-97db-4794-8393-562a0b2cf755",
  "name": "Create authority MARC",
  "description": "",
  "tags": {
    "tagList": [
      "authorities"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "MARC_AUTHORITY",
  "deleted": false,
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
('320b0d15-77a1-4723-9f85-e2c1a2db379b', '{
  "id": "320b0d15-77a1-4723-9f85-e2c1a2db379b",
  "name": "Create MARC DDA",
  "description": "",
  "tags": {
    "tagList": [
      "dda-related"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "MARC_BIBLIOGRAPHIC",
  "deleted": false,
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
('13cd54b7-47cb-4b47-8103-1c98b761f80d', '{
  "id": "13cd54b7-47cb-4b47-8103-1c98b761f80d",
  "name": "Create DDA instance",
  "description": "URL in holdings record",
  "tags": {
    "tagList": [
      "dda-related"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('51c8b6ac-0424-4c8d-9a53-7a810be50ff7', '{
  "id": "51c8b6ac-0424-4c8d-9a53-7a810be50ff7",
  "name": "Create DDA holdings",
  "description": "",
  "tags": {
    "tagList": [
      "dda-related"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
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
('bb917336-4150-4c13-95d5-6786da41e0ad', '{
  "id": "bb917336-4150-4c13-95d5-6786da41e0ad",
  "name": "Create Instance from Excel",
  "description": "No linked SRS MARC",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INSTANCE",
  "deleted": false,
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
('9cb7b00f-6e7a-4650-966b-c0ebdeaf05b1', '{
  "id": "9cb7b00f-6e7a-4650-966b-c0ebdeaf05b1",
  "name": "Create Holdings from Excel",
  "description": "",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
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
('35844d1b-b0f4-4f20-92da-f76e77602eb8', '{
  "id": "35844d1b-b0f4-4f20-92da-f76e77602eb8",
  "name": "Create Item from Excel",
  "description": "",
  "tags": {
    "tagList": [
      "acq-related", "preliminary"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ITEM",
  "deleted": false,
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
('f214089e-bd60-4711-aaa6-0001706e449e', '{
  "id": "f214089e-bd60-4711-aaa6-0001706e449e",
  "name": "Create Order from Excel",
  "description": "",
  "tags": {
    "tagList": [
      "acq-related", "orders"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "ORDER",
  "deleted": false,
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
}'),
('83d59d03-8692-4304-a530-30892c98c554', '{
  "id": "83d59d03-8692-4304-a530-30892c98c554",
  "name": "Create invoice",
  "description": "",
  "tags": {
    "tagList": [
      "acq-related", "edifact", "invoices"
    ]
  },
  "reactTo": "NON-MATCH",
  "action": "CREATE",
  "folioRecord": "INVOICE",
  "deleted": false,
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
}'),
('10694275-2341-4b2d-be25-7aa78d5bca41', '{
  "id": "10694275-2341-4b2d-be25-7aa78d5bca41",
  "name": "Demo Create Instance",
  "description": "",
  "action": "CREATE",
  "folioRecord": "INSTANCE",
  "deleted": false,
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "parentProfiles": [],
  "childProfiles": [],
  "metadata": {
    "createdDate": "2020-04-06T13:18:36.795+0000",
    "createdByUserId": "",
    "updatedDate": "2020-04-06T13:43:15.969+0000",
    "updatedByUserId": ""
  }
}'),
('4fc313a1-51af-4f47-a618-6fa8375fb54f', '{
  "id": "4fc313a1-51af-4f47-a618-6fa8375fb54f",
  "name": "Demo Create Holdings",
  "description": "",
  "action": "CREATE",
  "folioRecord": "HOLDINGS",
  "deleted": false,
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2020-04-06T14:51:00.575+0000",
    "createdByUserId": "",
    "updatedDate": "2020-04-06T14:51:00.575+0000",
    "updatedByUserId": ""
  }
}'),
('0cc8fcbf-b447-4dd3-8fd3-8387de90b3bd', '{
  "id": "0cc8fcbf-b447-4dd3-8fd3-8387de90b3bd",
  "name": "Demo Create Item",
  "description": "",
  "action": "CREATE",
  "folioRecord": "ITEM",
  "deleted": false,
  "userInfo": {
    "firstName": "DIKU",
    "lastName": "ADMINISTRATOR",
    "userName": "diku_admin"
  },
  "metadata": {
    "createdDate": "2020-04-06T14:52:13.928+0000",
    "createdByUserId": "",
    "updatedDate": "2020-04-06T14:52:13.928+0000",
    "updatedByUserId": ""
  }
}'
)ON CONFLICT DO NOTHING;
