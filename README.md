# Agrishield Zambia

Group name: Harvest Coders
Zambia University College of Technology
Object-Oriented Programming Group Project, Final Submission

Agrishield Zambia helps farmers record their fields, report crop damage and follow the progress of each report. Administrators review all reports and update their status. The backend is written in Java with the Javalin web framework, and the frontend is a single HTML page that talks to it through a REST API.

## The Problem We Are Solving

Many smallholder farmers in Zambia have no simple record of their fields and no reliable way to report crop damage caused by drought, pests, floods or disease. Reports are given by word of mouth or on paper, they are easily lost, and the farmer cannot see whether anyone has acted on them. Officers also have no single list of problems to respond to.

Agrishield Zambia solves this by giving farmers one place to record their fields, report damage with its cause and the action they took, and follow each report from Pending to Under review to Resolved. Administrators see all reports in one list and update their status.

## Group Members

| No. | Full Name | Student Number |
|-----|-----------|----------------|
| 1 (Group Leader) | Alintula Silwimba | 2410030 |
| 2 | Fortune Jere | 2410050 |
| 3 | Andrew Sichali | 2420953 |
| 4 | Kapalanga Godelive | 2410381 |
| 5 | John Kasabi | 2300328 |

## Demo Video
Demo Video  YouTube link https://youtu.be/rvAwVpLM5YE?feature=shared
YouTube link (3 minutes maximum): paste your link here

## OOP Concepts Used

| Concept | Where |
|---------|-------|
| Inheritance | `Farmer` and `Admin` extend the abstract class `User`. `User`, `Field` and `Report` extend the abstract class `Entity`. |
| Encapsulation | Fields are private. Values change only through validating setters, for example `User.setPhone`, `Report.setSeverity`. The password is stored only as a salted SHA-256 hash. |
| Polymorphism | `User` declares `canViewReport`, `canDeleteReport`, `canChangeStatus` and `getRole`. `Farmer` and `Admin` answer them differently. Every `Entity` has its own `toView()`. |
| Abstraction | Abstract classes `Entity` and `User`. |
| Custom exceptions | `AgriShieldException` is the parent of `ValidationException`, `AuthenticationException`, `AccessDeniedException`, `NotFoundException`, `DuplicateException` and `StorageException`. Each carries an HTTP status. |
| Collections | `Repository<T extends Entity>` is a generic class that uses a `LinkedHashMap`. Lists, `Optional` and streams are used for searching. Login tokens are kept in a `ConcurrentHashMap`. |
| Enums | `ProblemType` and `ReportStatus`. |

## Project Structure

```
pom.xml
src/main/java/com/agrishield/
  Main.java                 starts the server
  api/ApiServer.java        REST routes
  model/                    Entity, User, Farmer, Admin, Field, Report, ProblemType, ReportStatus
  exception/                AgriShieldException and its subclasses
  repository/Repository.java
  store/DataStore.java      saves data to agrishield-data.ser
  service/                  AuthService, FieldService, ReportService
src/main/resources/public/index.html   the frontend
```

## How to Run

### What you need
- Java 17 or newer (JDK)
- Maven
- A web browser

Check them in a terminal with `java -version` and `mvn -version`.

### Run from the terminal
1. Download or clone this repository and open a terminal in the project folder (the one that contains `pom.xml`).
2. Build the project:
```
mvn package
```
3. Start the server:
```
java -jar target/agrishield.jar
```
4. Open http://localhost:7070 in a browser.

### Run in VS Code
1. Install the Extension Pack for Java.
2. Choose File, then Open Folder, and select the project folder.
3. Open `src/main/java/com/agrishield/Main.java` and click Run above the `main` method.
4. Open http://localhost:7070 in a browser.

### Logging in
- Farmers: tap Get started and register with a phone number starting with +260, a district and a password of at least 6 characters.
- Administrator for testing: phone `+260970000000`, password `admin123`. Change these by setting the environment variables `ADMIN_PHONE` and `ADMIN_PASSWORD` before starting the server.

Data is saved in a file called `agrishield-data.ser` in the folder where the server is started. Stop the server with Ctrl+C.

## API

| Method | Route | Purpose |
|--------|-------|---------|
| POST | /api/register, /api/login, /api/logout | Accounts and sessions |
| GET | /api/me, /api/users (admin) | Current user, all users |
| GET, POST | /api/fields | List and add fields |
| DELETE | /api/fields/{id} | Delete a field and its reports |
| GET, POST | /api/reports | List and add reports |
| DELETE | /api/reports/{id} | Delete a report |
| PATCH | /api/reports/{id}/status | Change status (admin) |

Requests after login send the header `Authorization: Bearer <token>`.
