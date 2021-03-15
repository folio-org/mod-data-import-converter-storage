
INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) VALUES

('2ad182ee-75e8-4bdb-b859-530543a064dc', '{
  "id": "2ad182ee-75e8-4bdb-b859-530543a064dc",
  "name": "Default - GOBI monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for GOBI. Edit to add details specific to your library and invoices. If additional GOBI invoice profiles are needed, duplicate this one. If no GOBI invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-01-14T14:00:00.000",
    "updatedDate": "2021-01-14T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SLI[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('c89cf275-3ebf-4271-ae89-343dd5824d75', '{
  "id": "c89cf275-3ebf-4271-ae89-343dd5824d75",
  "name": "Default - EBSCO serials invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for EBSCO. Edit to add details specific to your library and invoices. If additional EBSCO invoice profiles are needed, duplicate this one. If no EBSCO invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T14:00:00.000",
    "updatedDate": "2021-03-01T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SNA[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "DTM+194[2]",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "DTM+206[2]",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('7bfd8670-b3f1-447c-ac43-29e04324809c', '{
  "id": "7bfd8670-b3f1-447c-ac43-29e04324809c",
  "name": "Default - WT Cox serials invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for WT Cox. Edit to add details specific to your library and invoices. If additional WT Cox invoice profiles are needed, duplicate this one. If no WT Cox invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T14:00:00.000",
    "updatedDate": "2021-03-01T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SNA[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "DTM+194[2]",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "DTM+206[2]",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('5c117edc-4f1a-4395-9a9f-ea48f28798d5', '{
  "id": "5c117edc-4f1a-4395-9a9f-ea48f28798d5",
  "name": "Default - Harrassowitz serials invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Harrassowitz. Edit to add details specific to your library and invoices. If additional Harrassowitz invoice profiles are needed, duplicate this one. If no Harrassowitz invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T14:00:00.000",
    "updatedDate": "2021-03-01T15:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+F+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+SNL[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SNA[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('fedf651c-5287-4775-9bc4-dd2460df97e3', '{
  "id": "fedf651c-5287-4775-9bc4-dd2460df97e3",
  "name": "Default - Amalivre monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Amalivre. Edit to add details specific to your library and invoices. If additional Amalivre invoice profiles are needed, duplicate this one. If no Amalivre invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+8?4[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2+3[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+F+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+SNL[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203?4[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('7a5b35cf-d574-4f1e-9610-b429372f8140', '{
  "id": "7a5b35cf-d574-4f1e-9610-b429372f8140",
  "name": "Default - Casalini monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Casalini. Edit to add details specific to your library and invoices. If additional Casalini invoice profiles are needed, duplicate this one. If no Casalini invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+86[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SNA[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "PRI+AAB[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('8f7e326f-326f-471f-88c2-724ee8e3b2e9', '{
  "id": "8f7e326f-326f-471f-88c2-724ee8e3b2e9",
  "name": "Default - CNPIEC monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for CNPIEC. Edit to add details specific to your library and invoices. If additional CNPIEC invoice profiles are needed, duplicate this one. If no CNPIEC invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else PIA+5+?IB[1]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "PIA+1+[1]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('fd351093-8df0-4f4e-b202-32ca5a0f00c6', '{
  "id": "fd351093-8df0-4f4e-b202-32ca5a0f00c6",
  "name": "Default - Coutts monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Coutts. Edit to add details specific to your library and invoices. If additional Coutts invoice profiles are needed, duplicate this one. If no Coutts invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "PRI+AAB[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('6cdd60de-b8c1-41ce-ab57-6684d5a18fad', '{
  "id": "6cdd60de-b8c1-41ce-ab57-6684d5a18fad",
  "name": "Default - Erasmus monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Erasmus. Edit to add details specific to your library and invoices. If additional Erasmus invoice profiles are needed, duplicate this one. If no Erasmus invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+86[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD++050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('4f8505ce-59c8-42f1-b925-b1e57d8f9269', '{
  "id": "4f8505ce-59c8-42f1-b925-b1e57d8f9269",
  "name": "Default - Hein serials invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Hein. Edit to add details specific to your library and invoices. If additional Hein invoice profiles are needed, duplicate this one. If no Hein invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM<137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA<86[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM<380<[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX<2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD<F<JTI<[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF<ON[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "PIA<5<?SA[1]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY<47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA<203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('950d9436-6a96-40f6-94c8-9ae39aef26e4', '{
  "id": "950d9436-6a96-40f6-94c8-9ae39aef26e4",
  "name": "Default - Midwest monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Midwest. Review and edit to add details specific to your library and invoices. If additional Midwest invoice profiles are needed, duplicate this one. If no Midwest invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+86[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX+2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+LI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "repeatableFieldAction": "EXTEND_EXISTING",
                "value": "",
                "subfields": [
                  {
                    "order": 0,
                    "path": "invoice.invoiceLines[].referenceNumbers[]",
                    "fields": [
                      {
                        "name": "refNumber",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumber",
                        "value": "RFF+SLI[2]",
                        "subfields": []
                      },
                      {
                        "name": "refNumberType",
                        "enabled": true,
                        "path": "invoice.invoiceLines[].referenceNumbers[].refNumberType",
                        "value": "\"Vendor order reference number\"",
                        "subfields": []
                      }
                    ]
                  }
                ]
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}'),

('8bc2ad7e-5bcf-4ee3-af34-236c0f7875ca', '{
  "id": "8bc2ad7e-5bcf-4ee3-af34-236c0f7875ca",
  "name": "Default - Nardi monograph invoice",
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",
  "deleted": false,
  "description": "Default EDIFACT invoice field mapping profile for Nardi. Edit to add details specific to your library and invoices. If additional Nardi invoice profiles are needed, duplicate this one. If no Nardi invoice profile is needed, delete this one.",
  "metadata": {
    "createdDate": "2021-03-01T15:00:00.000",
    "updatedDate": "2021-03-01T16:00:00.462+0000",
    "createdByUserId": "00000000-0000-0000-0000-000000000000",
    "updatedByUserId": "00000000-0000-0000-0000-000000000000"
  },
  "userInfo": {
    "lastName": "System",
    "userName": "System",
    "firstName": "System"
  },
  "mappingDetails": {
    "name": "invoice",
    "recordType": "INVOICE",
    "mappingFields": [
      {
        "name": "invoiceDate",
        "enabled": true,
        "path": "invoice.invoiceDate",
        "value": "DTM+137[2]",
        "subfields": []
      },
      {
        "name": "status",
        "enabled": true,
        "path": "invoice.status",
        "value": "\"Open\"",
        "subfields": []
      },
      {
        "name": "paymentDue",
        "enabled": true,
        "path": "invoice.paymentDue",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentTerms",
        "enabled": true,
        "path": "invoice.paymentTerms",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvalDate",
        "enabled": false,
        "path": "invoice.approvalDate",
        "value": "",
        "subfields": []
      },
      {
        "name": "approvedBy",
        "enabled": false,
        "path": "invoice.approvedBy",
        "value": "",
        "subfields": []
      },
      {
        "name": "acqUnitIds",
        "enabled": true,
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [{
          "order": 0,
          "path": "invoice.acqUnitIds[]",
          "fields": [{
            "name": "acqUnitIds",
            "enabled": true,
            "path": "invoice.acqUnitIds[]",
            "value": "",
            "subfields": []
          }]
        }],
        "acceptedValues": {
          "0ebb1f7d-983f-3026-8a4c-5318e0ebc041": "main"
        }
      },
      {
        "name": "billTo",
        "enabled": true,
        "path": "invoice.billTo",
        "value": "",
        "subfields": [],
        "acceptedValues": {}
      },
      {
        "name": "billToAddress",
        "enabled": false,
        "path": "invoice.billToAddress",
        "value": "",
        "subfields": []
      },
      {
        "name": "batchGroupId",
        "enabled": true,
        "path": "invoice.batchGroupId",
        "value": "",
        "subfields": [],
        "acceptedValues": {
          "2a2cb998-1437-41d1-88ad-01930aaeadd5": "FOLIO",
          "cd592659-77aa-4eb3-ac34-c9a4657bb20f": "Amherst (AC)"
        }
      },
      {
        "name": "subTotal",
        "enabled": false,
        "path": "invoice.subTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustmentsTotal",
        "enabled": false,
        "path": "invoice.adjustmentsTotal",
        "value": "",
        "subfields": []
      },
      {
        "name": "total",
        "enabled": false,
        "path": "invoice.total",
        "value": "",
        "subfields": []
      },
      {
        "name": "lockTotal",
        "enabled": true,
        "path": "invoice.lockTotal",
        "value": "MOA+9[2]",
        "subfields": []
      },
      {
        "name": "note",
        "enabled": true,
        "path": "invoice.note",
        "value": "",
        "subfields": []
      },
      {
        "name": "adjustments",
        "enabled": true,
        "path": "invoice.adjustments[]",
        "value": "",
        "subfields": []
      },
      {
        "name": "vendorInvoiceNo",
        "enabled": true,
        "path": "invoice.vendorInvoiceNo",
        "subfields": [],
        "value": "BGM+380+[1]"
      },
      {
        "name": "vendorId",
        "enabled": true,
        "path": "invoice.vendorId",
        "value": "",
        "subfields": []
      },
      {
        "name": "accountingCode",
        "enabled": true,
        "path": "invoice.accountingCode",
        "value": "",
        "subfields": []
      },
      {
        "name": "folioInvoiceNo",
        "enabled": false,
        "path": "invoice.folioInvoiceNo",
        "value": "",
        "subfields": []
      },
      {
        "name": "paymentMethod",
        "enabled": true,
        "path": "invoice.paymentMethod",
        "value": "",
        "subfields": []
      },
      {
        "name": "chkSubscriptionOverlap",
        "enabled": true,
        "path": "invoice.chkSubscriptionOverlap",
        "booleanFieldAction": "ALL_FALSE",
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "booleanFieldAction": "ALL_TRUE",
        "subfields": []
      },
      {
        "name": "currency",
        "enabled": true,
        "path": "invoice.currency",
        "subfields": [],
        "value": "CUX++2[2]"
      },
      {
        "name": "currentExchangeRate",
        "enabled": false,
        "path": "invoice.currentExchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "exchangeRate",
        "enabled": true,
        "path": "invoice.exchangeRate",
        "value": "",
        "subfields": []
      },
      {
        "name": "invoiceLines",
        "enabled": true,
        "path": "invoice.invoiceLines[]",
        "value": "",
        "repeatableFieldAction": "EXTEND_EXISTING",
        "subfields": [
          {
            "order": 0,
            "path": "invoice.invoiceLines[]",
            "fields": [
              {
                "name": "description",
                "enabled": true,
                "path": "invoice.invoiceLines[].description",
                "value": "{POL_title}; else IMD+L+050+[4-5]",
                "subfields": []
              },
              {
                "name": "poLineId",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineId",
                "value": "RFF+ON[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineNumber",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": false,
                "path": "invoice.invoiceLines[].invoiceLineStatus",
                "value": "",
                "subfields": []
              },
              {
                "name": "referenceNumbers",
                "enabled": true,
                "path": "invoice.invoiceLines[].referenceNumbers[]",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionInfo",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionInfo",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionStart",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionStart",
                "value": "",
                "subfields": []
              },
              {
                "name": "subscriptionEnd",
                "enabled": true,
                "path": "invoice.invoiceLines[].subscriptionEnd",
                "value": "",
                "subfields": []
              },
              {
                "name": "comment",
                "enabled": true,
                "path": "invoice.invoiceLines[].comment",
                "value": "",
                "subfields": []
              },
              {
                "name": "lineAccountingCode",
                "enabled": false,
                "path": "invoice.invoiceLines[].accountingCode",
                "value": "",
                "subfields": []
              },
              {
                "name": "accountNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].accountNumber",
                "value": "",
                "subfields": []
              },
              {
                "name": "quantity",
                "enabled": true,
                "path": "invoice.invoiceLines[].quantity",
                "value": "QTY+47[2]",
                "subfields": []
              },
              {
                "name": "lineSubTotal",
                "enabled": true,
                "path": "invoice.invoiceLines[].subTotal",
                "value": "MOA+203[2]",
                "subfields": []
              },
              {
                "name": "releaseEncumbrance",
                "enabled": true,
                "path": "invoice.invoiceLines[].releaseEncumbrance",
                "booleanFieldAction": "ALL_TRUE",
                "subfields": []
              },
              {
                "name": "fundDistributions",
                "enabled": true,
                "path": "invoice.invoiceLines[].fundDistributions[]",
                "value": "{POL_FUND_DISTRIBUTIONS}",
                "subfields": []
              },
              {
                "name": "lineAdjustments",
                "enabled": true,
                "path": "invoice.invoiceLines[].adjustments[]",
                "value": "",
                "subfields": []
              }
            ]
          }
        ]
      }
    ]
  }
}')

ON CONFLICT DO NOTHING;
