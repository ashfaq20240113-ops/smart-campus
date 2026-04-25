# 🏫 Smart Campus Sensor & Room Management API

**Module:** 5COSC022W — Client-Server Architectures  
**Institution:** Informatics Institute of Technology (IIT) Sri Lanka, Affiliated with the University of Westminster, UK  
**Student:** Junaideen Ashfaq Ahamed | UOW No: W2153015 | IIT No: 20240113  
**Module Leader:** Hamed Hamzeh  

---

## 📌 Overview

This project is a fully RESTful API built using **JAX-RS (Jersey)** and an embedded **Grizzly HTTP Server**, designed to manage the Smart Campus infrastructure. The API provides a seamless interface for facilities managers and automated building systems to manage campus **Rooms** and **Sensors** (e.g., CO2 monitors, temperature sensors, occupancy trackers).

### Key Features
- Full Room & Sensor CRUD operations
- Sub-resource pattern for Sensor Readings
- In-memory data storage using `ConcurrentHashMap`
- Advanced error handling with custom Exception Mappers
- Request & Response logging via JAX-RS Filters
- HATEOAS-compliant Discovery endpoint

---

## 🗂️ Project Structure

```
src/main/java/com/smartcampus/
├── application/
│   ├── Main.java                          # Entry point - starts Grizzly server
│   └── DataStore.java                     # Thread-safe singleton in-memory store
├── model/
│   ├── Room.java                          # Room POJO
│   ├── Sensor.java                        # Sensor POJO
│   └── SensorReading.java                 # SensorReading POJO
├── resource/
│   ├── DiscoveryResource.java             # GET /api/v1 - Discovery endpoint
│   ├── RoomResource.java                  # /api/v1/rooms
│   ├── SensorResource.java                # /api/v1/sensors
│   └── SensorReadingResource.java         # /api/v1/sensors/{id}/readings
├── exception/
│   ├── RoomNotEmptyException.java
│   ├── RoomNotEmptyExceptionMapper.java
│   ├── LinkedResourceNotFoundException.java
│   ├── LinkedResourceNotFoundExceptionMapper.java
│   ├── SensorUnavailableException.java
│   ├── SensorUnavailableExceptionMapper.java
│   └── GlobalExceptionMapper.java
└── filter/
    └── LoggingFilter.java                 # Request & Response logging
```

---

## ⚙️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming Language |
| JAX-RS (Jersey 3.x) | RESTful API Framework |
| Grizzly HTTP Server | Embedded lightweight server |
| Jackson | JSON serialization/deserialization |
| Maven | Build & dependency management |
| ConcurrentHashMap | Thread-safe in-memory data store |

---

## 🚀 How to Build & Run

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.x
- NetBeans 24 (or any Java IDE)

### Step 1 — Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/smart-campus-api.git
cd smart-campus-api
```

### Step 2 — Build the Project
```bash
mvn clean install
```

### Step 3 — Run the Server
```bash
mvn exec:java -Dexec.mainClass=com.smartcampus.application.Main
```

Or simply open the project in **NetBeans** and press **F6** to run.

### Step 4 — Verify Server is Running
You should see:
```
Started listener bound to [0.0.0.0:8080]
Smart Campus API started at: http://localhost:8080/api/v1/
Press ENTER to stop...
```

### Step 5 — Test the API
Open your browser or Postman and go to:
```
http://localhost:8080/api/v1
```

---

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

### 🔍 Discovery
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1` | Returns API metadata and resource links |

### 🏠 Rooms
| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/api/v1/rooms` | Get all rooms | 200 OK |
| POST | `/api/v1/rooms` | Create a new room | 201 Created |
| GET | `/api/v1/rooms/{roomId}` | Get room by ID | 200 OK |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room | 200 OK |

### 📡 Sensors
| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/api/v1/sensors` | Get all sensors | 200 OK |
| GET | `/api/v1/sensors?type={type}` | Filter sensors by type | 200 OK |
| POST | `/api/v1/sensors` | Register a new sensor | 201 Created |
| GET | `/api/v1/sensors/{sensorId}` | Get sensor by ID | 200 OK |

### 📊 Sensor Readings (Sub-Resource)
| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for sensor | 200 OK |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add new reading | 201 Created |

---

## 🧪 Sample curl Commands

### 1. Get API Discovery (Part 1)
```bash
curl -X GET http://localhost:8080/api/v1
```
**Expected:** `200 OK` with API metadata, version, contact info and HATEOAS links

