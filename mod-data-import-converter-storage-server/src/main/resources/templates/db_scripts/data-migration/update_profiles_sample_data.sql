-- match_profiles:
-- delete deprecated matchProfile
DELETE FROM ${myuniversity}_${mymodule}.match_profiles
WHERE id = '01bf0774-65dd-417f-8c76-4a417086ee20';

-- change values: MARC to MARC_BIBLIOGRAPHIC, EDIFACT to EDIFACT_INVOICE
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = CASE jsonb ->> 'incomingRecordType'
                WHEN 'MARC' THEN jsonb_set(jsonb, '{matchDetails, 0, incomingRecordType}', '"MARC_BIBLIOGRAPHIC"') ||
                                 jsonb_build_object('incomingRecordType', 'MARC_BIBLIOGRAPHIC')
                WHEN 'EDIFACT' THEN jsonb_set(jsonb, '{matchDetails, 0, incomingRecordType}', '"EDIFACT_INVOICE"') ||
                                    jsonb_build_object('incomingRecordType', 'EDIFACT_INVOICE')
                ELSE jsonb
            END;

-- change field name of the existing record to field path:
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = CASE jsonb #>> '{matchDetails, 0, existingMatchExpression, fields, 0, value}'
                WHEN 'PO_LINE_NUMBER'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"po_line.poLineNumber"')
                WHEN 'ISBN'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"instance.identifiers[].value"')
                WHEN 'INSTANCE_HRID'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"instance.hrid"')
                WHEN 'LOCATION_CODE'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"holdingsrecord.permanentLocationId"')
                WHEN 'VENDOR_INVOICE_NUMBER'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"invoice.vendorInvoiceNo"')
                WHEN 'ITEM_BARCODE'
                    THEN jsonb_set(jsonb, '{matchDetails, 0, existingMatchExpression, fields, 0, value}', '"item.barcode"')
                ELSE jsonb
            END;

-- delete "entityType" field
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = jsonb - 'entityType';

-- change "incomingRecordType" field value to STATIC_VALUE for particular matchProfile
UPDATE ${myuniversity}_${mymodule}.match_profiles
SET jsonb = jsonb_set(jsonb, '{matchDetails, 0, incomingRecordType}', '"STATIC_VALUE"') ||
            jsonb_build_object('incomingRecordType', 'STATIC_VALUE')
WHERE id = 'ab05c370-7b9d-400f-962b-cb7953b940dd';


-- mapping_profiles:
-- rename field "folioRecord" to "existingRecordType"
UPDATE ${myuniversity}_${mymodule}.mapping_profiles
SET jsonb = jsonb - 'folioRecord' || jsonb_build_object('existingRecordType', jsonb ->> 'folioRecord');


-- profiles associations:
-- update "order" value in job_to_action_profiles
UPDATE ${myuniversity}_${mymodule}.job_to_action_profiles
SET jsonb = CASE WHEN id IN ('1b652359-304f-403f-8a6a-494d5bf67b17', 'd4c18292-db7c-49a5-9a08-f5dfb13f8ec9', '22f79964-96d9-4a46-972f-8e65a669f613')
                     THEN jsonb_set(jsonb, '{order}', '1')
                 WHEN id IN ('9dba5efe-cfb5-4019-9e91-8931a89bf827', '9bc6eca9-74ca-4cdd-889d-b4f84a4e7c01', 'ef318a13-34d9-4d37-9691-f7bbdd4fd34a')
                     THEN jsonb_set(jsonb, '{order}', '2')
                 WHEN id IN ('3b7ecf9d-3760-4d7c-bb8d-baf807416bf6', '81826c58-e2d6-45c9-986a-baa667099687', 'abc4a70c-faa3-4050-8b4b-60b6a68c467e')
                     THEN jsonb_set(jsonb, '{order}', '3')
                 WHEN id IN ('134dbdb3-86d0-4d34-8642-def82d803750')
                     THEN jsonb_set(jsonb, '{order}', '4')
                 ELSE jsonb
            END;

-- delete deprecated profileAssociation from job_to_match_profiles table
DELETE FROM ${myuniversity}_${mymodule}.job_to_match_profiles
WHERE id = '95d29ddc-7e0e-459e-840e-36576a37c97b';

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
             '61199eee-1641-4315-9585-d62729347588');

