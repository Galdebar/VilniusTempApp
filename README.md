# DogFacts

A REST-full app that provides hourly temperature readings in Vilnius.
Consumes remote API to get the data: https://www.climacell.co/weather-api/

## Pre-requisites
- JDK 11
- Maven 3 or newer
- Docker environment running

## Quickstart
`$./mvn clean package`
`$./docker-compose up`

The app url will depend on the docker host IP. Default `192.168.99.100`
Default front-end port `8080`
Default API port `8085`
Both can be changed in the docker-compose.yml file.


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
    
       
## Design & Decisions
### RestTemplate
It comes pre-packaged with Spring Started Web, so it integrates into the rest of the app automatically and already has error handling when consuming remote API out-of-the-box.
It also allows easy object mapping.

### Testing
Since the app is very simple, even integration tests run fairly fast, so I've decided to forego unit testing.

### MongoDB
I prefer NoSQL databases, because they're also suitable for rapid prototyping and avoid Hibernate. Hibernate is great for prototyping, but for production I think a different ORM could be used, such as JOOQ

## Acknowledgement
- Tests are a bit haphazard and can be more thorough. Could've chosen to either go full integration, or strictly component. But since the app is fairly small, current tests don't take up too much time.
- There is an edge-case chance the build may fail since you can hit remote API rate limit.
- Database calls are not optimized.
