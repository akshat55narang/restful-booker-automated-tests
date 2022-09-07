# restful-booker-automated-tests

## Goal:
- Create automated tests to verify functionality of multiple operations exposed by Booking Api.

### Tools Used
1. Programming Language - Kotlin
2. Build Tool - Maven
3. Test Framework - Junit5
4. Api automation tool - RestAssured
5. Reports - Surefire reports

### Approach

- [BaseApi](src/main/kotlin/api/BaseApi.kt) includes helper methods ( including wrappers around RestAssured methods )
  All extending classes can extend BaseApi to make API calls.
- [BookingApi](src/main/kotlin/api/BookingApi.kt) Responsible for making API calls for all Booking Api operations
  and returning appropriate response.
- [BookingFixture](src/main/kotlin/fixtures/BookingFixture.kt) Supplies input data to BookingApi tests.
- [BookingApiTest](src/test/kotlin/BookingApiTest.kt) Contains BookingApi tests. Responsible for calling BookingApi 
  methods and performing assertions on received response.
- Used Serialization and De-Serialization for generating API requests and parsing responses.
- Environment Variables such as URI, Username and Password are accessed via Properties file 
  and can be overridden by command line. See [ConfigManager](src/main/kotlin/utils/ConfigManager.kt)

## How to Run:
-  Download this project on your local machine
> git clone https://github.com/akshat55narang/restful-booker-automated-tests.git

- From the restful-booker-tests directory
- Default configuration 
  - `mvn clean test`
- Override default configuration 
  - `mvn clean test -DdefaultUsername=admin -DdefaultPassword=password123`
- Run tests and generate surefire HTML report
  - `mvn clean surefire-report:report -DdefaultUsername=admin -DdefaultPassword=password123`

Environment variables are located in [default.properties](src/main/resources/default.properties)
The default values can be overridden from the command line, for example,
getBaseUri() method present in [ConfigManager](src/main/kotlin/utils/ConfigManager.kt)
will return the value from [default.properties](src/main/resources/default.properties) unless the value is supplied
via command line - `-DbaseUri`

> fun getBaseUri(): String {
return getParameterValue("baseUri", getConfig(BASE_URI))
}

### Test Artifact Location
Test Class - [BookingApiTest.kt](src/test/kotlin/BookingApiTest.kt)
- Report - target/surefire-reports/BookingApiTest.txt