---

### 2. Get All Rooms (Part 2)
```bash
curl -X GET http://localhost:8080/api/v1/rooms
```
**Expected:** `200 OK` with list of all rooms

---

### 3. Get Room by ID (Part 2)
```bash
curl -X GET http://localhost:8080/api/v1/rooms/LIB-301
```
**Expected:** `200 OK` with LIB-301 room details

---

### 4. Get Non-Existent Room — 404 Test (Part 2)
```bash
curl -X GET http://localhost:8080/api/v1/rooms/FAKE-999
```
**Expected:** `404 Not Found`

---

### 5. Create a New Room (Part 2)
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "HALL-001",
    "name": "Main Hall",
    "capacity": 200
  }'
```
**Expected:** `201 Created`

---

### 6. Delete Room WITH Sensors — 409 Test (Part 2 & Part 5)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```
**Expected:** `409 Conflict` — room has sensors assigned

---

### 7. Delete Room WITHOUT Sensors — Success (Part 2)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/HALL-001
```
**Expected:** `200 OK` — room decommissioned successfully

---

### 8. Delete Already Deleted Room — 404 Test (Part 2)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/HALL-001
```
**Expected:** `404 Not Found` — demonstrates idempotent behavior

---

### 9. Get All Sensors (Part 3)
```bash
curl -X GET http://localhost:8080/api/v1/sensors
```
**Expected:** `200 OK` with list of all sensors

---

### 10. Filter Sensors by Type (Part 3)
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```
**Expected:** `200 OK` with only CO2 sensors

---

### 11. Filter Sensors by Temperature Type (Part 3)
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```
**Expected:** `200 OK` with only Temperature sensors

---

### 12. Create a New Sensor (Part 3)
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-002",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 21.0,
    "roomId": "LAB-101"
  }'
```
**Expected:** `201 Created`

---

### 13. Create Sensor with Invalid Room — 422 Test (Part 3 & Part 5)
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-099",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 21.0,
    "roomId": "FAKE-ROOM"
  }'
```
**Expected:** `422 Unprocessable Entity` — roomId does not exist

---

### 14. Get All Readings for a Sensor (Part 4)
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```
**Expected:** `200 OK` with list of readings (empty array if none posted yet)

---

### 15. Post a New Sensor Reading (Part 4)
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 25.5}'
```
**Expected:** `201 Created` — also updates sensor currentValue to 25.5

---

