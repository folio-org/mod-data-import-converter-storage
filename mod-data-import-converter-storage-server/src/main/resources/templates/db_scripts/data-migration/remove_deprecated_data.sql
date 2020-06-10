-- profiles associations:
-- delete deprecated profileAssociation from job_to_match_profiles table
DELETE FROM ${myuniversity}_${mymodule}.job_to_match_profiles
WHERE id IN ('95d29ddc-7e0e-459e-840e-36576a37c97b', 'e9ff0afb-6a73-4ba7-a0db-4fb089d3041f',
             '883d82c7-1c70-4b9e-807f-800f5ea0948e', 'f8ee9bd4-6350-44f0-8f71-015638bde276',
             '1c409c83-12a5-4555-9720-9873a2dcb94d', '34588266-2dd9-4375-8003-88c20b865f02',
             '1705c78e-09ca-4e00-a7d1-3947cc925f57', '1d1add8e-1ab4-47ee-b82c-789d77d85a64',
             '3d9fdad6-797a-4b53-a9ce-09dc2af481e9');

-- delete deprecated profileAssociation from job_to_action_profiles table
DELETE FROM ${myuniversity}_${mymodule}.job_to_action_profiles
WHERE id IN ('1520d04c-2647-4129-880f-3ed984e41d54', '0b701517-0993-4b7e-9437-eb7b2423436c',
             'ede7ae28-df7e-401c-8860-6c532253d29d', '48fee989-1f2f-4aa3-91e0-c314d5e71a50',
             'f7ec383f-ddbb-4e47-9b3f-4bc986836407', 'c6b7cd4f-d5aa-492a-80df-ba80f8024caa',
             'a278791e-ad04-4ded-a1ac-597538baf837', '5a08c107-2487-418e-b856-f435a6154bc6',
             '4ad13dda-9e82-4dce-9039-33320785265f', '9b420a3c-b1c3-4eb8-b721-58bf9532de81',
             '081ae765-b6e6-499f-8916-08253ce19ce3', '200eefad-6e94-4c23-ae54-d126ff8798d7',
             '8e9feb53-7060-4528-8b95-7ca889cb95a7', '9850b9fa-8349-405c-af8e-3f037ce5076e',
             '3168e6df-539c-4704-ad00-988f5eed0461', 'fc65f21a-9a26-46e6-9f66-569ad26298b7',
             'aed92309-b388-4685-b68f-00ddf4e5067a', '99f3eefa-b7c4-45f5-aa4d-9235b542e3ab',
             '68020957-a8b8-4be5-a9b4-63a0af0c51cf', 'f65dbb76-39f1-45d3-bcb9-1b959e46b34e',
             '1b50889d-9557-43e3-bbee-91fc976a84b0', 'bced0aa4-8b24-4b71-add5-8cc185d18b2a',
             '61199eee-1641-4315-9585-d62729347588', 'd9cfbef2-1a3c-4681-8d82-71baace27cf9',
             '1b652359-304f-403f-8a6a-494d5bf67b17', '9dba5efe-cfb5-4019-9e91-8931a89bf827',
             '3b7ecf9d-3760-4d7c-bb8d-baf807416bf6', '134dbdb3-86d0-4d34-8642-def82d803750',
             'fbec5166-65b7-4222-bca1-ee3c4891d0d6', 'd4c18292-db7c-49a5-9a08-f5dfb13f8ec9',
             '9bc6eca9-74ca-4cdd-889d-b4f84a4e7c01', '81826c58-e2d6-45c9-986a-baa667099687',
             '36b65d30-ce12-4864-a794-ee1583d098aa', '22f79964-96d9-4a46-972f-8e65a669f613',
             'ef318a13-34d9-4d37-9691-f7bbdd4fd34a', 'abc4a70c-faa3-4050-8b4b-60b6a68c467e');

