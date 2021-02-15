
INSERT INTO ${myuniversity}_${mymodule}.mapping_profiles (id, jsonb) values
('2ad182ee-75e8-4bdb-b859-530543a064dc',
    '{
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
        "enabled": false,
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
        "enabled": "true",
        "path": "invoice.acqUnitIds[]",
        "value": "",
        "subfields": [],
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
        "value": false,
        "subfields": []
      },
      {
        "name": "exportToAccounting",
        "enabled": true,
        "path": "invoice.exportToAccounting",
        "value": true,
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
        "enabled": "true",
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
                "name": "poLineNumber",
                "enabled": true,
                "path": "invoice.invoiceLines[].poLineNumber",
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
                "name": "vendorRefNo",
                "enabled": true,
                "path": "invoice_line.vendorRefNo",
                "value": "RFF+SLI[2]",
                "subfields": []
              },
              {
                "name": "invoiceLineStatus",
                "enabled": "false",
                "path": "invoice.invoiceLines[].invoiceLineStatus",
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
                "value": "",
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
}') ON CONFLICT DO NOTHING;