### 16. Verify Sensor currentValue Updated (Part 4)
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001
```
**Expected:** `200 OK` — currentValue should now be `25.5`

---

### 17. Post Reading to MAINTENANCE Sensor — 403 Test (Part 5)
```bash
curl -X POST http://localhost:8080/api/v1/sensors/OCC-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 10.0}'
```
**Expected:** `403 Forbidden` — sensor is in MAINTENANCE status

---

## ⚠️ Error Handling

| Exception | HTTP Status | Scenario |
|-----------|-------------|----------|
| `RoomNotEmptyException` | 409 Conflict | Deleting a room that still has sensors |
| `LinkedResourceNotFoundException` | 422 Unprocessable Entity | Creating a sensor with a non-existent roomId |
| `SensorUnavailableException` | 403 Forbidden | Posting a reading to a MAINTENANCE sensor |
| `NotFoundException` | 404 Not Found | Resource not found |
| `Throwable` (catch-all) | 500 Internal Server Error | Any unexpected runtime error |

---

## 📝 Conceptual Report

### Part 1: Service Architecture & Setup

#### 1.1 JAX-RS Resource Lifecycle & Concurrency

**Question:** Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures.

**Answer:**

By default, JAX-RS will generate a new Resource object whenever a request to access a web resource comes in. This is referred to as the **per-request lifecycle**. Every HTTP request will have an independent object, and therefore the attributes in this object will be unique to that request.

This has profound effects on the management of the in-memory data structures. The objects generated are short-lived, and their states cannot persist beyond that particular request. To ensure that there is no loss of data in such cases, there is need to store the shared state somewhere else.

The **DataStore singleton pattern** has been used in this solution. The DataStore class maintains `ConcurrentHashMap` objects for rooms, sensors, and sensor readings. `ConcurrentHashMap` allows synchronized access to data, thus avoiding race conditions during multiple request processing since no explicit locking is needed. As a result, all resource objects, irrespective of their number, work with the same common data store.

---

#### 1.2 HATEOAS & Hypermedia

**Question:** Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

**Answer:**

HATEOAS is regarded as a characteristic of a mature RESTful architecture since it converts an API from a static set of endpoints to a navigable interface. The client does not need to have knowledge of all the available URLs; instead, it navigates through the available URLs embedded within the response itself.

For instance, the discovery point at `GET /api/v1` offers links to `/api/v1/rooms` and `/api/v1/sensors`. The client can easily understand the whole API architecture from this one discovery point without having to look elsewhere for information.

In comparison to static documents, HATEOAS is advantageous to the client developer in many ways:
- **Low coupling:** Client does not have to hardcode the URL. Any change to the URL will automatically redirect the client through the updated URL provided in the response.
- **Discoverability:** Client can use additional links for new features or resources available in the response without affecting existing operations.
- **Self-documenting:** The API informs the client about what actions are available at a particular state in the system.

---

### Part 2: Room Management

#### 2.1 Full Objects vs. IDs in List Responses

**Question:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

**Answer:**

When implementing the GET endpoints that return the list of resources, the developers should decide whether they will return only the IDs or full resource objects.

Sending only IDs consumes less bandwidth since the payload becomes significantly smaller. However, the client will have to make N+1 additional calls to get the entire information about each object — a problem known as the **N+1 request problem**, which slows down performance and complicates client-side implementation.

By implementing the `GET /api/v1/rooms` endpoint returning full objects, the developers can ensure that the client obtains all information at once without making additional calls. It helps to enhance performance, but the payload is considerably bigger, which may be critical if there are several thousand rooms. Taking into account that the application serves as an internal platform used by facilities managers who need detailed overviews, sending full objects is a better option.

---

#### 2.2 Idempotency of the DELETE Operation

**Question:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**Answer:**

The DELETE method is **not strictly idempotent** in this implementation. The initial DELETE on the same room yields a status code of `200 OK` with a successful message. If a DELETE on the same room is called again, a `404 Not Found` will be returned because the resource does not exist anymore.

Although the state of the server is the same in both cases (the room does not exist), the response code of the two requests is different. Strict idempotence dictates that the second DELETE yields the same result as the first one.

This is an intentional and common design choice. It is better for the client to see a 404 when attempting to delete a non-existent resource rather than getting a 200 silently. In a building management system, this makes it easier for the client to detect duplicate calls and provide accurate feedback.

---

### Part 3: Sensor Operations & Linking

#### 3.1 @Consumes and Content-Type Mismatch

**Question:** Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

**Answer:**

`@Consumes(MediaType.APPLICATION_JSON)` is an annotation that indicates that the POST endpoint will accept requests in JSON body format only. This annotation is interpreted by JAX-RS when it does content negotiation.

When JAX-RS gets a request from a client with the header `Content-Type: text/plain` or `Content-Type: application/xml`, it will refuse the request. An **HTTP 415 Unsupported Media Type** error code is sent back to the client.

There are some advantages in this approach:
- It strictly enforces a contract between the client and server, making sure that data is always in the correct form.
- It keeps erroneous data away from business logic code, preventing possible parsing errors and injection attacks.
- It returns a well-defined and standardized response in case of an error.

---

#### 3.2 @QueryParam vs. Path-Based Filtering

**Question:** Contrast @QueryParam filtering with path-based filtering (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior?

**Answer:**

Filtering with `@QueryParam`, like in `GET /api/v1/sensors?type=CO2`, is regarded as better than path parameter filtering such as `GET /api/v1/sensors/type/CO2` for the following reasons:

- **Semantic meaning:** Query parameters are semantically meant for filtering, sorting, and searching. A path segment `/type/CO2` suggests that `type/CO2` is a new unique resource identifier, which is misleading.
- **Optionality:** A query parameter filter is optional — `GET /api/v1/sensors` returns all sensors while `?type=CO2` filters them. Path-based filtering would require separate routes for each combination.
- **Combinability:** Different filters can be easily combined, e.g., `?type=CO2&status=ACTIVE`. Path-based filtering makes this very complex.
- **Cache-ability and RESTful architecture:** Query parameters do not change the base URL of the resource, making responses more cache-friendly.

---

### Part 4: Deep Nesting with Sub-Resources

#### 4.1 Architectural Benefits of the Sub-Resource Locator Pattern

**Question:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs?

**Answer:**

The Sub-Resource Locator pattern enables the delegation of nested path processing to a child resource from a parent resource class. In this case, `SensorResource` delegates `/sensors/{sensorId}/readings` requests to the `SensorReadingResource` class.

This approach yields substantial architectural advantages:

- **Separation of concerns:** Each resource class is responsible for only one thing. While `SensorResource` handles sensor CRUD, its readings are managed by `SensorReadingResource`. This results in better code structure and easier testing.
- **Improved simplicity:** Defining all the possible nested paths within a single controller leads to having hundreds of methods, making it extremely difficult to manage. Sub-resource classes keep each file appropriately sized.
- **Easy reuse:** Sub-resource classes can potentially be utilized in various parent classes independently.
- **Greater scalability:** Adding new sub-resource classes does not require any changes to existing classes, making it compliant with the **Open/Closed Principle**.

Contrasting this with defining all nested routes like `sensors/{id}/readings/{rid}` within one large controller class, the Sub-Resource Locator design creates a much more manageable and scalable codebase.

---

### Part 5: Advanced Error Handling, Exception Mapping & Logging

#### 5.1 HTTP 422 vs HTTP 404 for Missing References

**Question:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**Answer:**

HTTP 422 Unprocessable Entity is more semantically appropriate compared to HTTP 404 Not Found because the former implies that although the endpoint and the structure of the request are valid, something within the **content** is causing the issue.

- **HTTP 404 Not Found** signals that the specified resource or its URL could not be found at all. It is appropriate when navigating a non-existent URL such as `GET /api/v1/rooms/UNKNOWN-ID`.
- **HTTP 422 Unprocessable Entity** means the request had correct structure but the instructions within could not be carried out due to a **semantic error** — in this case, the `roomId` value references a non-existent resource in an otherwise valid payload.

Using 422 emphasizes that it is an issue with the request content and not the URL, helping client developers distinguish between routing errors and data validation errors.

---

#### 5.2 Cybersecurity Risks of Exposing Stack Traces

**Question:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**Answer:**

Exposure of internal Java stack traces is an extremely dangerous aspect from a cybersecurity point of view. A stack trace may disclose:

- **Internal package and class names:** Reveals internal application structure (e.g., `com.smartcampus.resource.RoomResource.deleteRoom`), allowing targeted attacks against specific classes and methods.
- **Framework and library versions:** Exact library names (e.g., `org.glassfish.jersey.server.ServerRuntime`) allow attackers to cross-reference known CVEs and exploit known vulnerabilities.
- **Server file system paths:** Reveals OS type and internal file paths, enabling path traversal and targeted configuration file theft.
- **Internal application logic and control flow:** Analyzing the call sequence helps attackers craft payloads that cause specific exceptions or bypass business logic.
- **Database structure:** ORM-related stack traces may leak table names and SQL query structures.

The `GlobalExceptionMapper` implemented in this project mitigates all of these risks by logging the complete stack trace only server-side using `java.util.logging`, while returning a clean, generic HTTP 500 response to the client with no internal details whatsoever.

---

#### 5.3 JAX-RS Filters vs Manual Logging

**Question:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

**Answer:**

Using JAX-RS filters (`ContainerRequestFilter` and `ContainerResponseFilter`) for cross-cutting concerns like logging represents a significantly superior architectural approach:

- **DRY (Don't Repeat Yourself):** The logging logic exists in exactly one place — the `LoggingFilter` class. Manual insertion would require duplicating the same `LOGGER.info()` call across every resource method.
- **Separation of Concerns:** Resource methods should focus exclusively on business logic. Logging is an infrastructure concern that should not be mixed with business logic, as doing so violates the Single Responsibility Principle.
- **Guaranteed Coverage:** A filter is applied automatically to every single incoming request without exception. With manual logging, it is easy to accidentally omit logging from a newly added endpoint.
- **Composability:** Filters can be chained. The same filter mechanism can also handle authentication, rate limiting, CORS, and metrics collection — all independently of resource classes.
- **Centralised Maintenance:** Changing the log format, level, or destination requires modifying only the single `LoggingFilter` class, leaving all resource classes completely untouched.

---

## 👨‍💻 Author

**Junaideen Ashfaq Ahamed**  
UOW No: W2153015 | IIT No: 20240113  
Informatics Institute of Technology (IIT) Sri Lanka  
Affiliated with the University of Westminster, UK