-- delete deprecated profileAssociation from match_to_action_profiles table
DELETE FROM ${myuniversity}_${mymodule}.match_to_action_profiles
WHERE id IN ('1520d04c-2647-4129-880f-3ed984e41d54', '0b701517-0993-4b7e-9437-eb7b2423436c',
             'ede7ae28-df7e-401c-8860-6c532253d29d', '48fee989-1f2f-4aa3-91e0-c314d5e71a50',
             'f7ec383f-ddbb-4e47-9b3f-4bc986836407', 'c6b7cd4f-d5aa-492a-80df-ba80f8024caa',
             'a278791e-ad04-4ded-a1ac-597538baf837', '5a08c107-2487-418e-b856-f435a6154bc6',
             '4ad13dda-9e82-4dce-9039-33320785265f', '9b420a3c-b1c3-4eb8-b721-58bf9532de81',
             '081ae765-b6e6-499f-8916-08253ce19ce3', '200eefad-6e94-4c23-ae54-d126ff8798d7',
             '8e9feb53-7060-4528-8b95-7ca889cb95a7', '9850b9fa-8349-405c-af8e-3f037ce5076e',
             '3168e6df-539c-4704-ad00-988f5eed0461', 'fc65f21a-9a26-46e6-9f66-569ad26298b7',
             '99e4d4ab-af9d-4cf3-bfad-ad00de5ddc42', '99f3eefa-b7c4-45f5-aa4d-9235b542e3ab',
             '68020957-a8b8-4be5-a9b4-63a0af0c51cf', 'f65dbb76-39f1-45d3-bcb9-1b959e46b34e',
             '1b50889d-9557-43e3-bbee-91fc976a84b0', 'bced0aa4-8b24-4b71-add5-8cc185d18b2a',
             '61199eee-1641-4315-9585-d62729347588', '04a36461-a25e-49e2-910c-809bbeede541',
             '3020cc03-2792-4ae4-ab0c-b1b9a723f1da', '26b91b0e-ba3a-4fc7-8410-3353c6d5341a');

-- delete deprecated profileAssociation from match_to_match_profiles table
DELETE FROM ${myuniversity}_${mymodule}.match_to_match_profiles
WHERE id = '95d29ddc-7e0e-459e-840e-36576a37c97b';

-- delete deprecated profileAssociation from action_to_mapping_profiles table
DELETE FROM ${myuniversity}_${mymodule}.action_to_mapping_profiles
WHERE id IN ('460332ee-9800-43fe-8b89-19c8e5a0a5b8', '53cb496c-e3b7-46a9-a364-94bd145a015a',
             '91f71c11-b353-4641-b89c-7316987812fd', '5700a788-2dc7-43e4-a056-8afd9aec3247',
             '6c8f991d-5e86-436a-a65b-35dcc445516b', '79079709-9129-4092-8ea2-d433788bb892',
             'de55cb3b-2ce5-40f6-83e3-a1731258366e', '43a16fef-7b97-4d6b-86bf-fca689f0b81e',
             '79016db5-41ac-4daf-9627-9b8ba53e65c8', '5869b958-03bb-488f-ad56-f5e0d41de7a0',
             '81a5d828-9069-4875-a6f5-e23184048cd3', '0d8e7482-fbef-4fa2-a349-7e619dabe3c3',
             '5aa5853a-9a0f-4f16-8aa7-794a02636c58', '2418abf4-b484-4644-9e4b-2ac4cdb845aa',
             'f6c69faa-90d0-457b-b0c5-5f1d39e0529b', '02a86ac4-5d0a-4a8d-a54b-1f257867c546',
             'be108fb8-98ba-413e-a719-1eabecd31229', 'abfeee4c-4705-4ae9-9525-5033ae9b5e8f',
             'b120786f-618b-4428-bbb2-0b5bfec00ea8', 'b07c11ac-1fcd-4b2a-bc80-39cef5bed299',
             '9450aea8-38de-4ade-9e0b-145b20600f4a', 'a9282a69-7834-467e-b8fe-ffd54532147b',
             '70ddbac5-9fbb-42b5-8a90-b2aff53c1c5b', '9cfcf7a3-d244-49ed-84c4-a6a4cf8cee47',
             '4f6408fe-fdcb-4c65-b083-ca682f6d5c42', '46cbc3a9-c945-419b-b8fd-6fb5d3e4d87c',
             '2b4a5a5b-1d89-4386-a528-713447dfd4aa', '29286a7e-8a4f-46cb-a0ce-fe73467792a8',
             '5cd7a657-7980-4048-8cb7-734cc5296262', '3af96514-6d49-46e0-ab47-ee7875dc02c6',
             '588f8797-9ce8-47f4-954c-49d7e46fb1a6', 'f6f86f76-8744-454c-b5e6-3d264bf53a00',
             '29e2e115-568e-48d9-95f0-1ca5dcb7d06d', '189e203a-48b8-4aef-86c6-44c268671ad5',
             '90041e77-796c-4cba-87e5-c8bc07ba1184');

