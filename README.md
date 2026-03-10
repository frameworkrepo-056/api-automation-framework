# API Automation Framework

Production-ready REST API test automation framework built with:
**RestAssured 5.3.2 · Cucumber 7 (BDD) · JUnit Platform Suite · Allure Reports · Java 21 · Maven**

---

## Prerequisites
- Java 21 (LTS) — `java -version`
- Maven 3.9+ — `mvn -version`

---

## Running Tests

### Run all tests (QA environment)
```bash
mvn clean test
```

### Run against a specific environment
```bash
mvn clean test -Denv=staging
```

### Run only smoke tests (@severity:critical)
```bash
mvn clean test -Dtest=SmokeTestRunner
```

### Run with a custom tag filter
```bash
mvn clean test -Dcucumber.filter.tags="@story:Fetch-All-Users"
```

### Generate and open Allure report
```bash
mvn allure:report
open target/site/allure-maven-plugin/index.html
```

---

## Project Structure
src/
├── main/java/com/company/automation/
│   ├── context/ScenarioContext.java        # Per-scenario state (PicoContainer-managed)
│   ├── core/
│   │   ├── client/BaseApiClient.java       # Base HTTP verbs (GET/POST/PUT/PATCH/DELETE)
│   │   ├── config/ConfigReader.java        # Config loader with system property override
│   │   ├── reporting/AllureAttachmentUtil  # Centralised Allure attachments
│   │   └── spec/RequestSpecFactory.java    # ThreadLocal RequestSpec per scenario
│   └── services/user/
│       ├── client/UserClient.java          # User API operations
│       └── endpoints/UserEndpoints.java    # User endpoint constants
└── test/
├── java/com/company/automation/
│   ├── core/validation/
│   │   ├── ResponseValidator.java      # Status, count, time assertions
│   │   └── SchemaValidator.java        # JSON Schema validation
│   ├── hooks/ApiHooks.java             # Before/After scenario lifecycle
│   ├── runners/
│   │   ├── ApiTestRunner.java          # Full suite runner
│   │   └── SmokeTestRunner.java        # Critical tests only
│   └── stepdefinitions/UserSteps.java  # BDD step glue
└── resources/
├── allure.properties               # Allure output config
├── config/config.propert   ies        # Environment URLs and settings
├── environment.properties          # Allure dashboard env panel
├── features/user/                  # Gherkin feature files
├── logback.xml                     # Logging configuration
└── schemas/user/                   # JSON Schema files

---

## Tagging Strategy

| Tag | Purpose |
|-----|---------|
| `@epic:*` | Allure epic grouping |
| `@feature:*` | Allure feature grouping |
| `@story:*` | Allure story grouping |
| `@severity:critical` | Smoke/critical path tests |
| `@severity:normal` | Standard regression tests |
| `@layer:api` | Test layer identifier |
| `@ignore` | Temporarily skip scenario |

---

## Adding a New API Resource

1. Add endpoint constants in `services/<resource>/endpoints/<Resource>Endpoints.java`
2. Create `services/<resource>/client/<Resource>Client.java` extending `BaseApiClient`
3. Add step definitions in `stepdefinitions/<Resource>Steps.java`
4. Add JSON schemas in `schemas/<resource>/`
5. Write feature files in `features/<resource>/`