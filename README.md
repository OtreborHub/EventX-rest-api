# EventX-rest-api

Backend REST realizzato con Spring Boot 3.x e Java 21, strutturato a strati e allineato all'OpenAPI `eventx-api.yaml` per i domini:

- Users
- Events
- Locations
- Reviews
- Tickets

## Stack

- Java 21
- Spring Boot 3.3.x
- Spring Web
- Spring Validation
- Spring Security + JWT Bearer stateless
- Spring Data MongoDB
- springdoc-openapi
- JUnit 5 / Mockito / MockMvc

## Struttura

- `controller`
- `service`
- `repository`
- `models`
- `dto`
- `security`
- `config`
- `exceptions`

## Avvio rapido

Prerequisiti:

- Java 21
- Maven 3.9+
- MongoDB disponibile, di default su `mongodb://localhost:27017/eventx`

Comandi:

```powershell
mvn test
mvn spring-boot:run
```

## Variabili configurabili

In locale (profilo di default) l'app usa i valori in `application.yml`.
Il file `.env` viene letto solo nel profilo `prod`.

Valori attesi in ambiente/`.env`:

```dotenv
MONGODB_URI=mongodb://localhost:27017/eventx
JWT_SECRET=...
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## Build JAR per deploy

```powershell
mvn clean install
```

Artefatto generato:

- `target/eventx-rest-api-0.0.1-SNAPSHOT.jar`

Esecuzione JAR:

```powershell
java -jar target/eventx-rest-api-0.0.1-SNAPSHOT.jar
```

## OpenAPI / Swagger

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Specifica usata dallo Swagger UI: `http://localhost:8080/openapi/eventx-api.yaml`

## Note di aderenza alla specifica

Scelte conservative adottate per punti ambigui dell'OpenAPI:

1. `POST /api/v1/utenti`: la password è trattata come obbligatoria, coerentemente con descrizione ed esempio.
2. `PUT /api/v1/utenti/{id}/profilo`: lo username **non** viene modificato, coerentemente con `UpdateProfileRequestDTO`.
3. `EventRequestDTO.status`: se assente in creazione viene inizializzato a `PUBLISHED`.

## Verifica eseguita

Suite test eseguita con successo:

```powershell
mvn test
```

