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
}'),
('ed9bb8c0-d584-4866-9dd0-7b72365cbcd7', '{
  "id": "ed9bb8c0-d584-4866-9dd0-7b72365cbcd7",
  "masterProfileId": "15426802-bb0d-4dfb-8eee-90f64fed0cf1",
  "detailProfileId": "ab05c370-7b9d-400f-962b-cb7953b940dd",
  "order": "0",
  "triggered": "false"
}'),
('f1345b9b-3a00-4419-8d07-2e9a61b8408f', '{
  "id": "f1345b9b-3a00-4419-8d07-2e9a61b8408f",
  "masterProfileId": "15426802-bb0d-4dfb-8eee-90f64fed0cf1",
  "detailProfileId": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "order": "1",
  "triggered": "false"
}'),
 ('7cf7bd62-a32c-4ea3-9e0e-e4d94ad09e71', '{
  "id": "7cf7bd62-a32c-4ea3-9e0e-e4d94ad09e71",
  "masterProfileId": "e83f0598-bbc7-437c-90e2-264c761539b9",
  "detailProfileId": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "order": "1",
  "triggered": "false"
}'),
('9e005b1b-3a7c-42ec-bb01-8b861b36411e', '{
  "id": "9e005b1b-3a7c-42ec-bb01-8b861b36411e",
  "masterProfileId": "d016e32e-08cf-4a69-842d-f16dcbd2053f",
  "detailProfileId": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "order": "1",
  "triggered": "false"
}'),
('832d6bfd-9ffd-4110-b87d-577e8b48bc5d', '{
  "id": "832d6bfd-9ffd-4110-b87d-577e8b48bc5d",
  "masterProfileId": "c53efe55-5817-4ca5-bcb1-e33861ba84c3",
  "detailProfileId": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "order": "1",
  "triggered": "false"
}'),
('0acb9fc0-8fa9-4b75-beb7-d1a326d13417', '{
  "id": "0acb9fc0-8fa9-4b75-beb7-d1a326d13417",
  "masterProfileId": "e83f0598-bbc7-437c-90e2-264c761539b9",
  "detailProfileId": "01bf0774-65dd-417f-8c76-4a417086ee20",
  "order": "1",
  "triggered": "false"
}'),
('b90d9d35-4a0c-4c9d-9c3a-f24c924e18b8', '{
  "id": "b90d9d35-4a0c-4c9d-9c3a-f24c924e18b8",
  "masterProfileId": "d016e32e-08cf-4a69-842d-f16dcbd2053f",
  "detailProfileId": "01bf0774-65dd-417f-8c76-4a417086ee20",
  "order": "1",
  "triggered": "false"
}'),
('78937a54-7416-4779-b628-54c25a91c51b', '{
  "id": "78937a54-7416-4779-b628-54c25a91c51b",
  "masterProfileId": "c53efe55-5817-4ca5-bcb1-e33861ba84c3",
  "detailProfileId": "01bf0774-65dd-417f-8c76-4a417086ee20",
  "order": "1",
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
  "masterProfileId": "bb689511-5365-4050-8084-a03d94728d88",
  "detailProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "order": "1",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.match_to_match_profiles (_id, jsonb) values
('a99a1d65-0796-44f0-9c7d-9e1ebd6b97c5', '{
  "id": "a99a1d65-0796-44f0-9c7d-9e1ebd6b97c5",
  "masterProfileId": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "detailProfileId": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "order": "0",
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
  "order": "1",
  "triggered": "false"
}'),
('0c0fbca5-d67b-4932-898b-3963802db42d', '{
  "id": "0c0fbca5-d67b-4932-898b-3963802db42d",
  "masterProfileId": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "detailProfileId": "738e3780-da45-451f-bd9f-f0b7a78c29a5",
  "order": "0",
  "triggered": "false"
}'),
('7124e794-af0a-44dc-8351-b57d8263428b', '{
  "id": "7124e794-af0a-44dc-8351-b57d8263428b",
  "masterProfileId": "ab05c370-7b9d-400f-962b-cb7953b940dd",
  "detailProfileId": "c4ce10c8-10fc-4213-b5ca-8210fcc65da6",
  "order": "0",
  "triggered": "false"
}'),
('f046af44-f158-44fa-b0a6-12886fab9c50', '{
  "id": "f046af44-f158-44fa-b0a6-12886fab9c50",
  "masterProfileId": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "detailProfileId": "c4ce10c8-10fc-4213-b5ca-8210fcc65da6",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

INSERT INTO ${myuniversity}_${mymodule}.action_to_action_profiles (_id, jsonb) values
('5e6068b2-a485-4e14-ad34-392fe2c6c10f', '{
  "id": "5e6068b2-a485-4e14-ad34-392fe2c6c10f",
  "masterProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "detailProfileId": "8176badd-bd7e-430d-aeb4-9f6ac342885f",
  "order": "2",
  "triggered": "false"
}'),
('fa07eef8-118f-4873-b9f8-40b7bba9916b', '{
  "id": "fa07eef8-118f-4873-b9f8-40b7bba9916b",
  "masterProfileId": "a260bf7f-a7c4-4c1d-bfb0-3be6230c71d4",
  "detailProfileId": "4b20facc-fbe8-4523-8f18-04ae35126ba6",
  "order": "1",
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
  "order": "0",
  "triggered": "false"
}'),
('b59a628b-2e30-412c-a66e-0871f2eeb4e6', '{
  "id": "b59a628b-2e30-412c-a66e-0871f2eeb4e6",
  "masterProfileId": "4b20facc-fbe8-4523-8f18-04ae35126ba6",
  "detailProfileId": "f4f075c6-67c9-411e-af24-01ceecd81b4b",
  "order": "0",
  "triggered": "false"
}'),
('e38df953-fb5d-44aa-b286-55e12a78fdf5', '{
  "id": "e38df953-fb5d-44aa-b286-55e12a78fdf5",
  "masterProfileId": "c4ce10c8-10fc-4213-b5ca-8210fcc65da6",
  "detailProfileId": "99b7bb53-a629-4058-a5f4-d2fcf5dc7125",
  "order": "0",
  "triggered": "false"
}') ON CONFLICT DO NOTHING;