-- mapping_profiles:
-- delete deprecated mappingProfiles
DELETE FROM ${myuniversity}_${mymodule}.mapping_profiles
WHERE id IN ('0d991835-fabb-4234-8472-3ae94bc7a1b3', '5b546bd4-ac38-44a8-aea1-30e460a9700f',
             'fce00bb4-6dea-499d-8645-c55c584a2970', '0d85d496-4504-4e79-be97-c6995d101b7e',
             'c5bccf59-cb5f-4edf-95a1-5dae28d75747', '8c65ab76-5b82-471b-9b8c-43b32385e7f7',
             '7545c184-e274-4720-ad3a-cc58e1f2d2c7', '8fdc2cc5-3846-4572-8261-d2be5192bc5a',
             'ed46cfd0-b533-4ebc-88e3-6612a712a64c', '91e37f33-9d12-4cc2-93c8-7cbad031cca6',
             '0cf967db-3d75-44ae-b116-81654bd36973', '6a05ac0e-e65f-43a2-8a55-cc4edd56137c',
             '4a887936-794d-4014-a1ec-046abfab01f6', '1737d899-6fb9-44f4-ba16-9a8b111cd61d',
             '2c655027-83ee-40e9-8221-d252c9bf7b65', '31cc9c07-8d38-4117-a4ec-287593ee9aa0',
             '156714f8-ee1b-4830-bb42-eaa39f0f54ac', 'c9a192aa-6010-4020-84db-6a67493b9a2f',
             '1f005754-a700-4578-9108-567cb389b18b', '888837b3-6081-4902-b2c7-8cc92ef621e4',
             'c928394d-4c5d-4350-a70c-549471a0662b', 'e0f069d4-ecd8-41c9-ad40-bbbdd54f39ac',
             'd46c2753-37e7-4c0e-8146-d7bca959e82c', '7369fd57-9d91-48b5-86ab-1b833dd4eded',
             '1a8c57b1-5ec5-473b-8fc7-e8ad13194621', 'cb6e0e3c-8748-413f-b9a6-2037522195c4',
             '2e45beb9-2aca-4f58-a42b-c121841e4331', '08f75aeb-d1b0-4a74-ab6c-9364617bae9c',
             'e7fc165e-a16a-46d4-a608-fd15d5b17a8d', '7b475a5e-4ac3-4ebe-bf4f-5d0ab32b94a5',
             'ecdba48b-11f8-441a-b852-f075810db54b', '13bee67e-9529-497b-8fe7-daad8765a6ed',
             '380bcf13-4370-4a4f-86cd-49d5c36d12a0', 'd16be1b4-d4cb-4133-8cc9-cf37f936dec2');

-- rename field "folioRecord" to "existingRecordType"
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb - 'folioRecord' || jsonb_build_object('existingRecordType', jsonb ->> 'folioRecord')
WHERE jsonb ? 'folioRecord';

