INSERT INTO ${myuniversity}_${mymodule}.match_profiles (id, jsonb) values
('cdf0ca3a-b515-4abd-82b6-48ce65374963', '{
  "id": "cdf0ca3a-b515-4abd-82b6-48ce65374963",
  "name": "POL-MARC",
  "description": "Use for POL in 990 $p",
  "tags": {
    "tagList": [
      "pol"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "ORDER",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "ORDER",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "990"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "p"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "po_line.poLineNumber"
          }
        ]
      }
    }
  ],
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
('a3510db6-5b3a-48ed-96c0-99a03df87b79', '{
  "id": "a3510db6-5b3a-48ed-96c0-99a03df87b79",
  "name": "MARC Identifiers",
  "description": "Try to match on ISBN, else create new record",
  "tags": {
    "tagList": [
      "isbn"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "INSTANCE",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "INSTANCE",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "020"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ],
        "qualifier": {
          "qualifierType": "BEGINS_WITH",
          "qualifierValue": "978"
        }
      },
      "matchCriterion": "EXISTING_VALUE_BEGINS_WITH_INCOMING_VALUE",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "instance.identifiers[].value"
          }
        ],
         "qualifier": {
          "qualifierType": "BEGINS_WITH",
          "qualifierValue": "978"
        }
      }
    }
  ],
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
 ('ab32efdb-43c2-4cb5-b7dc-49dd45c02106', '{
  "id": "ab32efdb-43c2-4cb5-b7dc-49dd45c02106",
  "name": "KB ID in 935",
  "description": "KB identifier in 935 $a",
  "tags": {
    "tagList": [
      "kb"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "MARC_BIBLIOGRAPHIC",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "MARC_BIBLIOGRAPHIC",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "935"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
         "fields": [
          {
            "label": "field",
            "value": "035"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ]
      }
    }
  ],
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
('88bb7c9f-79f2-4a97-b600-535f1d913378', '{
  "id": "88bb7c9f-79f2-4a97-b600-535f1d913378",
  "name": "001 to Instance HRID",
  "description": "MARC 001 to Instance ID (numerics only)",
  "tags": {
    "tagList": [
      "hrid"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "INSTANCE",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "INSTANCE",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "001"
          },
          {
            "label": "indicator1",
            "value": ""
          },
          {
            "label": "indicator2",
            "value": ""
          },
          {
            "label": "recordSubfield",
            "value": ""
          }
        ],
        "qualifier": {
          "comparisonPart": "NUMERICS_ONLY"
        }
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "instance.hrid"
          }
        ],
        "qualifier": {
          "comparisonPart": "NUMERICS_ONLY"
        }
      }
    }
  ],
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
('ab05c370-7b9d-400f-962b-cb7953b940dd', '{
  "id": "ab05c370-7b9d-400f-962b-cb7953b940dd",
  "name": "Related holdings HRID",
  "description": "Related location code = main",
  "tags": {
    "tagList": [
      "location", "submatch"
    ]
  },
  "incomingRecordType": "STATIC_VALUE",
  "existingRecordType": "HOLDINGS",
  "matchDetails": [
    {
      "incomingRecordType": "STATIC_VALUE",
      "existingRecordType": "HOLDINGS",
      "incomingMatchExpression": {
        "dataValueType": "STATIC_VALUE",
        "staticValueDetails": {
          "staticValueType": "TEXT",
          "text": "KU/CC/DI/M"
        }
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "holdingsrecord.permanentLocationId"
          }
        ]
      }
    }
  ],
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
('afe7eb12-ea47-4970-8d0e-981b988aed0c', '{
  "id": "afe7eb12-ea47-4970-8d0e-981b988aed0c",
  "name": "MARC 010",
  "description": "LCCN match",
  "tags": {
    "tagList": [
      "lccn"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "MARC_AUTHORITY",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "MARC_AUTHORITY",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "010"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "010"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ]
      }
    }
  ],
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
 ('23cec3f0-092c-4201-9ffc-643f61da03d8', '{
  "id": "23cec3f0-092c-4201-9ffc-643f61da03d8",
  "name": "OCLC 035 DDA",
  "description": "OCLC number match",
  "tags": {
    "tagList": [
      "oclc"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "MARC_BIBLIOGRAPHIC",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "MARC_BIBLIOGRAPHIC",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "035"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ],
        "qualifier": {
          "qualifierType": "BEGINS_WITH",
          "qualifierValue": "(OCoLC)",
          "comparisonPart": "NUMERICS_ONLY"
        }
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "035"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "a"
          }
        ],
        "qualifier": {
          "qualifierType": "BEGINS_WITH",
          "qualifierValue": "(OCoLC)",
          "comparisonPart": "NUMERICS_ONLY"
        }
      }
    }
  ],
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
('054a185d-f90b-409d-9203-4cc389e20d13', '{
  "id": "054a185d-f90b-409d-9203-4cc389e20d13",
  "name": "EDI regular",
  "description": "EDIFACT POL",
  "tags": {
    "tagList": [
      "pol"
    ]
  },
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "ORDER",
  "matchDetails": [
    {
      "incomingRecordType": "EDIFACT_INVOICE",
      "existingRecordType": "ORDER",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "RFF+LI"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "po_line.poLineNumber"
          }
        ]
      }
    }
  ],
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
('1490430a-4d72-494b-ae6b-a8e4d9614e0e', '{
  "id": "1490430a-4d72-494b-ae6b-a8e4d9614e0e",
  "name": "Invoice check",
  "description": "To check whether invoice has already been loaded",
  "tags": {
    "tagList": [
      "invoice", "submatch"
    ]
  },
  "incomingRecordType": "EDIFACT_INVOICE",
  "existingRecordType": "INVOICE",

  "matchDetails": [
    {
      "incomingRecordType": "EDIFACT_INVOICE",
      "existingRecordType": "INVOICE",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "BGM+1004"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "invoice.vendorInvoiceNo"
          }
        ]
      }
    }
  ],
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
('4c1d47ba-c527-4ef1-b497-42676c82254f', '{
  "id": "4c1d47ba-c527-4ef1-b497-42676c82254f",
  "name": "Item Barcode match",
  "description": "Find item record by barcode number",
  "tags": {
    "tagList": [
      "barcode", "item"
    ]
  },
  "incomingRecordType": "MARC_BIBLIOGRAPHIC",
  "existingRecordType": "ITEM",
  "matchDetails": [
    {
      "incomingRecordType": "MARC_BIBLIOGRAPHIC",
      "existingRecordType": "ITEM",
      "incomingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "945"
          },
          {
            "label": "indicator1",
            "value": " "
          },
          {
            "label": "indicator2",
            "value": " "
          },
          {
            "label": "recordSubfield",
            "value": "i"
          }
        ]
      },
      "matchCriterion": "EXACTLY_MATCHES",
      "existingMatchExpression": {
        "dataValueType": "VALUE_FROM_RECORD",
        "fields": [
          {
            "label": "field",
            "value": "item.barcode"
          }
        ]
      }
    }
  ],
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
}') ON CONFLICT DO NOTHING;
