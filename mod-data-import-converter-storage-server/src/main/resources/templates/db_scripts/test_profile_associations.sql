INSERT INTO ${myuniversity}_${mymodule}.job_to_match_profiles (_id, jsonb) values
('e9ff0afb-6a73-4ba7-a0db-4fb089d3041f', '{
  "id": "e9ff0afb-6a73-4ba7-a0db-4fb089d3041f",
  "masterProfileId": "448ae575-daec-49c1-8041-d64c8ed8e5b1",
  "detailProfileId": "cdf0ca3a-b515-4abd-82b6-48ce65374963",
  "order": "0",
  "triggered": "false"
}'),
('e1b1ba03-f5ec-43a9-b9b0-a6eef0b04e39', '{
  "id": "e1b1ba03-f5ec-43a9-b9b0-a6eef0b04e39",
  "masterProfileId": "bb689511-5365-4050-8084-a03d94728d88",
  "detailProfileId": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.job_to_action_profiles (_id, jsonb) values
('4fa14dab-3ed4-4e88-9586-6cf7a52438b4', '{
  "id": "4fa14dab-3ed4-4e88-9586-6cf7a52438b4",
  "masterProfileId": "295e28b4-aea2-4458-9073-385a31e1da05",
  "detailProfileId": "8176badd-bd7e-430d-aeb4-9f6ac342885f",
  "order": "0",
  "triggered": "false"
}'),
('c8f62bed-5c14-442c-9fae-bd239931b572', '{
  "id": "c8f62bed-5c14-442c-9fae-bd239931b572",
  "masterProfileId": "295e28b4-aea2-4458-9073-385a31e1da05",
  "detailProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_match_profiles (_id, jsonb) values
('a99a1d65-0796-44f0-9c7d-9e1ebd6b97c5', '{
  "id": "a99a1d65-0796-44f0-9c7d-9e1ebd6b97c5",
  "masterProfileId": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "detailProfileId": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "order": "1",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_action_profiles (_id, jsonb) values
('716612c2-14c9-49ac-80a2-fda3457f2695', '{
  "id": "716612c2-14c9-49ac-80a2-fda3457f2695",
  "masterProfileId": "cdf0ca3a-b515-4abd-82b6-48ce65374963",
  "detailProfileId": "738e3780-da45-451f-bd9f-f0b7a78c29a5",
  "order": "0",
  "triggered": "false"
}'),
('02fb38b3-c8f6-4660-ae5e-2a7bb3866d13', '{
  "id": "02fb38b3-c8f6-4660-ae5e-2a7bb3866d13",
  "masterProfileId": "a3510db6-5b3a-48ed-96c0-99a03df87b79",
  "detailProfileId": "93691541-5fb7-4ef9-951d-dbb8d762f9bb",
  "order": "0",
  "triggered": "false"
}'),
('f97a74a6-19bc-4bfa-8c3e-2dd80357e4d4', '{
  "id": "f97a74a6-19bc-4bfa-8c3e-2dd80357e4d4",
  "masterProfileId": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "detailProfileId": "c9573d66-c5e3-4bf7-a6a0-00a80e92cad5",
  "order": "0",
  "triggered": "false"
}'),
('0c0fbca5-d67b-4932-898b-3963802db42d', '{
  "id": "0c0fbca5-d67b-4932-898b-3963802db42d",
  "masterProfileId": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "detailProfileId": "c9573d66-c5e3-4bf7-a6a0-00a80e92cad5",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_action_profiles (_id, jsonb) values
('5e6068b2-a485-4e14-ad34-392fe2c6c10f', '{
  "id": "5e6068b2-a485-4e14-ad34-392fe2c6c10f",
  "masterProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "detailProfileId": "8176badd-bd7e-430d-aeb4-9f6ac342885f",
  "order": "0",
  "triggered": "false"
}'),
('fa07eef8-118f-4873-b9f8-40b7bba9916b', '{
  "id": "fa07eef8-118f-4873-b9f8-40b7bba9916b",
  "masterProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "detailProfileId": "4b20facc-fbe8-4523-8f18-04ae35126ba6",
  "order": "2",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_match_profiles (_id, jsonb) values
('83fcb7f1-3d9e-4925-9543-5987573e683d', '{
  "id": "83fcb7f1-3d9e-4925-9543-5987573e683d",
  "masterProfileId": "8176badd-bd7e-430d-aeb4-9f6ac342885f",
  "detailProfileId": "a3510db6-5b3a-48ed-96c0-99a03df87b79",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_mapping_profiles (_id, jsonb) values
('834366ba-b4ff-4404-ac10-a9003744e0fb', '{
  "id": "834366ba-b4ff-4404-ac10-a9003744e0fb",
  "masterProfileId": "738e3780-da45-451f-bd9f-f0b7a78c29a5",
  "detailProfileId": "354f753a-02f9-4cb4-a7ea-e9154979fb7a",
  "order": "0",
  "triggered": "false"
}'),
('9fabe787-aa70-4b23-9047-7466ae628b81', '{
  "id": "9fabe787-aa70-4b23-9047-7466ae628b81",
  "masterProfileId": "93691541-5fb7-4ef9-951d-dbb8d762f9bb",
  "detailProfileId": "ac82ec05-4c9d-41f9-bbe6-763992041909",
  "order": "0",
  "triggered": "false"
}'),
('f3361177-9a69-4e3c-a600-cdf97e102bac', '{
  "id": "f3361177-9a69-4e3c-a600-cdf97e102bac",
  "masterProfileId": "c9573d66-c5e3-4bf7-a6a0-00a80e92cad5",
  "detailProfileId": "452434f1-0df0-4aa1-8402-6d51ec96d6a6",
  "order": "0",
  "triggered": "false"
}'),
('eac6a251-7f7f-41f3-84f3-55f28996cf53', '{
  "id": "eac6a251-7f7f-41f3-84f3-55f28996cf53",
  "masterProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "detailProfileId": "79fc8de0-b094-43c5-89e0-4207420335f8",
  "order": "1",
  "triggered": "false"
}'),
('b59a628b-2e30-412c-a66e-0871f2eeb4e6', '{
  "id": "b59a628b-2e30-412c-a66e-0871f2eeb4e6",
  "masterProfileId": "4b20facc-fbe8-4523-8f18-04ae35126ba6",
  "detailProfileId": "f4f075c6-67c9-411e-af24-01ceecd81b4b",
  "order": "1",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

