# Welcome Service

### Description
The welcome service is part of the insurance system that issues and manages policies. It consumes and persists the policy issued events and provides an API to list and update them, according to business needs.

### Functionalities
* Welcome Service consumes events from a RabbitMQ that contain the policy issued details as well as contact details with the customers. It persists these events in the database.
* Welcome Service provides a REST API with endpoints for fetching pending welcome calls, 

### Assumptions
When developing the application, certain assumptions where made, regarding the integration points with external systems:
1. The `policy_reference` part of the policy issued event received in the AMQP exchange is a unique identifier for this event. So if we receive another event with the same `policy_reference` we will treat it as an update to the existing one.
2. The agents in the call center will select pending welcome calls to handle. To support this we have associated the welcome calls with the agent id, which we assume it is a unique identifier for each call center agent (for example a username sent by a frontend system).
3. When an agent selects a welcome call to handle, it will be assigned to his agent id, meaning it will update the db entry with his agent id.
4. After selecting a welcome call to handle it, the agent will invoke a REST call (for example via a button in the UI) that will update the status of the welcome call to either `NO_ANSWER` or `ANSWERED` according to the outcome of the call.
5. According to the above assumptions, the pending welcome calls that the agents see on their screen for handling will be filtered to include those with status `PENDING` and with `null` agent id, since if there is an assigned agent id it will mean another agent has selected to handle this welcome call.# welcome-service
# welcome-service
