[![REUSE Compliance](https://github.com/noi-techpark/unibz-mentoring-opendatahub/actions/workflows/reuse-lint.yml/badge.svg)](https://github.com/noi-techpark/opendatahub-docs/wiki/REUSE#badges)

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

### Stations
Stations can:
- Have 0-n data types
- Exist without measurements
- Have parent-child relationships

### Content Entities
Entites are a set onf information describing something.

## Dataset Details

### Focus Area Timeseries
- Domain: Mobility (E-charging stations)
- Primary Provider: `route220`
- Optional Extensions: ALPERIA and others

### Station Types
**EChargingStation**
- Represents a physical location with multiple chargers
- Data type: `number-available` (available columns count)


### Focus Content
- Domain: Tourism
- Entity: Accommodations

**Accommodations**
- Represents Accommodations with useful details

## API Reference

For this part I suggest working with Postman befor using the java http client.

[Content Api](https://tourism.opendatahub.com/swagger/index.html#/Accommodation/AccommodationList)
[Timeseries Api](https://swagger.opendatahub.com/?url=https://mobility.api.opendatahub.com/v2/apispec)


## Additional Resources
- [Open Data Hub Official Website](https://opendatahub.com/)
- [Historical Data and Request Limits](https://github.com/noi-techpark/odh-docs/wiki/Historical-Data-and-Request-Rate-Limits)
- [Open Data Hub General Overview](https://docs.opendatahub.com/en/latest/intro.html#project-overview)
- [Data Browser](https://databrowser.opendatahub.com/)
- [Ninja Api](https://github.com/noi-techpark/it.bz.opendatahub.api.mobility-ninja/blob/main/README.md)

## Getting Started
1. Fork this repository
2. Clone your fork
3. Use the provided Java HTTP client as a starting point
4. Test API calls using Postman before implementing in Java

### Ideas for data elaborations:
- Calculate the average availability percentage of echarging stations, aggregated per hour
- Predict the availability of an echarging station within the next hour
- etc..



