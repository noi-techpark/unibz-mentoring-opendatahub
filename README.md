# Mentoring Session For UNIBZ

## Objective:
The objective of this session will be to familiarize with the opendatahub, learning how to perform some basic api calls, passing parameters, choosing data providers, handling limits, mapping JSONs to POJOs for further elaborations etc...
For this demonstrative purpose we are going to use the `Time series / Mobility`, querying the `Ninja Api` and in particular we will focus on gettin the data from the EChargingStations of `route220`.
A simple Java http client to interact with the `opendatahub` is given so that the students can fork, clone and have a starting point for their own individual projects.

### Objects and Concepts:
Time series data takes the form of `Measurements`.  

__A measurement is a data point with a timestamp.__

Each measurement has exactly one
- `Station`, a geographical points with a name, ID and some additional information. It's the location where measurements are made.
Think a physical e-charging station somewhere on a parking lot. Or a thermometer somewhere in a field. Stations can have a parent station, e.g. a thermometer that is part of a greater weather station
- `Data type`, which, identifies what type of measurement it actually is. Is it a temperature? Is it the number of available cars? Is it the current occupation state of an echarging column? Is it an average, or a discrete value?
- `Provenance`, a unique identifier and version number of the app that provided the measurement.
- `Period`, the timeframe (in seconds) that the measurement references, and the periodicity with which it is updated. e.g. a temperature sensor that sends us it's data every 60 seconds has a period of 60 seconds. 

A **Station** might have measurements of 0-n **Data types**, for example a weather station could have both `temperature` and `humidity` measurements.  
An e-charging station that we know exists, but doesn't provide any real time data, probably has no measurements at all.  
Stations exist independently of measurements.

## Dataset

For this mentoring session you will use the E-charging datasets, which are part of the Open Data Hub's mobility domain.
To make things simpler, we will limit ourselves to data from only one data provider that has a modest number of stations `origin: route220`.  
If you are feeling adventurous, you can extend it to also include `ALPERIA` etc...

E-charging stations are organized in two levels:

- station type `EChargingStation` represent a location where one or more EV chargers are located.
- station type `EChargingPlug` represents an individual e-charging column, that is always part of a EChargingStation (it's parent)

The stations have measurements of data type `number-available`, which indicates how many columns are currently available at the location  
The plugs have measurements of data type `echarging-plug-status`, which is 0 or 1, indicating if the column is in use or not 

For most of the projects I can think of, it would makes sense to work at the plug level, and then aggregate your result up to station. 

## API calls:
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

## Historical Data and request limits
[Wiki](https://github.com/noi-techpark/odh-docs/wiki/Historical-Data-and-Request-Rate-Limits)





