# VilniusTemp REST API

A simple REST API to get hourly temperature readings in Vilnius.
Consumes remote API to get the data: https://www.climacell.co/weather-api/

## Temperature
Example of the default JSON object used in all endpoints:
````
{
    "temperature": 17.0,
    "observationTime": "2020-06-09T04:00:00"
}
````

## Endpoints
- `/current` - Gets the latest available temperature reading

- `/history` - Fetches full available temperature history. Currently max 30 days due to remote API limitations
    - `?days=` -  Optional query parameter. Returns list of temperature readings dating back the specified amount of days.
    Example request `GET/history?days=25`
    

