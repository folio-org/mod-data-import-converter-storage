# mod-data-import-converter-storage

Copyright (C) 2019-2022 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0.
See the file "[LICENSE](LICENSE)" for more information.

<!-- ../../okapi/doc/md2toc -l 2 -h 4 README.md -->
* [Introduction](#introduction)
* [Compiling](#compiling)
* [Docker](#docker)
* [Installing the module](#installing-the-module)
* [Deploying the module](#deploying-the-module)
* [REST Client](#rest-client)

## DEPRECATED
Renamed to [mod-di-converter-storage](https://github.com/folio-org/mod-di-converter-storage)

## Introduction

Data Import Converter Storage.

Provides PostgreSQL based storage to complement the data import module. Written in Java, using the raml-module-builder and uses Maven as its build system.

## Compiling

```
   mvn install
```

See that it says "BUILD SUCCESS" near the end.

## Docker

Build the docker container with:

```
   docker build -t mod-data-import-converter-storage .
```

Test that it runs with:

```
   docker run -t -i -p 8081:8081 mod-data-import-converter-storage
```

## Installing the module

Follow the guide of
[Deploying Modules](https://github.com/folio-org/okapi/blob/master/doc/guide.md#example-1-deploying-and-using-a-simple-module)
sections of the Okapi Guide and Reference, which describe the process in detail.

First of all you need a running Okapi instance.
(Note that [specifying](../README.md#setting-things-up) an explicit 'okapiurl' might be needed.)

```
   cd .../okapi
   java -jar okapi-core/target/okapi-core-fat.jar dev
```

We need to declare the module to Okapi:

```
curl -w '\n' -X POST -D -   \
   -H "Content-type: application/json"   \
   -d @target/ModuleDescriptor.json \
   http://localhost:9130/_/proxy/modules
```

That ModuleDescriptor tells Okapi what the module is called, what services it
provides, and how to deploy it.

## Deploying the module

Next we need to deploy the module. There is a deployment descriptor in
`target/DeploymentDescriptor.json`. It tells Okapi to start the module on 'localhost'.

Deploy it via Okapi discovery:

```
curl -w '\n' -D - -s \
  -X POST \
  -H "Content-type: application/json" \
  -d @target/DeploymentDescriptor.json  \
  http://localhost:9130/_/discovery/modules
```

Then we need to enable the module for the tenant:

```
curl -w '\n' -X POST -D -   \
    -H "Content-type: application/json"   \
    -d @target/TenantModuleDescriptor.json \
    http://localhost:9130/_/proxy/tenants/<tenant_name>/modules
```

## REST Client

Provides RMB generated Client to call the module's endpoints. The Client is packaged into the lightweight jar.

### Maven dependency 

```xml
    <dependency>
      <groupId>org.folio</groupId>
      <artifactId>mod-data-import-converter-storage-client</artifactId>
      <version>x.y.z</version>
      <type>jar</type>
    </dependency>
```
Where x.y.z - version of mod-data-import-converter-storage.

### Usage

ConverterStorageClient is generated by RMB and provides methods for all endpoints described in the RAML file
```java
    // create client object with okapi url, tenant id and token
    ConverterStorageClient client = new ConverterStorageClient("localhost", "diku", "token");
```
Clients methods work with RMB generated data classes based on json schemas. 
mod-data-import-converter-storage-client jar contains only generated by RMB DTOs and clients. 

Example of sending a request to the mod-data-import-converter-storage
```
    // send request to mod-data-import-converter-storage
    client.getConverterStorage(response->{
      // processing response
      if (response.statusCode() == 200){
        System.out.println("Call is successful");
      }
    });
```
 
## Sample data
The module contains sample data for job, action, match and mapping profiles. 
To create sample data for particular tenant you should enable the module for a tenant 
using `/_/proxy/tenants/id/install?tenantParameters=loadSample=true` endpoint with specified tenant parameter `loadSample=true`. \
 
## Issue tracker

See project [MODDICONV](https://issues.folio.org/browse/MODDICONV)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).

