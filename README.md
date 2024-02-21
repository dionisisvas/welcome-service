# Welcome Service

### Description
The welcome service is part of the insurance system that issues and manages policies. It consumes and persists the policy issued events and provides an API to list and update them, according to business needs.

### Functionalities
* Welcome Service consumes events from a RabbitMQ that contain the policy issued details as well as contact details with the customers. It persists these events in the database.
* Welcome Service provides a REST API with endpoints for fetching pending welcome calls, 

The project contains the following maven modules:
* **welcome-service-model**: Contains the entity models, repositories and schema migrations.
* **welcome-service-api**: Contains the API models.
* **welcome-service-server**: Contain the executable spring boot project.

### Running the Service
To run the service we need to build the database image which includes the schema. To do this:
```
cd welcome-service-model
chmod +x build-image.sh   (if required)
./build-image.sh
```
Now you can run the service on your local environment from mvn cmd, by going to `welcome-service-server` directory and typing the bellow command:
`mvn spring-boot:run`
You will need to provide connection details for the insurance-policy queue in order to have a fully functional service.

Additionally, a docker-compose file is provided in order to run the service without the need of setting up local services. To launch the docker-compose follow the bellow steps (we assume you start at the root project folder, ie `welcome-service` directory):
```
cd welcome-service-model
chmod +x build-image.sh   (if required)
./build-image.sh
cd ../welcome-service-server
chmod +x build-image.sh   (if required)
cd ../docker-compose
```
**Note**: Unfortunately currently the service cannot connect to postgres DB from the docker stack, any local running should be done with the mvn cmd method.

The documentation for the welcome-service REST API can be found here:
`http://localhost:19001/welcomeservice/api/swagger-ui/index.html`

### Database
The database model contains one table, `welcome_calls`. We used JPA for modeling our Entity classes and as a persistence API.
We also used liquibase for database migration and schema updating. Some sample data have been added through `welcome_calls-sample_data.csv.csv` file.

For example, tou can get the sample PENDING data with the following request:
```
curl -X 'GET' \
  'http://localhost:19001/welcomeservice/api/v1/welcomecalls/pending' \
  -H 'accept: */*'
```

### Assumptions
When developing the application, certain assumptions where made, regarding the integration points with external systems:
1. The `policyReference` part of the policy issued event received in the AMQP exchange is a unique identifier for this event. So if we receive another event with the same `policyReference` we will treat it as an update to the existing one.
2. The agents in the call center will select pending welcome calls to handle. To support this we have associated the welcome calls with the agent id, which we assume it is a unique identifier for each call center agent (for example a username sent by a frontend system).
3. When an agent selects a welcome call to handle, it will be assigned to his agent id, meaning it will update the db entry with his agent id.
4. After selecting a welcome call to handle it, the agent will invoke a REST call (for example via a button in the UI) that will update the status of the welcome call to either `NO_ANSWER` or `ANSWERED` according to the outcome of the call.
5. According to the above assumptions, the pending welcome calls that the agents see on their screen for handling will be filtered to include those with status `PENDING` and with `null` agent id, since if there is an assigned agent id it will mean another agent has selected to handle this welcome call.

### Future Work (TODO)
Some future work that could be done to enhance this project:
* If an email fails to be sent mark it as failed in the db (for example with a new status), so the application can try again next day, or so that agent can investigate why it failed (e.g. wrong email address).
* Add some integration and/or some BDD tests to enhance code maintainability and reduce risk of future changes breaking existing functionality.