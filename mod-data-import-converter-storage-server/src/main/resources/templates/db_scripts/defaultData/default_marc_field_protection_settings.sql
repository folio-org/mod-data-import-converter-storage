INSERT INTO ${myuniversity}_${mymodule}.marc_field_protection_settings (id, jsonb) values
('2d706874-8a10-4d3e-a190-33c301d157e3', '{
  "id": "2d706874-8a10-4d3e-a190-33c301d157e3",
  "field": "001",
  "indicator1": "",
  "indicator2": "",
  "subfield": "",
  "data": "*",
  "source": "SYSTEM",
  "metadata":{
    "createdDate":"2020-08-13T14:44:00.000",
    "createdByUserId":"00000000-0000-0000-0000-000000000000",
    "createdByUsername":"System",
    "updatedDate":"2020-08-13T14:44:00.000",
    "updatedByUserId":"00000000-0000-0000-0000-000000000000",
    "updatedByUsername":"System"
  }
}'),
 ('82d0b904-f8b0-4cc2-b238-7d8cddef7b7e', '{
  "id": "82d0b904-f8b0-4cc2-b238-7d8cddef7b7e",
  "field": "999",
  "indicator1": "f",
  "indicator2": "f",
  "subfield": "*",
  "data": "*",
  "source": "SYSTEM",
  "metadata":{
    "createdDate":"2020-08-13T14:44:00.000",
    "createdByUserId":"00000000-0000-0000-0000-000000000000",
    "createdByUsername":"System",
    "updatedDate":"2020-08-13T14:44:00.000",
    "updatedByUserId":"00000000-0000-0000-0000-000000000000",
    "updatedByUsername":"System"
  }
}') ON CONFLICT DO NOTHING;