-- action_profiles:
-- delete deprecated actionProfiles
DELETE FROM ${myuniversity}_${mymodule}.action_profiles
WHERE id IN ('cf3d7393-8534-44fb-93fd-7495d220f4f6', 'dce26cfa-4aa7-4e55-abf5-daba1e4acd37',
             '397430e8-d9e1-4f10-9254-730601daf286', '968cfe23-909e-422b-8a24-2387971b9d0b',
             '93691541-5fb7-4ef9-951d-dbb8d762f9bb', '3d1d7155-02ad-46d3-8704-eb6eda2bd19a',
             '5db3d727-1269-4ae3-99da-418b01b8da47', '8d73d518-6b64-4bcd-a10c-cd9d87b3931d',
             '96db6b49-607a-4544-b7e1-60115145b69d', '3bb0b2b3-b683-4cfb-b52c-175355d7f30c',
             'c9573d66-c5e3-4bf7-a6a0-00a80e92cad5', '7bbc68b6-fcfc-4b7c-9d8d-e1ec8a831142',
             '36d26d5d-720d-4ab0-954f-6b75dde517cb', '28d5e15c-d357-424e-838c-01c40a5b14e6',
             '70a3fe3a-7704-480d-8a93-8f0b6a72fcb0', 'bed8da3b-22d0-4b41-94b5-03a709bebf59',
             'ebfeecce-9ac2-447c-86d7-9509f1eac227', '0f8843e9-9282-4175-af88-6a0afbf875f3',
             'a2277721-0063-49aa-8643-3c7dbfd15c52', 'ae000c19-cac7-441f-bcc1-60d3b3f04ba7',
             'f8c44c83-3101-4b8f-ada0-f7f0e5fd986f', 'f76e1b9c-b312-4fb5-9d6c-b1f855dced9a',
             '8eeabfcc-4c11-4ec7-b273-9c04dc0013bb', 'ba8d822f-97db-4794-8393-562a0b2cf755',
             '320b0d15-77a1-4723-9f85-e2c1a2db379b', '13cd54b7-47cb-4b47-8103-1c98b761f80d',
             '51c8b6ac-0424-4c8d-9a53-7a810be50ff7', 'bb917336-4150-4c13-95d5-6786da41e0ad',
             '9cb7b00f-6e7a-4650-966b-c0ebdeaf05b1', '35844d1b-b0f4-4f20-92da-f76e77602eb8',
             'f214089e-bd60-4711-aaa6-0001706e449e', '83d59d03-8692-4304-a530-30892c98c554',
             '10694275-2341-4b2d-be25-7aa78d5bca41', '4fc313a1-51af-4f47-a618-6fa8375fb54f',
             '0cc8fcbf-b447-4dd3-8fd3-8387de90b3bd');

-- match_profiles:
-- delete deprecated matchProfiles
DELETE FROM ${myuniversity}_${mymodule}.match_profiles
WHERE id IN ('01bf0774-65dd-417f-8c76-4a417086ee20', 'cdf0ca3a-b515-4abd-82b6-48ce65374963',
             'a3510db6-5b3a-48ed-96c0-99a03df87b79', 'ab32efdb-43c2-4cb5-b7dc-49dd45c02106',
             '88bb7c9f-79f2-4a97-b600-535f1d913378', 'ab05c370-7b9d-400f-962b-cb7953b940dd',
             'afe7eb12-ea47-4970-8d0e-981b988aed0c', '23cec3f0-092c-4201-9ffc-643f61da03d8',
             '054a185d-f90b-409d-9203-4cc389e20d13', '1490430a-4d72-494b-ae6b-a8e4d9614e0e',
             '4c1d47ba-c527-4ef1-b497-42676c82254f', '727c67ff-6015-4279-8d5f-8b775f736114');

-- change values: MARC to MARC_BIBLIOGRAPHIC, EDIFACT to EDIFACT_INVOICE
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = CASE jsonb ->> 'incomingRecordType'
                WHEN 'MARC' THEN jsonb_set(jsonb, '{matchDetails, 0, incomingRecordType}', '"MARC_BIBLIOGRAPHIC"') ||
                                 jsonb_build_object('incomingRecordType', 'MARC_BIBLIOGRAPHIC')
                WHEN 'EDIFACT' THEN jsonb_set(jsonb, '{matchDetails, 0, incomingRecordType}', '"EDIFACT_INVOICE"') ||
                                    jsonb_build_object('incomingRecordType', 'EDIFACT_INVOICE')
                ELSE jsonb
            END;

-- delete "entityType" field
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = jsonb - 'entityType' WHERE jsonb ? 'entityType';

-- job_profiles:
-- delete deprecated jobProfiles
DELETE FROM ${myuniversity}_${mymodule}.job_profiles
WHERE id IN ('448ae575-daec-49c1-8041-d64c8ed8e5b1', '295e28b4-aea2-4458-9073-385a31e1da05',
             'bb689511-5365-4050-8084-a03d94728d88', '3e705998-9169-4f31-b048-90ffdcbd24c1',
             '15426802-bb0d-4dfb-8eee-90f64fed0cf1', '87e4ad58-d677-43dd-8b04-9795741b2103',
             '72af7eb7-d7e2-4d16-93ac-682b9a58a94c', '4d1b5024-2c49-42bd-b781-4330d14cefb0',
             'b32e79bc-01d9-4d31-bc08-a3621fcfc1aa', '558dede0-515d-11e9-8647-d663bd873d93',
             '828a787c-bcf3-4c28-891a-9e6f3ba5068b', 'b81d36ae-eda0-4633-a135-bde3a999a4f7');

