# OpenDataHub API Tutorial - UNIBZ Mentoring Session

## Overview
This tutorial focuses on working with the OpenDataHub API, specifically exploring e-charging station data from the mobility domain. Students will learn to make API calls, handle parameters, and process JSON responses using a provided Java HTTP client.

## Learning Objectives
- Understand OpenDataHub API basics
- Make API calls with various parameters
- Work with data providers and handle limits
- Map JSON responses to Java POJOs
- Process time series data

## Core Concepts

### Measurements
A measurement is a timestamped data point consisting of:
- **Station**: Geographic location where measurements are taken (e.g., e-charging station, weather station)
- **Data Type**: Nature of the measurement (e.g., temperature, availability status)
- **Provenance**: Identifier and version of the data provider
- **Period**: Timeframe and update frequency of measurements

### Station Hierarchy
Stations can:
- Have 0-n data types
- Exist without measurements
- Have parent-child relationships

## Dataset Details

### Focus Area
- Domain: Mobility (E-charging stations)
- Primary Provider: `route220`
- Optional Extensions: ALPERIA and others

### Station Types
1. **EChargingStation**
   - Represents a physical location with multiple chargers
   - Data type: `number-available` (available columns count)

2. **EChargingPlug**
   - Individual charging column within a station
   - Data type: `echarging-plug-status` (0 or 1 for usage state)
   - Always associated with a parent EChargingStation

## API Reference

For this part I suggest working with Postman befor using the java http client.

### Basic structure of the request:

`endpoint/v2/{representation}/{stationTypes}/{dataTypes}/{from}/{to}`

`endpoint/v2/{representation}/{stationTypes}/{dataTypes}/latest`

`endpoint/v2/{representation}/{stationTypes}/{dataTypes}`

### Some examples of generic calls:

**Get a list of all available stations:**

`https://mobility.api.opendatahub.com/v2/flat`

**Get all e-charging stations including details or e-charging plugs:**

`https://mobility.api.opendatahub.com/v2/flat/EChargingStation`

`https://mobility.api.opendatahub.com/v2/flat/EChargingPlug`

**Get all the `distinct` data providers for all the e-charging station data:**

`https://mobility.api.opendatahub.com/v2/flat/EChargingStation?select=sorigin`

**Get all the data types availables for the e-charging stations and for the e-charging plugs:**

`https://mobility.api.opendatahub.com/v2/flat/EChargingStation/*?select=tname`

`https://mobility.api.opendatahub.com/v2/flat/EChargingPlug/*?select=tname`

### Now let's get some historical measurements:

**Get e-charging station data from a recent day in november**

`https://mobility.api.opendatahub.com/v2/flat/EChargingPlug/echarging-plug-status/2024-11-15/2024-11-16`

**Limit the number of results**

Limit your output by adding `limit` to your request, and paginate your
results with an `offset`. If you want to disable the limit, set it to a negative
number, like `limit=-1`. Per default, the limit is set to a low number to
prevent excessive response times.


### Filtering with SELECT and WHERE

It is possible to filter against JSON fields (columns in a database) with
`select=alias,alias,alias,...`, or per record (rows in a database) with
`where=filter,filter,filter,...`. The latter, is a conjunction (`and`) of all
clauses. Also complex logic is possible, with nested `or(...)` and `and(...)`
clauses, for instance `where=or(filter,filter,and(filter,filter))`.

**alias**
An `alias` is a list of point-separated-fields, where each field corresponds
to a step inside the JSON hierarchy. Internally, the first field represents the
database column and all subsequent fields drill into the JSON hierarchy.
For example, `metadata.municipality.cap` is an JSONB inside the database with a
column `metadata` and a JSONB object called `municipality` which has a `cap`
inside.

**filter**
A `filter` has the form `alias.operator.value_or_list`.

**value_or_list**

- `value`: Whatever you want, also a regular expression. Use double-quotes to
  force string recognition. Alternatively, you can escape characters `,`, `'`
  and `"` with a `\`. Use url-encoding, if your tool does not support certain
  characters. Special values are `null`, numbers and omitted values. Examples:
  - `description.eq.null`, checks if a description is not set
  - `description.eq.`, checks if a description is a string of length 0
- `list`: `(value,value,value)`

**operator**

- `eq`: Equal
- `neq`: Not Equal
- `lt`: Less Than
- `gt`: Greater Than
- `lteq`: Less Than Or Equal
- `gteq`: Greater Than Or Equal
etc...

**logical operations**

- `and(filter,filter,...)`: Conjunction of filters (can be nested)
- `or(filter,filter,...)`: Disjunction of filters (can be nested)

### Filtering example:

How to retrieve only some given fields from the `active` e-charging plugs, this also speeds up the fetching of the data:

`https://mobility.api.opendatahub.com/v2/flat/EChargingPlug/echarging-plug-status/2024-11-15/2024-11-16?limit=200&select=sorigin,sname,mvalidtime,mperiod,mvalue&where=sorigin.eq.route220,sactive.eq.true`

## Additional Resources
- [Historical Data and Request Limits](https://github.com/noi-techpark/odh-docs/wiki/Historical-Data-and-Request-Rate-Limits)
- [Open Data Hub General Overview](https://docs.opendatahub.com/en/latest/intro.html#project-overview)
- [Data Browser](https://databrowser.opendatahub.com/)

## Getting Started
1. Fork this repository
2. Clone your fork
3. Use the provided Java HTTP client as a starting point
4. Test API calls using Postman before implementing in Java

### Ideas for data elaborations:
- Calculate the average availability percentage of echarging stations, aggregated per hour
- Predict the availability of an echarging station within the next hour
- ecc..



