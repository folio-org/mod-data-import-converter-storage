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

-- delete deprecated matchProfile
DELETE FROM ${myuniversity}_${mymodule}.match_profiles
WHERE id = '01bf0774-65dd-417f-8c76-4a417086ee20';
